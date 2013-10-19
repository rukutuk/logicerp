package pohaci.gumunda.util.bypass;

import java.io.FileInputStream;
import java.io.IOException;

import pohaci.gumunda.util.Crypto;

public class ReadLoginDBFile {
	public ReadLoginDBFile() throws IOException {
		m_username = "";
		m_password = "";
		FileInputStream inStream = new FileInputStream("password.txt");
		int inBytes = inStream.available();
		byte inBuf[] = new byte[inBytes];
		inStream.read(inBuf, 0, inBytes);
		inStream.close();
		int index = 0;
		int i = 0;
		do {
			if (i >= inBuf.length)
				break;
			if (inBuf[i] == 10) {
				index = i;
				break;
			}
			i++;
		} while (true);
		byte euser[] = new byte[index];
		byte epasswd[] = new byte[inBuf.length - (index + 1)];
		for (int j = 0; j < index; j++)
			euser[j] = inBuf[j];

		for (int j = index + 1; j < inBuf.length; j++)
			epasswd[j - (index + 1)] = inBuf[j];

		try {
			byte buser[] = Crypto.decrypt(euser);
			char cuser[] = new char[buser.length];
			for (int j = 0; j < buser.length; j++)
				cuser[j] = (char) buser[j];

			m_username = String.valueOf(cuser);
			byte bpass[] = Crypto.decrypt(epasswd);
			char cpasswd[] = new char[bpass.length];
			for (int j = 0; j < bpass.length; j++)
				cpasswd[j] = (char) bpass[j];

			m_password = String.valueOf(cpasswd);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getUserName() {
		return m_username;
	}

	public String getPassword() {
		return m_password;
	}

	public static void main(String args[]) throws IOException {
		ReadLoginDBFile read = new ReadLoginDBFile();
		System.out.println("username : ".concat(String.valueOf(String
				.valueOf(read.getUserName()))));
		System.out.println("password : ".concat(String.valueOf(String
				.valueOf(read.getPassword()))));
	}

	static final String FILE_NAME = "password.txt";

	String m_username;

	String m_password;

}

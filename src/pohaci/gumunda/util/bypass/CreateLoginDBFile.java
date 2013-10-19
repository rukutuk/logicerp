package pohaci.gumunda.util.bypass;

import pohaci.gumunda.util.Crypto;
import pohaci.utility.FileUtil;

public class CreateLoginDBFile {
	public CreateLoginDBFile() {
	}

	public void create(String username, String password) throws Exception {
		try {
			byte busername[] = Crypto.encrypt(username.getBytes());
			byte bpassword[] = Crypto.encrypt(password.getBytes());
			FileUtil.createFile("password.txt", busername);
			FileUtil.appendBlank("password.txt");
			FileUtil.appendFile("password.txt", bpassword);
			System.out.println("OK");
		} catch (Exception ex) {
			throw new Exception(String.valueOf(String
					.valueOf((new StringBuffer(
							"Gagal dalam membuat file : password.txt\n"))
							.append(ex.toString()))));
		}
	}

	static final String FILE_NAME = "password.txt";

}

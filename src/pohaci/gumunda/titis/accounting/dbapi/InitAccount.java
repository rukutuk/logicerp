package pohaci.gumunda.titis.accounting.dbapi;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dwx
 * @version 1.0
 */

import java.sql.Connection;
import java.util.Vector;

import pohaci.gumunda.titis.accounting.entity.Account;

public class InitAccount{
  static IAccountingSQL sqlsap = new AccountingSQLSAP();

  public static void initKodeRekeningAktiva(Connection conn) throws Exception {
    Vector vresult1 = new Vector();
    Vector vresult2 = new Vector();
    Vector vresult3 = new Vector();
    Vector vresult4 = new Vector();

    OtherAccount account = new OtherAccount("1", "AKTIVA", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");

    OtherAccount subaccount1 = new OtherAccount("1.1", "Aktiva Lancar", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult1.addElement(subaccount1);

    OtherAccount subaccount2 = new OtherAccount("1.1.1", "Kas dan Setara Kas", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    OtherAccount subaccount3 = new OtherAccount("1.1.1.1", "Kas Kecil", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    OtherAccount subaccount4 = new OtherAccount("1.1.1.1.1", "Kas Jakarta", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.1.2", "Kas Balikpapan", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.1.3", "Kas Surabaya-Porong", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.1.4", "Kas Bontang", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.1.5", "Kas Surabaya", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.1.6", "Kas O & M Prabumulih", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.1.7", "Kas .....", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    OtherAccount subaccounts[] = new OtherAccount[vresult4.size()];
    vresult4.copyInto(subaccounts);
    vresult4.clear();
    subaccount3.setSubAccount(subaccounts);

    subaccount3 = new OtherAccount("1.1.1.2", "Bank Rupiah", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount4 = new OtherAccount("1.1.1.2.1", "Bank Mandiri Jkt (ex BDN) A/C 103-00862", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.2.2", "Bank Mandiri Balikpapan", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.2.3", "Bank Mandiri Bontang", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.2.4", "Bank Mandiri Jkt (ex Exim)", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.2.5", "Bank Mandiri Prabumulih", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.2.6", "BNI Surabaya", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.2.7", "BNI Prabumulih", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.2.8", "BNI Cikajang", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.2.9", "Bank Niaga Faletehan", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccounts = new OtherAccount[vresult4.size()];
    vresult4.copyInto(subaccounts);
    vresult4.clear();
    subaccount3.setSubAccount(subaccounts);

    subaccount3 = new OtherAccount("1.1.1.3", "Bank Dollar", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount4 = new OtherAccount("1.1.1.3.1", "Bank Mandiri Jkt (ex BDN)", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.3.2", "Bank Mandiri Balikpapan", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.3.3", "Bank Mandiri Bontang", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.3.4", "Bank Mandiri Jkt (ex Exim)", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.3.5", "Bank Mandiri Jkt (ex Exim)", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.3.6", "Bank Mandiri Jkt (ex BDN)", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccounts = new OtherAccount[vresult4.size()];
    vresult4.copyInto(subaccounts);
    vresult4.clear();
    subaccount3.setSubAccount(subaccounts);

    subaccount3 = new OtherAccount("1.1.1.4", "Deposito", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount4 = new OtherAccount("1.1.1.4.1", "Deposito Rupiah", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccount4 = new OtherAccount("1.1.1.4.2", "Deposito USD", Account.categoryFromStringToID(Account.STR_CATEGORY_1), false, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult4.addElement(subaccount4);

    subaccounts = new OtherAccount[vresult4.size()];
    vresult4.copyInto(subaccounts);
    vresult4.clear();
    subaccount3.setSubAccount(subaccounts);


    subaccount3 = new OtherAccount("1.1.1.5", "Giro Belum Jatuh Tempo", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.1.1.6", "Cash in Transit", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    //
    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);


    subaccount2 = new OtherAccount("1.1.2", "Investasi Jangka Pendek", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("1.1.3", "Piutang", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("1.1.3.1", "Piutang Usaha", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.1.3.2", "Cadangan Penyisihan Piutang", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.1.3.3", "Piutang Pegawai", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.1.3.9", "Piutang Lain-lain", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);


    subaccount2 = new OtherAccount("1.1.4", "Persediaan", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("1.1.5", "Uang Muka", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("1.1.5.1", "Uang Muka Pembelian", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.1.5.2", "Uang Muka Proyek", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.1.5.9", "Uang Muka Lain-lain", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);


    subaccount2 = new OtherAccount("1.1.6", "Pajak Dibayar di Muka", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("1.1.6.1", "PPh Pasal 21", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.1.6.2", "PPh Pasal 22", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.1.6.3", "PPh Pasal 23", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.1.6.4", "PPh Pasal 25", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);

    subaccount2 = new OtherAccount("1.1.7", "Biaya Dibayar di Muka", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("1.1.7.1", "Asuransi", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.1.7.2", "Sewa", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.1.7.9", "Lain-lain", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);

    subaccount2 = new OtherAccount("1.1.8", "Pekerjaan Dalam Proses", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccounts = new OtherAccount[vresult2.size()];
    vresult2.copyInto(subaccounts);
    vresult2.clear();
    subaccount1.setSubAccount(subaccounts);


    subaccount1 = new OtherAccount("1.2", "Aktiva Tidak Lancar", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult1.addElement(subaccount1);

    subaccount2 = new OtherAccount("1.2.1", "Investasi Aktiva Kerjasama Operasi", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("1.2.1.1", "Nilai Perolehan", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.2.1.2", "Selisih Kurs Valuta Asing", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.2.1.3", "Akumulasi Amortisasi Aktiva KSO", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);

    subaccount2 = new OtherAccount("1.2.2", "Penyertaan Saham", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("1.2.3", "Aktiva Tetap", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("1.2.3.1", "Nilai Perolehan Tanah", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.2.3.2", "Nilai Perolehan Bangunan", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.2.3.3", "Nilai Perolehan Mesin & Peralatan", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.2.3.4", "Nilai Perolehan Inventaris Kantor", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.2.3.5", "Nilai Perolehan Kendaraan", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.2.3.12", "Akumulasi Penyusutan Bangunan", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.2.3.13", "Akumulasi Penyusutan Mesin & Peralatan", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.2.3.14", "Akumulasi Penyusutan Inventaris Kantor", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.2.3.15", "Akumulasi Penyusutan Kendaraan", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);

    subaccount2 = new OtherAccount("1.2.4", "Aktiva Lain-lain", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("1.2.4.1", "Piutang Hubungan Istimewa", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.2.4.2", "Aktiva dalam Penyelesaian", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.2.4.3", "Bank Garansi", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.2.4.4", "Aktiva Tetap Disisihkan", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.2.4.5", "Aktiva Pajak Tangguhan", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);

    subaccounts = new OtherAccount[vresult2.size()];
    vresult2.copyInto(subaccounts);
    vresult2.clear();
    subaccount1.setSubAccount(subaccounts);


    subaccount1 = new OtherAccount("1.3", "Akun Sementara", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult1.addElement(subaccount1);

    subaccount2 = new OtherAccount("1.3.1", "PPN Masukan", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("1.3.1.1", "Head Office", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.3.1.2", "Balikpapan", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.3.1.3", "Bontang", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.3.1.4", "Porong", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.3.1.5", "Prabumulih", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);

    subaccount2 = new OtherAccount("1.3.2", "PPN Perhitungan", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("1.3.2.1", "Head Office", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.3.2.2", "Balikpapan", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.3.2.3", "Bontang", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.3.2.4", "Porong", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("1.3.2.5", "Prabumulih", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);

    subaccount2 = new OtherAccount("1.3.3", "Selisih expense sheet", Account.categoryFromStringToID(Account.STR_CATEGORY_1), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccounts = new OtherAccount[vresult2.size()];
    vresult2.copyInto(subaccounts);
    vresult2.clear();
    subaccount1.setSubAccount(subaccounts);

    subaccounts = new OtherAccount[vresult1.size()];
    vresult1.copyInto(subaccounts);
    vresult1.clear();
    account.setSubAccount(subaccounts);

    Vector vector = new Vector();

    sqlsap.createAccount(account, conn);
    long index = sqlsap.getMaxIndex(IDBConstants.TABLE_ACCOUNT, conn);
    subaccounts = account.getSubAccount();
    if(subaccounts.length > 0) {
      for(int i = 0; i < subaccounts.length; i ++) {
        sqlsap.createAccount(subaccounts[i], conn);
        long subindex = sqlsap.getMaxIndex(IDBConstants.TABLE_ACCOUNT, conn);
        sqlsap.createAccountStructure((short)index, (short)subindex, conn);
        createSubAccount(subindex, subaccounts[i], conn, vector);
      }
    }

    System.out.println("Init Akun Aktiva OK");
  }


  public static void initKodeRekeningKewajiban(Connection conn) throws Exception {
    Vector vresult1 = new Vector();
    Vector vresult2 = new Vector();
    Vector vresult3 = new Vector();

    OtherAccount account = new OtherAccount("2", "KEWAJIBAN", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");

    OtherAccount subaccount1 = new OtherAccount("2.1", "Kewajiban Lancar", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult1.addElement(subaccount1);

    OtherAccount subaccount2 = new OtherAccount("2.1.1", "Hutang Usaha", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("2.1.2", "Uang Muka Penjualan", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("2.1.3", "Biaya Masih Harus Dibayar", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult2.addElement(subaccount2);

    OtherAccount subaccount3 = new OtherAccount("2.1.3.1", "Gaji, lembur, tunj. lapangan, insentif yang masih harus dibayar", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("2.1.3.2", "Telepon, listrik, air yang masih harus dibayar", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("2.1.3.3", "Asuransi Pegawai", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("2.1.3.4", "Bunga Yang Masih Harus Dibayar", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("2.1.3.9", "Lain-lain", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult3.addElement(subaccount3);

    OtherAccount subaccounts[] = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);
    //
    subaccount2 = new OtherAccount("2.1.4", "Hutang Pajak", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("2.1.4.1", "PPh Pasal 21", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("2.1.4.2", "PPh Pasal 23", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("2.1.4.3", "PPh Pasal 29", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult3.addElement(subaccount3);


    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);

    subaccount2 = new OtherAccount("2.1.5", "Hutang Bank Jangka Pendek", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("2.1.5.1", "Hutang Bank Mandiri", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);

    subaccount2 = new OtherAccount("2.1.6", "Hutang hubungan istimewa", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("2.1.9", "Hutang Lain-lain", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult2.addElement(subaccount2);

    subaccounts = new OtherAccount[vresult2.size()];
    vresult2.copyInto(subaccounts);
    vresult2.clear();
    subaccount1.setSubAccount(subaccounts);

    subaccount1 = new OtherAccount("2.2", "Kewajiban Jangka Panjang", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult1.addElement(subaccount1);

    subaccount2 = new OtherAccount("2.2.1", "Hutang Bank Jangka Panjang", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("2.2.1.1", "Hutang Bank Mandiri", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);

    subaccount2 = new OtherAccount("2.2.2", "Pendapatan Diterima Dimuka", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("2.2.3", "Kewajiban Pajak Tangguahan", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("2.2.4", "Kewajiban Imbalan Pasca Kerja Karyawan", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("2.2.9", "Hutang Lain-lain", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult2.addElement(subaccount2);

    subaccounts = new OtherAccount[vresult2.size()];
    vresult2.copyInto(subaccounts);
    vresult2.clear();
    subaccount1.setSubAccount(subaccounts);

    subaccount1 = new OtherAccount("2.3", "Akun Sementara", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult1.addElement(subaccount1);

    subaccount2 = new OtherAccount("2.3.1", "PPN Keluaran", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("2.3.1.1", "Head Office", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("2.3.1.2", "Balikpapan", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("2.3.1.3", "Bontang", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("2.3.1.4", "Porong", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("2.3.1.5", "Prabumulih", Account.categoryFromStringToID(Account.STR_CATEGORY_2), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);

    subaccounts = new OtherAccount[vresult2.size()];
    vresult2.copyInto(subaccounts);
    vresult2.clear();
    subaccount1.setSubAccount(subaccounts);

    subaccounts = new OtherAccount[vresult1.size()];
    vresult1.copyInto(subaccounts);
    vresult1.clear();
    account.setSubAccount(subaccounts);

    Vector vector = new Vector();

    sqlsap.createAccount(account, conn);
    long index = sqlsap.getMaxIndex(IDBConstants.TABLE_ACCOUNT, conn);
    subaccounts = account.getSubAccount();
    if(subaccounts.length > 0) {
      for(int i = 0; i < subaccounts.length; i ++) {
        sqlsap.createAccount(subaccounts[i], conn);
        long subindex = sqlsap.getMaxIndex(IDBConstants.TABLE_ACCOUNT, conn);
        sqlsap.createAccountStructure((short)index, (short)subindex, conn);
        createSubAccount(subindex, subaccounts[i], conn, vector);
      }
    }

    System.out.println("Init Akun Kewajiban OK");
  }


  public static void initKodeRekeningEkuitas(Connection conn) throws Exception {
    Vector vresult1 = new Vector();
    Vector vresult2 = new Vector();

    OtherAccount account = new OtherAccount("3", "EKUITAS", Account.categoryFromStringToID(Account.STR_CATEGORY_3), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");

    OtherAccount subaccount1 = new OtherAccount("3.1", "Modal Dasar", Account.categoryFromStringToID(Account.STR_CATEGORY_3), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult1.addElement(subaccount1);

    subaccount1 = new OtherAccount("3.2", "Saldo Laba", Account.categoryFromStringToID(Account.STR_CATEGORY_3), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult1.addElement(subaccount1);

    OtherAccount subaccount2 = new OtherAccount("3.2.1", "Saldo Laba Yang Dicadangkan", Account.categoryFromStringToID(Account.STR_CATEGORY_3), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("3.2.2", "Saldo Laba Yang Belum Dicadangkan", Account.categoryFromStringToID(Account.STR_CATEGORY_3), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult2.addElement(subaccount2);

    OtherAccount subaccounts[] = new OtherAccount[vresult2.size()];
    vresult2.copyInto(subaccounts);
    vresult2.clear();
    subaccount1.setSubAccount(subaccounts);

    subaccounts = new OtherAccount[vresult1.size()];
    vresult1.copyInto(subaccounts);
    vresult1.clear();
    account.setSubAccount(subaccounts);

    Vector vector = new Vector();

    sqlsap.createAccount(account, conn);
    long index = sqlsap.getMaxIndex(IDBConstants.TABLE_ACCOUNT, conn);
    subaccounts = account.getSubAccount();
    if(subaccounts.length > 0) {
      for(int i = 0; i < subaccounts.length; i ++) {
        sqlsap.createAccount(subaccounts[i], conn);
        long subindex = sqlsap.getMaxIndex(IDBConstants.TABLE_ACCOUNT, conn);
        sqlsap.createAccountStructure((short)index, (short)subindex, conn);
        createSubAccount(subindex, subaccounts[i], conn, vector);
      }
    }

    System.out.println("Init Akun Ekuitas OK");
  }


  public static void initKodeRekeningPendapatan(Connection conn) throws Exception {
    Vector vresult1 = new Vector();
    Vector vresult2 = new Vector();

    OtherAccount account = new OtherAccount("4", "PENDAPATAN", Account.categoryFromStringToID(Account.STR_CATEGORY_4), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");

    OtherAccount subaccount1 = new OtherAccount("4.1", "Penjualan", Account.categoryFromStringToID(Account.STR_CATEGORY_4), true, Account.balanceFromStringToID(Account.STR_CREDIT), "");
    vresult1.addElement(subaccount1);

    OtherAccount subaccounts[] = new OtherAccount[vresult2.size()];
    vresult2.copyInto(subaccounts);
    vresult2.clear();
    subaccount1.setSubAccount(subaccounts);

    subaccounts = new OtherAccount[vresult1.size()];
    vresult1.copyInto(subaccounts);
    vresult1.clear();
    account.setSubAccount(subaccounts);

    Vector vector = new Vector();

    sqlsap.createAccount(account, conn);
    long index = sqlsap.getMaxIndex(IDBConstants.TABLE_ACCOUNT, conn);
    subaccounts = account.getSubAccount();
    if(subaccounts.length > 0) {
      for(int i = 0; i < subaccounts.length; i ++) {
        sqlsap.createAccount(subaccounts[i], conn);
        long subindex = sqlsap.getMaxIndex(IDBConstants.TABLE_ACCOUNT, conn);
        sqlsap.createAccountStructure((short)index, (short)subindex, conn);
        createSubAccount(subindex, subaccounts[i], conn, vector);
      }
    }

    System.out.println("Init Akun Pendapatan OK");
  }


  public static void initKodeRekeningBeban(Connection conn) throws Exception {
    Vector vresult1 = new Vector();
    Vector vresult2 = new Vector();
    Vector vresult3 = new Vector();
//    Vector vresult4 = new Vector();

    OtherAccount account = new OtherAccount("5", "BEBAN", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");

    OtherAccount subaccount1 = new OtherAccount("5.1", "Beban Pokok Penjualan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult1.addElement(subaccount1);

    OtherAccount subaccount2 = new OtherAccount("5.1.1", "Beban Gaji dan Tunjangan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    OtherAccount subaccount3 = new OtherAccount("5.1.1.1", "Gaji", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.1.1.2", "Tunjangan Hari Raya", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.1.1.3", "Tunjangan Lapangan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.1.1.4", "Uang makan, transport", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.1.1.5", "Lembur, insentif", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.1.1.6", "Bonus", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.1.1.7", "Asuransi Pegawai", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.1.1.8", "PPh Pasal 21", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.1.1.9", "Pesangon", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    OtherAccount subaccounts[] = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);

    subaccount2 = new OtherAccount("5.1.2", "Beban Pegawai Lainnya", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("5.1.2.1", "Pengobatan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.1.2.2", "Seragam", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.1.2.3", "Lainnya", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);

    subaccount2 = new OtherAccount("5.1.3", "Materials, Tools & Consumables", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("5.1.4", "Beban Operasional Proyek", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("5.1.4.1", "Beban perjalanan dinas karyawan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.1.4.2", "Beban telepon, listrik, air", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.1.4.3", "Beban katering", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);


    subaccount2 = new OtherAccount("5.1.5", "Beban pihak ketiga", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("5.1.6", "Beban Penyusutan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("5.1.6.1", "Bangunan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.1.6.2", "Mesin dan Peralatan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.1.6.3", "Inventaris Kantor", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.1.6.4", "Kendaraan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);


    subaccount2 = new OtherAccount("5.1.7", "Beban Perbaikan dan Pemeliharaan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("5.1.9", "Beban Lain-lain", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccounts = new OtherAccount[vresult2.size()];
    vresult2.copyInto(subaccounts);
    vresult2.clear();
    subaccount1.setSubAccount(subaccounts);


    subaccount1 = new OtherAccount("5.2", "Beban Operasional", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult1.addElement(subaccount1);

    subaccount2 = new OtherAccount("5.2.1", "Beban Gaji dan Tunjangan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("5.2.1.1", "Gaji", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.1.2", "Tunjangan Hari Raya", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.1.3", "Tunjangan Lapangan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.1.4", "Uang makan, transport", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.1.5", "Lembur, insentif", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.1.6", "Bonus", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.1.7", "Asuransi Pegawai", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.1.8", "PPh Pasal 21", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.1.9", "Pesangon", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);


    subaccount2 = new OtherAccount("5.2.2", "Beban Umum", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("5.2.2.1", "Pengobatan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.2.2", "Seragam", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.2.9", "Lainnya", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);


    subaccount2 = new OtherAccount("5.2.3", "Beban Umum", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("5.2.3.1", "ATK, Cetak, Fotocopy", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.3.2", "Langganan Koran & Majalah", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.3.3", "Benda Pos & Materai", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.3.4", "Telephone, Listrik, Air", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.3.5", "Konsumsi Rapat & Pertemuan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.3.6", "Keperluan Rumah Tangga", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.3.7", "Legal, Audit dan Professional Fee", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.3.8", "Iuran, Sumbangan dan Jamuan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.3.9", "Beban Pemeliharaan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.3.10", "Pajak-pajak", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.3.11", "Perjalanan Dinas", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.3.12", "Beban Sewa", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.3.13", "Beban Umum Lainnya", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);


    subaccount2 = new OtherAccount("5.2.4", "Beban Penjualan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("5.2.4.1", "Beban Pemasaran", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.4.2", "Biaya pengiriman dokumen", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.4.3", "Iuran, Sumbangan dan Jamuan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.4.4", "Perjalanan Dinas", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.4.9", "Beban Pemasaran Lainnya", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);


    subaccount2 = new OtherAccount("5.2.5", "Beban Kendaraan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("5.2.5.1", "Pemeliharaan Kendaraan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.5.2", "Bahan Bakar", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.5.3", "Sewa mobil", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.5.4", "Pajak dan Asuransi", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.5.9", "Beban Kendaraan Lainnya", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);


    subaccount2 = new OtherAccount("5.2.6", "Beban Penyusutan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount3 = new OtherAccount("5.2.6.1", "Bangunan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.6.2", "Mesin dan Peralatan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.6.3", "Inventaris Kantor", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("5.2.6.4", "Kendaraan", Account.categoryFromStringToID(Account.STR_CATEGORY_5), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);

    subaccounts = new OtherAccount[vresult2.size()];
    vresult2.copyInto(subaccounts);
    vresult2.clear();
    subaccount1.setSubAccount(subaccounts);

    subaccounts = new OtherAccount[vresult1.size()];
    vresult1.copyInto(subaccounts);
    vresult1.clear();
    account.setSubAccount(subaccounts);

    Vector vector = new Vector();

    sqlsap.createAccount(account, conn);
    long index = sqlsap.getMaxIndex(IDBConstants.TABLE_ACCOUNT, conn);
    subaccounts = account.getSubAccount();
    if(subaccounts.length > 0) {
      for(int i = 0; i < subaccounts.length; i ++) {
        sqlsap.createAccount(subaccounts[i], conn);
        long subindex = sqlsap.getMaxIndex(IDBConstants.TABLE_ACCOUNT, conn);
        sqlsap.createAccountStructure((short)index, (short)subindex, conn);
        createSubAccount(subindex, subaccounts[i], conn, vector);
      }
    }

    System.out.println("Init Akun Beban OK");
  }

  public static void initKodeRekeningPendapatanDanBeban(Connection conn) throws Exception {
    Vector vresult1 = new Vector();
    Vector vresult2 = new Vector();
    Vector vresult3 = new Vector();

    OtherAccount account = new OtherAccount("6", "PENDAPATAN DAN BEBAN LAIN-LAIN", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");

    OtherAccount subaccount1 = new OtherAccount("6.1", "Pendapatan Lain-lain", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult1.addElement(subaccount1);

    OtherAccount subaccount2 = new OtherAccount("6.1.1", "Laba Penjualan Aktiva Tetap", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("6.1.2", "Penerimaan Kembali Piutang Usaha yang Telah Dihapuskan", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("6.1.3", "Jasa Giro", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("6.1.4", "Bunga", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("6.1.5", "Laba Selisih Kurs", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("6.1.9", "Lainnya", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    OtherAccount subaccounts[] = new OtherAccount[vresult2.size()];
    vresult2.copyInto(subaccounts);
    vresult2.clear();
    subaccount1.setSubAccount(subaccounts);

    subaccount1 = new OtherAccount("6.2", "Beban Lain-lain", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult1.addElement(subaccount1);

    subaccount2 = new OtherAccount("6.2.1", "Rugi Penjualan Aktiva Tetap", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("6.2.2", "Penghapusan Piutang", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("6.2.3", "Bunga Pinjaman", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("6.2.4", "Beban Administrasi Bank", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("6.2.5", "Rugi Selisih Kurs", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("6.2.9", "Lainnya", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccounts = new OtherAccount[vresult2.size()];
    vresult2.copyInto(subaccounts);
    vresult2.clear();
    subaccount1.setSubAccount(subaccounts);

    subaccount1 = new OtherAccount("6.3", "Laba Rugi", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult1.addElement(subaccount1);

    subaccount2 = new OtherAccount("6.3.1", "Laba Rugi Tahun Berjalan", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccount2 = new OtherAccount("6.3.2", "Pajak Penghasilan", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    OtherAccount subaccount3 = new OtherAccount("6.3.2.1", "Pajak kini", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccount3 = new OtherAccount("6.3.2.2", "Manfaat (Beban) Pajak Tangguhan", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult3.addElement(subaccount3);

    subaccounts = new OtherAccount[vresult3.size()];
    vresult3.copyInto(subaccounts);
    vresult3.clear();
    subaccount2.setSubAccount(subaccounts);

    subaccount2 = new OtherAccount("6.3.3", "Dividen", Account.categoryFromStringToID(Account.STR_CATEGORY_6), true, Account.balanceFromStringToID(Account.STR_DEBET), "");
    vresult2.addElement(subaccount2);

    subaccounts = new OtherAccount[vresult2.size()];
    vresult2.copyInto(subaccounts);
    vresult2.clear();
    subaccount1.setSubAccount(subaccounts);

    subaccounts = new OtherAccount[vresult1.size()];
    vresult1.copyInto(subaccounts);
    vresult1.clear();
    account.setSubAccount(subaccounts);

    Vector vector = new Vector();

    sqlsap.createAccount(account, conn);
    long index = sqlsap.getMaxIndex(IDBConstants.TABLE_ACCOUNT, conn);
    subaccounts = account.getSubAccount();
    if(subaccounts.length > 0) {
      for(int i = 0; i < subaccounts.length; i ++) {
        sqlsap.createAccount(subaccounts[i], conn);
        long subindex = sqlsap.getMaxIndex(IDBConstants.TABLE_ACCOUNT, conn);
        sqlsap.createAccountStructure((short)index, (short)subindex, conn);
        createSubAccount(subindex, subaccounts[i], conn, vector);
      }
    }

    System.out.println("Init Akun Pendapatan dan Beban Lain-lain OK");
  }



  private static void createSubAccount(long index, OtherAccount account, Connection conn, Vector vector) throws Exception {
    OtherAccount[] subaccounts = account.getSubAccount();
    if(subaccounts.length > 0) {
      for(int i = 0; i < subaccounts.length; i ++) {
        if(vector.contains(subaccounts[i].getCode())) {
          System.out.println("kode " + subaccounts[i].getCode());
        }
        else
          vector.addElement(subaccounts[i].getCode());

        sqlsap.createAccount(subaccounts[i], conn);
        long subindex = sqlsap.getMaxIndex(IDBConstants.TABLE_ACCOUNT, conn);
        sqlsap.createAccountStructure((short)index, (short)subindex, conn);
        createSubAccount(subindex, subaccounts[i], conn, vector);
      }
    }
  }
}

class OtherAccount extends Account {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
OtherAccount subaccounts[] = new OtherAccount[0];

  public OtherAccount(String code, String name, short category, boolean group, short balance, String note) {
    super(code, name, category, group, balance, note, "");
  }

  public void setSubAccount(OtherAccount accounts[]) {
    subaccounts = accounts;
  }

  public OtherAccount[] getSubAccount() {
    return subaccounts;
  }
}
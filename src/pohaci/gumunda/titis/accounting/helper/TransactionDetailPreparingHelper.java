/**
 * 
 */
package pohaci.gumunda.titis.accounting.helper;

import java.util.ArrayList;
import java.util.Iterator;

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardAccount;
import pohaci.gumunda.titis.accounting.cgui.TransactionDetail;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.NumberRounding;


/**
 * @author dark-knight
 *
 */
public class TransactionDetailPreparingHelper {
	
	public TransactionDetail[] prepareJournalStandardTransactionDetail(
			JournalStandardAccount[] journalStandardAccounts,
			TransactionDetail[] transactionDetails, Unit unit,
			Currency currencyForCalculatedAccount,
			double exchangeRateForCalculatedAccount, boolean forceValue) {
		
		ArrayList detailListSelected = new ArrayList();
		
		for(int i=0; i<transactionDetails.length; i++){
			JournalStandardAccount accountApplied = 
				findJournalStandardAccount(transactionDetails[i], journalStandardAccounts);
			//yang masuk adalah yang di standar jurnalnya bukan merupakan yang harus dihitung
			if(accountApplied!=null){
				if(!accountApplied.isCalculate()){
					//masuk
					TransactionDetail detail = transactionDetails[i];
					//jangan mau kalau nilainya 0
					if(detail.getValue()!=0){
						//relasi antara transaction detail dan journal standard account
						TransactionDetailSelected detailSelected = new TransactionDetailSelected(detail, accountApplied);
						detailListSelected.add(detailSelected);
					}
				}
			}
		}
		
		double[] calculatedValue = {0, 0};
		JournalStandardAccount[] calculatedAccounts = findCalculatedAccount(journalStandardAccounts);
		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
		
		if(calculatedAccounts!=null){
			if (calculatedAccounts.length > 0) {
				// waktunya menghitung yang harus dihitung
				Iterator iter = detailListSelected.iterator();
				while (iter.hasNext()) {
					TransactionDetailSelected detailSelected = (TransactionDetailSelected) iter.next();
					TransactionDetail detail = detailSelected.getDetail();
					JournalStandardAccount account = detailSelected.getAccount();

					// pastikan accountnya bukan yang akan dihitung
					if (!isCalculatedAccount(account, calculatedAccounts)) {
						// semua yang debet diitung
						// semua yang credit diitung
						
						// lakukan semuanya dalam currency dan exchange rate yang 
						// digunakan untuk calculatedAccount
						
						
						if(detail.getCurrency().getIndex()==currencyForCalculatedAccount.getIndex())
							if(account.getBalance()!=detail.getAccount().getBalance())
								if(forceValue) {
									double val = -detail.getValue();
									val = nr.round(val);
									calculatedValue[account.getBalance()] += val;
								}
								else {
									double val = detail.getValue();
									val = nr.round(val);
									calculatedValue[account.getBalance()] += val;
								}
									
							else {
								double val = detail.getValue();
								val = nr.round(val);
								calculatedValue[account.getBalance()] += val;
							}
								
						else
							if(account.getBalance()!=detail.getAccount().getBalance())
								if(forceValue) {
									double val = (-detail.getValue()) * detail.getExchangeRate();
									val = nr.round(val);
									calculatedValue[account.getBalance()] += (val);
								}
									
								else {
									double val = (detail.getValue()) * detail.getExchangeRate();
									val = nr.round(val);
									calculatedValue[account.getBalance()] += (val);
								}
							else {
								double val = detail.getValue() * detail.getExchangeRate();
								val = nr.round(val);
								calculatedValue[account.getBalance()] += (val);
							}
								
					}
				}
				
				calculatedValue[0] = nr.round(calculatedValue[0]);
				calculatedValue[1] = nr.round(calculatedValue[1]);
				
				// ambil yang posisinya lebih besar
				short balance = -1;
				if(calculatedValue[0]>calculatedValue[1]){ // debet > credit
					double desc = calculatedValue[1];
					calculatedValue[0] -= desc;
					calculatedValue[1] -= desc;
					balance = 0;
				}else if(calculatedValue[0]<calculatedValue[1]){
					double desc = calculatedValue[0];
					calculatedValue[0] -= desc;
					calculatedValue[1] -= desc;
					balance = 1;
				}else{
					calculatedValue[0] = 0;
					calculatedValue[1] = 0;
					balance = -1;
				}
				
				// jangan lakukan apa-apa jika nilai kalkulasi debet dan credit = 0
				if(!((calculatedValue[0]==0)&&(calculatedValue[1]==0))){
					// oke, buat transaction detail dan tentunya relasinya ama
					// accountnya ya
					JournalStandardAccount selectedCalcAcct = findRelatedBalanceCalcAccount(balance,calculatedAccounts);
					TransactionDetail calculatedDetail = null;
					if(selectedCalcAcct.getBalance()==selectedCalcAcct.getAccount().getBalance())
						calculatedDetail = new TransactionDetail(
							selectedCalcAcct.getAccount(), calculatedValue[balance],
							currencyForCalculatedAccount,
							exchangeRateForCalculatedAccount, unit, -1);
					else
						calculatedDetail = new TransactionDetail(
								selectedCalcAcct.getAccount(), -calculatedValue[balance],
								currencyForCalculatedAccount,
								exchangeRateForCalculatedAccount, unit, -1);

					TransactionDetailSelected calculatedDetailSelected = new TransactionDetailSelected(
							calculatedDetail, selectedCalcAcct);

					// oke tambahkan ke list
					detailListSelected.add(calculatedDetailSelected);
				}
			}
		}
		
		//sekarang waktunya ambil transaction detailnya tok
		//sebelumnya siapkan penampungnya
		ArrayList detailList = new ArrayList();
		Iterator iter = detailListSelected.iterator();
		while(iter.hasNext()){
			TransactionDetailSelected detailSelected = (TransactionDetailSelected) iter.next();
			TransactionDetail detail = detailSelected.getDetail();
			JournalStandardAccount account = detailSelected.getAccount();
			//cek posisi debet dan kredit
			//antara yang di jurnal standar dan yang di chart of account
			//jika sama biarin aja
			//jika beda baru dinegatifkan
			//but, tidak perlu dilakukan jika di force untuk tidak menceknya
			//forceValue==true artinya valuenya tetep dipertahankan sesuai yang diinput
			if (!forceValue) {
				if (account.getBalance() != account.getAccount().getBalance()) {
					detail.setValue(-detail.getValue());
				}
			}
			//tambahkan....
			detailList.add(detail);
		}
		
		//phew...akhirnya...
		TransactionDetail[] details =
			(TransactionDetail[]) detailList.toArray(new TransactionDetail[detailListSelected.size()]);
		
		return details;
	}
	
	private JournalStandardAccount findRelatedBalanceCalcAccount(short balance, JournalStandardAccount[] calculatedAccounts) {
		short related = 0;
		if(balance==0)
			related=1;
		else
			related=0;
		for(int i=0; i<calculatedAccounts.length; i++){
			if(calculatedAccounts[i].getBalance()==related)
				return calculatedAccounts[i];
		}
		return null;
	}

	private boolean isCalculatedAccount(JournalStandardAccount account, JournalStandardAccount[] calculatedAccounts) {
		for(int i=0; i<calculatedAccounts.length; i++){
			if(account.getAccount().getIndex()==calculatedAccounts[i].getAccount().getIndex())
				return true;
		}
		return false;
	}

	public TransactionDetail[] prepareJournalStandardTransactionDetail(
			JournalStandardAccount[] journalStandardAccounts,
			TransactionDetail[] transactionDetails, Unit unit,
			Currency currencyForCalculatedAccount,
			double exchangeRateForCalculatedAccount) {
		return prepareJournalStandardTransactionDetail(journalStandardAccounts, transactionDetails, unit,
				currencyForCalculatedAccount, exchangeRateForCalculatedAccount, false);
	}

	private JournalStandardAccount[] findCalculatedAccount(JournalStandardAccount[] journalStandardAccounts) {
		ArrayList list = new ArrayList();
		for(int i=0; i<journalStandardAccounts.length; i++){
			if(journalStandardAccounts[i].isCalculate())
				list.add(journalStandardAccounts[i]);
		}
		JournalStandardAccount[] jsa = (JournalStandardAccount[]) list.toArray(new JournalStandardAccount[list.size()]);
		return jsa;
	}

	private JournalStandardAccount findJournalStandardAccount(TransactionDetail detail, JournalStandardAccount[] journalStandardAccounts) {
		for(int i=0; i<journalStandardAccounts.length; i++){
			if(detail.getAccount().getIndex()==journalStandardAccounts[i].getAccount().getIndex()){
				return journalStandardAccounts[i];
			}
		}
		return null;
	}
	
	private class TransactionDetailSelected{
		private TransactionDetail detail = null;
		private JournalStandardAccount account = null;
		
		protected TransactionDetailSelected(TransactionDetail detail, JournalStandardAccount account) {
			this.detail = detail;
			this.account = account;
		}

		public JournalStandardAccount getAccount() {
			return account;
		}

		public TransactionDetail getDetail() {
			return detail;
		}

		public void setAccount(JournalStandardAccount account) {
			this.account = account;
		}

		public void setDetail(TransactionDetail detail) {
			this.detail = detail;
		}
		
		
	}
}

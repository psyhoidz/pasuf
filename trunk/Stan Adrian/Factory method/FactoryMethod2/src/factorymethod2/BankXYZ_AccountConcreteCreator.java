package factorymethod2;


/**
 
 * 
 *  BankXYZ_AccountConcreteCreator este concrete creator class care este 
 * definita prin abstractizare de catre BankAccountCreator. 
 * BankXYZ_AccountConcreteCreator class creaza obiectele specifice
 * BankAccountProduct depinzand de tipul care este specificat in factory 
 * method. Obiectele create sunt returnate  de catre factory class prin
 * abstractizare de catre BankAccountProduct

 */
public class BankXYZ_AccountConcreteCreator extends BankAccountCreator {

	protected BankAccountProduct createBankAccount(String accountType) {
		if ("CHEQUE".equals(accountType)) {
			return new BankXYZ_ChequeAccountProduct();
			
		} else if ("SAVINGS".equals(accountType)) {
			return new BankXYZ_SavingsAccountProduct();
			
		} else {
			return null;
		}
	} // method createBankAccount
	
} // class BankXYZ_AccountConcreteCreator

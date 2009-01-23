package factorymethod2;


/**
 *  BankABC_AccountConcreteCreator este concrete creator class care este 
 * definita prin abstractizare de catre BankAccountCreator. 
 * BankABC_AccountConcreteCreator class creaza obiectele specifice
 * BankAccountProduct depinzand de tipul care este specificat in factory 
 * method. Obiectele create sunt returnate  de catre factory class prin
 * abstractizare de catre BankAccountProduct
 */
public class BankABC_AccountConcreteCreator extends BankAccountCreator {

	protected BankAccountProduct createBankAccount(String accountType) {
		if ("CHEQUE".equals(accountType)) {
			return new BankABC_ChequeAccountProduct();
			
		} else if ("SAVINGS".equals(accountType)) {
			return new BankABC_SavingsAccountProduct();
			
		} else {
			return null;
		}
	} // method createBankAccount

} // class BankABC_AccountConcreteCreator

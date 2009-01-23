package factorymethod2;


/**
 * 
 * BankAccountCreator este clasa abstracta care defineste factory method. 
 * Implementarea metodelor factory este delegata subclaselor sale
 *
 *  
 */
public abstract class BankAccountCreator {
	
	protected abstract BankAccountProduct createBankAccount(String accountType);
	
} // class BankAccountCreator

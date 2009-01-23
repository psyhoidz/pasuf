package factorymethod2;


/**
 * 
 * BankAccountProduct este clasa abstract product care defines 
 * clasele concrete product care sunt create  de metodele factory 
 * 
 */
public abstract class BankAccountProduct {
	
	
	public abstract void depositMoney(double depositAmount);
	
	public abstract void displayBalance();
	
	public abstract void withdrawMoney(double withdrawAmount);
	

} // class BankAccountProduct

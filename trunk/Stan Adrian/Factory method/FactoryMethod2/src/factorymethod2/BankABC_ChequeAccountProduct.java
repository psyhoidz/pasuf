package factorymethod2;


/**
 * The BankABC_ChequeAccountProduct is used to illustrate the Factory Method Pattern of GoF. The 
 * BankABC_ChequeAccountProduct is the concrete product class that is defined through abstraction
 * BankAccountProduct. The BankABC_ChequeAccountProduct class will be created by the factory method
 * and the instance of the class will be returned by the factory class through the BankAccountProduct
 * abstraction.
 * 
 * @author Andre Mare
 */
public class BankABC_ChequeAccountProduct extends BankAccountProduct {

	
	private double accountBalance = 0.00;
	
	
	public BankABC_ChequeAccountProduct() {
		System.out.println("Bank ABC - Cheque Account : Creating account.");
	} // no-arg constructor
	
	
	public void depositMoney(double depositAmount) {
		accountBalance += depositAmount;
		System.out.println("Bank ABC - Cheque Account : Deposit money " + depositAmount);
	} // method depositMoney
	
	
	public void displayBalance() {
		System.out.println("Bank ABC - Cheque Account : Acount Balance " + accountBalance);
	} // method displayBalance
	
	
	public void withdrawMoney(double withdrawAmount) {
		accountBalance -= withdrawAmount;
		System.out.println("Bank ABC - Cheque Account : Withdraw money " + withdrawAmount);
	} // method withdrawMoney
	
	
} // class BankABC_ChequeAccountProduct

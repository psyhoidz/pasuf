package factorymethod2;


/**
  * The BankABC_SavingsAccountProduct este concrete product care este
 * definita prin abestractizare de care BankAccountProduct. The 
 * BankABC_SavingsAccountProduct va fi creata de catre factory method
 * si instanta clasei va fi returnata de  catre factory class 
 * BankAccountProduct prin abstractizare.
 * 
 * 
 * 
 */
public class BankXYZ_ChequeAccountProduct extends BankAccountProduct {

	
	private double accountBalance = 0.00;
	
	
	public BankXYZ_ChequeAccountProduct() {
		System.out.println("Bank XYZ - Cheque Account : Creating account.");
	} // no-arg constructor
	
	
	public void depositMoney(double depositAmount) {
		accountBalance += depositAmount;
		System.out.println("Bank XYZ - Cheque Account : Deposit money " + depositAmount);
	} // method depositMoney
	
	
	public void displayBalance() {
		System.out.println("Bank XYZ - Cheque Account : Acount Balance " + accountBalance);
	} // method displayBalance
	
	
	public void withdrawMoney(double withdrawAmount) {
		accountBalance -= withdrawAmount;
		System.out.println("Bank XYZ - Cheque Account : Withdraw money " + withdrawAmount);
	} // method withdrawMoney
	
} // class BankXYZ_ChequeAccountProduct

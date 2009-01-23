package factorymethod2;

public class main {
	
	public static void main(String[] args) {
		BankAccountCreator bankSystemCreator = new BankABC_AccountConcreteCreator();
		BankAccountProduct account = bankSystemCreator.createBankAccount("CHEQUE");
		
		account.depositMoney(10.00);
		account.displayBalance();
		account.withdrawMoney(5.00);
		account.displayBalance();
	}

}

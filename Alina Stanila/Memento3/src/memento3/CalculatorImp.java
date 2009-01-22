/**
 * Implementarea Originatorului
 * Metoda backupLastCalculation corespunde metodei createMemento(), in ac metoda obiectul Memento
 * este creat si toate starile Orinatorului sunt salvate in Memento.
 * Metoda restorePreviousCalculation() ii corespunde metodei setMemento(), restaureaza ultima stare salvata.
 */

package memento3;

/**
 * Implementarea Originatorului
 * @author Ali
 */
public class CalculatorImp implements Calculator {

	private int firstNumber;
	private int secondNumber;

	@Override
	public PreviousCalculationToCareTaker backupLastCalculation() {

		// Creaza un obiect Memento ce va fi fol pt restauraea nr-lui
		return new PreviousCalculationImp(firstNumber,secondNumber);
	}

	@Override
	public int getCalculationResult() {

		// result is adding two numbers
		return firstNumber + secondNumber;
	}

	@Override
	public void restorePreviousCalculation(PreviousCalculationToCareTaker memento) {

		this.firstNumber = ((PreviousCalculationToOriginator)memento).getFirstNumber();
		this.secondNumber = ((PreviousCalculationToOriginator)memento).getSecondNumber();
	}

	@Override
	public void setFirstNumber(int firstNumber) {

		this.firstNumber =  firstNumber;
	}

	@Override
	public void setSecondNumber(int secondNumber) {

		this.secondNumber = secondNumber;
	}
}


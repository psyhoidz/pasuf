/**
 * Shows the memento implementation, note that the memento must implement two interfaces,
 * the one to the caretaker as well as the one to the originator.
 */

package memento3;

/**
 * Memento Object Implementation
 *
 * Note that this object implements both interfaces to Originator and CareTaker
 * @author Ali
 */
public class PreviousCalculationImp implements PreviousCalculationToCareTaker,PreviousCalculationToOriginator {

	private int firstNumber;
	private int secondNumber;

	public PreviousCalculationImp(int firstNumber, int secondNumber) {

		this.firstNumber =  firstNumber;
		this.secondNumber = secondNumber;
	}

	@Override
	public int getFirstNumber() {

		return firstNumber;
	}

	@Override
	public int getSecondNumber() {

		return secondNumber;
	}

}

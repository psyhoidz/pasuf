/**
 * Simulates a user using the calculator to add numbers, the user calculates a result,
 * then enters wrong numbers, he is not satisfied with the result and he hits Ctrl + Z to
 * undo last operation and restore previous result.open the template in the editor.
 */

package memento3;

/**
 * CareTaker
 * @author Ali
 */
public class Main {

    public static void main(String[] args) {
		Calculator calculator = new CalculatorImp();

		// assume user enters two numbers
		calculator.setFirstNumber(10);
		calculator.setSecondNumber(100);

		// find result
		System.out.println(calculator.getCalculationResult());

		// Store result of this calculation in case of error
		PreviousCalculationToCareTaker memento = calculator.backupLastCalculation();

		// user enters a number
		calculator.setFirstNumber(17);

		// user enters a wrong second number and calculates result
		calculator.setSecondNumber(-290);

		// calculate result
		System.out.println(calculator.getCalculationResult());

		// user hits CTRL + Z to undo last operation and see last result
		calculator.restorePreviousCalculation(memento);

		// result restored
		System.out.println(calculator.getCalculationResult());
	}

    

}

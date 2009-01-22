/**
 * This simple example is a calculator that finds the result of addition of two numbers,
 * with the additional option to undo last operation and restore previous result.
 */
package memento3;

/**
 * Memento interface to CalculatorOperator (Caretaker)
 * The code below shows the memento object interface to caretaker.
 * Note that this interface is a placeholder only and has no methods to honor encapsulation in that
 * the memento is opaque to the caretaker.
 * @author Ali
 */
public interface PreviousCalculationToCareTaker {
    // no operations permitted for the caretaker
}

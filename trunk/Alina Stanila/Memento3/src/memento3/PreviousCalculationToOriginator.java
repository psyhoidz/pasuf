/**
 * The code below shows the Memento to Originator interface;
 * note that this interface provides the necessary methods for the originator to restore its original state.
 */
package memento3;

/**
 * Memento Interface to Originator
 * This interface allows the originator to restore its state
 * @author Ali
 */
public interface PreviousCalculationToOriginator {

    public int getFirstNumber();

    public int getSecondNumber();
}

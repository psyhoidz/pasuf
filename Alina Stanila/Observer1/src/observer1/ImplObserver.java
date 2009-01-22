/*
 * Clasa ImplObserver implementeaza interfata Observer
 */

package observer1;

/**
 * Implementeaza interfata Observer
 * @author Ali
 */
public class ImplObserver implements Observer
{
    String state = "";

    public void update(Subject o)
    {
        state = o.getState();
        System.out.println("Updtae primit de la Subject, stare: " + state);
    }
}

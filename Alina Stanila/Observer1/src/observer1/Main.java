/*
 * 
 */

package observer1;

/**
 * @author Ali
 */
public class Main {
    
    public static void main(String[] args)
    {
        Observer o = new ImplObserver();
        Subject s = new ImplSubject();
        s.addObserver(o);
        /**
         * Se creaza seteaza starea
         */
        s.setState("New State");
    }
}

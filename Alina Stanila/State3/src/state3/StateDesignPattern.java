
package state3;

/**
 * Main
 * @author Ali
 */
public class StateDesignPattern {

    public static void main(String[] args) {
        ProjectCurrentState chain = new ProjectCurrentState();
        /**
         * Afiseaza starea curenta
         */
        chain.pull();
        /**
         * Schimbarea starii
         */
        chain.set_state(new DesignStage());
        /**
         * Afiseaza starea curenta
         */
        chain.pull();

    }
}

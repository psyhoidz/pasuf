/**
 * Patternul Memento este folosit de doua clase: Originatorul si Caretaker
 * Originatorul = un obiect cu o anumita stare interna;
 * Caretaker = va modifica starea Originatorul dar va dori sa revina la o alta stare salvata;
 * Caretaker va cere Originatorului un obiect de tipul Memento, apoi face diferite operatii, si va dori sa revina la
 * o stare salvata inaintea implementarii setului de operatii, va returna obiectul de tip Memento Originatorului.
 * Obiectul Memento nu va fi modificat de Caretaker.
 */
package memento1;

/**
 * Implementeaza starea curenta
 * @author Ali
 */
public class Originator {

    private String state;

    /**
     * Se seteaza starea curenta
     */
    public void set(String state) {
        System.out.println("Originator: Setting state to " + state);
        this.state = state;
    }
    /**
     * Se salveaza starea curenta
     */
    public Object saveToMemento() {
        System.out.println("Originator: Saving to Memento.");
        return new Memento(state);
    }
    /**
     * Se restaureaza ultima stare salvata
     */
    public void restoreFromMemento(Object m) {
        if (m instanceof Memento) {
            Memento memento = (Memento) m;
            state = memento.getSavedState();
            System.out.println("Originator: State after restoring from Memento: " + state);
        }
    }
}

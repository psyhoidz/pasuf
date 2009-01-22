/**
 * Patternul Memento este folosit de doua clase: Originatorul si Caretaker
 * Originatorul = un obiect cu o anumita stare interna;
 * Caretaker = va modifica starea Originatorul dar va dori sa revina la o alta stare salvata;
 * Caretaker va cere Originatorului un obiect de tipul Memento, apoi face diferite operatii, si va dori sa revina la
 * o stare salvata inaintea implementarii setului de operatii, va returna obiectul de tip Memento Originatorului.
 * Obiectul Memento nu va fi modificat de Caretaker.
 */
package memento2;

/**
 *
 * @author Ali
 */
public class Memento {

    private String state;

    public Memento(String stateToSave) {
        state = stateToSave;
    }

    /**
     * Returneaza starile salvate
     */
    public String getSavedState() {
        return state;
    }
}

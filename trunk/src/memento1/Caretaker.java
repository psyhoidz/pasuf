/**
 * Patternul Memento este folosit de doua clase: Originatorul si Caretaker
 * Originatorul = un obiect cu o anumita stare interna;
 * Caretaker = va modifica starea Originatorul dar va dori sa revina la o alta stare salvata;
 * Caretaker va cere Originatorului un obiect de tipul Memento, apoi face diferite operatii, si va dori sa revina la
 * o stare salvata inaintea implementarii setului de operatii, va returna obiectul de tip Memento Originatorului.
 * Obiectul Memento nu va fi modificat de Caretaker.
 */
package memento1;

import java.util.*;

/**
 * Realizeaza anumite actiunii asupra un obiect de tipul Memento
 * @author Ali
 */
public class Caretaker {

    private List<Object> savedStates = new ArrayList<Object>();

    /**
     * Adauga un Memento, adica salveaza o stare
     */
    public void addMemento(Object m) {
        savedStates.add(m);
    }
    /**
     * Returneaza indexul starilor salvate
     */
    public Object getMemento(int index) {
        return savedStates.get(index);
    }
}

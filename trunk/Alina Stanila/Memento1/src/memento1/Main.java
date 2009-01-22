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
 *
 * @author Ali
 */
public class Main {
    public static void main(String[] args) {
        /**
         * Se creaza un obiect de tipul Caretaker
         */
        Caretaker caretaker = new Caretaker();
        /**
         * Se creaza un obiect de tipul Originator
         */
        Originator originator = new Originator();
        /**
         * Se setteaza starea curenta ca fiind State1
         */
        originator.set("State1");
        /**
         * Se setteaza starea curenta ca fiind State2
         */
        originator.set("State2");
        /**
         * Se salveaza starea curenta, adica State2
         */
        caretaker.addMemento(originator.saveToMemento());
        /**
         * Se setteaza starea curenta ca fiind State3
         */
        originator.set("State3");
        /**
         * Se salveaza starea curenta, adica State3
         */
        caretaker.addMemento(originator.saveToMemento());
        /**
         * Se setteaza starea curenta ca fiind State3
         */
        originator.set("State4");
        /**
         * Se revine la ultima stare salvata
         */
        originator.restoreFromMemento(caretaker.getMemento(1));
    }
}

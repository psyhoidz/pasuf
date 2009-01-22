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
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /**
         * Se creaza un obiect de tipul Caretaker
         */
        Caretaker caretaker = new Caretaker();
        /**
         * Se creaza un obiect de tipul Originator
         */
        Originator originator = new Originator();

        originator.set("a");
        originator.set("b");
        caretaker.addMemento(originator.saveToMemento());
        originator.set("c");
        caretaker.addMemento(originator.saveToMemento());
        originator.set("d");
        originator.set("f");
        /**
         * Se revine la ultima stare salvata
         */
        originator.restoreFromMemento(caretaker.getMemento(1));
    }
}

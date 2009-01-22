/**
 * Clasa Senzor implementeaza interfata Subject
 */
package observer2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Implementeaza interfata Subject
 * @author Ali
 */
public class Senzor implements Subject {

    private List<Observer> obs = new LinkedList<Observer>();
    private int valCurenta = 0;

    /** Adauga un Obsever*/
    public void addObserver(Observer o) {
        obs.add(o);
    }

    /** Sterge un Obsever*/
    public void removeObserver(Observer o) {
        obs.remove(o);
    }

    /** Notifica Observerii*/
    public void notifyObserver() {
        Iterator i = obs.iterator();

        while (i.hasNext()) {
            Observer o = (Observer) i.next();
            o.upDate(this);
        }
    }

    /** Genereaza un numar random*/
    public void generare() {
        Random serieRandom = new Random();
        valCurenta = serieRandom.nextInt(80);
        notifyObserver();
    }

    /** Returneaza valoarea curenta*/
    public int getVal() {
        return valCurenta;
    }
}

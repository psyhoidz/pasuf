/**
 * Interfata Subject utilizata pentru a adauga, inlatura obiecte Observer
 * precum si pentru a lua valoarea obiectelor
 */

package observer2;

/**
 * Utilizata pentru a adauga, inlatura obiecte Observer
 * precum si pentru a lua valoarea obiectelor
 * @author Ali
 */
public interface Subject
{
    /** Adauga un Obsever*/
    public void addObserver(Observer o);
    /** Sterge un Obsever*/
    public void removeObserver(Observer o);
     /** Returneaza valoarea curenta*/
    public int getVal();
}

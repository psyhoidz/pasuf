/*
 * Interfata Subject utilizata pentru a adauga, inlatura obiecte Observer
 * precum si pentru a seta si lua starea obiectelor
 */
package observer1;

/**
 * Utilizata pentru a adauga, inlatura obiecte Observer
 * precum si pentru a prelua si seta valoarea obiectelor
 * @author Ali
 */
public interface Subject {

    /**
     * Adauga un Obsever
     */
    public void addObserver(Observer o);

    /**
     * Sterge un Obsever
     */
    public void removeObserver(Observer o);

    /**
     * Preia starea
     */
    public String getState();

    /**
     * seteaza starea
     */
    public void setState(String state);
}

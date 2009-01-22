/*
 * Interfata Observer utilizata pentru a notifica obiectele atunci cand se face o schimbare
 */

package observer1;

/**
 * Utilizata pentru a notifica obiectele atunci cand se face o schimbare
 * @author Ali
 */
public interface Observer {
    public void update( Subject o );
}

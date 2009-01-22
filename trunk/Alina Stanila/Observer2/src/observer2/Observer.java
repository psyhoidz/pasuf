/**
 * Interfata Observer utilizata pentru a notifica obiectele atunci cand se face o schimbare
 */

package observer2;

/**
 * Utilizata pentru a notifica obiectele atunci cand se face o schimbare
 * @author Ali
 */
public interface Observer
{
    /** Notifica obiectele atunci cand se face o schimbare*/
    public void upDate(Subject s);
}

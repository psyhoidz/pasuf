/**
 * Clasa ValoareCurenta implementeaza interfata Observer
 */
package observer2;

import javax.swing.JTextField;

/**
 * Implementeaza interfata Observer
 * @author Ali
 */
public class ValoareCurenta extends JTextField implements Observer {

    /** Notifica obiectele atunci cand se face o schimbare*/
    public void upDate(Subject s) {
        this.setText(Integer.toString(s.getVal()));

    }
}

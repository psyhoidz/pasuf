/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package observer3;

/**
 * Implementeaza AlarmListener
 * @author Ali
 */
public class Gates implements AlarmListener {

    public void alarm() {
        System.out.println("Gates closed");
    }
}

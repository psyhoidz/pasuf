/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package observer3;

/**
 *
 * @author Ali
 */
public class SensorSystem {

    private java.util.Vector listeners = new java.util.Vector();

    public void register(AlarmListener al) {
        listeners.addElement(al);
    }

    public void soundTheAlarm() {
        for (java.util.Enumeration e = listeners.elements(); e.hasMoreElements();) {
            ((AlarmListener) e.nextElement()).alarm();
        }
    }
}

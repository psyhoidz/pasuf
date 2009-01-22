/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package observer3;

/**
 * Implementeaza AlarmListener si extind CheckList
 * @author Ali
 */
public class Surveillance extends CheckList implements AlarmListener {

    public void alarm() {
        System.out.println("Surveillance - by the numbers:");
        byTheNumbers();
    }

    @Override
    protected void isolate() {
        System.out.println("   train the cameras");
    }
}

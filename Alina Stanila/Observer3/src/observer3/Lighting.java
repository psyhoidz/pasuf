/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package observer3;

/**
 * Implementeaza AlarmListener
 * @author Ali
 */
public class Lighting implements AlarmListener{

    public void alarm() {
       System.out.println( "Lights up" );
    }

}

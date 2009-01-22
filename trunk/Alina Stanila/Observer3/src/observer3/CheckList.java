/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package observer3;

/**
 *
 * @author Ali
 */
public class CheckList {

    public void byTheNumbers() {
        localize();
        isolate();
        identify();
    }

    protected void localize() {
        System.out.println("   establish a perimeter");
    }

    protected void isolate() {
        System.out.println("   isolate the grid");
    }

    protected void identify() {
        System.out.println("   identify the source");
    }
}

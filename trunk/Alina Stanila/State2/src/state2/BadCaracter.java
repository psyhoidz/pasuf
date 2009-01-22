/**
 *  Va afisa atributul unui BadCaracter
 */
package state2;

/**
 * Implementeaza interfata Caracter
 * @author Ali
 */
public class BadCaracter implements Caracter {

    public void giveMeMoney() {
        System.out.println("No, I dont have anything with me");
    }
}

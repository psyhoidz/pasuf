/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package state2;

/**
 * Clasa Persona, definirea acesteia si testarea programului
 * @author Ali
 */
public class Person {

    private String name;
    private Caracter caracter;

    public Person(String name, Caracter caracter) {
        this.name = name;
        this.caracter = caracter;
    }

    public void giveMeMoney() {
        caracter.giveMeMoney();
    }

    public static void main(String[] args) {

        Person object = new Person("John", new GoodCharacter());
        object.giveMeMoney();

        object = new Person("John", new BadCaracter());
        object.giveMeMoney();
    }
}

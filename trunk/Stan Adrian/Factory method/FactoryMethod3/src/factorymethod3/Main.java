package factorymethod3;

public class Main {
public static void main(String[] args) {
       NameFactory nfactory = new NameFactory();
       Namer namer = nfactory.getNamer("Stan, Adrian");
//trimitele numele
//returneaza continutul
    System.out.println("First Name:"+namer.getFirst());
    System.out.println("Last Name:"+namer.getLast());
    }

}

package abstractfactory3;

public class Parts {
  	/**
* variabila string care retine specificatile calculatorului
*/
public String specification;

/**
* Constructorul clasei care retine specificatile
*/
public Parts(String specification) {
this.specification = specification;
}

/**
* metoda care returneaza specificatile calculatorului
*/
public String getSpecification() {
return specification;
}

}// End of class

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package abstractfactory1;

class LuxuryCar implements Car {
  private String name;

  public LuxuryCar(String cName) {
    name = cName;
  }

  public String getCarName() {
    return name;
  }

  public String getCarFeatures() {
    return "Luxury Car Features ";
  };

} // End of class
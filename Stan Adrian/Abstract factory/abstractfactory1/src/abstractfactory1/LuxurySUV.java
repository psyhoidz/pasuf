/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package abstractfactory1;

class LuxurySUV implements SUV {
  private String name;

  public LuxurySUV(String sName) {
    name = sName;
  }

  public String getSUVName() {
    return name;
  }

  public String getSUVFeatures() {
    return "Luxury SUV Features ";
  };

} // End of class
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package abstractfactory1;

class NonLuxuryVehicleFactory extends VehicleFactory {

  public Car getCar() {
    return new NonLuxuryCar("NL-C");
  }

  public SUV getSUV() {
    return new NonLuxurySUV("NL-S");
  }
} // End of class


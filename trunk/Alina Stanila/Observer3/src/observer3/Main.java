/** Observer design pattern, class inheritance vs type inheritance
 * SensorSystem is the "subject".  Lighting, Gates, and Surveillance are the
 * "views".  The subject is only coupled to the "abstraction" of AlarmListener.
 * An object's class defines how the object is implemented.  In contrast, an
 * object's type only refers to its interface.  Class inheritance defines an
 * object's implementation in terms of another object's implementation.  Type
 * inheritance describes when an object can be used in place of another.
 */
package observer3;

/**
 *
 * @author Ali
 */
public class Main {
    public static void main(String[] args) {
        SensorSystem ss = new SensorSystem();
        ss.register(new Gates());
        ss.register(new Lighting());
        ss.register(new Surveillance());
        ss.soundTheAlarm();
    }
}
  

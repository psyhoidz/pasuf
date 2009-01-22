/*
 * Clasa ImplSubject implementeaza interfata Subject
 */

package observer1;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
/**
 * Implementeaza interfata Subject
 * @author Ali
 */
public class ImplSubject implements Subject
{
    List obs = new ArrayList();
    String state = "";

    public void addObserver(Observer o)
    {
        obs.add( o );
    }

    public void removeObserver(Observer o)
    {
        obs.remove(o);
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state =  state;
        notifyObservers();
    }

    public void notifyObservers()
    {
        Iterator i = obs.iterator();
        while (i.hasNext())
            {
            Observer o = (Observer) i.next();
            o.update(this);
            }
    }
}

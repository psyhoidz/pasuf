/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package state3;

/**
 *
 * @author Ali
 */
public class ProjectCurrentState {

    private State m_current_state;

    public ProjectCurrentState() {
        m_current_state = new RequirementsStage();
    }

    public void set_state(State s) {
        m_current_state = s;
    }

    public void pull() {
        m_current_state.pull();
    }
}

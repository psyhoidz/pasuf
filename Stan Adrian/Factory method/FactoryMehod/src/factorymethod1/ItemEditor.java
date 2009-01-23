/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package factorymethod1;

import javax.swing.JComponent;

interface ItemEditor {
    public JComponent getGUI();
    public void commitChanges();
}
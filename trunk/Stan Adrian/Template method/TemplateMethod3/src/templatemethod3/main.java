/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package templatemethod3;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;


public class main extends JFrame {
  stdTriangle t, t1;

  IsocelesTriangle it;

  public main() {
    super("Template Method - Draw triangles");
    addWindowListener(new WindowAdapter() {
            @Override
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    TPanel tp = new TPanel();
    t = new stdTriangle(new Point(10, 10), new Point(150, 50), new Point(
        100, 75));
    it = new IsocelesTriangle(new Point(150, 100), new Point(240, 40),
        new Point(175, 150));
    t1 = new stdTriangle(new Point(150, 100), new Point(240, 40),
        new Point(175, 150));
    tp.addTriangle(t);
    tp.addTriangle(it);
    tp.addTriangle(t1);

    getContentPane().add(tp);
    setSize(300, 200);
    setBackground(Color.white);
    setVisible(true);
  }

  public static void main(String[] arg) {
    new main();
  }
}
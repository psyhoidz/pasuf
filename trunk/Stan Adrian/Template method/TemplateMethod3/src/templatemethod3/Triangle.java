/*
 *Sa consideram un proram simplu pentru deseare de triunghiuri.
 * Incepem cu o clasa abstracta denumita tiunghi, si apoi derivam niste
 * triunghiuri speciale din ea


 */

package templatemethod3;

import java.awt.Graphics;
import java.awt.Point;

abstract class Triangle {
  Point p1, p2, p3;

  public Triangle(Point a, Point b, Point c) {
    //salveaza punctele
    p1 = a;
    p2 = b;
    p3 = c;
  }

  public void draw(Graphics g) {
    //Aceasta metoda deseneaza un triunghi
    drawLine(g, p1, p2);
    Point current = draw2ndLine(g, p2, p3);
    closeTriangle(g, current);
  }

  public void drawLine(Graphics g, Point a, Point b) {
     //aceasta metoda desneaza prima linie 
    g.drawLine(a.x, a.y, b.x, b.y);
  }

  //aceasta metoda trebuie implementata separat pentru fiecare dintre
  //  triunghiuri
  abstract public Point draw2ndLine(Graphics g, Point a, Point b);

  public void closeTriangle(Graphics g, Point c) {
    //deseneaza ultima linie
    g.drawLine(c.x, c.y, p1.x, p1.y);
  }

}

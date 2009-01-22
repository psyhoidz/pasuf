/**
 * Clasa ValuareCurentaClock implementeaza interfata Observer si
 * extinde canvasul folosit ca suprafata de lucru pentru afisare.
 * Deseneaza un arc de marimea numarului aleator.
 */
package observer2;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;

/**Implementeaza interfata Observer si
 * extinde canvasul folosit ca suprafata de lucru pentru afisare.
 * Deseneaza un arc de marimea numarului aleator.
 * @author Ali
 */
public class ValuareCurentaClock extends Canvas implements Observer {

    private int ultimaVal = 0;

    /** Notifica obiectele atunci cand se face o schimbare*/
    public void upDate(Subject s) {
        ultimaVal = s.getVal();
        repaint();
    }

    /** Deseneaza obiectele*/
    @Override
    public void paint(Graphics g) {
        int d = 80;
        /** Diamnetrul cercului*/
        int xc = d / 2;
        /** Centrul cercului pe x*/
        int yc = d / 2;
         /** Centrul cercului pe y*/
        g.drawOval(0, 0, d, d);
        g.drawLine(xc, yc, xc, 0);
        g.drawLine(xc, yc, d, yc);

        g.setColor(Color.RED);
        g.fillArc(0, 0, 80, 80, 0, ultimaVal);
    }
}

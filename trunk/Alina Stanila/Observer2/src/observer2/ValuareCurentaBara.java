/**
 * Clasa ValuareCurentaBara implementeaza interfata Observer si
 * extinde canvasul folosit ca suprafata de lucru pentru afisare.
 * Deseneaza o bara de lungimea numarului aleator.
 */
package observer2;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;


/** Implementeaza interfata Observer si
 * extinde canvasul folosit ca suprafata de lucru pentru afisare.
 * Deseneaza o bara de lungimea numarului aleator.
 * @author Ali
 */
public class ValuareCurentaBara extends Canvas implements Observer {

    private int ultimaVal = 0;

    /** Notifica obiectele atunci cand se face o schimbare*/
    public void upDate(Subject s) {
        ultimaVal = s.getVal();
        repaint();
    }

    /** Update-uie interfata grafica*/
    @Override
    public void update(Graphics g) {
        super.update(g);
    }

    /** Deseneaza obiectele*/
    @Override
    public void paint(Graphics g) {
        g.drawRect(0, 0, 80, 20);
        g.setColor(Color.blue);
        g.fillRect(0, 0, ultimaVal, 20);
    }
}

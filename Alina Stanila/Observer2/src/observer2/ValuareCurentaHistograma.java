/**
 * Clasa ValuareCurentaClock implementeaza interfata Observer, ComponentListener si
 * extinde canvasul folosit ca suprafata de lucru pentru afisare.
 * Deseneaza o histograma.
 */
package observer2;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**Implementeaza interfata Observer, ComponentListener si
 * extinde canvasul folosit ca suprafata de lucru pentru afisare.
 * Deseneaza o histograma.
 * @author Ali
 */
public class ValuareCurentaHistograma extends Canvas implements Observer, ComponentListener {

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        count = getWidth();
        valori = new int[count];
    }

    public void componentShown(ComponentEvent e) {
        count = getWidth();
        valori = new int[count];
    }
    private int count = 100;
    private int[] valori = new int[count];

    public ValuareCurentaHistograma() {
        addComponentListener(this);
    }

    /** Notifica obiectele atunci cand se face o schimbare*/
    public void upDate(Subject s) {
        for (int i = 0; i < count - 1; i++) {
            valori[i] = valori[i + 1];
        }
        valori[count - 1] = s.getVal();
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
        int w = 1;
        for (int i = 0; i < count; i++) {
            g.fillRect(0 + w * i, 0, w, valori[i]);
        }
    }
}

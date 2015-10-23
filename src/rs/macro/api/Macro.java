package rs.macro.api;

import rs.macro.api.methods.RuneScape;
import rs.macro.api.util.Time;
import rs.macro.api.util.fx.PixelOperator;
import rs.macro.internal.ui.MacroSelector;

import java.awt.*;
import java.awt.event.*;

/**
 * @author Tyler Sedlar
 * @since 4/30/2015
 */
public abstract class Macro extends Thread implements MouseListener,
        MouseMotionListener, MouseWheelListener, KeyListener {

    private boolean interrupted;
    private long startTime;

    public void atStart() {
    }

    public void atEnd() {
    }

    public long startTime() {
        return startTime;
    }

    public long runtime() {
        return Time.millis() - startTime;
    }

    public abstract int loop();

    public Manifest manifest() {
        return getClass().getAnnotation(Manifest.class);
    }


    public PixelOperator operator() {
        return RuneScape.pixels().operator();
    }

    @Override
    public void interrupt() {
        super.interrupt();
        interrupted = true;
    }

    @Override
    public boolean isInterrupted() {
        return interrupted;
    }

    @Override
    public final void run() {
        startTime = Time.millis();
        while (!interrupted) {
            if (Time.overdue()) {
                break;
            }
            int loop = loop();
            if (loop < 0) {
                break;
            }
            Time.sleep(loop);
        }
        MacroSelector.unsetSlave();
    }

    public final void addRuntimeCallback(long everyMillis, Runnable callback) {
        new Thread(() -> {
            while (!interrupted) {
                Time.sleep((int) everyMillis);
                try {
                    if (!interrupted) {
                        callback.run();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public final void handle(AWTEvent e) {
        if (e instanceof MouseEvent) {
            if (e instanceof MouseWheelEvent) {
                mouseWheelMoved((MouseWheelEvent) e);
            } else {
                if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                    mousePressed((MouseEvent) e);
                } else if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                    mouseReleased((MouseEvent) e);
                } else if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                    mouseClicked((MouseEvent) e);
                } else if (e.getID() == MouseEvent.MOUSE_MOVED) {
                    mouseMoved((MouseEvent) e);
                } else if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
                    mouseDragged((MouseEvent) e);
                }
            }
        } else if (e instanceof KeyEvent) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                keyPressed((KeyEvent) e);
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                keyReleased((KeyEvent) e);
            } else if (e.getID() == KeyEvent.KEY_TYPED) {
                keyTyped((KeyEvent) e);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
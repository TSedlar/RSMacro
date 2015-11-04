package rs.macro.api;

import rs.macro.api.access.RuneScape;
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
    private boolean paused;

    /**
     * Method which handles what happens at the start of a macro.
     */
    public void atStart() {
    }

    /**
     * Method which handles what happens at the end of a macro.
     */
    public void atEnd() {
    }

    /**
     * @return The starting time of the macro.
     */
    public long startTime() {
        return startTime;
    }

    /**
     * @return The runtime of the macro.
     */
    public long runtime() {
        return Time.millis() - startTime;
    }

    public abstract int loop();

    /**
     * @return The macro's manifest file.
     */
    public Manifest manifest() {
        return getClass().getAnnotation(Manifest.class);
    }

    /**
     * @return The macro's PixelOperator.
     */
    public PixelOperator operator() {
        return RuneScape.pixels().operator();
    }

    /**
     * Sets the macro's pause state.
     *
     * @param paused <t>true</t> to pause the macro; otherwise, <t>false</t>.
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * Interrupts the macro.
     */
    @Override
    public void interrupt() {
        super.interrupt();
        interrupted = true;
    }

    /**
     * @return <t>true</t> if the macro is interrupted; otherwise, <t>false</t>.
     */
    @Override
    public boolean isInterrupted() {
        return interrupted;
    }

    /**
     * Handles the running of the macro.
     */
    @Override
    public final void run() {
        startTime = Time.millis();
        while (!interrupted) {
            if (Time.overdue()) {
                System.out.println("Runtime is overdue.");
                break;
            }
            if (RuneScape.pixels().operator().image() == null) {
                Time.sleep(500, 1000);
            }
            if (paused) {
                Time.sleep(50, 100);
                continue;
            }
            int loop;
            try {
                loop = loop();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            if (loop < 0) {
                break;
            }
            Time.sleep(loop);
        }
        MacroSelector.unsetMacro();
    }

    /**
     * Submits a callback.
     *
     * @param everyMillis The number of milliseconds to run the callback.
     * @param callback    The callback to submit.
     */
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

    /**
     * Handles the dispatched mouse and key events.
     *
     * @param e The AWTEvent to handle.
     */
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
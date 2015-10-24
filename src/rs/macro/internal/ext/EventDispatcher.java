package rs.macro.internal.ext;

import rs.macro.api.methods.Environment;
import rs.macro.api.util.Random;
import rs.macro.api.util.Time;
import rs.macro.internal.ui.MacroSelector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public class EventDispatcher {

    private static final String MASK = "automated";

    private final GameCanvas canvas;
    private final EventQueue queue;

    public int mouseX, mouseY;
    public long pressTime;

    /**
     * Creates the EventDispatcher object.
     *
     * @param canvas The GameCanvas to attach this EventDispatcher to.
     */
    public EventDispatcher(GameCanvas canvas) {
        this.canvas = canvas;
        canvas.requestFocusInWindow();
        queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
        MouseListener mouseListener = canvas.getMouseListeners()[0];
        MouseMotionListener motionListener = canvas.getMouseMotionListeners()[0];
        MouseWheelListener wheelListener = canvas.getMouseWheelListeners()[0];
        KeyListener keyListener = canvas.getKeyListeners()[0];
        FocusListener focusListener = canvas.getFocusListeners()[0];
        queue.push(new EventQueue() {
            public void dispatchEvent(AWTEvent evt) {
                boolean consumed = false;
                if (!evt.getSource().equals(MASK)) {
                    if (MacroSelector.current() != null) {
                        MacroSelector.current().handle(evt);
                    }
                    boolean blocked = false;
                    if (evt instanceof MouseEvent) {
                        blocked = (Environment.input() & Environment.INPUT_MOUSE) !=
                                Environment.INPUT_MOUSE;
                        consumed = ((MouseEvent) evt).isConsumed();
                    } else if (evt instanceof KeyEvent) {
                        blocked = (Environment.input() & Environment.INPUT_KEYBOARD) !=
                                Environment.INPUT_KEYBOARD;
                        consumed = ((KeyEvent) evt).isConsumed();
                    }
                    if (blocked) {
                        if (!evt.getSource().equals(canvas)) {
                            super.dispatchEvent(evt);
                        }
                        return;
                    } else if (consumed) {
                        return;
                    } else {
                        super.dispatchEvent(evt);
                        return;
                    }
                }
                evt.setSource(canvas);
                if (evt instanceof MouseEvent) {
                    MouseEvent e = (MouseEvent) evt;
                    if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                        mouseListener.mousePressed(e);
                    } else if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                        mouseListener.mouseReleased(e);
                    } else if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                        mouseListener.mouseClicked(e);
                    } else if (e.getID() == MouseEvent.MOUSE_ENTERED) {
                        mouseListener.mouseEntered(e);
                    } else if (e.getID() == MouseEvent.MOUSE_EXITED) {
                        mouseListener.mouseExited(e);
                    } else if (e.getID() == MouseEvent.MOUSE_MOVED) {
                        motionListener.mouseMoved(e);
                    } else if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
                        motionListener.mouseDragged(e);
                    } else if (e instanceof MouseWheelEvent) {
                        wheelListener.mouseWheelMoved((MouseWheelEvent) e);
                    }
                } else if (evt instanceof KeyEvent) {
                    KeyEvent e = (KeyEvent) evt;
                    if (e.getID() == KeyEvent.KEY_PRESSED) {
                        keyListener.keyPressed(e);
                    } else if (e.getID() == KeyEvent.KEY_TYPED) {
                        keyListener.keyTyped(e);
                    } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                        keyListener.keyReleased(e);
                    }
                } else if (evt instanceof FocusEvent) {
                    FocusEvent e = (FocusEvent) evt;
                    if (e.getID() == FocusEvent.FOCUS_GAINED) {
                        focusListener.focusGained(e);
                    } else if (e.getID() == FocusEvent.FOCUS_LOST) {
                        focusListener.focusLost(e);
                    }
                }
            }
        });
        canvas.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                pressTime = Time.millis();
            }
        });
        canvas.addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }

            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
    }

    /**
     * Masks the given AWTEvent to have an 'automated' source.
     *
     * @param e The AWTEvent to mask.
     * @return The masked AWTEvent.
     */
    private AWTEvent mask(AWTEvent e) {
        e.setSource(MASK);
        return e;
    }

    /**
     * Creates a masked MouseEvent with the given arguments.
     *
     * @param type       The type of event.
     * @param x          The X coordinate.
     * @param y          The Y coordinate.
     * @param button     The button to press.
     * @param timeOffset The time to delay.
     * @return A masked MouseEvent with the given arguments.
     */
    private AWTEvent generateMouseEvent(int type, int x, int y, int button, int timeOffset) {
        return mask(new MouseEvent(canvas, type, System.currentTimeMillis() + timeOffset,
                0, x, y, button != MouseEvent.MOUSE_MOVED ? 1 : 0, false, button));
    }

    /**
     * Creates a masked MouseEvent with the given arguments.
     *
     * @param type   The type of event.
     * @param x      The X coordinate.
     * @param y      The Y coordinate.
     * @param button The button to press.
     * @return A masked MouseEvent with the given arguments.
     */
    private AWTEvent generateMouseEvent(int type, int x, int y, int button) {
        return generateMouseEvent(type, x, y, button, 0);
    }

    /**
     * Moves the mouse to the given location.
     *
     * @param x The X coordinate.
     * @param y The Y coordinate.
     */
    public void moveMouse(int x, int y) {
        queue.postEvent(generateMouseEvent(MouseEvent.MOUSE_MOVED, (mouseX = x), (mouseY = y),
                MouseEvent.NOBUTTON));
    }

    /**
     * Presses the mouse left/right button relative to the given argument.
     *
     * @param left <t>true</t> to press left, otherwise <t>false</t> to press right.
     */
    public void pressMouse(boolean left) {
        pressTime = Time.millis();
        queue.postEvent(generateMouseEvent(MouseEvent.MOUSE_PRESSED, mouseX, mouseY,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3));
    }

    /**
     * Releases the mouse left/right button relative to the given argument.
     *
     * @param left <t>true</t> to release left, otherwise <t>false</t> to release left.
     */
    public void releaseMouse(boolean left) {
        int offset = Random.nextInt(20, 30);
        queue.postEvent(generateMouseEvent(MouseEvent.MOUSE_RELEASED, mouseX, mouseY,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3, offset));
        queue.postEvent(generateMouseEvent(MouseEvent.MOUSE_CLICKED, mouseX, mouseY,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3, offset));
    }

    /**
     * Clicks the mouse left/right button relative to the given argument.
     *
     * @param left <t>true</t> to click left, otherwise <t>false</t> to click right.
     */
    public void clickMouse(boolean left) {
        pressMouse(left);
        releaseMouse(left);
    }

    /**
     * Scrolls the mouse wheel in the given direction.
     *
     * @param up     <t>true</t> to scroll upwards, otherwise <t>false</t>.
     * @param clicks The amount of 'clicks' to move the wheel.
     */
    public void scrollMouse(boolean up, int clicks) {
        queue.postEvent(new MouseWheelEvent(canvas, MouseEvent.MOUSE_WHEEL,
                System.currentTimeMillis(), 0, mouseX, mouseY, 0, false,
                MouseWheelEvent.WHEEL_UNIT_SCROLL, 1, up ? -clicks : clicks));
    }

    /**
     * Generates a KeyEvent with the given arguments.
     *
     * @param key        The 'key code' to be used.
     * @param type       The type of KeyEvent.
     * @param timeOffset The time to delay.
     * @return A KeyEvent with the given arguments.
     */
    private KeyEvent generateKeyEvent(int key, int type, int timeOffset) {
        KeyStroke stroke = KeyStroke.getKeyStroke(key, 0);
        return new KeyEvent(canvas, type, System.currentTimeMillis() + timeOffset,
                stroke.getModifiers(), stroke.getKeyCode(), stroke.getKeyChar());
    }

    /**
     * Generates a KeyEvent with the given arguments.
     *
     * @param key        The 'key char' to be used.
     * @param type       The type of KeyEvent.
     * @param timeOffset The time to delay.
     * @return A KeyEvent with the given arguments.
     */
    private KeyEvent generateKeyEvent(char key, int type, int timeOffset) {
        KeyStroke stroke = KeyStroke.getKeyStroke(key);
        return new KeyEvent(canvas, type, System.currentTimeMillis() + timeOffset,
                stroke.getModifiers(), stroke.getKeyCode(), key);
    }

    /**
     * Presses the given key.
     *
     * @param key The 'key char' to press.
     */
    public void pressKey(char key) {
        int code = (int) key;
        queue.postEvent(generateKeyEvent(code, KeyEvent.KEY_PRESSED, 0));
        boolean arrow = code >= KeyEvent.VK_LEFT && code <= KeyEvent.VK_DOWN;
        if (!arrow) {
            queue.postEvent(generateKeyEvent(key, KeyEvent.KEY_TYPED, 0));
        }
    }

    /**
     * Releases the given key.
     *
     * @param key The 'key char' to release.
     */
    public void releaseKey(char key) {
        queue.postEvent(generateKeyEvent((int) key, KeyEvent.KEY_RELEASED,
                Random.nextInt(20, 30)));
    }

    /**
     * Types the given key.
     *
     * @param key The 'key char' to type.
     */
    public void typeKey(char key) {
        pressKey(key);
        releaseKey(key);
    }

    /**
     * Types the given string.
     *
     * @param string The string to type.
     */
    public void type(String string) {
        for (char c : string.toCharArray()) {
            typeKey(c);
            Time.sleep(70, 100);
        }
    }
}

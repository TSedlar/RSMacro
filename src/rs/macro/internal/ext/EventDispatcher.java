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

    private final GameCanvas canvas;
    private final EventQueue queue;

    public int mouseX, mouseY;
    public long pressTime;

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
                if (!evt.getSource().equals("bot")) {
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

    private AWTEvent mask(AWTEvent e) {
        e.setSource("bot");
        return e;
    }

    private AWTEvent generateMouseEvent(int type, int x, int y, int button, int timeOffset) {
        return mask(new MouseEvent(canvas, type, System.currentTimeMillis() + timeOffset, 0, x, y,
                button != MouseEvent.MOUSE_MOVED ? 1 : 0, false, button));
    }

    private AWTEvent generateMouseEvent(int type, int x, int y, int button) {
        return generateMouseEvent(type, x, y, button, 0);
    }

    public void moveMouse(int x, int y) {
        queue.postEvent(generateMouseEvent(MouseEvent.MOUSE_MOVED, (mouseX = x), (mouseY = y),
                MouseEvent.NOBUTTON));
    }

    public void pressMouse(boolean left) {
        pressTime = Time.millis();
        queue.postEvent(generateMouseEvent(MouseEvent.MOUSE_PRESSED, mouseX, mouseY,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3));
    }

    public void releaseMouse(boolean left) {
        int offset = Random.nextInt(20, 30);
        queue.postEvent(generateMouseEvent(MouseEvent.MOUSE_RELEASED, mouseX, mouseY,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3, offset));
        queue.postEvent(generateMouseEvent(MouseEvent.MOUSE_CLICKED, mouseX, mouseY,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3, offset));
    }

    public void clickMouse(boolean left) {
        pressMouse(left);
        releaseMouse(left);
    }

    public void scrollMouse(boolean up, int clicks) {
        queue.postEvent(new MouseWheelEvent(canvas, MouseEvent.MOUSE_WHEEL, System.currentTimeMillis(), 0,
                mouseX, mouseY, 0, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, 1, up ? -clicks : clicks));
    }

    private KeyEvent generateKeyEvent(int key, int type, int timeOffset) {
        KeyStroke stroke = KeyStroke.getKeyStroke(key, 0);
        return new KeyEvent(canvas, type, System.currentTimeMillis() + timeOffset,
                stroke.getModifiers(), stroke.getKeyCode(), stroke.getKeyChar());
    }

    private KeyEvent generateKeyEvent(char key, int type, int timeOffset) {
        KeyStroke stroke = KeyStroke.getKeyStroke(key);
        return new KeyEvent(canvas, type, System.currentTimeMillis() + timeOffset,
                stroke.getModifiers(), stroke.getKeyCode(), key);
    }

    public void pressKey(char key) {
        int code = (int) key;
        queue.postEvent(generateKeyEvent(code, KeyEvent.KEY_PRESSED, 0));
        boolean arrow = code >= KeyEvent.VK_LEFT && code <= KeyEvent.VK_DOWN;
        if (!arrow) {
            queue.postEvent(generateKeyEvent(key, KeyEvent.KEY_TYPED, 0));
        }
    }

    public void releaseKey(char key) {
        queue.postEvent(generateKeyEvent((int) key, KeyEvent.KEY_RELEASED, Random.nextInt(20, 30)));
    }

    public void typeKey(char key) {
        pressKey(key);
        releaseKey(key);
    }

    public void type(String string) {
        for (char c : string.toCharArray()) {
            typeKey(c);
            Time.sleep(70, 100);
        }
    }
}

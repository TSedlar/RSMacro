package java.awt;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import java.awt.image.BufferStrategy;

public class Canvas extends GameCanvas implements Accessible {

    private static final long serialVersionUID = -2284879212465893870L;
    private static final String baseName = "canvas";

    private static int nameCounter = 0;

    public Canvas() {
    }

    public Canvas(GraphicsConfiguration config) {
        this.setGraphicsConfiguration(config);
    }

    @Override
    public boolean hasFocus() {
        return true;
    }

    @Override
    public Graphics getGraphics() {
        return super.getGraphics();
    }

    @Override
    public String constructComponentName() {
        synchronized (Canvas.class) {
            return baseName + nameCounter++;
        }
    }

    @Override
    public void addNotify() {
        synchronized (getTreeLock()) {
            if (this.peer == null) {
                this.peer = getToolkit().createCanvas(this);
            }
            super.addNotify();
        }
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, this.width, this.height);
    }

    @Override
    public void update(Graphics g) {
        g.clearRect(0, 0, this.width, this.height);
        paint(g);
    }

    @Override
    public boolean postsOldMouseEvents() {
        return true;
    }

    @Override
    public void createBufferStrategy(int numBuffers) {
        super.createBufferStrategy(numBuffers);
    }

    @Override
    public void createBufferStrategy(int numBuffers, BufferCapabilities caps)
            throws AWTException {
        super.createBufferStrategy(numBuffers, caps);
    }

    @Override
    public BufferStrategy getBufferStrategy() {
        return super.getBufferStrategy();
    }

    @Override
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTCanvas();
        }
        return this.accessibleContext;
    }

    protected class AccessibleAWTCanvas extends Component.AccessibleAWTComponent {

        private static final long serialVersionUID = -6325592262103146699L;

        protected AccessibleAWTCanvas() {
            super();
        }

        @Override
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.CANVAS;
        }
    }
}
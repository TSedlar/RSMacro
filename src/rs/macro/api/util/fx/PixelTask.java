package rs.macro.api.util.fx;

import rs.macro.api.Macro;
import rs.macro.api.util.Imaging;
import rs.macro.api.util.LoopTask;
import rs.macro.api.util.fx.listener.PixelListener;
import rs.macro.internal.ui.MacroSelector;

import java.awt.image.BufferedImage;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public class PixelTask extends LoopTask {

    private BufferedImage image;
    private int[] pixels;
    private int delay = 100;
    private PixelOperator operator = new PixelOperator();

    /**
     * Constructs a PixelTask using the following argument:
     *
     * @param image The BufferedImage.
     */
    public PixelTask(BufferedImage image) {
        if (image != null) {
            setImage(image);
        }
    }

    /**
     * Constructs a PixelTask using a null image.
     */
    public PixelTask() {
        this(null);
    }

    /**
     * @return The PixelOperator.
     */
    public PixelOperator operator() {
        return operator;
    }

    /**
     * Sets the BufferedImage
     *
     * @param image The BufferedImage to set.
     */
    public void setImage(BufferedImage image) {
        if (this.image != null) {
            this.pixels = null;
            this.image.flush();
            this.image = null;
        }
        if (image != null) {
            this.image = image;
        }
    }

    /**
     * Sets the task's delay.
     *
     * @param delay The delay.
     */
    public void setLoopDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public final int loop() {
        if (image != null) {
            pixels = Imaging.pixelsFor(image);
            operator.setData(image, pixels);
            Macro macro = MacroSelector.current();
            if (macro != null && macro instanceof PixelListener) {
                ((PixelListener) macro).onPixelsUpdated(operator);
            }
        }
        return delay;
    }
}

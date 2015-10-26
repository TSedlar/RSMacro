package java.awt;

import java.awt.image.BufferedImage;

public class GameCanvas extends Component {

    public static final Dimension GAME_SIZE = new Dimension(765, 503);

    public static BufferedImage buffer, raw;

    public static CanvasRender render = null;

    public static GameCanvas instance;

    public GameCanvas() {
        raw = new BufferedImage(GAME_SIZE.width, GAME_SIZE.height, BufferedImage.TYPE_3BYTE_BGR);
        buffer = new BufferedImage(GAME_SIZE.width, GAME_SIZE.height, BufferedImage.TYPE_3BYTE_BGR);
        instance = this;
    }

    @Override
    public boolean hasFocus() {
        return true;
    }

    @Override
    public Graphics getGraphics() {
        Graphics g = super.getGraphics();
        if (g != null && buffer != null && raw != null) {
            Graphics2D paint = buffer.createGraphics();
            paint.drawImage(raw, 0, 0, null);
            try {
                if (render != null) {
                    render.render(paint);
                }
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
            paint.dispose();
            g.drawImage(buffer, 0, 0, null);
            g.dispose();
            return raw.createGraphics();
        }
        return null;
    }
}
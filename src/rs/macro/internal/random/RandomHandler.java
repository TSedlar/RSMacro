package rs.macro.internal.random;

import rs.macro.api.Macro;
import rs.macro.api.access.RuneScape;
import rs.macro.api.access.input.Mouse;
import rs.macro.api.util.LoopTask;
import rs.macro.api.util.Random;
import rs.macro.api.util.Renderable;
import rs.macro.api.util.fx.Text;
import rs.macro.internal.ui.MacroSelector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyler Sedlar
 * @since 10/23/15
 */
public class RandomHandler extends LoopTask implements Renderable {

    private static final Color RANDOM_MOUSE_COLOR = new Color(100, 100, 200, 100);
    private static final Color RANDOM_TEXT_BACKGROUND = new Color(40, 40, 40, 150);
    private static final int MOUSE_THICKNESS = 6;

    private final List<RandomEvent> events = new ArrayList<>();

    private RandomEvent current = null;
    private String label;

    /**
     * A list of RandomEvents that are being handled.
     *
     * @return A list of RandomEvents that are being handled.
     */
    public List<RandomEvent> events() {
        return events;
    }

    /**
     * Adds the given RandomEvent into the list of events to be handled.
     *
     * @param event The RandomEvent to be handled.
     */
    public void submit(RandomEvent event) {
        events.add(event);
    }

    /**
     * When stopping the handler, all RandomEvents are set to not being solved.
     */
    @Override
    public void atEnd() {
        for (RandomEvent random : events) {
            random.solving = false;
        }
    }

    @Override
    public void render(Graphics2D g) {
        if (current == null) {
            return;
        }
        int mx = Mouse.x(), my = Mouse.y();
        int width = RuneScape.image().getWidth();
        int height = RuneScape.image().getHeight();
        int thickness = (MOUSE_THICKNESS / 2);
        g.setColor(RANDOM_MOUSE_COLOR);
        g.fillRect(0, 0, mx - thickness, my - thickness);
        g.fillRect(mx + thickness, 0, width - mx - thickness, my - thickness);
        g.fillRect(0, my + thickness, mx - thickness, height - my - thickness);
        g.fillRect(mx + thickness, my + thickness, width - mx - thickness,
                height - my - thickness);
        g.setColor(RANDOM_TEXT_BACKGROUND);
        g.fillRect(0, height - 15, width, 15);
        int textWidth = g.getFontMetrics().stringWidth(label);
        Text.drawRuneString(g, label, (width / 2) - (textWidth / 2), height - 3, Color.YELLOW);
    }

    @Override
    public int loop() {
        for (RandomEvent event : events) {
            if (event.solving) {
                boolean solved = false;
                try {
                    solved = event.solve();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (solved) {
                    event.solving = false;
                    Macro macro = MacroSelector.current();
                    if (macro != null) {
                        macro.setPaused(false);
                    }
                }
                return 0;
            }
        }
        for (RandomEvent event : events) {
            if (event.activate()) {
                (current = event).solving = true;
                RandomManifest manifest = event.getClass().getAnnotation(RandomManifest.class);
                System.out.println(String.format("[Random Event] Started %s v%s by %s",
                        manifest.name(), manifest.version(), manifest.author()));
                label = String.format("%s v%s by %s", manifest.name(),
                        manifest.version(), manifest.author());
                Macro macro = MacroSelector.current();
                if (macro != null) {
                    macro.setPaused(true);
                }
                return 0;
            }
        }
        return Random.nextInt(50, 100);
    }
}

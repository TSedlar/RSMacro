package rs.macro.internal.random;

import rs.macro.api.Macro;
import rs.macro.api.util.LoopTask;
import rs.macro.api.util.Random;
import rs.macro.internal.ui.MacroSelector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyler Sedlar
 * @since 10/23/15
 */
public class RandomHandler extends LoopTask {

    private final List<RandomEvent> events = new ArrayList<>();

    public List<RandomEvent> events() {
        return events;
    }

    public void submit(RandomEvent event) {
        events.add(event);
    }

    @Override
    public int loop() {
        for (RandomEvent event : events) {
            if (event.solving) {
                if (event.solve()) {
                    Macro macro = MacroSelector.current();
                    if (macro != null) {
                        macro.setPaused(false);
                    }
                    event.solving = false;
                }
                return 0;
            }
        }
        for (RandomEvent event : events) {
            if (event.activate()) {
                RandomManifest manifest = event.getClass().getAnnotation(RandomManifest.class);
                System.out.println(String.format("[Random Event] Started %s v%s by %s",
                        manifest.name(), manifest.version(), manifest.author()));
                Macro macro = MacroSelector.current();
                if (macro != null) {
                    macro.setPaused(true);
                }
                event.solving = true;
                return 0;
            }
        }
        return Random.nextInt(50, 100);
    }
}

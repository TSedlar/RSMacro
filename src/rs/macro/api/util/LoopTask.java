package rs.macro.api.util;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public abstract class LoopTask {

    private boolean running;
    private Thread thread;

    public abstract int loop();

    public final void start() {
        if (running) {
            return;
        }
        this.running = true;
        this.thread = new Thread(() -> {
            int loop = -1;
            do {
                try {
                    loop = loop();
                    Time.sleep(loop);
                } catch (Exception e) {
                    e.printStackTrace();
                    running = false;
                }
            } while (loop > 0 && running && !thread.isInterrupted());
            stop();
        });
        this.thread.start();
    }

    public final void stop() {
        if (!running) {
            return;
        }
        running = false;
        thread.interrupt();
    }
}

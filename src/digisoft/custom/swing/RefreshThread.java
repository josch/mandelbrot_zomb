package digisoft.custom.swing;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/04/16
 */
public class RefreshThread extends Thread {

    private double delay;
    private RefreshListener runnable;

    public RefreshThread(RefreshListener refreshListener, int fps) {
        super();

        runnable = refreshListener;
        delay = 1e9 / fps;
    }

    @Override
    public void run() {
        try {
            double t = System.nanoTime();
            while (true) {
                runnable.refreshing();

                t += delay;

                long sleepTime = (long) ((t - System.nanoTime()) / 1e6);

                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

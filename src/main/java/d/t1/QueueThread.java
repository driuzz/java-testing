package d.t1;

import java.util.Random;

public abstract class QueueThread implements Runnable {
    private static final Random RNG = new Random();

    protected final SimpleQueue queue;
    protected final Thread thread;

    protected boolean running = false;

    protected QueueThread(SimpleQueue queue) {
        this.queue = queue;
        this.thread = new Thread(this);
    }

    public void start() {
        running = true;
        thread.start();
    }

    public void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static long random(int min, int max, long factor) {
        if (max > min) {
            return (RNG.nextInt(max - min) + min) * factor;
        } else {
            return min * factor;
        }
    }

    public static void sleep(int min, int max) {
        try {
            long millis = random(min, max, 100);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

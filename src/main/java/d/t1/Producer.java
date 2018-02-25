package d.t1;

public class Producer extends QueueThread {
    private final int min;
    private final int max;

    public Producer(SimpleQueue queue, int min, int max) {
        super(queue);
        this.min = min;
        this.max = max;
    }

    @Override
    public void run() {
        while (running) {
            queue.put(random(0, 20, 1));
            sleep(min, max);
        }
    }
}

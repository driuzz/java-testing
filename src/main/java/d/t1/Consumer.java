package d.t1;

public class Consumer extends QueueThread {
    private final int min;
    private final int max;

    public Consumer(SimpleQueue queue, int min, int max) {
        super(queue);
        this.min = min;
        this.max = max;
    }

    @Override
    public void run() {
        while (running || !queue.isEmpty()) {
            queue.pop();
            sleep(min, max);
        }
    }
}

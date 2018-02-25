package d.t1;

import java.util.ArrayDeque;
import java.util.Queue;

public class SimpleQueue {
    private static final int SIZE = 5;

    private Queue<Long> queue = new ArrayDeque<>(SIZE);

    public SimpleQueue() {
    }

    public synchronized void put(Long value) {
        int repeats = 3;
        while (isFull() && repeats-- > 0) {
            System.out.println("Queue.put: " + queue + " is full, awaiting: " + value);
            wait1s();
        }

        if (!isFull()) {
            System.out.println("Queue.put: " + queue + " <- " + value);
            queue.offer(value);
        }

        notify();
    }

    public synchronized Long pop() {
        int repeats = 3;
        while (isEmpty() && repeats-- > 0) {
            System.out.println("Queue.pop: " + queue + " is empty, waiting...");
            wait1s();
        }

        Long value = -1L;
        if (!isEmpty()) {
            value = queue.poll();
            System.out.println("Queue.pop: " + value + " <- " + queue);
        }

        notify();
        return value;
    }

    private void wait1s() {
        try {
            wait(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized boolean isFull() {
        return queue.size() == SIZE;
    }
}

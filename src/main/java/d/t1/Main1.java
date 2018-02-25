package d.t1;

public class Main1 {

    public static void main(String[] args) {
        SimpleQueue queue = new SimpleQueue();
        Producer producer1 = new Producer(queue, 1, 11);
        Producer producer2 = new Producer(queue, 5, 25);
        Consumer consumer1 = new Consumer(queue, 3, 17);
        Consumer consumer2 = new Consumer(queue, 1, 11);

        System.out.println(">Producer1");
        producer1.start();

        System.out.println("Sleep: 2000");
        QueueThread.sleep(20, 20);

        System.out.println(">Consumer1");
        consumer1.start();

        System.out.println("Sleep: 10000");
        QueueThread.sleep(100, 100);

        System.out.println(">Producer2");
        producer2.start();
        System.out.println(">Consumer2");
        consumer2.start();

        System.out.println("Sleep: 10000");
        QueueThread.sleep(100, 100);

        System.out.println("<Producer1");
        producer1.stop();
        System.out.println("<Consumer1");
        consumer1.stop();
        System.out.println("<Producer2");
        producer2.stop();
        System.out.println("<Consumer2");
        consumer2.stop();
    }
}

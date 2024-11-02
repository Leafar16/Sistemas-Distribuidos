package Guiao4;

public class Barrier2Test {

    public static void main(String[] args) {
        int numThreads = 5; // Number of threads to test the barrier with
        Barrier2 barrier = new Barrier2(numThreads);

        // Create and start multiple threads
        for (int i = 0; i < numThreads; i++) {
            new Thread(new Worker(barrier, i)).start();
        }
    }
}

class Worker implements Runnable {
    private final Barrier2 barrier;
    private final int id;

    public Worker(Barrier2 barrier, int id) {
        this.barrier = barrier;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            System.out.println("Thread " + id + " is waiting at the barrier.");
            barrier.await();
            System.out.println("Thread " + id + " has crossed the barrier.");
        } catch (InterruptedException e) {
            System.err.println("Thread " + id + " was interrupted.");
        }
    }
}

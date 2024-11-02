package Guiao4;

public class BarrierTest {
    public static void main(String[] args) {
        int N = 10; // Number of threads that must reach the barrier before proceeding
        Barrier barrier = new Barrier(N);
        
        // Create and start N threads
        for (int i = 0; i < N; i++) {
            new Thread(new BarrierTask(barrier), "Thread-" + (i + 1)).start();
        }
    }
}

// Task that each thread will execute, which tests the barrier
class BarrierTask implements Runnable {
    private final Barrier barrier;

    public BarrierTask(Barrier barrier) {
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + " reached the barrier.");
            barrier.await(); // Wait at the barrier
            System.out.println(Thread.currentThread().getName() + " passed the barrier.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


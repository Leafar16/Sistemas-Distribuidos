package Guiao5;

import java.util.HashSet;
import java.util.Set;

public class TestWarehouse {

    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();

        // Supplier thread to supply items to the warehouse
        Thread supplier1 = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    warehouse.supply("itemA", 10);
                    warehouse.supply("itemB", 5);
                    System.out.println("Supplier1 added 10 of itemA and 5 of itemB");
                    Thread.sleep(1000); // Pause to simulate time between supplies
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Another supplier thread
        Thread supplier2 = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    warehouse.supply("itemB", 10);
                    warehouse.supply("itemC", 15);
                    System.out.println("Supplier2 added 10 of itemB and 15 of itemC");
                    Thread.sleep(1500); // Pause to simulate time between supplies
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Consumer thread that tries to consume items from the warehouse
        Thread consumer1 = new Thread(() -> {
            try {
                Set<String> itemsToConsume = new HashSet<>();
                itemsToConsume.add("itemA");
                itemsToConsume.add("itemB");
                while (true) {
                    warehouse.consume(itemsToConsume);
                    System.out.println("Consumer1 consumed 1 of itemA and 1 of itemB");
                    Thread.sleep(2000); // Pause to simulate time between consumptions
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Another consumer thread
        Thread consumer2 = new Thread(() -> {
            try {
                Set<String> itemsToConsume = new HashSet<>();
                itemsToConsume.add("itemB");
                itemsToConsume.add("itemC");
                while (true) {
                    warehouse.consume(itemsToConsume);
                    System.out.println("Consumer2 consumed 1 of itemB and 1 of itemC");
                    Thread.sleep(2500); // Pause to simulate time between consumptions
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Start all threads
        supplier1.start();
        supplier2.start();
        consumer1.start();
        consumer2.start();

        // Wait for threads to complete (for demonstration purposes only)
        try {
            supplier1.join();
            supplier2.join();
            consumer1.join();
            consumer2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

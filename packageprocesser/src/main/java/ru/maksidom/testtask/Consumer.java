package ru.maksidom.testtask;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable {
    private final BlockingQueue<PackageEntity> queue;

    public Consumer(BlockingQueue<PackageEntity> queue) {

        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                PackageEntity packElement = queue.take();
                packageProcess(packElement);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

    private void packageProcess(PackageEntity packageEnt) throws InterruptedException {
        System.out.println(Thread.currentThread() + " is working with package " + packageEnt.getContain());
        TimeUnit.SECONDS.sleep(1);
    }

}

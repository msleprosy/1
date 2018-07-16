package ru.maksidom.testtask;

import java.util.List;
import java.util.concurrent.*;

public class Producer {

    private final PackageRepository repository;
    private final ExecutorService executorService;
    private final BlockingQueue<PackageEntity> queue;


    public Producer(PackageRepository repository, int consumerCount) {
        this.queue = new LinkedBlockingDeque<>();
        this.repository = repository;
        this.executorService = Executors.newFixedThreadPool(consumerCount);
        for (int i = 0; i < consumerCount; i++) {
            executorService.execute(new Consumer(queue));
        }
    }

    public void start() {
        boolean stillWork = true;
        boolean stopDetect = false;
        long lastPackageId = 0;
        while (stillWork) {
            List<PackageEntity> byIdGreaterThan = repository.findByIdGreaterThan(lastPackageId);
            if (!stopDetect) {
                     if (byIdGreaterThan.size() != 0 ) {
                        lastPackageId = byIdGreaterThan.get(byIdGreaterThan.size() - 1).getId();
                    }
                for (PackageEntity packageEntity : byIdGreaterThan) {
                    if ("stop".equals(packageEntity.getContain().toLowerCase())) {
                        stopDetect = true;
                        break;
                    }
                    queue.add(packageEntity);
                }
                wait(1);
            } else {
                if (queue.size() == 0)
                    stillWork = false;
                else
                    wait(1);
            }
        }
        executorService.shutdown();
    }

    private static void wait(int second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
           Thread.currentThread().interrupt();
        }
    }

}

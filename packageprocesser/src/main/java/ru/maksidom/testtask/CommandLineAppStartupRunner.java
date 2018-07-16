package ru.maksidom.testtask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    @Autowired
    private PackageRepository repository;

    @Override
    public void run(String... strings) throws Exception {
        Producer p = new Producer(repository, 3);
        p.start();
    }
}

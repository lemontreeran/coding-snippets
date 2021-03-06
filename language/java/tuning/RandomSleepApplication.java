package com.uber.profiling.examples;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class HelloWorldApplication {

    /**
     * This application could be used to test the java agent.
     * For example, you could run it with following argument:
     * -javaagent:target/uber-java-agent-0.0.1-jar-with-dependencies.jar=reporter=com.uber.profiling.reporters.ConsoleOutputReporter,tag=tag1,metricInterval=10000,durationProfiling=com.uber.profiling.examples.HelloWorldApplication.*
     */
    public static void main(String[] args) throws Throwable {
        long totalRunningMillis = 1 * 60 * 1000;
        long sleepMillis = 1000;

        if (args.length >= 1) {
            totalRunningMillis = Long.parseLong(args[0]);
        }

        if (args.length >= 2) {
            sleepMillis = Long.parseLong(args[1]);
        }

        long startMillis = System.currentTimeMillis();
        long lastPrintMillis = 0;

        Random random = new Random();

        while (System.currentTimeMillis() - startMillis < totalRunningMillis) {
            if (System.currentTimeMillis() - lastPrintMillis >= 10000) {
                System.out.println("Hello World " + System.currentTimeMillis());
                lastPrintMillis = System.currentTimeMillis();
            }

            sleepMillis += random.nextInt(100);
            sleepMillis -= random.nextInt(100);

            privateSleepMethod(sleepMillis);

            AtomicLong atomicLong = new AtomicLong(sleepMillis);
            publicSleepMethod(atomicLong);
        }
    }

    private static void privateSleepMethod(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void publicSleepMethod(AtomicLong millis) {
        try {
            Thread.sleep(millis.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

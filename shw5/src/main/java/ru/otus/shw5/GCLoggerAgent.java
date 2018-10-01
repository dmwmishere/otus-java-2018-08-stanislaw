package ru.otus.shw5;

import java.lang.instrument.Instrumentation;

public class GCLoggerAgent {

    private static final Thread t = new Thread(() -> {

        while (true) {
            try {
                Thread.sleep(1000);
                GC.printGCMetrics();
            } catch (Exception e) {
                System.err.println("Agent exception. Will exit: " + e.getLocalizedMessage());
                break;
            }
        }
    });

    public static void premain(String args, Instrumentation inst) {
        System.out.println("GC logging agent started!");
        t.start();
    }
}

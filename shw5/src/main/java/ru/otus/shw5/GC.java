package ru.otus.shw5;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.util.Map;
import java.util.stream.Collectors;

public class GC {

    public static void printGCMetrics() {

        Map<String, String> gcWorkStats = ManagementFactory.getGarbageCollectorMXBeans().stream().collect(
                Collectors.toMap(MemoryManagerMXBean::getName, gc ->
                        "count="  + gc.getCollectionCount() +
                        ", time=" + gc.getCollectionTime()
                )
        );

        Map<String, String> poolStats = ManagementFactory.getMemoryPoolMXBeans().stream()
                //Optional filter
                .filter(mp -> !mp.getName().startsWith("Code") & !mp.getName().startsWith("Compressed") & !mp.getName().startsWith("Metaspace"))
                .collect(
                Collectors.toMap(MemoryPoolMXBean::getName,
                        mp -> "commited=" + mp.getUsage().getCommitted() +
                                ", used=" + mp.getUsage().getUsed() //+
//                                ", init=" + mp.getUsage().getInit() / 1024f +
//                                ", max=" + mp.getUsage().getMax() / 1024f
                ));

        System.out.println(
                gcWorkStats.toString() + "; "
                        + poolStats.toString()

        );
    }

}

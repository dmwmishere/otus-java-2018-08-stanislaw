package ru.otus.shw6;

import ru.otus.data.TableData;
import ru.otus.dbservice.DBService;
import ru.otus.shw6.engine.CacheEngine;
import ru.otus.shw6.engine.MyCacheEngine;

public class DBCachedService implements DBService {

    CacheEngine<Integer, TableData> cache = new MyCacheEngine<>(5000);

    // These are mock methods

    @Override
    public TableData getById(int id) {
        TableData td = cache.get(id);
        if(td == null){
            System.out.println(id + " not in cache...");
            try {
                Thread.sleep(20); // emulate heavy query
            } catch(InterruptedException ie){
                ie.printStackTrace();
                return null;
            }
            td = new TableData(id, "Some value", new byte[1024*1024]);
            cache.put(id, td);
        } else {
            System.out.println("cache hit by id = " + id);
        }
        return td;
    }

    @Override
    public int insertInto(TableData td) {
        cache.put(td.getId(), td);
        return 1; // return number of affected rows
    }

    public void printCacheStats(){
        cache.printStats();
    }

}

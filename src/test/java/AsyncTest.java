import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class AsyncTest {
    private static String task(int t) {
        System.out.println("task" + t + " : start");
        try {
            Thread.sleep(50L * t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "task" + t;
    }


    public static void main(String[] args) {
//        ExecutorService executor = Executors.newCachedThreadPool();
//        List<CompletableFuture<String>> futures = new ArrayList<>();
//        for (int i = 1; i <= 100; i++) {
//            final int t = i;
//            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> task(t), executor);
//            futures.add(future);
//        }
//        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
//        executor.shutdown();
//        futures.forEach(f -> System.out.println(f.join()));
        Thread thread = new Thread(() -> System.out.println(2));
        thread.start();
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> System.out.println(1));

//        future.join();
    }
}
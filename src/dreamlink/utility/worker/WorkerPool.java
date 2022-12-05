package dreamlink.utility.worker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dreamlink.Config;

public class WorkerPool {

    public static WorkerPool instance = new WorkerPool();

    private ExecutorService executor;

    public void setup() {
        var numThreads = Config.instance.numThreads;
        this.executor = Executors.newFixedThreadPool(numThreads);
    }

    public WorkerTask submit(Runnable task) {
        var future = this.executor.submit(task);
        return new WorkerTask(future);
    }

    public void destroy() {
        if(this.executor != null) {
            this.executor.shutdown();
        }
    }
    
}

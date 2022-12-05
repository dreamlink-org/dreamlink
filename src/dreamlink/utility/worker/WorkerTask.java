package dreamlink.utility.worker;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class WorkerTask {

    private Future<?> future;

    public WorkerTask(Future<?> future) {
        this.future = future;
    }

    public boolean isDone() {
        return this.future.isDone();
    }

    public void join() {
        try {
            this.future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    
}

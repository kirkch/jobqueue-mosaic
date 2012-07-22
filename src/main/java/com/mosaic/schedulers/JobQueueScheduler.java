package com.mosaic.schedulers;

import com.mosaic.jobqueues.JobQueue;
import com.mosaic.Future;

/**
 *
 */
public class JobQueueScheduler implements AsyncScheduler {
    private JobQueue mailbox;

    public JobQueueScheduler( JobQueue mb ) {
        this.mailbox = mb;
    }

    public <T> Future<T> schedule( AsyncJob<T> job ) {
        mailbox.push( job );

        return job.getFuture();
    }
}

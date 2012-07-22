package com.mosaic.jobqueues;

import com.mosaic.Monitor;
import com.mosaic.schedulers.AsyncJob;

/**
 *
 */
public class NotifyAllJobQueueWrapper extends BaseJobQueueWrapper {

    private final Monitor LOCK;

    public NotifyAllJobQueueWrapper( JobQueue wrappedMailbox, Monitor lock ) {
        super( wrappedMailbox );

        LOCK = lock;
    }

    public void push( AsyncJob job ) {
        super.push( job );

        LOCK.wakeAll();
    }

}

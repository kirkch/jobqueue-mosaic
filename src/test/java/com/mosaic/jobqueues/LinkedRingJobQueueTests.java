package com.mosaic.jobqueues;

/**
 *
 */
public class LinkedRingJobQueueTests extends JobQueueInterfaceTestCases {

    public LinkedRingJobQueueTests() {
        super( new LinkedRingJobQueue(3) );

        bulkPopMayReturnLessThanAllJobs = true;
    }

}

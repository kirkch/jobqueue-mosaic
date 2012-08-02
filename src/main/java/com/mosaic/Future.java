package com.mosaic;

import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
@SuppressWarnings("unchecked")
public class Future<T> {
    private static final State INITIAL_STATE = new State();

    // todo
    // cancel/wasCancelled
    // onCompletionCallback
    // onResultCallback/onErrorCallback/onCancelCallback
    // map/flatMap

    private final AtomicReference<State<T>> stateRef = new AtomicReference<State<T>>( INITIAL_STATE );

    // todo remove monitor by using callback after callbacks have been added
    private final Monitor                   stateChangedMonitor = new Monitor();

    public Future() {}

    public Future( T result ) {
        completeWithResult( result );
    }

    public Future( Error error ) {
        completeWithError( error );
    }

    public Future( Throwable ex ) {
        completeWithError( new Error(ex) );
    }

    public boolean hasCompleted() {
        return stateRef.get().hasCompleted();
    }

    public boolean hasResult() {
        return stateRef.get().hasResult();
    }

    public boolean hasError() {
        return stateRef.get().hasError();
    }

    public void completeWithResult( T r ) {
        State currentState = stateRef.get();
        if ( currentState.hasCompleted() ) {
            return;
        }

        State newState = new State(r);
        stateRef.compareAndSet( currentState, newState );
    }

    public void completeWithError( Error e ) {
        State currentState = stateRef.get();
        if ( currentState.hasCompleted() ) {
            return;
        }

        State newState = new State(e);
        stateRef.compareAndSet( currentState, newState );
    }

    public T getResult() {
        State currentState = stateRef.get();

        if ( !currentState.hasCompleted() ) {
            throw new IllegalStateException( "future has not been completed, call isCompleted() first" );
        } else if ( !currentState.hasResult() ) {
            currentState.error.throwAsUncheckedException();
        }

        return (T) currentState.result;
    }

    public T getResultBlocking( int maxMillis ) {
        long startMillis = System.currentTimeMillis();

        while ( !hasCompleted() ) {
            long durationSoFar = System.currentTimeMillis() - startMillis;
            long sleepTime     = maxMillis - durationSoFar;

            if ( sleepTime <= 0 ) {
                throw new TimeoutException(String.format("timed out after %sms",durationSoFar));
            }

            stateChangedMonitor.sleep( sleepTime );
        }

        return getResult();
    }

    public Error getError() {
        State currentState = stateRef.get();

        if ( !currentState.hasCompleted() ) {
            throw new IllegalStateException( "future has not been completed, call isCompleted() first" );
        } else if ( !currentState.hasError() ) {
            throw new IllegalStateException( "future contains a result, not an exception" );
        }

        return currentState.error;
    }


    private static enum Status {
        NotCompleted(false), HasResult(true), HasError(true);

        private final boolean isCompleted;

        private Status( boolean isCompleted ) {
            this.isCompleted = isCompleted;
        }

        public boolean hasResult() {
            return this == HasResult;
        }

        public boolean hasError() {
            return this == HasError;
        }
    }

    private static class State<T> {
        private final Status status;

        private final Error error;
        private final T     result;

        State() {
            this( null, null, Status.NotCompleted );
        }

        private State( Error error ) {
            this( error, null, Status.HasError );
        }

        private State( T r ) {
            this( null, r, Status.HasResult );
        }

        private State( Error error, T r, Status status ) {
            this.error  = error;
            this.result = r;
            this.status = status;
        }

        public boolean hasCompleted() {
            return status.isCompleted;
        }

        public boolean hasResult() {
            return status.hasResult();
        }

        public boolean hasError() {
            return status.hasError();
        }
    }
}

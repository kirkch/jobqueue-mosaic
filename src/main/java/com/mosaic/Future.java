package com.mosaic;

import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
public class Future<T> {
    private static final State INITIAL_STATE = new State();

    private final AtomicReference<State<T>> stateRef = new AtomicReference<State<T>>( INITIAL_STATE );


    public boolean hasCompleted() {
        return stateRef.get().hasCompleted();
    }

    public boolean hasResult() {
        return stateRef.get().hasResult();
    }

    public boolean hasException() {
        return stateRef.get().hasException();
    }

    public void completeWithResult( T r ) {
        State currentState = stateRef.get();
        if ( currentState.hasCompleted() ) {
            return;
        }

        State newState = new State(r);
        stateRef.compareAndSet( currentState, newState );
    }

    public void completeWithException( Throwable e ) {
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
            throw new IllegalStateException( "future contains an exception, not a result", currentState.ex );
        }

        return (T) currentState.result;
    }

    public Throwable getException() {
        State currentState = stateRef.get();

        if ( !currentState.hasCompleted() ) {
            throw new IllegalStateException( "future has not been completed, call isCompleted() first" );
        } else if ( !currentState.hasException() ) {
            throw new IllegalStateException( "future contains a result, not an exception" );
        }

        return currentState.ex;
    }


    private static enum Status {
        NotCompleted(false), HasResult(true), HasException(true);

        private final boolean isCompleted;

        private Status( boolean isCompleted ) {
            this.isCompleted = isCompleted;
        }

        public boolean hasResult() {
            return this == HasResult;
        }

        public boolean hasException() {
            return this == HasException;
        }
    }

    private static class State<T> {
        private final Status status;

        private final Throwable ex;
        private final T         result;

        State() {
            this( null, null, Status.NotCompleted );
        }

        private State( Throwable ex ) {
            this( ex, null, Status.HasException );
        }

        private State( T r ) {
            this( null, r, Status.HasResult );
        }

        private State( Throwable ex, T r, Status status ) {
            this.ex     = ex;
            this.result = r;
            this.status = status;
        }

        public boolean hasCompleted() {
            return status.isCompleted;
        }

        public boolean hasResult() {
            return status.hasResult();
        }

        public boolean hasException() {
            return status.hasException();
        }
    }
}

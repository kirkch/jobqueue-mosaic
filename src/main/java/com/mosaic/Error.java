package com.mosaic;

import com.mosaic.utils.SetUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A single abstraction for errors. When an error can either be expected or exceptional.<p/>
 *
 * The Java language supports two exit types from a method. Returning a value (or void), or exceptional termination. Modelling of an error
 * has to be mapped into one of those two types. With a mix of conflicting recommendations as to what to map to an exception and what not to;
 * with perhaps the most common recommendation being to map exceptional cases to exceptions only. This however
 * leaves the modelling of expected error states to the return types of methods, which may not always get done; as well as a slew of competing
 * views as to what an exceptional case actually is.<p/>
 *
 * When chaining futures from this library together in an asynchronous flow of work, an ErrorState acts as a condition gate
 * that changes the flow of work from happy path to error path without necessarily encurring the cost of a heavy weight exception.<p/>
 *
 * This leads to the following pragmatic advise for when to use exceptions, use com.mosaic.Error to jump from the happy path chaining of
 * asynchronous work to the unhappy path. Include an exception if the stack trace is important to you for debugging, else they can be
 * left out.<p/>
 *
 * This class can be extended to create a hierarchy, however that will usually not be necessary as a dynamic solution using
 * tags has been included.<p/>
 *
 * This class is immutable, and thus thread safe. Methods that appear to change the instances state do so by creating
 * a new instance.
 */
@SuppressWarnings("unchecked")
public class Error {

// NB this class name clashes with java.lang.Error; not great. However I struggled with alternatives and ultimately
// decided that in my daily programming the java.lang.Error definition was less application to me than this one; that is
// I rarely use instances of java.lang.Error in framework code and I never use them directly within business logic.
// It will however initially confuse people new to this library. This will be reviewed at a later date.

    private final String      description;
    private final String      errorType;
    private final Set<String> tags;
    private final Throwable   underlyingException;

    public Error( String description, String errorType, String...tags ) {
        this( description, errorType, SetUtils.asSet(tags), null );
    }

    public Error( Throwable ex ) {
        this( ex.getMessage(), ex.getClass().getName(), Collections.EMPTY_SET, ex );
    }

    protected Error( String description, String errorType, Set<String> tags, Throwable ex ) {
        this.description         = description;
        this.errorType           = errorType;
        this.tags                = tags;
        this.underlyingException = ex;
    }


    /**
     * Retrieve a human readable description of the error.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieve the machine readable description/classification of the error.
     */
    public String getErrorType() {
        return errorType;
    }

    /**
     * Retrieve a set of classification tags assigned to this error. Tags are a more flexible and dynamic solution to Java's type
     * hierarchy for classifying exceptions.
     */
    public Set<String> getTags() {
        return tags;
    }

    /**
     * Returns true if the specified tag exists within the error.
     */
    public boolean containsTag( String tag ) {
        return tags.contains( tag );
    }

    /**
     * Returns true if all of the specified tag exist within the error.
     */
    public boolean containsTags( String...tags ) {
        return this.tags.containsAll( Arrays.asList(tags) );
    }

    /**
     * Create a new instance of this error that includes the specified tags.
     */
    public Error addTags( String...tags ) {
        if ( containsTags(tags) ) {
            return this;
        }

        Set<String> newTags = SetUtils.asSet(tags);
        newTags.addAll( this.tags );

        return new Error( description, errorType, newTags, underlyingException );
    }

    /**
     * Create a new instance of this error with the specified tags removed.
     */
    public Error removeTags( String...tags ) {
        Set<String> newTags = new HashSet<String>(this.tags);
        newTags.removeAll( Arrays.asList(tags) );

        if ( newTags.size() == this.tags.size() ) {
            return this;
        }

        return new Error( description, errorType, newTags, underlyingException );
    }


    /**
     * Is this error represented by an instance of java.lang.Throwable.
     */
    public boolean isExceptional() {
        return underlyingException != null;
    }


    public Throwable asException() {
        if ( underlyingException != null ) {
            return underlyingException;
        }

        throw new RuntimeException( String.format("%s(%s, tags=%s)", errorType, description, tags.toString()) );
    }

    /**
     * If the error is an instance of Throwable, then throw it. Does not wrap or modify the exception. Else convert
     * the error into an exception and throw that.
     */
    public void throwAsException() throws Throwable {
        throw asException();
    }

    /**
     * If the error is an unchecked exception, then throw it. If the exception is checked, then convert it into
     * a runtime exception and throw it. Else convert the error into an exception and throw that.
     */
    public void throwAsUncheckedException() {
        Throwable ex = asException();

        if ( ex instanceof RuntimeException ) {
            throw (RuntimeException) ex;
        } else if ( ex instanceof java.lang.Error ) {
            throw (java.lang.Error) ex;
        } else {
            throw new RuntimeException(ex);
        }
    }

}

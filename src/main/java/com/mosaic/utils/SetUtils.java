package com.mosaic.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@SuppressWarnings("unchecked")
public class SetUtils {

    public static <T> Set<T> asSet( T...elements ) {
        if ( elements.length == 0 ) {
            return Collections.EMPTY_SET;
        }

        Set<T> set = new HashSet<T>(elements.length*2);

        Collections.addAll( set, elements );

        return set;
    }

}

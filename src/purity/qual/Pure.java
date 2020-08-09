package purity.qual;

import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.InvisibleQualifier;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TargetLocations;
import org.checkerframework.framework.qual.TypeUseLocation;

import java.lang.annotation.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@TargetLocations({ TypeUseLocation.EXPLICIT_UPPER_BOUND })
@DefaultQualifierInHierarchy
@SubtypeOf({})
public @interface Pure {
//    /** The type of purity. */
//    public static enum Kind {
//        /** The method has no visible side effects. */
//        SIDE_EFFECT_FREE,
//
//        /** The method returns exactly the same value when called in the same environment. */
//        DETERMINISTIC
//    }
}

package fenum.qual;

import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TargetLocations;
import org.checkerframework.framework.qual.TypeUseLocation;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;

import java.lang.annotation.*;

/**
 * The top of the fake enumeration type hierarchy.
 *
 * @checker_framework.manual #fenum-checker Fake Enum Checker
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@TargetLocations({TypeUseLocation.EXPLICIT_LOWER_BOUND, TypeUseLocation.EXPLICIT_UPPER_BOUND})
@SubtypeOf({})
@DefaultFor({TypeUseLocation.LOCAL_VARIABLE, TypeUseLocation.RESOURCE_VARIABLE})
@DefaultQualifierInHierarchy
public @interface FenumTop {}

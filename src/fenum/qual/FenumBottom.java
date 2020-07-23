package fenum.qual;

import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TargetLocations;
import org.checkerframework.framework.qual.TypeUseLocation;

import java.lang.annotation.*;

/**
 * The bottom type in the Fenum type system. Programmers should rarely write this type.
 *
 * <p>Its relationships are set up via the FenumAnnotatedTypeFactory.
 *
 * @checker_framework.manual #propkey-checker Property File Checker
 * @checker_framework.manual #bottom-type the bottom type
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@TargetLocations({TypeUseLocation.EXPLICIT_LOWER_BOUND, TypeUseLocation.EXPLICIT_UPPER_BOUND})
// Subtype relationships are set up by passing this class as a bottom
// to the multigraph hierarchy constructor.
@SubtypeOf({FenumA.class, FenumB.class, FenumC.class, FenumUnqualified.class})
@DefaultFor(TypeUseLocation.LOWER_BOUND)
public @interface FenumBottom {}

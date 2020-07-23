package fenum.qual;

import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TypeUseLocation;
import org.checkerframework.framework.qual.QualifierForLiterals;
import org.checkerframework.framework.qual.LiteralKind;

import java.lang.annotation.*;

/**
 * An unqualified type. Such a type is incomparable to (that is, neither a subtype nor a supertype
 * of) any fake enum type.
 *
 * <p>This annotation may not be written in source code; it is an implementation detail of the
 * checker.
 *
 * @checker_framework.manual #fenum-checker Fake Enum Checker
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
//@Target({}) // empty target prevents programmers from writing this in a program
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@SubtypeOf({FenumTop.class})
@DefaultFor(TypeUseLocation.EXCEPTION_PARAMETER)
@QualifierForLiterals({
        LiteralKind.BOOLEAN,
        LiteralKind.CHAR,
        LiteralKind.DOUBLE,
        LiteralKind.FLOAT,
        LiteralKind.INT,
        LiteralKind.LONG,
        LiteralKind.NULL,
        LiteralKind.STRING,
}) 
public @interface FenumUnqualified {}

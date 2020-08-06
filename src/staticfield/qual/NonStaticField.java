package staticfield.qual;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import org.checkerframework.framework.qual.SubtypeOf;

@Documented
@Target({ ElementType.TYPE_USE})
@SubtypeOf({ AnyField.class })
public @interface NonStaticField {
}

package dummy.purity.qual;

import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TargetLocations;
import org.checkerframework.framework.qual.TypeUseLocation;

import java.lang.annotation.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@TargetLocations({ TypeUseLocation.EXPLICIT_UPPER_BOUND })
@SubtypeOf({Pure.class})
public @interface SideEffectFree {
}

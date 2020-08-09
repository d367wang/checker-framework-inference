package dummy.purity.utils;

import checkers.inference.model.AnnotationLocation;

import javax.lang.model.element.AnnotationMirror;

public class ConstMethodSlot extends MethodSlot {

    protected ConstMethodSlot(String id, AnnotationMirror anno) {
        super(id, anno, AnnotationLocation.MISSING_LOCATION);
        this.isConstant = true;
    }
}

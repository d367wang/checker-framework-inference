package purity.utils;

import checkers.inference.model.AnnotationLocation;

public class ConstMethodSlot extends MethodSlot {

    protected ConstMethodSlot(String id) {
        super(id, AnnotationLocation.MISSING_LOCATION);
    }
}

package purity;

import checkers.inference.*;
import checkers.inference.model.ConstantSlot;
import checkers.inference.model.ConstraintManager;
import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.framework.type.AnnotatedTypeMirror.AnnotatedDeclaredType;
import org.checkerframework.framework.type.AnnotatedTypeMirror.AnnotatedPrimitiveType;
import org.checkerframework.framework.type.treeannotator.ListTreeAnnotator;
import org.checkerframework.framework.type.treeannotator.LiteralTreeAnnotator;
import org.checkerframework.framework.type.treeannotator.TreeAnnotator;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.PrimitiveType;

/**
 * DataflowInferenceAnnotatedTypeFactory handles boxing and unboxing for
 * primitive types. The Dataflow type should always same as declared type for
 * both cases.
 * 
 * @author jianchu
 *
 */
public class PurityInferenceAnnotatedTypeFactory extends InferenceAnnotatedTypeFactory {

    public PurityInferenceAnnotatedTypeFactory(InferenceChecker inferenceChecker,
                                                 boolean withCombineConstraints, BaseAnnotatedTypeFactory realTypeFactory,
                                                 InferrableChecker realChecker, SlotManager slotManager, ConstraintManager constraintManager) {
        super(inferenceChecker, withCombineConstraints, realTypeFactory, realChecker, slotManager,
                constraintManager);
        postInit();
    }

//    @Override
//    public TreeAnnotator createTreeAnnotator() {
//        return new ListTreeAnnotator(new LiteralTreeAnnotator(this),
//                new DataflowInferenceTreeAnnotator(this, realChecker, realTypeFactory,
//                        variableAnnotator, slotManager));
//    }

}

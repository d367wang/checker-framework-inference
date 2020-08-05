package staticfield;

import checkers.inference.BaseInferrableChecker;
import checkers.inference.InferenceChecker;
import checkers.inference.InferrableChecker;
import checkers.inference.SlotManager;
import checkers.inference.model.ConstraintManager;
import staticfield.qual.AnyField;
import staticfield.qual.StaticField;

import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;
import org.checkerframework.javacutil.AnnotationBuilder;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.util.Elements;

public class StaticFieldInferenceChecker extends BaseInferrableChecker {
  public AnnotationMirror ANY_FIELD, STATIC_FIELD;

    @Override
    public void initChecker() {
        super.initChecker();
        setAnnotations();
    }

    protected void setAnnotations() {
        final Elements elements = processingEnv.getElementUtils();
        ANY_FIELD = AnnotationBuilder.fromClass(elements, AnyField.class);
        STATIC_FIELD = AnnotationBuilder.fromClass(elements, StaticField.class);
    }

    @Override
    public StaticFieldInferenceVisitor createVisitor(InferenceChecker ichecker, BaseAnnotatedTypeFactory factory,
                                         boolean infer) {
        return new StaticFieldInferenceVisitor(this, ichecker, factory, infer);
    }

    @Override
    public StaticFieldAnnotatedTypeFactory createRealTypeFactory() {
        return new StaticFieldAnnotatedTypeFactory(this);
    }

    @Override
    public StaticFieldInferenceATF createInferenceATF(InferenceChecker inferenceChecker,
                                                                    InferrableChecker realChecker, BaseAnnotatedTypeFactory realTypeFactory,
                                                                    SlotManager slotManager, ConstraintManager constraintManager) {
        StaticFieldInferenceATF dataflowInferenceTypeFactory = new StaticFieldInferenceATF(
                inferenceChecker, realChecker.withCombineConstraints(), realTypeFactory, realChecker,
                slotManager, constraintManager);
        return dataflowInferenceTypeFactory;
    }
}

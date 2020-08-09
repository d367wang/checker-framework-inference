package dummy;

import checkers.inference.*;
import checkers.inference.model.ConstraintManager;
import com.sun.source.util.TreePath;
import dummy.purity.PurityAnnotatedTypeFactory;
import dummy.purity.qual.Impure;
import dummy.purity.qual.Pure;
import dummy.purity.solve.constraint.PurityConstraintConverter;
import dummy.purity.solve.solver.PuritySolverEngine;
import dummy.purity.utils.MethodSlotManager;
import dummy.qual.DummyBottom;
import dummy.qual.DummyTop;

import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;
import org.checkerframework.javacutil.AnnotationBuilder;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Set;


public class DummyChecker extends BaseInferrableChecker {

    @Override
    public void initChecker() {
        super.initChecker();
    }


    /*
    @Override
    public void typeProcessingOver() {
        // Reset ignore overflow.
        super.typeProcessingOver();


        ConstraintManager constraintManager = new ConstraintManager();
        SlotManager slotManager = new DefaultSlotManager(getProcessingEnvironment(),
                getTypeFactory().getSupportedTypeQualifiers(), true );
        PurityConstraintConverter converter = new PurityConstraintConverter(slotManager, constraintManager, methodSlotManager);
        converter.convert();

        System.out.println("Start solving..........");
        new PuritySolverEngine().solve(
                new HashMap<>(),
                slotManager.getSlots(),
                constraintManager.getConstraints(),
                getTypeFactory().getQualifierHierarchy(),
                getProcessingEnvironment());
    }
    */


    @Override
    public DummyVisitor createVisitor(InferenceChecker ichecker, BaseAnnotatedTypeFactory factory,
                                      boolean infer) {
        return new DummyVisitor(this, ichecker, factory, infer);
    }

    @Override
    public DummyAnnotatedTypeFactory createRealTypeFactory() {
        return new DummyAnnotatedTypeFactory(this);
    }

    @Override
    public DummyInferenceATF createInferenceATF(InferenceChecker inferenceChecker,
                                                InferrableChecker realChecker, BaseAnnotatedTypeFactory realTypeFactory,
                                                SlotManager slotManager, ConstraintManager constraintManager) {
        DummyInferenceATF dummyInferenceATF = new DummyInferenceATF(
                inferenceChecker, realChecker.withCombineConstraints(), realTypeFactory, realChecker,
                slotManager, constraintManager);
        return dummyInferenceATF;
    }




}

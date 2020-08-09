package purity;

import checkers.inference.BaseInferrableChecker;
import checkers.inference.DefaultSlotManager;
import checkers.inference.InferenceChecker;
import checkers.inference.SlotManager;
import checkers.inference.model.ConstraintManager;

import purity.solve.constraint.PurityConstraintConverter;
import purity.solve.solver.PuritySolverEngine;
import purity.utils.MethodSlotManager;

import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;

import java.util.HashMap;

public class PurityChecker extends BaseInferrableChecker {
    private MethodSlotManager methodSlotManager;

    @Override
    public void initChecker() {
        super.initChecker();
        methodSlotManager = new MethodSlotManager();
    }

    public MethodSlotManager getMethodSlotManager() {
        return methodSlotManager;
    }

    @Override
    public PurityAnnotatedTypeFactory createRealTypeFactory() {
      return new PurityAnnotatedTypeFactory(this);
          
    }

    @Override
    public PurityVisitor createVisitor(InferenceChecker ichecker, BaseAnnotatedTypeFactory factory,
                                         boolean infer) {
        return new PurityVisitor(this, ichecker, factory, infer);
    }

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

}

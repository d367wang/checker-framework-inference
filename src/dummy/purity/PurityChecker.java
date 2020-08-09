package dummy.purity;

import checkers.inference.BaseInferrableChecker;
import checkers.inference.InferenceChecker;
import dummy.PurityInferenceController;
import dummy.purity.solve.constraint.PurityConstraintConverter;
import dummy.purity.solve.solver.PuritySolverEngine;

import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;

import java.util.HashMap;

public class PurityChecker extends BaseInferrableChecker {

    @Override
    public void initChecker() {
        super.initChecker();
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

}

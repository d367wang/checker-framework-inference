package fenum;

import checkers.inference.BaseInferrableChecker;
import checkers.inference.InferenceAnnotatedTypeFactory;
import checkers.inference.InferenceChecker;
import checkers.inference.InferenceVisitor;
import checkers.inference.InferrableChecker;
import checkers.inference.SlotManager;
import checkers.inference.model.ConstraintManager;


import checkers.inference.dataflow.InferenceAnalysis;
import checkers.inference.dataflow.InferenceTransfer;
import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;
import org.checkerframework.framework.flow.CFTransfer;


public class FenumInfChecker extends BaseInferrableChecker {

    @Override
    public void initChecker() {
        super.initChecker();
    }

    @Override
    public InferenceVisitor<?, ?> createVisitor(InferenceChecker ichecker, BaseAnnotatedTypeFactory factory, boolean infer) {
        return new FenumInfVisitor(this, ichecker, factory, infer);
    }

    @Override
    public BaseAnnotatedTypeFactory createRealTypeFactory() {
        return new FenumAnnotatedTypeFactory(this);
    }
  /*
    @Override
    public CFTransfer createInferenceTransferFunction(InferenceAnalysis analysis) {
        return new InferenceTransfer(analysis);
    }
  */

  @Override
  public InferenceAnnotatedTypeFactory createInferenceATF( 
          InferenceChecker inferenceChecker,
          InferrableChecker realChecker,
          BaseAnnotatedTypeFactory realTypeFactory,
          SlotManager slotManager,
          ConstraintManager constraintManager) {

    return new FenumInfAnnotatedTypeFactory(
        inferenceChecker,
        realChecker.withCombineConstraints(),
        realTypeFactory,
        realChecker,
        slotManager,
        constraintManager
    );
  }

}

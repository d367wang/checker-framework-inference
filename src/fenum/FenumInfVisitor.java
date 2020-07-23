package fenum;

import checkers.inference.InferenceChecker;
import checkers.inference.InferenceVisitor;
import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;

public class FenumInfVisitor extends InferenceVisitor<FenumInfChecker, BaseAnnotatedTypeFactory> {

    public FenumInfVisitor(
            FenumInfChecker checker,
            InferenceChecker ichecker,
            BaseAnnotatedTypeFactory factory,
            boolean infer) {
        super(checker, ichecker, factory, infer);
    }
}

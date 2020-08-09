package dummy;

import checkers.inference.*;
import checkers.inference.model.Constraint;
import checkers.inference.model.ConstraintManagerForMethod;

import checkers.inference.solver.frontend.LatticeBuilder;
import checkers.inference.solver.frontend.Lattice;
import checkers.inference.solver.frontend.TwoQualifiersLattice;

import dummy.purity.PurityAnnotatedTypeFactory;
import dummy.purity.SlotManagerForMethod;
import dummy.purity.solve.constraint.PurityConstraintConverter;
import dummy.purity.solve.solver.PuritySolverEngine;
import dummy.purity.utils.MethodSlotManager;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.framework.type.QualifierHierarchy;

public class PurityInferenceController {
    public final Logger logger = Logger.getLogger(PurityInferenceController.class.getName());

    private static PurityInferenceController mPurityInferenceController;

    private BaseInferrableChecker dummyChecker;
    private BaseAnnotatedTypeFactory dummyTypeFactory;

    private SlotManagerForMethod slotManagerForMethod;
    private ConstraintManagerForMethod constraintManagerForMethod;
    private MethodSlotManager methodSlotManager;

    // Hold the results of solving.
    private InferenceResult solverResult;


    private PurityInferenceController() {
        if (mPurityInferenceController != null) {
            logger.warning("Only a single instance should ever be created!");
        }
        mPurityInferenceController = this;
        dummyChecker = (BaseInferrableChecker)InferenceMain.getInstance().getRealChecker();
        dummyTypeFactory = InferenceMain.getInstance().getRealTypeFactory();
    }

  public static PurityInferenceController getInstance() {
    if (mPurityInferenceController == null) {
      mPurityInferenceController = new PurityInferenceController();
    }
    return mPurityInferenceController;
        
  }
  
    public DummyAnnotatedTypeFactory getDummyTypeFactory() {
        assert dummyTypeFactory != null : "real type factory should be created";
        return (DummyAnnotatedTypeFactory)dummyTypeFactory;
    }

    public SlotManagerForMethod getSlotManagerForMethod() {
        if (slotManagerForMethod == null) {
            slotManagerForMethod = new SlotManagerForMethod(dummyChecker.getProcessingEnvironment(),
                    getDummyTypeFactory().getSupportedTypeQualifiers(), true);
            logger.finer("Created slot manager for method" + slotManagerForMethod);
        }
        return slotManagerForMethod;
    }


    public ConstraintManagerForMethod getConstraintManagerForMethod() {
        if (this.constraintManagerForMethod == null) {
            this.constraintManagerForMethod = new ConstraintManagerForMethod();
            this.constraintManagerForMethod.init(InferenceMain.getInstance().getInferenceTypeFactory());
            logger.finer("Created constraint manager for method" + constraintManagerForMethod);
        }
        return constraintManagerForMethod;
    }


    protected InferenceSolver getSolver() {
        try {
            InferenceSolver solver = (InferenceSolver) Class.forName(
                    "dummy.purity.solve.solver.PuritySolverEngine",
                    true,
                    ClassLoader.getSystemClassLoader())
                    .getDeclaredConstructor().newInstance();
            logger.finer("Created solver: " + solver);
            return solver;
        } catch (Throwable e) {
            logger.log(Level.SEVERE, "Error instantiating solver class \"" + InferenceOptions.solver + "\".", e);
            System.exit(5);
            return null; // Dead code
        }
    }

    public void solve() {
        PurityConstraintConverter converter = new PurityConstraintConverter(
                this.getSlotManagerForMethod(),
                this.getConstraintManagerForMethod(),
                this.getMethodSlotManager());
        converter.convert();

        //QualifierHierarchy qualifierHierarchy = getDummyTypeFactory().createPurityQualifierHierarchy();
        //System.out.println(qualifierHierarchy.toString());
        
        TwoQualifiersLattice lattice = new LatticeBuilder().buildTwoTypeLattice(methodSlotManager.PURE,
                                                                                methodSlotManager.IMPURE);
        for(Constraint c : constraintManagerForMethod.getConstraints()) {
          System.out.println(c);
                  
        }

        System.out.println("Start solving..........");

        try {
            PuritySolverEngine solver = new PuritySolverEngine();
            this.solverResult = solver.solve(
                    new HashMap<>(),
                    slotManagerForMethod.getSlots(),
                    constraintManagerForMethod.getConstraints(),
                    lattice,
                    dummyChecker.getProcessingEnvironment());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public MethodSlotManager getMethodSlotManager() {
        if (this.methodSlotManager == null) {
            this.methodSlotManager = new MethodSlotManager(dummyTypeFactory);
        }
        return methodSlotManager;
    }
}

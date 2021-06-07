package checkers.inference.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.checkerframework.framework.source.SourceChecker;
import org.checkerframework.framework.type.QualifierHierarchy;
import org.checkerframework.framework.type.VisitorState;
import org.checkerframework.javacutil.BugInCF;
import checkers.inference.InferenceAnnotatedTypeFactory;
import checkers.inference.VariableAnnotator;
import checkers.inference.model.ArithmeticConstraint.ArithmeticOperationKind;

/**
 * Constraint manager holds constraints that are generated by InferenceVisitor.
 *
 * @author mcarthur
 *
 */
public class ConstraintManager {

    private boolean ignoreConstraints = false;

    private final Set<Constraint> constraints = new HashSet<Constraint>();

    private InferenceAnnotatedTypeFactory inferenceTypeFactory;

    private SourceChecker checker;

    private QualifierHierarchy realQualHierarchy;

    private VisitorState visitorState;

    public void init(InferenceAnnotatedTypeFactory inferenceTypeFactory) {
        this.inferenceTypeFactory = inferenceTypeFactory;
        this.realQualHierarchy = inferenceTypeFactory.getRealQualifierHierarchy();
        this.visitorState = inferenceTypeFactory.getVisitorState();
        this.checker = inferenceTypeFactory.getContext().getChecker();
    }

    public Set<Constraint> getConstraints() {
        return constraints;
    }

    /**
     * If the {@code ignoreConstraints} flag is set to false, then this method checks to see if the
     * given {@link Constraint} is an instance of {@link AlwaysFalseConstraint}. If so, a warning is
     * issued. If not, it adds the {@link Constraint} to the constraint set only if the constraint
     * is not an {@link AlwaysTrueConstraint}.
     *
     * @param constraint a (possibly normalized) constraint
     */
    private void add(Constraint constraint) {
        if (!ignoreConstraints) {
            if (constraint instanceof AlwaysFalseConstraint) {
                throw new BugInCF(
                        "An AlwaysFalseConstraint is being added to the constraint set.");
            } else if (!(constraint instanceof AlwaysTrueConstraint)) {
                constraints.add(constraint);
            }
        }
    }

    /**
     * Allows {@link ImplicationConstraint} to add its component assumptions
     * directly to the manager as part of one of its normalization cases.
     *
     * @param constraints
     *            a collection of (possibly normalized) constraints
     */
    protected void addAll(Iterable<Constraint> constraints) {
        for (Constraint c : constraints) {
            add(c);
        }
    }

    public void startIgnoringConstraints() {
        ignoreConstraints = true;
    }

    public void stopIgnoringConstraints() {
        ignoreConstraints = false;
    }

    // All createXXXConstraint methods create a (possibly normalized) constraint for the given
    // slots. It does not issue errors for unsatisfiable constraints.

    /**
     * Creates a {@link SubtypeConstraint} between the two slots, which may be normalized to
     * {@link AlwaysTrueConstraint}, {@link AlwaysFalseConstraint}, or {@link EqualityConstraint}.
     */
    public Constraint createSubtypeConstraint(Slot subtype, Slot supertype) {
        return SubtypeConstraint.create(subtype, supertype, getCurrentLocation(),
                realQualHierarchy);
    }

    /**
     * Creates an {@link EqualityConstraint} between the two slots, which may be normalized to
     * {@link AlwaysTrueConstraint} or {@link AlwaysFalseConstraint}.
     */
    public Constraint createEqualityConstraint(Slot first, Slot second) {
        return EqualityConstraint.create(first, second, getCurrentLocation());
    }

    /**
     * Creates an {@link InequalityConstraint} between the two slots, which may be normalized to
     * {@link AlwaysTrueConstraint} or {@link AlwaysFalseConstraint}.
     */
    public Constraint createInequalityConstraint(Slot first, Slot second) {
        return InequalityConstraint.create(first, second, getCurrentLocation());
    }

    /**
     * Creates a {@link ComparableConstraint} between the two slots, which may be normalized to
     * {@link AlwaysTrueConstraint} or {@link AlwaysFalseConstraint}.
     */
    public Constraint createComparableConstraint(Slot first, Slot second) {
        return ComparableConstraint.create(first, second, getCurrentLocation(), realQualHierarchy);
    }

    /**
     * Creates a {@link CombineConstraint} between the three slots.
     */
    public CombineConstraint createCombineConstraint(Slot target, Slot decl, CombVariableSlot result) {
        return CombineConstraint.create(target, decl, result, getCurrentLocation());
    }

    /**
     * Creates a {@link PreferenceConstraint} for the given slots with the given weight.
     */
    public PreferenceConstraint createPreferenceConstraint(VariableSlot variable, ConstantSlot goal,
            int weight) {
        return PreferenceConstraint.create(variable, goal, weight, getCurrentLocation());
    }

    /**
     * Creates an {@link ExistentialConstraint} for the given slot and lists of constraints.
     */
    public ExistentialConstraint createExistentialConstraint(Slot slot,
            List<Constraint> ifExistsConstraints, List<Constraint> ifNotExistsConstraints) {
        return ExistentialConstraint.create(slot, ifExistsConstraints,
                ifNotExistsConstraints, getCurrentLocation());
    }

    public Constraint createImplicationConstraint(List<Constraint> assumptions, Constraint conclusion) {
        return ImplicationConstraint.create(assumptions, conclusion, getCurrentLocation());
    }

    /**
     * Create an {@link ArithmeticConstraint} for the given operation and slots.
     */
    public ArithmeticConstraint createArithmeticConstraint(ArithmeticOperationKind operation,
            Slot leftOperand, Slot rightOperand, ArithmeticVariableSlot result) {
        return ArithmeticConstraint.create(operation, leftOperand, rightOperand, result,
                getCurrentLocation());
    }

    // TODO: give location directly in Constraint.create() methods
    private AnnotationLocation getCurrentLocation() {
        if (visitorState.getPath() != null) {
            return VariableAnnotator.treeToLocation(inferenceTypeFactory,
                    visitorState.getPath().getLeaf());
        } else {
            return AnnotationLocation.MISSING_LOCATION;
        }
    }

    // All addXXXConstraint methods create a (possibly normalized) constraint for the given slots.
    // They also issue errors for unsatisfiable constraints, unless the method name has "NoErrorMsg"
    // in it.

    /**
     * Creates and adds a {@link SubtypeConstraint} between the two slots to the constraint set,
     * which may be normalized to {@link AlwaysTrueConstraint} or {@link EqualityConstraint}. An
     * error is issued if the {@link SubtypeConstraint} is always unsatisfiable.
     */
    public void addSubtypeConstraint(Slot subtype, Slot supertype) {
        Constraint constraint = createSubtypeConstraint(subtype, supertype);
        if (constraint instanceof AlwaysFalseConstraint) {
            // TODO: forward error msg keys and nodes from InferenceVisitor to create a more
            // relevant error message (eg assignment.type.incompatible) at the precise code AST node
            // this subtype constraint originates from.
            // Same for constraints below.
            checker.reportError(visitorState.getPath().getLeaf(), "subtype.constraint.unsatisfiable", subtype, supertype);
        } else {
            add(constraint);
        }
    }

    /**
     * Same as {@link #addSubtypeConstraint(Slot, Slot)} except we return false instead of raising
     * an error if the constraint is always unsatisfiable.
     *
     * @return false if the subtype constraint is always unsatisfiable, true otherwise.
     */
    public boolean addSubtypeConstraintNoErrorMsg(Slot subtype, Slot supertype) {
        Constraint constraint = createSubtypeConstraint(subtype, supertype);
        if (constraint instanceof AlwaysFalseConstraint) {
            return false;
        } else {
            add(constraint);
            return true;
        }
    }

    /**
     * Creates and adds an {@link EqualityConstraint} between the two slots to the constraint set,
     * which may be normalized to {@link AlwaysTrueConstraint}. An error is issued if the
     * {@link EqualityConstraint} is always unsatisfiable.
     */
    public void addEqualityConstraint(Slot first, Slot second) {
        Constraint constraint = createEqualityConstraint(first, second);
        if (constraint instanceof AlwaysFalseConstraint) {
            checker.reportError(visitorState.getPath().getLeaf(), "equality.constraint.unsatisfiable", first, second);
        } else {
            add(constraint);
        }
    }

    /**
     * Creates and adds an {@link InequalityConstraint} between the two slots to the constraint set,
     * which may be normalized to {@link AlwaysTrueConstraint}. An error is issued if the
     * {@link InequalityConstraint} is always unsatisfiable.
     */
    public void addInequalityConstraint(Slot first, Slot second) {
        Constraint constraint = createInequalityConstraint(first, second);
        if (constraint instanceof AlwaysFalseConstraint) {
            checker.reportError(visitorState.getPath().getLeaf(), "inequality.constraint.unsatisfiable", first, second);
        } else {
            add(constraint);
        }
    }

    /**
     * Creates and adds a {@link ComparableConstraint} between the two slots to the constraint set,
     * which may be normalized to {@link AlwaysTrueConstraint}. An error is issued if the
     * {@link ComparableConstraint} is always unsatisfiable.
     */
    public void addComparableConstraint(Slot first, Slot second) {
        Constraint constraint = createComparableConstraint(first, second);
        if (constraint instanceof AlwaysFalseConstraint) {
            checker.reportError(visitorState.getPath().getLeaf(), "comparable.constraint.unsatisfiable", first, second);
        } else {
            add(constraint);
        }
    }

    /**
     * Creates and adds a {@link CombineConstraint} to the constraint set.
     */
    public void addCombineConstraint(Slot target, Slot decl, CombVariableSlot result) {
        add(createCombineConstraint(target, decl, result));
    }

    /**
     * Creates and adds a {@link PreferenceConstraint} to the constraint set.
     */
    public void addPreferenceConstraint(VariableSlot variable, ConstantSlot goal, int weight) {
        add(createPreferenceConstraint(variable, goal, weight));
    }

    /**
     * Creates and adds a {@link ExistentialConstraint} to the constraint set.
     */
    public void addExistentialConstraint(Slot slot, List<Constraint> ifExistsConstraints,
            List<Constraint> ifNotExistsConstraints) {
        add(createExistentialConstraint(slot, ifExistsConstraints, ifNotExistsConstraints));
    }

    public void addArithmeticConstraint(ArithmeticOperationKind operation, Slot leftOperand,
            Slot rightOperand, ArithmeticVariableSlot result) {
        add(createArithmeticConstraint(operation, leftOperand, rightOperand, result));
    }

    public void addImplicationConstraint(List<Constraint> assumptions, Constraint conclusion) {
        add(createImplicationConstraint(assumptions, conclusion));
    }
}

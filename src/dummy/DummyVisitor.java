package dummy;

import com.sun.source.tree.*;
import dummy.purity.PurityChecker;
import dummy.purity.qual.Impure;
import dummy.purity.qual.Pure;
import dummy.purity.utils.MethodSlot;
import dummy.purity.utils.MethodSlotManager;

import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.common.basetype.BaseTypeVisitor;
import org.checkerframework.javacutil.AnnotationUtils;
import org.checkerframework.javacutil.TreeUtils;

import checkers.inference.InferenceChecker;
import checkers.inference.InferenceVisitor;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.Elements;
import java.util.List;

public class DummyVisitor  extends InferenceVisitor<DummyChecker, BaseAnnotatedTypeFactory>  {

    private ExecutableElement currentMethod;
    private MethodSlotManager methodSlotManager;

    public DummyVisitor(DummyChecker checker, InferenceChecker ichecker,
                           BaseAnnotatedTypeFactory factory, boolean infer) {
        super(checker, ichecker, factory, infer);
        methodSlotManager = PurityInferenceController.getInstance().getMethodSlotManager();

        setAnnotations();
    }


    protected void setAnnotations() {

    }

    @Override
    public Void visitMethod(MethodTree node, Void p) {
        final ExecutableElement methodElem = TreeUtils.elementFromDeclaration(node);
        currentMethod = methodElem;

        MethodSlot methodSlot = methodSlotManager.addNewMethodSlot(atypeFactory, node);

        if (methodElem.getKind() == ElementKind.CONSTRUCTOR) {

        } else if (methodElem.getKind() == ElementKind.METHOD) {
            List<? extends AnnotationMirror> allAnnos = methodElem.getAnnotationMirrors();
            System.out.println("annotations on method " + currentMethod);
            System.out.println(allAnnos);

        }
        return super.visitMethod(node, p);
    }


    @Override
    public Void visitAssignment(AssignmentTree node, Void p) {
        ExpressionTree variable = node.getVariable();

        // lhs is a field access
        if (TreeUtils.isFieldAccess(variable)) {
            if (TreeUtils.getReceiverTree(variable) == null) {
                // modify this object / static field -> impure
                System.out.println("modify this object");
                methodSlotManager.addEqualityConstraint(currentMethod.getSimpleName().toString(),
                        methodSlotManager.getImpureSlot().getId());
            }
        } else if (variable instanceof ArrayAccessTree) {
            // lhs is array access
        } else {
            // lhs is a local variable
            //assert isLocalVariable(variable);
        }
        return super.visitAssignment(node, p);
    }


    @Override
    public Void visitMethodInvocation(MethodInvocationTree node, Void p) {
        final ExecutableElement methodElem = TreeUtils.elementFromUse(node);
        System.out.println(currentMethod.getSimpleName() + " call " + methodElem.getSimpleName());

        methodSlotManager.addSubtypeOfConstraint(
                currentMethod.getSimpleName().toString(),
                methodElem.getSimpleName().toString());

        return super.visitMethodInvocation(node, p);
    }
}

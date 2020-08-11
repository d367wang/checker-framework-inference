package dummy;

import com.sun.source.tree.*;
import com.sun.tools.javac.code.Symbol.MethodSymbol;

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

    private MethodSlot currentMethodSlot;
    private MethodSlotManager methodSlotManager;

    public DummyVisitor(DummyChecker checker, InferenceChecker ichecker,
                           BaseAnnotatedTypeFactory factory, boolean infer) {
        super(checker, ichecker, factory, infer);
        methodSlotManager = PurityInferenceController.getInstance().getMethodSlotManager();

    }


  
    @Override
    public Void visitMethod(MethodTree node, Void p) {
        final ExecutableElement methodElem = TreeUtils.elementFromDeclaration(node);

        MethodSlot methodSlot = methodSlotManager.addNewMethodSlot(atypeFactory, node);
        currentMethodSlot = methodSlot;

        AnnotationMirror anno = atypeFactory.getDeclAnnotation(methodElem, Pure.class);
        if (anno != null) {
          methodSlotManager.addEqualityConstraint(methodSlot.getId(),
                                                  methodSlotManager.getPureSlot().getId());
                  
        }

        anno = atypeFactory.getDeclAnnotation(methodElem, Impure.class);
        if (anno != null) {
          methodSlotManager.addEqualityConstraint(methodSlot.getId(),
                                                  methodSlotManager.getImpureSlot().getId());
                  
        }
        
        /*
        if (methodElem.getKind() == ElementKind.CONSTRUCTOR) {

        } else if (methodElem.getKind() == ElementKind.METHOD) {
            
        }
        List<? extends AnnotationMirror> allAnnos = methodElem.getAnnotationMirrors();
        System.out.println("annotations on method " + currentMethod);
        System.out.println(allAnnos);
        */

        System.out.println("\nvisiting method -------" + methodSlot.getId());

        return super.visitMethod(node, p);
    }


    @Override
    public Void visitAssignment(AssignmentTree node, Void p) {
        ExpressionTree variable = node.getVariable();

        // lhs is a field access
        if (TreeUtils.isFieldAccess(variable)) {
            ExpressionTree receiver = TreeUtils.getReceiverTree(variable);
          
            if (receiver == null) {
                // modify this object / static field -> impure
                System.out.println("modify this object");
                methodSlotManager.addEqualityConstraint(currentMethodSlot.getId(),
                        methodSlotManager.getImpureSlot().getId());

            } else if(receiver.getKind() == Tree.Kind.IDENTIFIER){
              if(!TreeUtils.isLocalVariable(receiver)) {
                System.out.println("write non-local variable");
                methodSlotManager.addEqualityConstraint(currentMethodSlot.getId(),
                                                        methodSlotManager.getImpureSlot().getId());
              }
                          
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
        System.out.println("\n" + currentMethodSlot.getId() + " call " + methodElem.getSimpleName());

        methodSlotManager.addSubtypeOfConstraint(
            currentMethodSlot.getId(),
            MethodSlot.generateMethodId(methodElem));

        return super.visitMethodInvocation(node, p);
    }
}

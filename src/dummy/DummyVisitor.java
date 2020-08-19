package dummy;

import com.sun.source.tree.*;
import com.sun.tools.javac.code.Symbol.MethodSymbol;

import dummy.purity.qual.Impure;
import dummy.purity.utils.MethodSlot;
import dummy.purity.utils.MethodSlotManager;

import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.common.basetype.BaseTypeVisitor;

import org.checkerframework.dataflow.qual.Deterministic;
import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.Pure.Kind;
import org.checkerframework.dataflow.qual.SideEffectFree;
import org.checkerframework.dataflow.util.PurityUtils;

import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.javacutil.AnnotationUtils;
import org.checkerframework.javacutil.ElementUtils;
import org.checkerframework.javacutil.TreeUtils;

import checkers.inference.InferenceChecker;
import checkers.inference.InferenceVisitor;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class DummyVisitor  extends InferenceVisitor<DummyChecker, BaseAnnotatedTypeFactory>  {

    private MethodSlot currentMethodSlot;
    private ExecutableElement currentMethodElem;
  
    private MethodSlotManager methodSlotManager;

    public DummyVisitor(DummyChecker checker, InferenceChecker ichecker,
                           BaseAnnotatedTypeFactory factory, boolean infer) {
        super(checker, ichecker, factory, infer);
        methodSlotManager = PurityInferenceController.getInstance().getMethodSlotManager();

    }


  
    @Override
    public Void visitMethod(MethodTree node, Void p) {
        final ExecutableElement methodElem = TreeUtils.elementFromDeclaration(node);
        currentMethodElem = methodElem;

        MethodSlot methodSlot = methodSlotManager.addNewMethodSlot(atypeFactory, node);
        currentMethodSlot = methodSlot;

        System.out.println("\nvisiting method -------" + methodSlot.getId());

       
        if (methodElem.getKind() == ElementKind.CONSTRUCTOR) {
            methodSlotManager.addEqualityConstraint(methodSlot.getId(),
                                                  methodSlotManager.getSideEffectFreeSlot().getId());
            System.out.println(methodSlot.getId() + " = side-effect-free");
            
        } else if (methodElem.getReturnType().getKind() == TypeKind.VOID) {
            methodSlotManager.addSubtypeOfConstraint(
                methodSlotManager.getDeterministicSlot().getId(),
                methodSlot.getId());
        }
        

        return super.visitMethod(node, p);
    }


    @Override
    public Void visitAssignment(AssignmentTree node, Void p) {

        if (currentMethodElem.getKind() == ElementKind.CONSTRUCTOR) {
            return super.visitAssignment(node, p);
        }
      
        ExpressionTree variable = node.getVariable();

        // lhs is a field access
        if (TreeUtils.isFieldAccess(variable)) {
            ExpressionTree receiver = TreeUtils.getReceiverTree(variable);
          
            if (receiver == null) {
                // modify this object / static field -> impure
                methodSlotManager.addSubtypeOfConstraint(
                    currentMethodSlot.getId(),
                    methodSlotManager.getDeterministicSlot().getId());

            } else if(receiver.getKind() == Tree.Kind.IDENTIFIER){
                if(!TreeUtils.isLocalVariable(receiver)) {
                    // modify non-local variable
                    methodSlotManager.addSubtypeOfConstraint(
                        currentMethodSlot.getId(),
                        methodSlotManager.getDeterministicSlot().getId());

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

      //        Set<AnnotationMirror> annos = atypeFactory.getDeclAnnotations(methodElem);
      //        System.out.println("get annotations from element:");
      //        for (AnnotationMirror a : annos) {
      //            System.out.println(a);
      //        }

      if (methodElem.getKind() == ElementKind.CONSTRUCTOR) {
        methodSlotManager.addSubtypeOfConstraint(currentMethodSlot.getId(),
                                                 methodSlotManager.getSideEffectFreeSlot().getId());
        System.out.println(currentMethodSlot.getId() + "<: side_effect_free");

        return super.visitMethodInvocation(node, p);
                
      }


      //if (atypeFactory.isFromByteCode(methodElem)) {
      if (!ElementUtils.isElementFromSourceCode(methodElem)) {
        System.out.println("method " + methodElem.getSimpleName() + " from byte code:");

        if (!PurityUtils.hasPurityAnnotation(atypeFactory, methodElem)) {
          methodSlotManager.addEqualityConstraint(currentMethodSlot.getId(),
                                                  methodSlotManager.getImpureSlot().getId());
          System.out.println(currentMethodSlot.getId() + "= impure");

                      
        } else {
          EnumSet<Pure.Kind> purityKinds = PurityUtils.getPurityKinds(atypeFactory, methodElem);

          if (purityKinds.contains(Kind.SIDE_EFFECT_FREE) &&
              !purityKinds.contains(Kind.DETERMINISTIC)) {
            methodSlotManager.addSubtypeOfConstraint(currentMethodSlot.getId(),
                                                     methodSlotManager.getSideEffectFreeSlot().getId());
            System.out.println(currentMethodSlot.getId() + "<: side_effect_free");

                            
          } else if (!purityKinds.contains(Kind.SIDE_EFFECT_FREE) &&
                     purityKinds.contains(Kind.DETERMINISTIC)) {
            methodSlotManager.addSubtypeOfConstraint(currentMethodSlot.getId(),
                                                     methodSlotManager.getDeterministicSlot().getId());
            System.out.println(currentMethodSlot.getId() + "<: deterministic");
                            
          }
                      
        }
                
      } else {
          methodSlotManager.addSubtypeOfConstraint(
              currentMethodSlot.getId(),
              MethodSlot.generateMethodId(methodElem));

          System.out.println(currentMethodSlot.getId() + " <: " + MethodSlot.generateMethodId(methodElem));
      }

      return super.visitMethodInvocation(node, p);
          
    }



    @Override
    public Void visitNewClass(NewClassTree node, Void ignore) {
      // Ordinarily, "new MyClass()" is forbidden.  It is permitted, however, when it is the
      // expression in "throw EXPR;".  (In the future, more expressions could be permitted.)
      //
      // The expression in "throw EXPR;" is allowed to be non-@Deterministic, so long as it is
      // not within a catch block that could catch an exception that the statement throws.
      // For example, EXPR can be object creation (a "new" expression) or can call a
      // non-deterministic method.
      //
      // Coarse rule (currently implemented):
      //  * permit only "throw new SomeExpression(args)", where the constructor is
      //    @SideEffectFree and the args are pure, and forbid all enclosing try statements
      //    that have a catch clause.
      // More precise rule:
      //  * permit other non-deterministic expresssions within throw (at which time move this
      //    logic to visitThrow()).
      //  * the only bad try statements are those with a catch block that is:
      //     * unchecked exceptions
      //        * checked = Exception or lower, but excluding RuntimeException and its
      //          subclasses
      //     * super- or sub-classes of the type of _expr_
      //        * if _expr_ is exactly "new SomeException", this can be changed to just
      //          "superclasses of SomeException".
      //     * super- or sub-classes of exceptions declared to be thrown by any component of
      //       _expr_.
      //     * need to check every containing try statement, not just the nearest enclosing
      //       one.

      // Object creation is usually prohibited, but permit "throw new SomeException();"
      // if it is not contained within any try statement that has a catch clause.
      // (There is no need to check the latter condition, because the Purity Checker
      // forbids all catch statements.)
      Tree parent = getCurrentPath().getParentPath().getLeaf();
      boolean okThrowDeterministic = parent.getKind() == Tree.Kind.THROW;

      assert TreeUtils.isUseOfElement(node) : "@AssumeAssertion(nullness): tree kind";
      Element ctorElement = TreeUtils.elementFromUse(node);
      boolean deterministic = okThrowDeterministic;
      boolean sideEffectFree = PurityUtils.isSideEffectFree(atypeFactory, ctorElement);
      // This does not use "addNotBothReason" because the reasons are different:  one is
      // because the constructor is called at all, and the other is because the constuctor
      // is not side-effect-free.
      /*
      if (!deterministic && !sideEffectFree) {
        methodSlotManager.addEqualityConstraint(currentMethodSlot.getId(),
                                                methodSlotManager.getImpureSlot().getId());

                    System.out.println(currentMethodSlot.getId() +  "= impure");

                
      } else if (sideEffectFree) {
        methodSlotManager.addSubtypeOfConstraint(
            currentMethodSlot.getId(),
            methodSlotManager.getSideEffectFreeSlot().getId());
                
      } else if (deterministic) {
        methodSlotManager.addSubtypeOfConstraint(
            currentMethodSlot.getId(),
            methodSlotManager.getDeterministicSlot().getId());
                
      }
      */

      //if (!deterministic) {
      if (!deterministic && (currentMethodElem.getReturnType().getKind() != TypeKind.VOID)) {
        methodSlotManager.addSubtypeOfConstraint(
            currentMethodSlot.getId(),
            methodSlotManager.getSideEffectFreeSlot().getId());
      }

      // TODO: if okThrowDeterministic, permit arguments to the newClass to be
      // non-deterministic (don't add those to purityResult), but still don't permit them to
      // have side effects.  This should probably wait until a rewrite of the Purity Checker.
      return super.visitNewClass(node, ignore);
          
    }
}

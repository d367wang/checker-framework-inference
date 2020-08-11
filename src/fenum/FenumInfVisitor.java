package fenum;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.LiteralTree;

import checkers.inference.InferenceChecker;
import checkers.inference.InferenceVisitor;
import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;

import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.framework.type.AnnotatedTypeMirror.AnnotatedExecutableType;

import org.checkerframework.javacutil.TreeUtils;

import javax.lang.model.element.ExecutableElement;

public class FenumInfVisitor extends InferenceVisitor<FenumInfChecker, BaseAnnotatedTypeFactory> {

    public FenumInfVisitor(
            FenumInfChecker checker,
            InferenceChecker ichecker,
            BaseAnnotatedTypeFactory factory,
            boolean infer) {
        super(checker, ichecker, factory, infer);
    }



    @Override
    public Void visitVariable(VariableTree node, Void p) {
      System.out.println("\nFenumInfVisitor visiting variable tree\n");
      
      AnnotatedTypeMirror type = atypeFactory.getAnnotatedType(node);
      mainIsNot(type, realChecker.FENUM_BOTTOM, "not.fenumbottom", node);

      return super.visitVariable(node, p);
          
    }


      @Override
      public Void visitMethod(MethodTree node, Void p) {
        final ExecutableElement methodElem = TreeUtils.elementFromDeclaration(node);
        AnnotatedExecutableType methodType = (AnnotatedExecutableType) atypeFactory.getAnnotatedType(node);
        mainIsNot(methodType.getReturnType(), realChecker.FENUM_BOTTOM, "not.fenumbottom", node);
        for (AnnotatedTypeMirror t : methodType.getParameterTypes()) {
          mainIsNot(t, realChecker.FENUM_BOTTOM, "not.fenumbottom", node);
                  
        }
        return super.visitMethod(node, p);
      }


    @Override
    public Void visitAssignment(AssignmentTree node, Void p) {
      System.out.println("\nFenumInfVisitor visiting assignment tree\n");
      return super.visitAssignment(node, p);
          
    }

      @Override
      public Void visitLiteral(LiteralTree node, Void p) {
        System.out.println("\nFenumInfVisitor visiting literal tree\n");
        return super.visitLiteral(node, p);
            
      }
}

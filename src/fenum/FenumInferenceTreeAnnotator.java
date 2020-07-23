package fenum;

import com.sun.source.tree.*;

import fenum.qual.Fenum;

import org.checkerframework.framework.type.AnnotatedTypeFactory;
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.javacutil.TreeUtils;

import checkers.inference.*;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;

import java.util.List;
import java.util.logging.Logger;
import java.util.Set;

public class FenumInferenceTreeAnnotator extends InferenceTreeAnnotator {
  private final VariableAnnotator variableAnnotator;
  private final AnnotatedTypeFactory realTypeFactory;
  private final SlotManager slotManager;

    public FenumInferenceTreeAnnotator(InferenceAnnotatedTypeFactory atypeFactory,
                                       InferrableChecker realChecker,
                                       AnnotatedTypeFactory realAnnotatedTypeFactory,
                                       VariableAnnotator variableAnnotator,
                                       SlotManager slotManager) {
        super(atypeFactory, realChecker, realAnnotatedTypeFactory, variableAnnotator, slotManager);
        this.variableAnnotator = variableAnnotator;
        this.realTypeFactory = realAnnotatedTypeFactory;
        this.slotManager = InferenceMain.getInstance().getSlotManager();
    }


    @Override
    public Void visitAnnotatedType(AnnotatedTypeTree node, AnnotatedTypeMirror atm) {
            System.out.println("fenum inference tree annotator is visiting annotated type tree");
        return super.visitAnnotatedType(node, atm);
    }


      @Override
      public Void visitAssignment(AssignmentTree assignmentTree, AnnotatedTypeMirror type) {
        //logger.fine("------------------------ visiting assignment tree ---------------------------");
            System.out.println("fenum inference tree annotator is visiting assignment tree");
            //InferenceMain.getInstance().logger.fine("fenum inference tree annotator is visiting assignment tree");
        return super.visitAssignment(assignmentTree, type);
      }

  @Override
  public Void visitMemberSelect(MemberSelectTree node, AnnotatedTypeMirror type) {
    System.out.println("fenum inference tree annotator is visiting member select tree: " + node.getIdentifier().toString());
    return super.visitMemberSelect(node, type);
        
  }

      @Override
      public Void visitClass(ClassTree classTree, AnnotatedTypeMirror classType) {
            System.out.println("fenum inference tree annotator is visiting class tree");
        return super.visitClass(classTree, classType);
            
      }

      @Override
      public Void visitIdentifier(IdentifierTree node, AnnotatedTypeMirror identifierType) {
        System.out.println("fenum inference tree annotator is visiting identifier: " + node.getName().toString());
        return super.visitIdentifier(node, identifierType);
            
      }

      @Override
      public Void visitTypeParameter(TypeParameterTree typeParamTree, AnnotatedTypeMirror atm) {
            System.out.println("fenum inference tree annotator is visiting type parameter tree");
        return super.visitTypeParameter(typeParamTree, atm);
            
      }

      @Override
      public Void visitMethod(MethodTree methodTree, AnnotatedTypeMirror atm) {
        System.out.println("fenum inference tree annotator is visiting method tree: " + methodTree.getName().toString());
        return super.visitMethod(methodTree, atm);
            
      }

      @Override
      public Void visitMethodInvocation(MethodInvocationTree methodInvocationTree, AnnotatedTypeMirror atm) {
            System.out.println("fenum inference tree annotator is visiting method invocation tree");
        return super.visitMethodInvocation(methodInvocationTree, atm);
            
      }

      @Override
      public Void visitNewClass(NewClassTree newClassTree, AnnotatedTypeMirror atm) {
            System.out.println("fenum inference tree annotator is visiting new class tree");
        return super.visitNewClass(newClassTree, atm);
            
      }

      @Override
      public Void visitVariable(VariableTree varTree, AnnotatedTypeMirror atm) {
        System.out.println("fenum inference tree annotator is visiting variable tree: " + varTree.getName().toString());
        //final VariableElement varElem = TreeUtils.elementFromDeclaration(varTree);
        // AnnotationMirror anno = realTypeFactory.getDeclAnnotation(varElem, Fenum.class);
        AnnotationMirror anno = realTypeFactory.getAnnotationMirror(varTree, Fenum.class);

        //Set<AnnotationMirror> annos = atm.getExplicitAnnotations();
            //List<? extends AnnotationMirror> annos = TreeUtils.annotationsFromTree(varTree);
        if(anno != null) {
              System.out.println("find annotation: " + anno.toString());
              ((FenumAnnotatedTypeFactory)realTypeFactory).addFenumAnno(anno);
                      
        } 
            return super.visitVariable(varTree, atm);
            
      }

      @Override
      public Void visitNewArray(NewArrayTree newArrayTree, AnnotatedTypeMirror atm) {
            System.out.println("fenum inference tree annotator is visiting new array tree");
        return super.visitNewArray(newArrayTree, atm);
            
      }

      @Override
      public Void visitTypeCast(TypeCastTree typeCast, AnnotatedTypeMirror atm) {
            System.out.println("fenum inference tree annotator is visiting type cast tree");
        return super.visitTypeCast(typeCast, atm);
            
      }

      @Override
      public Void visitInstanceOf(InstanceOfTree instanceOfTree, AnnotatedTypeMirror atm) {
            System.out.println("fenum inference tree annotator is visiting instanceof tree");
        return super.visitInstanceOf(instanceOfTree, atm);
            
      }

      @Override
      public Void visitLiteral(LiteralTree literalTree, AnnotatedTypeMirror atm) {
        System.out.println("fenum inference tree annotator is visiting literal tree: " + literalTree.getValue().toString());
        return super.visitLiteral(literalTree, atm);
            
      }

      @Override
      public Void visitUnary(UnaryTree node, AnnotatedTypeMirror type) {
            System.out.println("fenum inference tree annotator is visiting unary tree");
        return super.visitUnary(node, type);
            
      }

      @Override
      public Void visitCompoundAssignment(CompoundAssignmentTree node, AnnotatedTypeMirror type) {
            System.out.println("fenum inference tree annotator is visiting compound assignment tree");
        return super.visitCompoundAssignment(node, type);
            
      }

      @Override
      public Void visitBinary(BinaryTree node, AnnotatedTypeMirror type) {
            System.out.println("fenum inference tree annotator is visiting binary tree");
        return super.visitBinary(node, type);
            
      }

      @Override
      public Void visitParameterizedType(ParameterizedTypeTree param, AnnotatedTypeMirror atm) {
            System.out.println("fenum inference tree annotator is visiting parameterized type tree");
        return super.visitParameterizedType(param, atm);
            
      }

}

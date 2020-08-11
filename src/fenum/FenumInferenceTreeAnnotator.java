package fenum;

import checkers.inference.model.AnnotationLocation;
import checkers.inference.model.ConstantSlot;
import checkers.inference.model.VariableSlot;
import checkers.inference.qual.VarAnnot;
import checkers.inference.util.ASTPathUtil;

import com.sun.source.tree.*;
import com.sun.source.util.TreePath;

import fenum.qual.Fenum;

import org.checkerframework.framework.type.AnnotatedTypeFactory;
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.javacutil.AnnotationBuilder;
import org.checkerframework.javacutil.TreeUtils;
import org.checkerframework.javacutil.ElementUtils;

import checkers.inference.*;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;

import java.util.List;
import java.util.logging.Logger;
import java.util.Set;

import scenelib.annotations.io.ASTPath;
import scenelib.annotations.io.ASTRecord;


public class FenumInferenceTreeAnnotator extends InferenceTreeAnnotator {
  private final VariableAnnotator variableAnnotator;
  private final AnnotatedTypeFactory realTypeFactory;
  private final SlotManager slotManager;

  public static final Logger logger = Logger.getLogger(FenumInferenceTreeAnnotator.class.getSimpleName());
  
  private final AnnotationMirror realTop;
  

    public FenumInferenceTreeAnnotator(InferenceAnnotatedTypeFactory atypeFactory,
                                       InferrableChecker realChecker,
                                       AnnotatedTypeFactory realAnnotatedTypeFactory,
                                       VariableAnnotator variableAnnotator,
                                       SlotManager slotManager) {
        super(atypeFactory, realChecker, realAnnotatedTypeFactory, variableAnnotator, slotManager);
        this.variableAnnotator = variableAnnotator;
        this.realTypeFactory = realAnnotatedTypeFactory;
        this.slotManager = InferenceMain.getInstance().getSlotManager();

        this.realTop = realTypeFactory.getQualifierHierarchy().getTopAnnotations().iterator().next();
        
    }

  /*
      @Override
      public Void visitAssignment(AssignmentTree assignmentTree, AnnotatedTypeMirror type) {
        //logger.fine("------------------------ visiting assignment tree ---------------------------");
            System.out.println("\nfenum inference tree annotator is visiting assignment tree");
            System.out.println("assignment tree type is " + type.getClass().getSimpleName() + "\n");
            //InferenceMain.getInstance().logger.fine("fenum inference tree annotator is visiting assignment tree");
        return super.visitAssignment(assignmentTree, type);
      }
*/

      @Override
      public Void visitIdentifier(IdentifierTree node, AnnotatedTypeMirror identifierType) {
        System.out.println("\nfenum inference tree annotator is visiting identifier: " + node.getName().toString());
            System.out.println("identifier tree type is " + identifierType.getClass().getSimpleName() + "\n");
        return super.visitIdentifier(node, identifierType);
            
      }


      @Override
      public Void visitVariable(VariableTree varTree, AnnotatedTypeMirror atm) {
        System.out.println("\nfenum inference tree annotator is visiting variable tree: " + varTree.getName().toString());
            System.out.println("variable tree type is " + atm.getClass().getSimpleName() + "\n");
            
        //final VariableElement varElem = TreeUtils.elementFromDeclaration(varTree);
        // AnnotationMirror anno = realTypeFactory.getDeclAnnotation(varElem, Fenum.class);
        AnnotationMirror anno = realTypeFactory.getAnnotationMirror(varTree, Fenum.class);

        //Set<AnnotationMirror> annos = atm.getExplicitAnnotations();
            //List<? extends AnnotationMirror> annos = TreeUtils.annotationsFromTree(varTree);
        if(anno != null) {
              System.out.println("find annotation: " + anno.toString());
              ((FenumAnnotatedTypeFactory)realTypeFactory).addFenumAnno(anno);
                      
        } 
        /*
        VariableElement elem = (VariableElement) TreeUtils.elementFromDeclaration(varTree);
        Set<Modifier> modifiers = elem.getModifiers();
        final ExpressionTree initializer = varTree.getInitializer();
        // if the variable is final static, and the initializer is literal, change the ATM of literal to @VarAnno
        if (modifiers.contains(Modifier.STATIC) &&
            modifiers.contains(Modifier.FINAL) &&
            initializer instanceof LiteralTree) {
          System.out.println("final static variable!!!!!!!!!!!!!!!!!!!!!!!!!");
          System.out.println("initializer for variable " + varTree.getName() + " is literal, replace ATM");
          final AnnotatedTypeMirror initializerATM = atypeFactory.getAnnotatedType(initializer);

          final TreePath path = atypeFactory.getPath(initializer);
          ASTRecord record = ASTPathUtil.getASTRecordForPath(atypeFactory, path);
          AnnotationLocation location = new AnnotationLocation.AstPathLocation(record);
          VariableSlot varSlot = slotManager.createVariableSlot(location);
          initializerATM.replaceAnnotation(slotManager.getAnnotation(varSlot));
                  
        }
        */
        
        return super.visitVariable(varTree, atm);
            
      }

 

      @Override
      public Void visitLiteral(LiteralTree literalTree, AnnotatedTypeMirror atm) {
        System.out.println("\nfenum inference tree annotator is visiting literal tree: " + literalTree.getValue().toString());
            System.out.println("literal tree type is " + atm.getClass().getSimpleName() + "\n");

        TreePath path = atypeFactory.getPath(literalTree);
        if (path != null) {
          final TreePath parentPath = path.getParentPath();
          final Tree parentNode = parentPath.getLeaf();
          System.out.println("parentNode of the literal is " + parentNode.getClass().getSimpleName());
          if (parentNode.getKind() == Tree.Kind.VARIABLE) {
                System.out.println("********************assign literal to variable ***********************");
              final Element varElem = TreeUtils.elementFromDeclaration((VariableTree)parentNode);
              if (ElementUtils.isFinal(varElem) && ElementUtils.isStatic(varElem)) {
                System.out.println("********************assign literal to a final static variable****************");

                AnnotatedTypeMirror parentATM = atypeFactory.getAnnotatedType(parentNode);
                AnnotationMirror am = parentATM.getAnnotationInHierarchy(this.realTop);
                if(am != null) {
                  final ConstantSlot cs = slotManager.createConstantSlot(am);
                  AnnotationBuilder ab = new AnnotationBuilder(realTypeFactory.getProcessingEnv(), VarAnnot.class);
                  ab.setValue("value", cs.getId());
                  AnnotationMirror varAnno = ab.build();
                  atm.replaceAnnotation(varAnno);
                                      
                }
              }
            }
        }
        return super.visitLiteral(literalTree, atm);
            
      }

}

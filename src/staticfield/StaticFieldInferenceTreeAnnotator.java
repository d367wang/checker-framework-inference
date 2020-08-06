package staticfield;

import checkers.inference.*;
import checkers.inference.model.ConstantSlot;
import checkers.inference.qual.VarAnnot;
import com.sun.source.tree.*;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.util.TreePath;
import org.checkerframework.framework.type.AnnotatedTypeFactory;
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.framework.type.AnnotatedTypeMirror.AnnotatedArrayType;
import org.checkerframework.javacutil.AnnotationBuilder;
import org.checkerframework.javacutil.ElementUtils;
import org.checkerframework.javacutil.TreeUtils;
import staticfield.qual.StaticField;
import staticfield.qual.NonStaticField;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * DataflowInferenceTreeAnnotator creates constant slot for base cases.
 * 
 * @author jianchu
 *
 */
public class StaticFieldInferenceTreeAnnotator extends InferenceTreeAnnotator {

    private final VariableAnnotator variableAnnotator;
    private final AnnotatedTypeFactory realTypeFactory;
    private final SlotManager slotManager;

    public StaticFieldInferenceTreeAnnotator(InferenceAnnotatedTypeFactory atypeFactory,
                                             InferrableChecker realChecker, AnnotatedTypeFactory realAnnotatedTypeFactory,
                                             VariableAnnotator variableAnnotator, SlotManager slotManager) {
        super(atypeFactory, realChecker, realAnnotatedTypeFactory, variableAnnotator, slotManager);

        this.variableAnnotator = variableAnnotator;
        this.realTypeFactory = realAnnotatedTypeFactory;
        this.slotManager = InferenceMain.getInstance().getSlotManager();
    }


    @Override
    public Void visitVariable(VariableTree varTree, AnnotatedTypeMirror atm) {
      Element elm = TreeUtils.elementFromDeclaration(varTree);
      ProcessingEnvironment processingEnv = this.realTypeFactory.getProcessingEnv();
      final Elements elements = processingEnv.getElementUtils();

      if(ElementUtils.isStatic(elm)) {
        AnnotationMirror anno = AnnotationBuilder.fromClass(elements, StaticField.class);
        replaceATM(atm, anno);
                
      } else if (TreeUtils.typeOf(varTree).getKind().isPrimitive()) {
        AnnotationMirror anno = AnnotationBuilder.fromClass(elements, NonStaticField.class);
        replaceATM(atm, anno);
                
      } else {
        super.visitVariable(varTree, atm);
                
      }
      return null;
    }


    private void replaceATM(AnnotatedTypeMirror atm, AnnotationMirror anno) {
        final ConstantSlot cs = slotManager.createConstantSlot(anno);
        AnnotationBuilder ab = new AnnotationBuilder(realTypeFactory.getProcessingEnv(), VarAnnot.class);
        ab.setValue("value", cs.getId());
        AnnotationMirror varAnno = ab.build();
        atm.replaceAnnotation(varAnno);
    }
}

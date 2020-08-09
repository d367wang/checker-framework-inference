package dummy;

import dummy.qual.DummyBottom;
import dummy.qual.DummyTop;
import dummy.purity.qual.Impure;
import dummy.purity.qual.Pure;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.framework.type.QualifierHierarchy;
import org.checkerframework.framework.util.GraphQualifierHierarchy;
import org.checkerframework.framework.util.MultiGraphQualifierHierarchy.MultiGraphFactory;
import org.checkerframework.javacutil.AnnotationBuilder;


public class DummyAnnotatedTypeFactory extends BaseAnnotatedTypeFactory {

    protected final AnnotationMirror DUMMYBOTTOM, DUMMYTOP;
    protected final AnnotationMirror IMPURE;
  
    /**
     * For each Java type is present in the target program, typeNamesMap maps
     * String of the type to the TypeMirror.
     */
    private final Map<String, TypeMirror> typeNamesMap = new HashMap<String, TypeMirror>();

    public DummyAnnotatedTypeFactory(BaseTypeChecker checker) {
        super(checker);
        DUMMYTOP = AnnotationBuilder.fromClass(elements, DummyTop.class);
        DUMMYBOTTOM = AnnotationBuilder.fromClass(elements, DummyBottom.class);
        IMPURE = AnnotationBuilder.fromClass(elements, Impure.class);
        
        postInit();
    }

//    @Override
//    public TreeAnnotator createTreeAnnotator() {
//        return new ListTreeAnnotator(super.createTreeAnnotator(), new DataflowAnnotatedTypeFactory.DataflowTreeAnnotator());
//    }

    @Override
    public QualifierHierarchy createQualifierHierarchy(MultiGraphFactory factory) {
        return new DummyQualifierHierarchy(factory, DUMMYBOTTOM);
    }

    private final class DummyQualifierHierarchy extends GraphQualifierHierarchy {

        public DummyQualifierHierarchy(MultiGraphFactory f, AnnotationMirror bottom) {
            super(f, bottom);
        }
    }


  public QualifierHierarchy createPurityQualifierHierarchy() {
    /*
            Set<Class<? extends Annotation>> qualSet =
                getBundledTypeQualifiers(
                    Pure.class,
                    Impure.class);
            return createQualifierHierarchy(this.elements, qualSet, createQualifierHierarchyFactory());
    */
    return new GraphQualifierHierarchy(createQualifierHierarchyFactory(), IMPURE);
    
  }
}

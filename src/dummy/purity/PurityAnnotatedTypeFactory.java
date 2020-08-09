package dummy.purity;

import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.framework.type.QualifierHierarchy;
import org.checkerframework.framework.type.treeannotator.ListTreeAnnotator;
import org.checkerframework.framework.type.treeannotator.LiteralTreeAnnotator;
import org.checkerframework.framework.type.treeannotator.TreeAnnotator;
import org.checkerframework.framework.util.GraphQualifierHierarchy;
import org.checkerframework.framework.util.MultiGraphQualifierHierarchy;
import org.checkerframework.javacutil.AnnotationBuilder;

import dummy.purity.qual.Impure;
import dummy.purity.qual.Pure;

import javax.lang.model.element.AnnotationMirror;
import java.lang.annotation.Annotation;
import java.util.Set;

public class PurityAnnotatedTypeFactory extends BaseAnnotatedTypeFactory{
    protected final AnnotationMirror IMPURE, PURE;

    public PurityAnnotatedTypeFactory(BaseTypeChecker checker) {
        super(checker);
        IMPURE = AnnotationBuilder.fromClass(elements, Impure.class);
        PURE = AnnotationBuilder.fromClass(elements, Pure.class);
        postInit();
    }

//    @Override
//    public TreeAnnotator createTreeAnnotator() {
//        return new ListTreeAnnotator(super.createTreeAnnotator(), new DataflowAnnotatedTypeFactory.DataflowTreeAnnotator());
//    }


    @Override
    protected Set<Class<? extends Annotation>> createSupportedTypeQualifiers() {
        // Load everything in qual directory, and top, bottom, unqualified, and fake enum
        Set<Class<? extends Annotation>> qualSet =
                getBundledTypeQualifiers(
                        Pure.class,
                        Impure.class);

        // TODO: warn if no qualifiers given?
        return qualSet;
    }


        @Override
    public QualifierHierarchy createQualifierHierarchy(MultiGraphQualifierHierarchy.MultiGraphFactory factory) {
        return new PurityQualifierHierarchy(factory, IMPURE);
    }

    private final class PurityQualifierHierarchy extends GraphQualifierHierarchy {

        public PurityQualifierHierarchy(MultiGraphFactory f, AnnotationMirror bottom) {
            super(f, bottom);
        }
    }
}

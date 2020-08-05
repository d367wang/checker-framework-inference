package staticfield;

import staticfield.qual.AnyField;
import staticfield.qual.StaticField;

import javax.lang.model.element.AnnotationMirror;

import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;
import org.checkerframework.javacutil.AnnotationBuilder;

import org.checkerframework.framework.type.QualifierHierarchy;
import org.checkerframework.framework.type.treeannotator.TreeAnnotator;

import org.checkerframework.framework.util.GraphQualifierHierarchy;
import org.checkerframework.framework.util.MultiGraphQualifierHierarchy.MultiGraphFactory;

public class StaticFieldAnnotatedTypeFactory extends BaseAnnotatedTypeFactory {

  protected final AnnotationMirror ANY_FIELD, STATIC_FIELD;

    public StaticFieldAnnotatedTypeFactory(BaseTypeChecker checker) {
        super(checker);
        ANY_FIELD = AnnotationBuilder.fromClass(elements, AnyField.class);
        STATIC_FIELD = AnnotationBuilder.fromClass(elements, StaticField.class);
        postInit();
    }


    @Override
    public QualifierHierarchy createQualifierHierarchy(MultiGraphFactory factory) {
        return new StaticFieldQualifierHierarchy(factory, STATIC_FIELD);
    }


    public class StaticFieldTreeAnnotator extends TreeAnnotator {
        public StaticFieldTreeAnnotator() {
            super(StaticFieldAnnotatedTypeFactory.this);
        }
    }


  private final class StaticFieldQualifierHierarchy extends GraphQualifierHierarchy {

    public StaticFieldQualifierHierarchy(MultiGraphFactory f, AnnotationMirror bottom) {
      super(f, bottom);
    }
        
  }
}

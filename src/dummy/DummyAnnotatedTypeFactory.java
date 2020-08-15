package dummy;

import dummy.qual.DummyAnno;

import dummy.purity.qual.Impure;
import dummy.purity.qual.Pure;
import dummy.purity.qual.Deterministic;
import dummy.purity.qual.SideEffectFree;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.framework.type.QualifierHierarchy;
import org.checkerframework.framework.util.MultiGraphQualifierHierarchy;
import org.checkerframework.framework.util.MultiGraphQualifierHierarchy.MultiGraphFactory;
import org.checkerframework.javacutil.AnnotationBuilder;
import org.checkerframework.javacutil.AnnotationUtils;

public class DummyAnnotatedTypeFactory extends BaseAnnotatedTypeFactory {

    protected final AnnotationMirror DUMMYANNO;
    protected final AnnotationMirror IMPURE, PURE;
  
    /**
     * For each Java type is present in the target program, typeNamesMap maps
     * String of the type to the TypeMirror.
     */
    private final Map<String, TypeMirror> typeNamesMap = new HashMap<String, TypeMirror>();

    public DummyAnnotatedTypeFactory(BaseTypeChecker checker) {
        super(checker);
        DUMMYANNO= AnnotationBuilder.fromClass(elements, DummyAnno.class); 
        PURE = AnnotationBuilder.fromClass(elements, Pure.class);
        IMPURE = AnnotationBuilder.fromClass(elements, Impure.class);
        
        postInit();
    }

//    @Override
//    public TreeAnnotator createTreeAnnotator() {
//        return new ListTreeAnnotator(super.createTreeAnnotator(), new DataflowAnnotatedTypeFactory.DataflowTreeAnnotator());
//    }

    @Override
    public QualifierHierarchy createQualifierHierarchy(MultiGraphFactory factory) {
        return new DummyQualifierHierarchy(factory);
    }

    public final class DummyQualifierHierarchy extends MultiGraphQualifierHierarchy {

        public DummyQualifierHierarchy(MultiGraphFactory f) {
            super(f);
        }

      /*
        @Override
        public boolean isSubtype(AnnotationMirror subAnno, AnnotationMirror superAnno) {
          if (AnnotationUtils.areSame(subAnno, superAnno)) {
            return true;
                        
          }

          if (AnnotationUtils.areSameByClass(subAnno, Pure.class)) {
            return false;
                        
          } else if (AnnotationUtils.areSameByClass(subAnno, SideEffectFree.class) ||
                     AnnotationUtils.areSameByClass(subAnno, Deterministic.class)) {
            return AnnotationUtils.areSameByClass(superAnno, Pure.class);
                        
          } else if (AnnotationUtils.areSameByClass(subAnno, Impure.class)) {
            return AnnotationUtils.areSameByClass(superAnno, Pure.class) ||
                AnnotationUtils.areSameByClass(superAnno, SideEffectFree.class) ||
                AnnotationUtils.areSameByClass(superAnno, Deterministic.class);
                        
          } else {
            return super.isSubtype(subAnno, superAnno);
                        
          }
                  
          } */


                @Override
                public boolean isSubtype(AnnotationMirror subAnno, AnnotationMirror superAnno) {
                  boolean res;
                  if (AnnotationUtils.areSame(subAnno, superAnno)) {
                    res = true;

                                
                  } else {
                    if (AnnotationUtils.areSameByClass(subAnno, Pure.class)) {
                      res = false;

                                      
                    } else if (AnnotationUtils.areSameByClass(subAnno, SideEffectFree.class) ||
                               AnnotationUtils.areSameByClass(subAnno, Deterministic.class)) {
                      res = AnnotationUtils.areSameByClass(superAnno, Pure.class);

                                      
                    } else if (AnnotationUtils.areSameByClass(subAnno, Impure.class)) {
                      res = AnnotationUtils.areSameByClass(superAnno, Pure.class) ||
                            AnnotationUtils.areSameByClass(superAnno, SideEffectFree.class) ||
                            AnnotationUtils.areSameByClass(superAnno, Deterministic.class);

                                      
                    } else {
                      res = false;
                                      
                    }
                                
                  }
                  return res;
                          
                }


        public AnnotationMirror getPurityTop() {
          return PURE;
        }

        public AnnotationMirror getPurityBottom() {
          return IMPURE;
                
        }
    }


  public final Set<Class<? extends Annotation>> getPurityTypeQualifiers() {
      Set<Class<? extends Annotation>> qualSet = new HashSet<>(
          Arrays.asList(Pure.class, SideEffectFree.class, Deterministic.class, Impure.class));
      return qualSet;
  }
}

package dummy.purity.utils;

import checkers.inference.VariableAnnotator;
import checkers.inference.model.AnnotationLocation;
import checkers.inference.util.ASTPathUtil;
import com.sun.source.tree.MethodTree;

import org.checkerframework.framework.type.AnnotatedTypeFactory;
import org.checkerframework.javacutil.TreeUtils;
import org.checkerframework.javacutil.ElementUtils;

import scenelib.annotations.io.ASTRecord;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;


public class MethodSlot {
//    private ExecutableElement methodElement;
    private AnnotationLocation location;

    private MethodTree tree;

    protected String id;

    private AnnotationMirror anno;

    protected boolean isConstant = false;


    public String getId() {
        return id;
    }

    public AnnotationLocation getLocation() {
        return location;
    }

    public boolean isConstant() {
        return isConstant;
    }

    public AnnotationMirror getAnno() {
        return anno;
    }

    protected MethodSlot(String id, MethodTree node, AnnotationMirror anno, AnnotationLocation location) {
        this.location = location;
        this.anno = anno;
        this.id = id;
        this.tree = node;
    }


  public static MethodSlot create(AnnotatedTypeFactory typeFactory, MethodTree node, AnnotationMirror anno) {
    AnnotationLocation location = VariableAnnotator.treeToLocation(typeFactory, node.getReturnType());
    return new MethodSlot(generateMethodId(node), node, anno, location);
        
  }

  public static String generateMethodId(MethodTree node) {
    final ExecutableElement methodElem = TreeUtils.elementFromDeclaration(node);
    return generateMethodId(methodElem);
        
  }

  public static String generateMethodId(ExecutableElement elm) {
    TypeElement typeElement = ElementUtils.enclosingClass(elm);
    return String.join("::", typeElement.getQualifiedName().toString(), elm.toString());
        
  }


    public boolean equals(MethodSlot other) {
        return this.tree == other.tree;
    }


  public boolean isVoid() {
    final ExecutableElement methodElem = TreeUtils.elementFromDeclaration(this.tree);
    return methodElem.getReturnType().getKind() == TypeKind.VOID;
        
  }
}

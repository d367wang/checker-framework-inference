package dummy.purity.utils;

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

public class MethodSlot {
//    private ExecutableElement methodElement;
    private AnnotationLocation location;

    private MethodTree tree;

    protected String id;

    private AnnotationMirror anno;

    protected boolean isConstant = false;

//    private SlotKind kind;


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

    protected MethodSlot(String id, AnnotationMirror anno, AnnotationLocation location) {
        this.location = location;
        this.anno = anno;
        this.id = id;
    }


  public static MethodSlot create(AnnotatedTypeFactory typeFactory, MethodTree node, AnnotationMirror anno) {
    ASTRecord record = ASTPathUtil.getASTRecordForNode(typeFactory, node);
    AnnotationLocation location = new AnnotationLocation.AstPathLocation(record);
    return new MethodSlot(generateMethodId(node), anno, location);
        
  }

  public static String generateMethodId(MethodTree node) {
    final ExecutableElement methodElem = TreeUtils.elementFromDeclaration(node);
    return generateMethodId(methodElem);
        
  }

  public static String generateMethodId(ExecutableElement elm) {
    TypeElement typeElement = ElementUtils.enclosingClass(elm);
    return String.join(".", typeElement.getQualifiedName().toString(), elm.toString());
        
  }

//    public enum SlotKind {
//        VARIABLE,
//        CONSTANT,
//    }
//
//    public void setKind(SlotKind k) { this.kind = k; }
//    public SlotKind getKind() { return kind;}
//
//    public boolean isVariable() {
//        return !isConstant();
//    }
//
//    public boolean isConstant() {
//        return getKind() == SlotKind.CONSTANT;
//    }

    public boolean equals(MethodSlot other) {
        return this.tree == other.tree;
    }
}

package staticfield;

import checkers.inference.InferenceChecker;
import checkers.inference.InferenceVisitor;

import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.VariableTree;
import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;

import org.checkerframework.framework.type.AnnotatedTypeMirror;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

import org.checkerframework.javacutil.ElementUtils;
import org.checkerframework.javacutil.TreeUtils;

public class StaticFieldInferenceVisitor extends InferenceVisitor<StaticFieldInferenceChecker, BaseAnnotatedTypeFactory> {
    public StaticFieldInferenceVisitor(StaticFieldInferenceChecker checker, InferenceChecker ichecker, BaseAnnotatedTypeFactory baseAnnotatedTypeFactory, boolean infer) {
        super(checker, ichecker, baseAnnotatedTypeFactory, infer);
    }

    @Override
    public Void visitAssignment(AssignmentTree node, Void p) {
        ExpressionTree variable = node.getVariable();
        ExpressionTree expr = node.getExpression();
        AnnotatedTypeMirror varType = atypeFactory.getAnnotatedType(variable);

        if(variable instanceof VariableTree) {
            VariableTree varTree = (VariableTree) variable;
            //VariableElement varElement = (VariableElement)TreeUtils.elementFromUse(varTree);
            VariableElement varElement = (VariableElement)TreeUtils.elementFromTree(varTree);
            if (ElementUtils.isStatic(varElement)) {
                return super.scan(node.getExpression(), p);
            }
        }

        return super.visitAssignment(node, p);
    }

}

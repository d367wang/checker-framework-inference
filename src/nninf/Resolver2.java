package nninf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

import org.checkerframework.javacutil.BugInCF;

import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.api.JavacScope;
import com.sun.tools.javac.code.Kinds.Kind;
import com.sun.tools.javac.code.Kinds.KindSelector;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

/**
 * A Utility class to find symbols corresponding to string references
 *
 * TODO: This class is only supposed to be used temporarily until a good solution for the MapGetHeuristic is found.
 */
public class Resolver2 {
    private final Resolve resolve;
    private final Names names;
    private final Trees trees;

    private final Method FIND_IDENT;
    private final Method FIND_IDENT_IN_PACKAGE;
    private final Method FIND_MEMBER_TYPE;
    private final Method FIND_IDENT_IN_TYPE;

    public Resolver2(ProcessingEnvironment env) {
        Context context = ((JavacProcessingEnvironment)env).getContext();
        this.resolve = Resolve.instance(context);
        this.names = Names.instance(context);
        this.trees = Trees.instance(env);
        Method fi, fiip, fmt, fiit;
        fi = fiip = fmt = fiit = null;
        try {
            fi = Resolve.class.getDeclaredMethod(
                    "findIdent",
                    Env.class, Name.class, KindSelector.class);
            fi.setAccessible(true);

            fiip = Resolve.class.getDeclaredMethod(
                    "findIdentInPackage",
                    Env.class, TypeSymbol.class, Name.class, KindSelector.class);
            fiip.setAccessible(true);

            fmt = Resolve.class.getDeclaredMethod(
                    "findMemberType",
                    Env.class,
                    Type.class,
                    Name.class,
                    TypeSymbol.class);
            fmt.setAccessible(true);

            fiit = Resolve.class.getDeclaredMethod(
                    "findIdentInType",
                    Env.class, Type.class, Name.class, KindSelector.class);
            fiit.setAccessible(true);
        } catch (Exception e) {
            throw new BugInCF("Compiler 'Resolve' class doesn't contain required 'findXXX' method", e);
            // Need the local fi, fiip, fmt, and fiit variables to keep def assignment happy
        }
        this.FIND_IDENT = fi;
        this.FIND_IDENT_IN_PACKAGE = fiip;
        this.FIND_MEMBER_TYPE = fmt;
        this.FIND_IDENT_IN_TYPE = fiit;
    }

    /**
     * Finds the variable referenced in the passed {@code String}.
     *
     * This method may only operate on variable references, e.g. local
     * variables, parameters, fields.
     *
     * The reference string may be either an single Java identifier (e.g. "field")
     * or dot-separated identifiers (e.g. "Collections.EMPTY_LIST").
     *
     * The method adheres to all the rules of Java's scoping (while also
     * considering the imports) for name resolution.
     *
     * @param reference     the variable reference string
     * @param path          the tree path to the local scope
     * @return  the variable reference
     */
    public Element findVariable(String reference, TreePath path) {
        JavacScope scope = (JavacScope) trees.getScope(path);
        Env<AttrContext> env = scope.getEnv();

        if (!reference.contains(".")) {
            // Simple variable
            return wrapInvocation(
                    FIND_IDENT,
                    env, names.fromString(reference), KindSelector.VAR);
        } else {
            int lastDot = reference.lastIndexOf('.');
            String expr = reference.substring(0, lastDot);
            String name = reference.substring(lastDot + 1);

            Element site = findType(expr, env);
            Name ident = names.fromString(name);

            return wrapInvocation(
                    FIND_IDENT_IN_TYPE,
                    env, site.asType(), ident, KindSelector.VAR);
        }

    }

    private Element findType(String reference, Env<AttrContext> env) {
        if (!reference.contains(".")) {
            // Simple variable
            return wrapInvocation(
                    FIND_IDENT,
                    env, names.fromString(reference), KindSelector.of(KindSelector.TYP, KindSelector.PCK));
        } else {
            int lastDot = reference.lastIndexOf(".");
            String expr = reference.substring(0, lastDot);
            String idnt = reference.substring(lastDot + 1);

            Symbol site = (Symbol)findType(expr, env);
            if (site.kind == Kind.ERR) {
                return site;
            }
            Name name = names.fromString(idnt);
            if (site.kind == Kind.PCK) {
                env.toplevel.packge = (PackageSymbol)site;
                return wrapInvocation(
                        FIND_IDENT_IN_PACKAGE,
                        env, site, name, KindSelector.of(KindSelector.TYP, KindSelector.PCK));
            } else {
                env.enclClass.sym = (ClassSymbol)site;
                return wrapInvocation(
                        FIND_MEMBER_TYPE,
                        env, site.asType(), name, site);
            }
        }
    }

    private Symbol wrapInvocation(Method method, Object... args) {
        try {
            return (Symbol)method.invoke(resolve, args);
        } catch (IllegalAccessException e) {
            throw new BugInCF("Resolver.wrapInvocation: unexpected Reflection error", e);
        } catch (IllegalArgumentException e) {
            throw new BugInCF("Resolver.wrapInvocation: unexpected Reflection error", e);
        } catch (InvocationTargetException e) {
            throw new BugInCF("Resolver.wrapInvocation: unexpected Reflection error", e);
        }
    }
}

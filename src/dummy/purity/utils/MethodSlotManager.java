package dummy.purity.utils;

import checkers.inference.qual.VarAnnot;
import com.sun.source.tree.MethodTree;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.Elements;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dummy.purity.qual.Impure;
import dummy.purity.qual.Pure;
import org.checkerframework.framework.type.AnnotatedTypeFactory;
import org.checkerframework.javacutil.AnnotationBuilder;


public class MethodSlotManager {

    AnnotatedTypeFactory atypeFactory;
    public final AnnotationMirror PURE, IMPURE, VARANNO;

    private final MethodSlot PURE_METHOD_SLOT;
    private final MethodSlot IMPURE_METHOD_SLOT;

    private Set<String> methodIds;
    private Map<String, MethodSlot> idToMethodSlots;

    private Map<String, Set<String>> subtypeOf;
    private Map<String, String> equalTo;

    public MethodSlotManager(AnnotatedTypeFactory typeFactory) {
        this.atypeFactory = typeFactory;
        
        final Elements elements = atypeFactory.getProcessingEnv().getElementUtils();
        PURE = AnnotationBuilder.fromClass(elements, Pure.class);
        IMPURE = AnnotationBuilder.fromClass(elements, Impure.class);
        VARANNO = AnnotationBuilder.fromClass(elements, VarAnnot.class);

        PURE_METHOD_SLOT = new ConstMethodSlot("pure", PURE);
        IMPURE_METHOD_SLOT = new ConstMethodSlot("impure", IMPURE);

        idToMethodSlots = new HashMap<>();
        methodIds = new HashSet<>();
        subtypeOf = new HashMap<>();
        equalTo = new HashMap<>();

        methodIds.add("pure");
        methodIds.add("impure");
        idToMethodSlots.put("pure", PURE_METHOD_SLOT);
        idToMethodSlots.put("impure", IMPURE_METHOD_SLOT);
    }

    public MethodSlot getPureSlot() {
        return PURE_METHOD_SLOT;
    }

    public MethodSlot getImpureSlot() {
        return IMPURE_METHOD_SLOT;
    }


    public Map<String, MethodSlot> getIdToMethodSlots() {
        return idToMethodSlots;
    }

    public Set<String> getMethodIds() {
        return methodIds;
    }

    public Map<String, Set<String>> getSubtypeOf() {
        return subtypeOf;
    }

    public Map<String, String> getEqualTo() {
        return equalTo;
    }

    public boolean methodExists(String id) {
        return methodIds.contains(id);
    }

    public boolean methodExists(ExecutableElement elm) {
        return methodIds.contains(elm.getSimpleName().toString());
    }

    public MethodSlot getMethodSlotByElement(ExecutableElement elm) {
        String id = elm.getSimpleName().toString();
        return idToMethodSlots.get(id);
    }

    public void addEqualityConstraint(String s1, String s2) {
        equalTo.put(s1, s2);
    }


    public void addSubtypeOfConstraint(String s1, String s2) {
        if (!subtypeOf.containsKey(s1)) {
            subtypeOf.put(s1, new HashSet<String>());
        }
        subtypeOf.get(s1).add(s2);
    }

    public boolean equalMethodSlot(MethodSlot m1, MethodSlot m2) {
        return m1.getId().equals(m2.getId());
    }

    public MethodSlot addNewMethodSlot(AnnotatedTypeFactory typeFactory, MethodTree node) {
        MethodSlot methodSlot = MethodSlot.create(typeFactory, node, VARANNO);
        idToMethodSlots.put(methodSlot.getId(), methodSlot);
        methodIds.add(methodSlot.getId());
        return methodSlot;
    }

}

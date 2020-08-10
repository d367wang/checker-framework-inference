package dummy.purity.solve.constraint;

import checkers.inference.InferenceMain;
import checkers.inference.SlotManager;
import checkers.inference.model.*;
import dummy.purity.qual.Impure;
import dummy.purity.qual.Pure;
import dummy.purity.utils.MethodSlot;
import dummy.purity.utils.MethodSlotManager;

import javax.lang.model.element.AnnotationMirror;
import java.util.*;

public class PurityConstraintConverter {

    private SlotManager slotManager;
    private ConstraintManager constraintManager;
    private MethodSlotManager methodSlotManager;

    Map<String, MethodSlot> idToMethodSlots;
    Map<String, VariableSlot> methodToVariableSlot;

    private Map<String, String> equalTo;
    private Map<String, Set<String>> subtypeOf;


    public PurityConstraintConverter(SlotManager slotManager,
                                     ConstraintManager constraintManager,
                                     MethodSlotManager methodSlotManager) {
        this.slotManager = slotManager;
        this.constraintManager = constraintManager;
        this.methodSlotManager = methodSlotManager;

        this.methodToVariableSlot = new HashMap<>();

        this.idToMethodSlots = methodSlotManager.getIdToMethodSlots();
        this.equalTo = methodSlotManager.getEqualTo();
        this.subtypeOf = methodSlotManager.getSubtypeOf();

    }


    public void convert() {
        // create variable slot for all methods
        for (String s : idToMethodSlots.keySet()) {
            System.out.println(s);
            System.out.println(idToMethodSlots.get(s));
        }

        ConstantSlot pureSlot = (ConstantSlot) convertToVariableSlot(methodSlotManager.getPureSlot());
        ConstantSlot impureSlot = (ConstantSlot) convertToVariableSlot(methodSlotManager.getImpureSlot());
        methodToVariableSlot.put(methodSlotManager.getPureSlot().getId(), pureSlot);
        methodToVariableSlot.put(methodSlotManager.getImpureSlot().getId(), impureSlot);

        for (Map.Entry<String, MethodSlot> entry : idToMethodSlots.entrySet()) {
            String id = entry.getKey();
            MethodSlot methodSlot = entry.getValue();

            if (!methodSlot.isConstant()) {
                VariableSlot varSlot = convertToVariableSlot(methodSlot);

                System.out.println("create slot for " + id);
                System.out.println(varSlot);
                methodToVariableSlot.put(id, varSlot);
                if (!equalTo.keySet().contains(id)) {
                    constraintManager.addPreferenceConstraint(varSlot, pureSlot, 3);
                }
            }
        }

        for (Map.Entry<String, VariableSlot> e3:methodToVariableSlot.entrySet()) {
          System.out.println(e3.getKey() + " -> " + e3.getValue());
        }

        // add equality constraints
        for (Map.Entry<String, String> e1 : equalTo.entrySet()) {
            VariableSlot s1 = methodToVariableSlot.get(e1.getKey());
            VariableSlot s2 = methodToVariableSlot.get(e1.getValue());
            constraintManager.addEqualityConstraint(s1, s2);
        }

        // add subtype constraints
        for (Map.Entry<String, Set<String>> e2 : subtypeOf.entrySet()) {
            VariableSlot s1 = methodToVariableSlot.get(e2.getKey());
            Set<String> methodIds = e2.getValue();
            for (String m : methodIds) {
                VariableSlot s2 = methodToVariableSlot.get(m);
                constraintManager.addSubtypeConstraint(s1, s2);
            }
        }
    }

    private VariableSlot convertToVariableSlot(MethodSlot methodSlot) {
        if (methodSlot.isConstant()) {
            return slotManager.createConstantSlot(methodSlot.getAnno());
        }
        AnnotationLocation location = methodSlot.getLocation();
        System.out.println(methodSlot.getId() + " location");
        System.out.println(location);
        VariableSlot varSlot = slotManager.createVariableSlot(location);
        return varSlot;
    }

}

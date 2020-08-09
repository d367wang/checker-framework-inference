package purity.solve.constraint;

import checkers.inference.SlotManager;
import checkers.inference.model.*;
import purity.utils.MethodSlot;
import purity.utils.MethodSlotManager;

import java.util.*;

public class PurityConstraintConverter {

    private SlotManager slotManager;
    private ConstraintManager constraintManager;
    private MethodSlotManager methodSlotManager;

    Map<String, MethodSlot> idToMethodSlots = methodSlotManager.getIdToMethodSlots();
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
        this.equalTo = methodSlotManager.getEqualTo();
        this.subtypeOf = methodSlotManager.getSubtypeOf();
    }


    public void convert() {
        // create variable slot for all methods
        for (Map.Entry<String, MethodSlot> entry : idToMethodSlots.entrySet()) {
            String id = entry.getKey();
            MethodSlot methodSlot = entry.getValue();
            VariableSlot varSlot = convertToVariableSlot(methodSlot);

            methodToVariableSlot.put(id, varSlot);
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
        AnnotationLocation location = methodSlot.getLocation();
        VariableSlot varSlot = slotManager.createVariableSlot(location);
        return varSlot;
    }

}

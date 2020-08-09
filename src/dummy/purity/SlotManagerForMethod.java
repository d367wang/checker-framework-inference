package dummy.purity;

import checkers.inference.DefaultSlotManager;

import javax.annotation.processing.ProcessingEnvironment;
import java.lang.annotation.Annotation;
import java.util.Set;

public class SlotManagerForMethod extends DefaultSlotManager {
    public SlotManagerForMethod(ProcessingEnvironment processingEnvironment, Set<Class<? extends Annotation>> realQualifiers, boolean storeConstants) {
        super(processingEnvironment, realQualifiers, storeConstants);
    }
}

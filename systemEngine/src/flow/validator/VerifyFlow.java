package flow.validator;

import jaxb.schema.generated.STFlow;
import jaxb.schema.generated.STFlows;
import jaxb.schema.generated.STStepper;

import java.util.List;

public class VerifyFlow {
    private STStepper stepper;

    private VerifyFlow(STStepper stepper) {
        this.stepper = stepper;
    }

    public void verifyIfExistsFlowsWithDuplicateNames()
    {
        List<STFlow> stFlows = stepper.getSTFlows().getSTFlow();
        boolean isDuplicateNames=
        stFlows.
            stream().
                anyMatch(flow -> stFlows.stream().filter(f -> f != flow).
                        anyMatch(otherFlow -> otherFlow.getName().equals(flow.getName())));
if(!isDuplicateNames) {
}
}
    }

}

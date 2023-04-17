//import Steps.impl.CollectFilesInFolder;
//import Steps.impl.FilesDeleter;

/*public class Main {
    public static void main(String[] args) throws InterruptedException{
        SpendSomeTime obj=new SpendSomeTime(new NumberDataDefinition(5));
        obj.sleep();

        SpendSomeTime obj2=new SpendSomeTime(new NumberDataDefinition(0));
      //  obj2.sleep();


        SpendSomeTime obj1=new SpendSomeTime(new NumberDataDefinition(-3));
      //  obj1.sleep();*/


        //StringDataDefinition name = new StringDataDefinition("C:\\Users\\Amit\\Documents\\WALLPAPERS");
       // StringDataDefinition filter = new StringDataDefinition("xml");
      /*  CollectFilesInFolder obj3 = new CollectFilesInFolder(name);
        ListDataDefinition<FileDataDefinition> list = obj3.collector();
        FilesDeleter files = new FilesDeleter(list);
        files.deleteFiles();


    }
}*/

public class Main {
    public static void main(String[] args) {

       /* FlowDefinition flow1 = new FlowDefinitionImpl("Flow 1", "Hello world");
        Object StepDefinitionRegistry = null; //?
        flow1.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.HELLO_WORLD.getStepDefinition()));
        flow1.validateFlowStructure();

        FlowDefinition flow2 = new FlowDefinitionImpl("Flow 2", "show two person details");
        flow2.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.HELLO_WORLD.getStepDefinition()));
        flow2.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.PERSON_DETAILS.getStepDefinition(), "Person 1 Details"));
        flow2.getFlowSteps().add(new StepUsageDeclarationImpl(StepDefinitionRegistry.PERSON_DETAILS.getStepDefinition(), "Person 2 Details"));
        flow2.getFlowFormalOutputs().add("DETAILS");
        flow2.validateFlowStructure();

        FlowExecutor fLowExecutor = new FlowExecutor();

        FlowExecution flow2Execution1 = new FlowExecution("1", flow2);
        // collect all user inputs and store them on the flow execution object
        fLowExecutor.executeFlow(flow2Execution1);

        FlowExecution flow2Execution2 = new FlowExecution("2", flow2);
        // collect all user inputs and store them on the flow execution object
        fLowExecutor.executeFlow(flow2Execution1);*/
/*
        File a = new File("C:\\Users\\Amit\\Desktop\\New folder\\a.txt");
        File b = new File("C:\\Users\\Amit\\Desktop\\New folder\\b.txt");
        File c = new File("C:\\Users\\Amit\\Desktop\\New folder\\c.txt");

        List<File> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);



        String pre = "Amit";
        String suf = "Eden";
        FilesRenamer obj = new FilesRenamer();
        obj.invoke(list,pre,suf);*/
    }
}
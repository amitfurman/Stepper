import DataDefinition.FileDataDefinition;
import DataDefinition.ListDataDefinition;
import DataDefinition.NumberDataDefinition;
import DataDefinition.StringDataDefinition;
import Steps.CollectFilesInFolder;
import Steps.FilesDeleter;
import Steps.SpendSomeTime;

public class Main {
    public static void main(String[] args) throws InterruptedException{
/*        SpendSomeTime obj=new SpendSomeTime(new NumberDataDefinition(5));
        obj.sleep();

        SpendSomeTime obj2=new SpendSomeTime(new NumberDataDefinition(0));
      //  obj2.sleep();


        SpendSomeTime obj1=new SpendSomeTime(new NumberDataDefinition(-3));
      //  obj1.sleep();*/


        StringDataDefinition name = new StringDataDefinition("C:\\Users\\Amit\\Documents\\WALLPAPERS");
       // StringDataDefinition filter = new StringDataDefinition("xml");
        CollectFilesInFolder obj3 = new CollectFilesInFolder(name);
        ListDataDefinition<FileDataDefinition> list = obj3.collector();
        FilesDeleter files = new FilesDeleter(list);
        files.deleteFiles();



    }
}

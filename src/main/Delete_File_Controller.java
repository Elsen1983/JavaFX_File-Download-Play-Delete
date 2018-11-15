package main;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class Delete_File_Controller {

    private String filePath;
    private File file;

    public Delete_File_Controller(String filePath) {

        this.filePath = filePath;

    }

    public void fileDelete() {

        file = new File(filePath);

        System.out.println("Delete this: " + filePath);

        try
        {
            Files.deleteIfExists(Paths.get(filePath));
        }
        catch(NoSuchFileException e)
        {
            System.out.println("No such file/directory exists");
        }
        catch(DirectoryNotEmptyException e)
        {
            System.out.println("Directory is not empty.");
        }
        catch(IOException e)
        {
            System.out.println("Invalid permissions.");
            e.printStackTrace();
        }

        System.out.println("Deletion successful.");

    }
}

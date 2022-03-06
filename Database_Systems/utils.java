package Database_Systems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

// enum keyWords {
//     CREATE,
//     SELECT,
//     FROM,
//     INSERT,
//     UPDATE,
//     DELETE
// }

public class utils {

    public static void createFile()
    {
        try {
            File schemaFile = new File("schema.txt");
            if (schemaFile.createNewFile()) {
              System.out.println("File created: " + schemaFile.getName());
            } else {
              System.out.println("File already exists.");
            }
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

    public static void printDate()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date currentDate = new Date();
        System.out.println(formatter.format(currentDate));
    }

    public static void hello(String name) {
        System.out.println("Hello " + name);
    }

    public static void Create(String[] commands)
    {
        //TODO: Error Checking remaining
        String fileContents="";
        fileContents+=commands[2]+"#";
        //* i=4 because from index 4 attributes name and datatype start.
        for(int i=4;i<commands.length;i++)
        {
            // System.out.print(commands[i]);
            if(!commands[i].equals(",") && !commands[i].equals(");"))
            {
                fileContents+=commands[i]+"#";
            }
            
            // System.out.print(" # ");
        }
        fileContents = fileContents.substring(0,fileContents.length()-1);
        // System.out.println(fileContents);
        
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter("schema.txt"));
            fileWriter.write(fileContents);
            System.out.println("Table Added Successfully");
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error occured while adding Table");
            e.printStackTrace();
        }
    }
    
    public static void Help(String[] commands)
    {
        //TODO: Error Checking remaining
        try {
            File schemaFile = new File("schema.txt");
            Scanner fileContents = new Scanner(schemaFile);
            while (fileContents.hasNextLine())
            {
                String data = fileContents.nextLine();
                System.out.println(data);
            }
            fileContents.close();
        } catch(FileNotFoundException e)
        {
            System.out.println("Error occured Displaying Tables");
            e.printStackTrace();
        }
        
    }
}
package Database_Systems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    static ArrayList<ArrayList<String>> tableSchema;

    public static void loadTables()
    {
        tableSchema = new ArrayList<ArrayList<String>>();
        try {
            File schemaFile = new File("schema.txt");
            Scanner fileContents = new Scanner(schemaFile);
            while (fileContents.hasNextLine())
            {
                String lineContents = fileContents.nextLine();
                String dataArray[] = lineContents.split("#");
                ArrayList<String> temp = new ArrayList<String>();
                for(String data:dataArray)
                {
                    temp.add(data);
                }
                tableSchema.add(temp);
                // System.out.println(lineContents);
            }
            System.out.println(tableSchema);

            fileContents.close();
        } catch(FileNotFoundException e)
        {
            System.out.println("Error occured Displaying Tables");
            e.printStackTrace();
        }
    }

    public static void createFile(String fileName)
    {
        try {
            File schemaFile = new File(fileName);
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

    // TODO: Remove this in next Commit
    // public static void hello(String name) {
    //     System.out.println("Hello " + name);
    // }

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
        String dataArray[] = fileContents.split("#");
        //* To Create the table file.
        createFile(dataArray[0]+".txt");
        ArrayList<String> temp = new ArrayList<String>();
        for(String data:dataArray)
        {
            temp.add(data);
        }
        tableSchema.add(temp);
        fileContents+="\n";
        // System.out.println(fileContents);
        
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter("schema.txt",true));
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
        System.out.println(tableSchema);
    }

    public static String tableExists(String tableName)
    {
        for(ArrayList<String> tableDetails:tableSchema)
        {
            if(tableDetails.get(0).equals(tableName))
            {
                return tableDetails.get(0);
            }
        }
        return "Not Exist";
    }

    public static void Insert(String[] commands)
    {
        //TODO: Error Checking remaining
        String tableName = commands[2];
        String rowDetails="";
        tableName = tableExists(tableName);
        if(tableName.equals("Not Exist")) 
        {
            System.out.println(commands[2] + " doesn't exists");
            return;
        }
        for(int i=5;i<commands.length;i++)
        {
            if(!commands[i].equals(",")  && !commands[i].equals(");"))
            {
                rowDetails+=commands[i] + "#";
            }
        }
        rowDetails = rowDetails.substring(0,rowDetails.length()-1);
        rowDetails+="\n";
        String fileName = tableName + ".txt";
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName,true));
            fileWriter.write(rowDetails);
            System.out.println("Row Added Successfully in " + tableName);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error occured while adding Row in " + tableName);
            e.printStackTrace();
        }
        
    }
}
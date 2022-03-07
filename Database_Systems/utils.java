package Database_Systems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

// keyWords 
//     CREATE,
//     SELECT,
//     FROM,
//     INSERT,
//     UPDATE,
//     DELETE
// 

public class utils {

    static HashMap<String,ArrayList<String>> tableSchema;

    //* Loading the table schema initially in a Hashmap 
    public static void loadTables()
    {
        tableSchema = new HashMap<String,ArrayList<String>>();
        try {
            File schemaFile = new File("schema.txt");
            Scanner fileContents = new Scanner(schemaFile);
            while (fileContents.hasNextLine())
            {
                String lineContents = fileContents.nextLine();
                String dataArray[] = lineContents.split("#");
                ArrayList<String> temp = new ArrayList<String>();
                String key = dataArray[0];
                for(int i=1;i<dataArray.length;i++)
                {
                    temp.add(dataArray[i]);
                }
                tableSchema.put(key, temp);
                // System.out.println(lineContents);
            }
            // System.out.println(tableSchema);

            fileContents.close();
        } catch(FileNotFoundException e)
        {
            System.out.println("Error occured Displaying Tables");
            e.printStackTrace();
        }
    }

    //* To check if a table with the given name exists 
    public static Boolean tableExists(String tableName)
    {
        if(tableSchema.get(tableName) != null)
        {
            return true;
        }
        return false;
    }

    //* To create a file
    public static void createFile(String fileName)
    {
        try {
            File schemaFile = new File(fileName);
            if (schemaFile.createNewFile()) {
              System.out.println("File created: " + schemaFile.getName());
            } 
            // else {
            //   System.out.println("File already exists.");
            // }
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

    //* To print Time and Date initially
    public static void printDate()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date currentDate = new Date();
        System.out.println(formatter.format(currentDate));
    }

    //* To Clear the Console of Output
    public static void Clear(String[] commands)
    {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void Create(String[] commands)
    {
        //TODO: Error Checking remaining
        //* 7 because CREATE TABLE TABLE_NAME ( Attribute_Name Datatype , Attribute_Name Datatype );
        if(commands.length < 7 || (commands.length-7)%3 != 0 )
        {
            System.out.println("Incorrect CREATE Command");
            return;
        }
        String fileContents="";
        String tableName = commands[2];
        if(tableExists(tableName)) 
        {
            System.out.println(commands[2] + " already exists");
            return;
        }
        fileContents+=commands[2]+"#";
        //* i=4 because from index 4 attributes name and datatype start.
        for(int i=4;i<commands.length;i+=3)
        {
            // System.out.print(commands[i]);
            // if(!commands[i].equals(",") && !commands[i].equals(");"))
            // {
            //     fileContents+=commands[i]+"#";
            // }
            //* "^CHAR\\([1-9][0-9]?\\)$" is used to detect CHAR(1) to CHAR(99)
            if(commands[i+1].equals("INT") || commands[i+1].equals("DECIMAL") || commands[i+1].matches("^CHAR\\([1-9][0-9]?\\)$"))
            {
                fileContents+=commands[i]+"#"+commands[i+1]+"#";
            } 
            //* "^CHAR\\(-[1-9][0-9]?\\)$" is used to detect Negative numbers e.g. CHAR(-20)
            else if (commands[i+1].matches("^CHAR\\(-[1-9][0-9]?\\)$"))
            {
                System.out.println("Negative Length Columns are not allowed");
                return;
            } 
            //* "^CHAR\\(0\\)$" is used to detect 0 number e.g. CHAR(0)
            else if (commands[i+1].matches("^CHAR\\(0\\)$"))
            {
                System.out.println("Zero Length Columns are not allowed");
                return;
            } 
            //* If anyone of the above conditions do not match that means the Datatype is Unknown 
            else {
                System.out.println("Unkown Datatype - " + commands[i+1]);
                return;
            }            
            // System.out.print(" # ");
        }
        fileContents = fileContents.substring(0,fileContents.length()-1);
        String dataArray[] = fileContents.split("#");
        //* To Create the table file.
        createFile(dataArray[0]+".txt");
        ArrayList<String> temp = new ArrayList<String>();
        String key = dataArray[0];
        for(int i=1;i<dataArray.length;i++)
        {
            temp.add(dataArray[i]);
        }
        tableSchema.put(key, temp);
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

    public static void Drop(String[] commands)
    {
        //TODO: Error Checking remaining
        if(commands.length != 2)
        {
            System.out.println("Incorrect DROP Command");
            return;
        } 
        String tableName = commands[1].substring(0,commands[1].length() - 1);
        if(!tableExists(tableName)) 
        {
            System.out.println(tableName + " doesn't exists");
            return;
        }
        tableSchema.remove(tableName);
        String fileContents="";
        for(String key : tableSchema.keySet())
        {
            fileContents+=key+"#";
            for(String temp:tableSchema.get(key))
            {
                fileContents+=temp+"#";
            }
            fileContents = fileContents.substring(0,fileContents.length()-1);
            fileContents+="\n";
        }

        //* To delete the existing .txt file of that table 
        File fileName = new File(tableName+".txt");
        if(fileName.delete())
        {
            System.out.println("Deleted the file: " + fileName.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }

        //* To update the schema.txt file
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter("schema.txt"));
            fileWriter.write(fileContents);
            System.out.println(tableName + " Table Dropped Successfully");
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error occured while Dropping Table "+tableName);
            e.printStackTrace();
        }



    }
    
    public static void Help(String[] commands)
    {
        //TODO: Error Checking remaining
        if(commands.length < 2 || commands.length > 3) 
        {
            System.out.println("Incorrect HELP Command");
            return;
        }
        else if(commands.length == 2 && commands[1].equals("TABLES;")) 
        {
            int maxLength = 0;
            String tableContents="",temp="",horizontalRow="";
            for(String key : tableSchema.keySet())
            {
                if(maxLength < key.length())
                {
                    maxLength = key.length();
                }
            }
            for(int i=0;i<maxLength+4;i++)
            {
                horizontalRow+="-";
            }
            tableContents+=horizontalRow+"\n"; 
            tableContents+=String.format("| %-"+(maxLength)+"s |\n","TABLES");;
            tableContents+=horizontalRow+"\n"; 
            for(String key : tableSchema.keySet())
            {
                temp+=String.format("| %-"+(maxLength)+"s |\n",key);
            }
            tableContents+=temp;
            tableContents+=horizontalRow+"\n"; 
            System.out.print(tableContents);
        }
        else if(commands.length == 3 && commands[1].equals("DESCRIBE"))
        {
            String tableName = commands[2].substring(0,commands[2].length() - 1);
            if(!tableExists(tableName)) 
            {
                System.out.println(tableName + " doesn't exist");
                return;
            }
            System.out.println(tableName);
            ArrayList<String> tempArray = new ArrayList<String>();
            tempArray = tableSchema.get(tableName);
            System.out.println("-----------------------------");
            System.out.println("| Attribute Name | Datatype |");
            System.out.println("-----------------------------");
            for(int i=0;i<tempArray.size();i+=2)
            {
                String formattedRow = String.format("| %-14s |",tempArray.get(i)) + String.format(" %-8s |",tempArray.get(i+1));
                System.out.println(formattedRow);
            }
            System.out.println("-----------------------------");
        }
        else 
        {
            System.out.println("Incorrect HELP Command");
            return;
        }

    }

    public static void Insert(String[] commands)
    {
        //TODO: Error Checking remaining
        String tableName = commands[2];
        String rowDetails="";
        if(!tableExists(tableName)) 
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
package Database_Systems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
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
    
    //* 
    static int MAX_TABLE_LENGTH;

    //* Storing the Schema of the table 
    static HashMap<String,ArrayList<String>> tableSchema;
    //* Storing the Detail of each table 
    static HashMap<String,ArrayList<String>> tableDetails;
    //* Storing the Maximum Attribute Length of each table 
    static HashMap<String,Integer> tableLengthCount;

    //* Loading the table schema initially in a Hashmap 
    public static void loadTableSchema()
    {
        tableSchema = new HashMap<String,ArrayList<String>>();
        tableDetails = new HashMap<String,ArrayList<String>>();
        tableLengthCount = new HashMap<String,Integer>();
        MAX_TABLE_LENGTH=0;

        try {
            File schemaFile = new File("schema.txt");
            Scanner fileContents = new Scanner(schemaFile);
            while (fileContents.hasNextLine())
            {
                String lineContents = fileContents.nextLine();
                String[] dataArray = lineContents.split("#");
                ArrayList<String> temp = new ArrayList<String>();
                String key = dataArray[0];
                int maxAttributeLength=0;

                if(MAX_TABLE_LENGTH < key.length())
                {
                    MAX_TABLE_LENGTH = key.length();
                }
                for(int i=1;i<dataArray.length;i++)
                {
                    if(i%2 != 0)
                    {
                        if(maxAttributeLength < dataArray[i].length())
                        {
                            maxAttributeLength = dataArray[i].length();
                        }
                    }
                    temp.add(dataArray[i]);
                }
                tableSchema.put(key, temp);
                tableLengthCount.put(key, maxAttributeLength);
                // System.out.println(lineContents);
            }
            System.out.println(tableLengthCount);
            // System.out.println(tableSchema);

            fileContents.close();
        } catch(FileNotFoundException e)
        {
            System.out.println("Error occurred Displaying Tables");
            e.printStackTrace();
        }
    }

    //* Loading the table details in a Hashmap tableDetails
    public static void loadTables(String tableName)
    {
        System.out.println("Loading from File");
        try {
            File schemaFile = new File(tableName + ".txt");
            Scanner fileContents = new Scanner(schemaFile);
            ArrayList<String> temp = new ArrayList<String>();
            while (fileContents.hasNextLine())
            {
                String lineContents = fileContents.nextLine();
                // String[] dataArray = lineContents.split("#");
                temp.add(lineContents);
                // String key = dataArray[0];
                // for(int i=0;i<dataArray.length;i++)
                // {
                //     temp.add(dataArray[i]);
                // }
                // System.out.println(lineContents);
            }
            tableDetails.put(tableName, temp);
            // System.out.println(tableSchema);

            fileContents.close();
        } catch(FileNotFoundException e)
        {
            System.out.println("Error occurred Displaying Tables");
            e.printStackTrace();
        }
    }

    //* To check if a table with the given name exists 
    private static Boolean tableExists(String tableName)
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
        } 
        //* | (Pipe) used for catching multiple expections in a single catch block
        catch (InterruptedException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // private static void printTable(ArrayList<String> colHeadings,ArrayList<String> data)
    // {
    //     String tableContents="",temp="",horizontalRow="";
    //     String.format("%"+((maxAttributeLength+4) + 11)+"s","").replace(' ','-'))

    // }

    private static boolean isNumeric(String str)
    {
        return str != null && !str.matches("[0-9.]+.[0-9.]+");
    }

    private static boolean isDecimal(String str)
    {
        return str != null && str.matches("[0-9.]+.[0-9.]+");
    }

    private static boolean isString(String str)
    {
        return str != null && str.matches("'[a-zA-Z]+'");
    }

    public static void Create(String[] commands)
    {
        //TODO: Error Checking remaining
        //* 7 because CREATE TABLE TABLE_NAME ( Attribute_Name Datatype );
        //* (commands.length-7)%3 because CREATE TABLE TABLE_NAME ( Attribute_Name Datatype , Attribute_Name Datatype );
        if(commands.length < 7 || (commands.length-7)%3 != 0 || commands[1].equals("TABLE"))
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
                System.out.println("Unknown Datatype - " + commands[i+1]);
                return;
            }            
        }
        fileContents = fileContents.substring(0,fileContents.length()-1);
        String[] dataArray = fileContents.split("#");
        //* Updating MAX_TABLE_LENGTH if tableName is greater
        if(MAX_TABLE_LENGTH < dataArray[0].length())
        {
            MAX_TABLE_LENGTH = dataArray[0].length();
        }
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
        
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter("schema.txt",true));
            fileWriter.write(fileContents);
            System.out.println("Table Added Successfully");
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error occurred while adding Table");
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
            System.out.println("Error occurred while Dropping Table "+tableName);
            e.printStackTrace();
        }
    }
    
    public static void Help(String[] commands)
    {
        //TODO: Yet to add CMD for help in list of cmds
        if(commands.length != 2) 
        {
            System.out.println("Incorrect HELP Command");
            return;
        }
        if(!commands[1].equals("TABLES;"))
        {
            System.out.println("Incorrect HELP Command");
            return;
        }
        // System.out.println(MAX_TABLE_LENGTH);
        String tableContents="",temp="",horizontalRow="";
        for(int i=0;i<MAX_TABLE_LENGTH+4;i++)
        {
            horizontalRow+="-";
        }
        tableContents+=horizontalRow+"\n"; 
        tableContents+=String.format("| %-"+(MAX_TABLE_LENGTH)+"s |\n","TABLES");
        tableContents+=horizontalRow+"\n"; 
        for(String key : tableSchema.keySet())
        {
            temp+=String.format("| %-"+(MAX_TABLE_LENGTH)+"s |\n",key);
        }
        tableContents+=temp;
        tableContents+=horizontalRow+"\n"; 
        System.out.print(tableContents);
    }

    public static void Describe(String[] commands)
    {
        if(commands.length != 2) 
        {
            System.out.println("Incorrect DESCRIBE Command");
            return;
        }
        String tableName = commands[1].substring(0,commands[1].length() - 1);
        if(!tableExists(tableName)) 
        {
            System.out.println(tableName + " doesn't exist");
            return;
        }
        System.out.println(tableName);
        int maxAttributeLength = tableLengthCount.get(tableName);
        if(maxAttributeLength < "Attribute Name".length())
        {
            maxAttributeLength = "Attribute Name".length();
        }
        ArrayList<String> tempArray;
        tempArray = tableSchema.get(tableName);
        System.out.println(String.format(String.format("%"+((maxAttributeLength+4) + 11)+"s","").replace(' ','-')));
        System.out.println(String.format("| %-"+maxAttributeLength+"s |","Attribute Name") + String.format(" %-8s |","Datatype"));
        System.out.println(String.format(String.format("%"+((maxAttributeLength+4) + 11)+"s","").replace(' ','-')));
        // System.out.println("-----------------------------");
        // System.out.println("| Attribute Name | Datatype |");
        // System.out.println("-----------------------------");
        for(int i=0;i<tempArray.size();i+=2)
        {
            String formattedRow = String.format("| %-"+(maxAttributeLength)+"s |",tempArray.get(i)) + String.format(" %-8s |",tempArray.get(i+1));
            System.out.println(formattedRow);
        }
        System.out.println(String.format(String.format("%"+((maxAttributeLength+4) + 11)+"s","").replace(' ','-')));
        // System.out.println("-----------------------------");
    }

    public static void Insert(String[] commands)
    {
        //TODO: Error Checking remaining
        //* 7 because INSERT INTO TABLE_NAME VALUES ( ATTRIBUTE_NAME ); 
        if(commands.length < 7 || (!commands[1].equals("INTO") && !commands[3].equals("VALUES")))
        {
            System.out.println("Incorrect INSERT Command");
            return;
        }
        if(!commands[4].equals("(") || !commands[commands.length-1].equals(");"))
        {
        	System.out.println("Incorrect INSERT Command");
            return;
	    }    
        // To check if the Command has additional ( and ) in it.
        String remainingString = "";
        for(int i=5;i<commands.length-1;i++)
        {
        	remainingString+=commands[i];
	    }  
        if(remainingString.contains(")") || remainingString.contains("("))
        {
        	System.out.println("Incorrect INSERT Command");
            return;
	    }
        // System.out.println(remainingString);
        String[] attributeValues = remainingString.split(",");
        
        String tableName = commands[2];
        if(!tableExists(tableName)) 
        {
            System.out.println(commands[2] + " doesn't exists");
            return;
        }
        String rowDetails="";
        ArrayList<String> headRow = tableSchema.get(tableName);
        // System.out.println(headRow);
        if(attributeValues.length != headRow.size()/2)
        {
            System.out.println("Insufficient Attributes");
            return;
        }

        for(int i=0;i<headRow.size()/2;i++)
        {
            if(headRow.get(2*i+1).contains("CHAR"))
            {
                if(isString(attributeValues[i]))
                {
                    int maxCharSize = Integer.parseInt(headRow.get(2*i+1).substring(5,headRow.get(2*i+1).length()-1));
                    if(attributeValues[i].length()-2 > maxCharSize)
                    {
                        System.out.println(attributeValues[i] + " is too long");
                        return;
                    }

                    rowDetails+=attributeValues[i].substring(1,attributeValues[i].length()-1)+"#";
                } 
                else 
                {
                    System.out.println("Expected CHAR Datatype");
                    return;
                }
                
            }
            else if(headRow.get(2*i+1).equals("INT"))
            {
                if(isNumeric(attributeValues[i]))
                {
                    rowDetails+=attributeValues[i]+"#";
                }
                else 
                {
                    System.out.println("Expected INT Datatype");
                    return;
                }
            }
            else if(headRow.get(2*i+1).contains("DECIMAL"))
            {
                if(isDecimal(attributeValues[i]))
                {
                    rowDetails+=attributeValues[i]+"#";
                }
                else 
                {
                    System.out.println("Expected DECIMAL Datatype");
                    return;
                }
            }
        }
        rowDetails = rowDetails.substring(0,rowDetails.length()-1);
        //* Adding into tableDetails
        if(tableDetails.get(tableName) == null)
        {
            loadTables(tableName);
        }
        tableDetails.get(tableName).add(rowDetails);
        rowDetails+="\n";
        String fileName = tableName + ".txt";
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName,true));
            fileWriter.write(rowDetails);
            System.out.println("Row Added Successfully in " + tableName);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error occurred while adding Row in " + tableName);
            e.printStackTrace();
        }
        
    }

    public static void Select(String[] commands)
    {
        //* 4 becuase SELECT * FROM STUDENT;
        if(commands.length < 4)
        {
            System.out.println("Incorrect SELECT Command");
            return;
        }
        
        // String[] commands = {"SELECT","NAME","ID","FROM","STUDENT","WHERE"};
        // for(int i=0;i<commands.length;++i)
        // {
        //     if(commands[i].equals("FROM"))
        //     {
        //         System.out.println(i);
        //         break;
        //     }
        // }

        String tableName = commands[3];
        tableName = tableName.substring(0,tableName.length()-1);
        if(!tableExists(tableName)) 
        {
            System.out.println(tableName + " doesn't exists");
            return;
        }

        if(tableDetails.get(tableName) == null)
        {
            loadTables(tableName);
        }
        System.out.println(tableDetails.get(tableName));
    }

    public static void Delete(String[] commands)
    {
        //* 7 becuase DELETE FROM TABLE_NAME WHERE ID = 2;
        if(commands.length < 7 || !(commands[1].equals("FROM") && commands[3].equals("WHERE")))
        {
            System.out.println("Incorrect DELETE Command");
            return;
        }
        String tableName = commands[2];
        if(!tableExists(tableName)) 
        {
            System.out.println(tableName + " doesn't exists");
            return;
        }
        if(tableDetails.get(tableName) == null)
        {
            loadTables(tableName);
        }
        ArrayList<String> headRow = tableSchema.get(tableName);
        boolean columnExist = false;
        int indexColumnNo = 0;
        for(indexColumnNo=0;indexColumnNo<headRow.size()/2;++indexColumnNo)
        {
            if(headRow.get(2*indexColumnNo).equals(commands[4]))
            {
                columnExist = true;
                break;
            }
        }
        if(!columnExist)
        {
            System.out.println(commands[4]+" doesn't exists in table "+tableName);
            return;
        }
        ArrayList<String> tableRows = tableDetails.get(tableName);
        int count = 0;
        for(int i=0;i<tableRows.size();++i)
        {
            String[] temp = tableRows.get(i).split("#");
            // System.out.println(temp.length);
            String condition = commands[6].substring(0,commands[6].length()-1);
            if(condition.equals(temp[indexColumnNo]))
            {
                System.out.println("Match Found");
                tableDetails.get(tableName).remove(i);
                ++count;
            }
        }
        System.out.println("Count is "+count);
        if(count != 0)
        {
            ArrayList<String> tableRow = tableDetails.get(tableName);
            String rowDetails="";
            for(int i=0;i<tableRow.size();++i)
            {
                rowDetails+=tableRow.get(i)+"\n";
            }
            String fileName=tableName+".txt";
            try {
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName));
                fileWriter.write(rowDetails);
                System.out.println(count + " rows deleted Successfully in " + tableName);
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error occurred while deleting rows in " + tableName);
                e.printStackTrace();
            }
        } 
        else {
            System.out.println("No Row deleted");
        }
    }

    public static void Update(String[] commands)
    {
        //* 11 becuase UPDATE TABLE_NAME SET ATTRIBUTE_NAME1 = ATTRIBUTE_VALUE1 WHERE ATTRIBUTE_NAME2 = ATTRIBUTE_VALUE2;
        if(commands.length < 11)
        {
            System.out.println("Incorrect UPDATE Command");
        }
        String tableName = commands[1];
        if(!tableExists(tableName)) 
        {
            System.out.println(tableName + " doesn't exists");
            return;
        }
        if(tableDetails.get(tableName) == null)
        {
            loadTables(tableName);
        }
    }
}
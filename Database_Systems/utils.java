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
import java.util.List;
import java.util.Map;
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
    static Map<String,List<String>> tableSchema;
    //* Storing the Detail of each table 
    static Map<String,List<String>> tableDetails;
    //* Storing the Maximum Attribute Length of each table 
    static Map<String,Integer> tableLengthCount;

    //* Loading the table schema initially in a Hashmap 
    public static void loadTableSchema()
    {
        tableSchema = new HashMap<String,List<String>>();
        tableDetails = new HashMap<String,List<String>>();
        tableLengthCount = new HashMap<String,Integer>();
        MAX_TABLE_LENGTH=0;

        try 
        {
            File schemaFile = new File("schema.txt");
            Scanner fileContents = new Scanner(schemaFile);
            while (fileContents.hasNextLine())
            {
                String lineContents = fileContents.nextLine();
                String[] dataArray = lineContents.split("#");
                List<String> temp = new ArrayList<String>();
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
        } 
        catch(FileNotFoundException e)
        {
            System.out.println("Error occurred Displaying Tables");
            e.printStackTrace();
        }
    }

    //* Loading the table details in a Hashmap tableDetails
    public static void loadTables(String tableName)
    {
        // System.out.println("Loading from File");
        try {
            File schemaFile = new File(tableName + ".txt");
            Scanner fileContents = new Scanner(schemaFile);
            List<String> temp = new ArrayList<String>();
            while (fileContents.hasNextLine())
            {
                String lineContents = fileContents.nextLine();
                temp.add(lineContents);
            }
            tableDetails.put(tableName, temp);

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
        return tableSchema.get(tableName) != null;
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
        //* | (Pipe) used for catching multiple exceptions in a single catch block
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

    //* regex for checking if given string is Numeric
    private static boolean isNumeric(String str)
    {
        return str != null && !str.matches("[0-9.]+.[0-9.]+");
    }
    
    //* regex for checking if given string is Decimal
    private static boolean isDecimal(String str)
    {
        return str != null && str.matches("[0-9.]+.[0-9.]+");
    }
    
    //* regex for checking if given string is String
    private static boolean isString(String str)
    {
        return str != null && str.matches("'[a-zA-Z]+'");
    }

    public static void Create(String[] commands)
    {
        //TODO: Error Checking remaining
        //* 7 because CREATE TABLE TABLE_NAME ( Attribute_Name Datatype );
        //* (commands.length-7)%3 because CREATE TABLE TABLE_NAME ( Attribute_Name Datatype , Attribute_Name Datatype );
        if(commands.length < 7 || (commands.length-7)%3 != 0 || !commands[1].equals("TABLE"))
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
        List<String> temp = new ArrayList<String>();
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
        if(commands.length != 3 || !commands[1].equals("TABLE"))
        {
            System.out.println("Incorrect DROP Command");
            return;
        } 
        String tableName = commands[2].substring(0,commands[1].length() - 1);
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
        //TODO: Yet to add CMD for help in list of Commands
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
        List<String> tempArray = tableSchema.get(tableName);
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
        // TODO: Error Checking remaining
        //* 7 because INSERT INTO TABLE_NAME VALUES ( ATTRIBUTE_NAME ); 
        if(commands.length < 7 || !(commands[1].equals("INTO") && commands[3].equals("VALUES")))
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
        List<String> headRow = tableSchema.get(tableName);
        System.out.println(headRow);
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
                // System.out.println(attributeValues[i]);
                //* For String Like '3' This will accept it as INT. If want to change this then in the isString() regex add 0-9
                if(isNumeric(attributeValues[i]) && !isString(attributeValues[i]))
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
        //* Error checking in Select Command
        //* 4 because SELECT * FROM STUDENT;
        if(commands.length < 4)
        {
            System.out.println("Incorrect SELECT Command");
            return;
        }
        //* To remove the ending ;
        commands[commands.length - 1] = commands[commands.length - 1].substring(0,commands[commands.length - 1].length()-1);
        
        int indexFrom = commands.length;
        int indexWhere = commands.length;
        for(int i=0;i<commands.length;++i)
        {
            if(commands[i].equals("FROM"))
            {
                indexFrom = i;
            }
            if(commands[i].equals("WHERE"))
            {
                indexWhere = i;
            }
        }

        if(indexFrom == commands.length ) 
        {
            System.out.println("Expected FROM Keyword in SELECT Command");
            return;
        }

        if(indexFrom < 2)
        {
            System.out.println("Columns not given");
            return;
        }

        String tableName = commands[indexFrom+1];
        if(!tableExists(tableName)) 
        {
            System.out.println(tableName + " doesn't exists");
            return;
        }
        
        if(tableDetails.get(tableName) == null)
        {
            loadTables(tableName);
        }
        List<String> tableHeader = tableSchema.get(tableName);
        boolean conditionAND=false;
        boolean conditionOR=false;
        List<Integer> columnIndexes = new ArrayList<Integer>();
        List<Integer> conditionColumnIndexes = new ArrayList<Integer>();
        List<String> conditionOperator = new ArrayList<String>();
        List<String> conditionList = new ArrayList<String>();
        //* This means FROM keyword is at index 2 and all columns have to be considered
        if(indexFrom == 2 && commands[1].equals("*"))
        {
            
            // SELECT * FROM STUDENT WHERE NAME = Hello; - 5 to 7
            // SELECT * FROM STUDENT WHERE NAME = Hello AND ID = 2; - 5 to 11
            // SELECT * FROM STUDENT WHERE NAME = Hello AND ID = 2 AND SAL = 30; - 5 to 15
            for(int i=5;i<commands.length;i+=4)
            {
                //* AND OR positions 8, 12, 16
                if(i+3 < commands.length)
                {
                    if(commands[i+3].equals("AND"))
                    {
                        conditionAND = true;
                    }
                    else if(commands[i+3].equals("OR"))
                    {
                        conditionOR = true;
                    }
                    else
                    {
                        System.out.println("Expected AND or OR keyword");
                        return;
                    }
                }
                boolean columnExist = false;
                for(int indexColumnNo=0;indexColumnNo<tableHeader.size();indexColumnNo+=2)
                {
                    if(tableHeader.get(indexColumnNo).equals(commands[i]))
                    {
                        columnExist = true;
                        conditionColumnIndexes.add(indexColumnNo/2);
                        conditionOperator.add(commands[i+1]);
                        conditionList.add(commands[i+2]);
                        break;
                    }
                }
                if(!columnExist)
                {
                    System.out.println(commands[i]+" doesn't exists in table "+tableName);
                    return;
                }
            }
            
            if(conditionColumnIndexes.size() != 0)
            {
                for (Integer conditionColumnIndex : conditionColumnIndexes) {
                    System.out.println(conditionColumnIndex + " " + tableHeader.get(conditionColumnIndex * 2));
                }
            }
            
            //* To display the Table Header Columns
            for(int i=0;i<tableHeader.size();i+=2)
            {
                System.out.print(String.format("%-10s ",tableHeader.get(i)));
            }
            System.out.println();
            // 1#Gaurav#gaurav@gmail.com
            for(String tableRow : tableDetails.get(tableName)) 
            {
                String[] tableArray = tableRow.split("#");
                String rowDisplay = "";
                boolean displayRow = false;
                if(conditionAND)
                {
                    displayRow = true;
                }
                // 1, Gaurav, gaurav@gmail.com
                //* Condition exists 
                if(conditionColumnIndexes.size() != 0)
                {
                    for(int j=0;j<conditionColumnIndexes.size();++j)
                    {
                        // 8, 12, 16
                        //* AND condition
                        if(conditionAND)
                        {
                            switch (conditionOperator.get(j)) {
                                case "=":
                                    if (!conditionList.get(j).equals(tableArray[conditionColumnIndexes.get(j)])) {
                                        displayRow = false;
                                    }
                                    break;
                                case "<":
                                    if (!(Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) < Integer.parseInt(conditionList.get(j)))) {
                                        displayRow = false;
                                    }
                                    break;
                                case ">":
                                    if (!(Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) > Integer.parseInt(conditionList.get(j)))) {
                                        displayRow = false;
                                    }
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + conditionOperator.get(j));
                            }
                        }
                        //* OR condition
                        else if(conditionOR)
                        {
                            switch (conditionOperator.get(j)) {
                                case "=":
                                    if (conditionList.get(j).equals(tableArray[conditionColumnIndexes.get(j)])) {
                                        displayRow = true;
                                    }
                                    break;
                                case "<":
                                    if (Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) < Integer.parseInt(conditionList.get(j))) {
                                        displayRow = true;
                                    }
                                    break;
                                case ">":
                                    if (Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) > Integer.parseInt(conditionList.get(j))) {
                                        displayRow = true;
                                    }
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + conditionOperator.get(j));
                            }
                        }
                        //* Neither AND nor OR. Only 1 condition
                        else 
                        {
                            switch (conditionOperator.get(j)) {
                                case "=":
                                    if (conditionList.get(j).equals(tableArray[conditionColumnIndexes.get(j)])) {
                                        displayRow = true;
                                    }
                                    break;
                                case "<":
                                    if (Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) < Integer.parseInt(conditionList.get(j))) {
                                        displayRow = true;
                                    }
                                    break;
                                case ">":
                                    if (Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) > Integer.parseInt(conditionList.get(j))) {
                                        displayRow = true;
                                    }
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + conditionOperator.get(j));
                            }
                        }
                    }
                }
                //* Condition doesn't exist so display all the rows
                else 
                {
                    displayRow = true;
                }
                if(displayRow)
                {
                    for (String s : tableArray) {
                        rowDisplay += String.format("%-10s ", s);
                    }
                }
                if(displayRow)
                {
                    System.out.println(rowDisplay);
                }
            }
        } 
        //* This means FROM keyword is at index 2 and then check the name of a single column given in query
        //* OR
        //* This means FROM keyword is not at index 2 and then check the names of all column given in query
        else {

            if((indexFrom-1)%2 == 0)
            {
                System.out.println("SELECT Command has extra ',' or ',' not given between names of columns");
                return;
            }
            
            for(int i=1;i<indexFrom;++i)
            {
                if(i%2 == 0)
                {
                    if(!commands[i].equals(","))
                    {
                        System.out.println("Expected ,");
                        return;
                    }
                } else {
                    boolean columnExist = false;
                    for(int j=0;j<tableHeader.size();j+=2) 
                    {
                        if(commands[i].equals(tableHeader.get(j)))
                        {
                            columnIndexes.add(j/2);
                            columnExist = true;
                            break;
                        }
                    }
                    if(!columnExist)
                    {
                        System.out.println(commands[i] + " doesn't exist in " + tableName + " table");
                        return;
                    }
                }
            }

            for(int i=indexFrom + 3;i<commands.length;i+=4)
            {
                if(i+3 < commands.length)
                {
                    if(commands[i+3].equals("AND"))
                    {
                        conditionAND = true;
                    }
                    else if(commands[i+3].equals("OR"))
                    {
                        conditionOR = true;
                    }
                    else
                    {
                        System.out.println("Expected AND or OR keyword");
                        return;
                    }
                }
                boolean columnExist = false;
                for(int indexColumnNo=0;indexColumnNo<tableHeader.size();indexColumnNo+=2)
                {
                    if(tableHeader.get(indexColumnNo).equals(commands[i]))
                    {
                        columnExist = true;
                        conditionColumnIndexes.add(indexColumnNo/2);
                        conditionOperator.add(commands[i+1]);
                        conditionList.add(commands[i+2]);
                        break;
                    }
                }
                if(!columnExist)
                {
                    System.out.println(commands[i]+" doesn't exists in table "+tableName);
                    return;
                }
            }

            if(conditionColumnIndexes.size() != 0)
            {
                for (Integer conditionColumnIndex : conditionColumnIndexes) {
                    System.out.println(conditionColumnIndex + " " + tableHeader.get(conditionColumnIndex * 2));
                }
            }

            for(Integer columnIndex : columnIndexes)
            {
                System.out.print(String.format("%-10s",tableHeader.get(columnIndex*2)));
            }
            System.out.println();
            
            for(String tableRow : tableDetails.get(tableName)) 
            {
                String[] tableArray = tableRow.split("#");
                String rowDisplay = "";
                boolean displayRow = false;
                if(conditionAND)
                {
                    displayRow = true;
                }
                // 1, Gaurav, gaurav@gmail.com
                //* Condition exists 
                if(conditionColumnIndexes.size() != 0)
                {
                    for(int j=0;j<conditionColumnIndexes.size();++j)
                    {
                        // 8, 12, 16
                        //* AND condition
                        if(conditionAND)
                        {
                            switch (conditionOperator.get(j)) {
                                case "=":
                                    if (!conditionList.get(j).equals(tableArray[conditionColumnIndexes.get(j)])) {
                                        displayRow = false;
                                    }
                                    break;
                                case "<":
                                    if (!(Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) < Integer.parseInt(conditionList.get(j)))) {
                                        displayRow = false;
                                    }
                                    break;
                                case ">":
                                    if (!(Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) > Integer.parseInt(conditionList.get(j)))) {
                                        displayRow = false;
                                    }
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + conditionOperator.get(j));
                            }
                        }
                        //* OR condition
                        else if(conditionOR)
                        {
                            switch (conditionOperator.get(j)) {
                                case "=":
                                    if (conditionList.get(j).equals(tableArray[conditionColumnIndexes.get(j)])) {
                                        displayRow = true;
                                    }
                                    break;
                                case "<":
                                    if (Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) < Integer.parseInt(conditionList.get(j))) {
                                        displayRow = true;
                                    }
                                    break;
                                case ">":
                                    if (Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) > Integer.parseInt(conditionList.get(j))) {
                                        displayRow = true;
                                    }
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + conditionOperator.get(j));
                            }
                        }
                        //* Neither AND nor OR. Only 1 condition
                        else 
                        {
                            switch (conditionOperator.get(j)) {
                                case "=":
                                    if (conditionList.get(j).equals(tableArray[conditionColumnIndexes.get(j)])) {
                                        displayRow = true;
                                    }
                                    break;
                                case "<":
                                    if (Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) < Integer.parseInt(conditionList.get(j))) {
                                        displayRow = true;
                                    }
                                    break;
                                case ">":
                                    if (Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) > Integer.parseInt(conditionList.get(j))) {
                                        displayRow = true;
                                    }
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + conditionOperator.get(j));
                            }
                        }
                    }
                }
                //* Condition doesn't exist so display all the rows
                else 
                {
                    displayRow = true;
                }
                if(displayRow)
                {
                    for(Integer columnIndex : columnIndexes) 
                    {
                        rowDisplay += String.format("%-10s",tableArray[columnIndex]);
                    }
                }
                if(displayRow)
                {
                    System.out.println(rowDisplay);
                }
            }
        }
    }
    
    public static void Delete(String[] commands)
    {
        int count = 0;
        String tableName;
		//* To remove the ending ;
		commands[commands.length - 1] = commands[commands.length - 1].substring(0,commands[commands.length - 1].length()-1);
		// System.out.println(commands[commands.length - 1]);
        //* 3 because DELETE FROM TABLE_NAME; which deletes all the records of the table
        if(commands.length == 3 && commands[1].equals("FROM"))
        {
            tableName = commands[2];
            if(!tableExists(tableName)) 
            {
                System.out.println(tableName + " doesn't exists");
                return;
            }
            if(tableDetails.get(tableName) == null)
            {
                loadTables(tableName);
            }   
            
            List<String> tableRows = tableDetails.get(tableName);
            
            for(int i=0;i<tableRows.size();++i)
            {
                tableDetails.get(tableName).remove(i);
                // --i because the size of tableRow gets reduced by 1 when a record is removed from ArrayList
                // and hence it skips the next record and goes to the +2 index record.
                --i;
                ++count;
            }
        }
        
        //* 7 because DELETE FROM TABLE_NAME WHERE ID = 2;
        else if(commands.length >= 7 && (commands[1].equals("FROM") && commands[3].equals("WHERE")))
        {
            tableName = commands[2];
            if(!tableExists(tableName)) 
            {
                System.out.println(tableName + " doesn't exists");
                return;
            }
            if(tableDetails.get(tableName) == null)
            {
                loadTables(tableName);
            }
            List<String> tableHeader = tableSchema.get(tableName);
            // int indexColumnNo = 0;
			boolean conditionAND=false;
			boolean conditionOR=false;
			List<Integer> conditionColumnIndexes = new ArrayList<Integer>();
			List<String> conditionOperator = new ArrayList<String>();
			List<String> conditionList = new ArrayList<String>();
            //* DELETE FROM STUDENT WHERE NAME = Vivek; - 4 to 6
            //* DELETE FROM STUDENT WHERE NAME = Vivek AND ID = 1 AND SAL = 30; - 4 to 14
            // System.out.println(headRow);
			for(int i=4;i<commands.length;i+=4)
            {
                //* AND OR positions 7, 11, 15
                if(i+3 < commands.length)
                {
                    if(commands[i+3].equals("AND"))
                    {
                        conditionAND = true;
                    }
                    else if(commands[i+3].equals("OR"))
                    {
                        conditionOR = true;
                    }
                    else
                    {
                        System.out.println("Expected AND or OR keyword");
                        return;
                    }
                }
                boolean columnExist = false;
                for(int indexColumnNo=0;indexColumnNo<tableHeader.size();indexColumnNo+=2)
                {
                    if(tableHeader.get(indexColumnNo).equals(commands[i]))
                    {
                        columnExist = true;
                        conditionColumnIndexes.add(indexColumnNo/2);
                        conditionOperator.add(commands[i+1]);
                        conditionList.add(commands[i+2]);
                        break;
                    }
                }
                if(!columnExist)
                {
                    System.out.println(commands[i]+" doesn't exists in table "+tableName);
                    return;
                }
            }

			// if(conditionColumnIndexes.size() != 0)
            // {
            //     for(int i=0;i<conditionColumnIndexes.size();++i)
            //     {
            //         System.out.println(conditionColumnIndexes.get(i) + " " + tableHeader.get(conditionColumnIndexes.get(i)*2));
            //     }
            // }

			// 1#Gaurav#gaurav@gmail.com
            for(int tableRow=0;tableRow<tableDetails.get(tableName).size();++tableRow) 
            {
                String[] tableArray = tableDetails.get(tableName).get(tableRow).split("#");
                boolean deleteRow = false;
                if(conditionAND)
                {
                    deleteRow = true;
                }
                // 1, Gaurav, gaurav@gmail.com
                //* Condition exists 
                if(conditionColumnIndexes.size() != 0)
                {
                    for(int j=0;j<conditionColumnIndexes.size();++j)
                    {
                        // 7, 11, 15
                        // System.out.println(tableArray[conditionColumnIndexes.get(j)]);
                        // System.out.println(conditionList.get(j));
                        //* AND condition
                        if(conditionAND)
                        {
                            switch (conditionOperator.get(j)) {
                                case "=":
                                    if (!conditionList.get(j).equals(tableArray[conditionColumnIndexes.get(j)])) {
                                        deleteRow = false;
                                    }
                                    break;
                                case "<":
                                    if (!(Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) < Integer.parseInt(conditionList.get(j)))) {
                                        deleteRow = false;
                                    }
                                    break;
                                case ">":
                                    if (!(Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) > Integer.parseInt(conditionList.get(j)))) {
                                        deleteRow = false;
                                    }
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + conditionOperator.get(j));
                            }
                        }
                        //* OR condition
                        else if(conditionOR)
                        {
                            switch (conditionOperator.get(j)) {
                                case "=":
                                    if (conditionList.get(j).equals(tableArray[conditionColumnIndexes.get(j)])) {
                                        deleteRow = true;
                                    }
                                    break;
                                case "<":
                                    if (Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) < Integer.parseInt(conditionList.get(j))) {
                                        deleteRow = true;
                                    }
                                    break;
                                case ">":
                                    if (Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) > Integer.parseInt(conditionList.get(j))) {
                                        deleteRow = true;
                                    }
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + conditionOperator.get(j));
                            }
                        }
                        //* Neither AND nor OR. Only 1 condition
                        else 
                        {
                            switch (conditionOperator.get(j)) {
                                case "=":
                                    if (conditionList.get(j).equals(tableArray[conditionColumnIndexes.get(j)])) {
                                        deleteRow = true;
                                    }
                                    break;
                                case "<":
                                    if (Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) < Integer.parseInt(conditionList.get(j))) {
                                        deleteRow = true;
                                    }
                                    break;
                                case ">":
                                    if (Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) > Integer.parseInt(conditionList.get(j))) {
                                        deleteRow = true;
                                    }
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + conditionOperator.get(j));
                            }
                        }
                    }
                }
                //* Condition doesn't exist so display all the rows
                else 
                {
                    deleteRow = false;
                }
                if(deleteRow)
                {
					// System.out.println("Match Found");
                    tableDetails.get(tableName).remove(tableRow);
                    // --tableRow because the size of tableRow gets reduced by 1 when a record is removed from ArrayList
                    // and hence it skips the next record and goes to the +2 index record.
                    --tableRow;
                    ++count;
                }
            }
        }
        else 
        {
            System.out.println("Incorrect DELETE Command");
            return;
        }

        System.out.println("Count is "+count);
        if(count != 0)
        {
            List<String> tableRow = tableDetails.get(tableName);
            String rowDetails="";
            for (String s : tableRow) {
                rowDetails += s + "\n";
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
        int count = 0;
        String tableName;
		//* To remove the ending ;
		commands[commands.length - 1] = commands[commands.length - 1].substring(0,commands[commands.length - 1].length()-1);
        // System.out.println(commands[commands.length - 1]);
        int indexWhere = commands.length;
        for(int i=0;i<commands.length;++i)
        {
            if(commands[i].equals("WHERE"))
            {
                indexWhere = i;
                break;
            }
        }
        //* UPDATE CUSTOMER SET NAME = 'Alfred';
        //* UPDATE CUSTOMER SET NAME = 'Alfred' , EMAIL = 'shashank@gmail.com';
        if(commands.length>5 && commands[2].equals("SET") && indexWhere == commands.length)
        {
            tableName = commands[1];
            if(!tableExists(tableName)) 
            {
                System.out.println(tableName + " doesn't exists");
                return;
            }
            if(tableDetails.get(tableName) == null)
            {
                loadTables(tableName);
            }
            List<String> tableHeader = tableSchema.get(tableName);
            List<Integer> updateColumnIndexes = new ArrayList<Integer>();
			List<String> updateList = new ArrayList<String>();
            for(int i=3;i<commands.length;i+=4)
            {
                if(!commands[i+1].equals("="))
                {
                    System.out.println("Expected =");
                    return;
                }

                boolean columnExist = false;
                for(int indexColumnNo=0;indexColumnNo<tableHeader.size();indexColumnNo+=2)
                {
                    if(tableHeader.get(indexColumnNo).equals(commands[i]))
                    {
                        columnExist = true;
                        updateColumnIndexes.add(indexColumnNo/2);
                        updateList.add(commands[i+2]);
                        break;
                    }
                }
                if(!columnExist)
                {
                    System.out.println(commands[i]+" doesn't exists in table "+tableName);
                    return;
                }
            }
            if(updateColumnIndexes.size() != 0)
            {
                for(int i=0;i<updateColumnIndexes.size();++i)
                {
                    System.out.println(updateColumnIndexes.get(i) + " " + tableHeader.get(updateColumnIndexes.get(i)*2) + " " + updateList.get(i));
                }
            }

            // 1#Gaurav#gaurav@gmail.com
            for(int tableRow=0;tableRow<tableDetails.get(tableName).size();++tableRow) 
            {
                String[] tableArray = tableDetails.get(tableName).get(tableRow).split("#");
                String updatedRow = "";
                // tableDetails.get(tableName)
                for(int j=0;j<updateColumnIndexes.size();++j)
                {
                    tableArray[updateColumnIndexes.get(j)] = updateList.get(j);
                }
                for (String s : tableArray) {
                    updatedRow += s + "#";
                }
                //* To remove extra # at back
                updatedRow = updatedRow.substring(0,updatedRow.length()-1);
                tableDetails.get(tableName).set(tableRow, updatedRow);
                ++count;
            }
        } 
        //* 10 because UPDATE CUSTOMER SET NAME = Alfred WHERE ID = 1;
        else if(commands.length > 9 && commands[2].equals("SET") && indexWhere != commands.length)
        {
            tableName = commands[1];
            if(!tableExists(tableName)) 
            {
                System.out.println(tableName + " doesn't exists");
                return;
            }
            if(tableDetails.get(tableName) == null)
            {
                loadTables(tableName);
            }

            //* 
            boolean conditionAND=false;
			boolean conditionOR=false;
            List<String> tableHeader = tableSchema.get(tableName);
            List<Integer> updateColumnIndexes = new ArrayList<Integer>();
			List<String> updateList = new ArrayList<String>();
            List<Integer> conditionColumnIndexes = new ArrayList<Integer>();
            List<String> conditionOperator = new ArrayList<String>();
			List<String> conditionList = new ArrayList<String>();

            //* For checking if the update columns exists or not
            for(int i=3;i<indexWhere;i+=4)
            {
                if(!commands[i+1].equals("="))
                {
                    System.out.println("Expected =");
                    return;
                }

                boolean columnExist = false;
                for(int indexColumnNo=0;indexColumnNo<tableHeader.size();indexColumnNo+=2)
                {
                    if(tableHeader.get(indexColumnNo).equals(commands[i]))
                    {
                        columnExist = true;
                        updateColumnIndexes.add(indexColumnNo/2);
                        updateList.add(commands[i+2]);
                        break;
                    }
                }
                if(!columnExist)
                {
                    System.out.println(commands[i]+" doesn't exists in table "+tableName);
                    return;
                }
            }

            //* For checking if the condition columns exists or not
            for(int i=indexWhere+1;i<commands.length;i+=4)
            {
                //* AND OR positions 7, 11, 15
                if(i+3 < commands.length)
                {
                    if(commands[i+3].equals("AND"))
                    {
                        conditionAND = true;
                    }
                    else if(commands[i+3].equals("OR"))
                    {
                        conditionOR = true;
                    }
                    else
                    {
                        System.out.println("Expected AND or OR keyword");
                        return;
                    }
                }
                boolean columnExist = false;
                for(int indexColumnNo=0;indexColumnNo<tableHeader.size();indexColumnNo+=2)
                {
                    if(tableHeader.get(indexColumnNo).equals(commands[i]))
                    {
                        columnExist = true;
                        conditionColumnIndexes.add(indexColumnNo/2);
                        conditionOperator.add(commands[i+1]);
                        conditionList.add(commands[i+2]);
                        break;
                    }
                }
                if(!columnExist)
                {
                    System.out.println(commands[i]+" doesn't exists in table "+tableName);
                    return;
                }
            }

            for(int tableRow=0;tableRow<tableDetails.get(tableName).size();++tableRow) 
            {
                String[] tableArray = tableDetails.get(tableName).get(tableRow).split("#");
                String updatedRow = "";
                boolean updateRow = false;
                if(conditionAND)
                {
                    updateRow = true;
                }
                // 1, Gaurav, gaurav@gmail.com
                //* Condition exists 
                if(conditionColumnIndexes.size() != 0)
                {
                    for(int j=0;j<conditionColumnIndexes.size();++j)
                    {
                        // 7, 11, 15
                        // System.out.println(tableArray[conditionColumnIndexes.get(j)]);
                        // System.out.println(conditionList.get(j));
                        //* AND condition
                        if(conditionAND)
                        {
                            switch (conditionOperator.get(j)) {
                                case "=":
                                    if (!conditionList.get(j).equals(tableArray[conditionColumnIndexes.get(j)])) {
                                        updateRow = false;
                                    }
                                    break;
                                case "<":
                                    if (!(Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) < Integer.parseInt(conditionList.get(j)))) {
                                        updateRow = false;
                                    }
                                    break;
                                case ">":
                                    if (!(Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) > Integer.parseInt(conditionList.get(j)))) {
                                        updateRow = false;
                                    }
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + conditionOperator.get(j));
                            }
                        }
                        //* OR condition
                        else if(conditionOR)
                        {
                            switch (conditionOperator.get(j)) {
                                case "=":
                                    if (conditionList.get(j).equals(tableArray[conditionColumnIndexes.get(j)])) {
                                        updateRow = true;
                                    }
                                    break;
                                case "<":
                                    if (Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) < Integer.parseInt(conditionList.get(j))) {
                                        updateRow = true;
                                    }
                                    break;
                                case ">":
                                    if (Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) > Integer.parseInt(conditionList.get(j))) {
                                        updateRow = true;
                                    }
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + conditionOperator.get(j));
                            }
                        }
                        //* Neither AND nor OR. Only 1 condition
                        else 
                        {
                            switch (conditionOperator.get(j)) {
                                case "=":
                                    if (conditionList.get(j).equals(tableArray[conditionColumnIndexes.get(j)])) {
                                        updateRow = true;
                                    }
                                    break;
                                case "<":
                                    if (Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) < Integer.parseInt(conditionList.get(j))) {
                                        updateRow = true;
                                    }
                                    break;
                                case ">":
                                    if (Integer.parseInt(tableArray[conditionColumnIndexes.get(j)]) > Integer.parseInt(conditionList.get(j))) {
                                        updateRow = true;
                                    }
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + conditionOperator.get(j));
                            }
                        }
                    }
                }
                //* Check if condition matches. If yes then update the row.
                if(updateRow)
                {
                    for(int j=0;j<updateColumnIndexes.size();++j)
                    {
                        tableArray[updateColumnIndexes.get(j)] = updateList.get(j);
                    }
                    for (String s : tableArray) {
                        updatedRow += s + "#";
                    }
                    //* To remove extra # at back
                    updatedRow = updatedRow.substring(0,updatedRow.length()-1);
                    tableDetails.get(tableName).set(tableRow, updatedRow);
                    ++count;
                }
            }

        }
        else
        {
            System.out.println("Incorrect UPDATE Command");
            return;
        }
        System.out.println("Count is "+count);
        if(count != 0)
        {
            List<String> tableRow = tableDetails.get(tableName);
            String rowDetails="";
            for (String s : tableRow) {
                rowDetails += s + "\n";
            }
            String fileName=tableName+".txt";
            try {
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName));
                fileWriter.write(rowDetails);
                System.out.println(count + " rows updated Successfully in " + tableName);
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error occurred while updating rows in " + tableName);
                e.printStackTrace();
            }
        } 
        else {
            System.out.println("No Row Updated");
        }
    }

    public static void Alter(String[] commands)
    {
        if(commands.length != 6 || !commands[1].equals("TABLE"))
        {
            System.out.println("Incorrect ALTER Command");
            return;
        }
        
        //* To remove the ending ;
		commands[commands.length - 1] = commands[commands.length - 1].substring(0,commands[commands.length - 1].length()-1);
        
        String tableName = commands[2];
        if(!tableExists(tableName)) 
        {
            System.out.println(tableName + " doesn't exist");
            return;
        }
        
        //* 6 because ALTER TABLE table_name ADD column_name datatype; 
        if(commands[3].equals("ADD"))
        {
            List<String> tableHeader = tableSchema.get(tableName);
            
            for(int i=0;i<tableHeader.size();i+=2)
            {
                if(commands[4].equals(tableHeader.get(i)))
                {
                    System.out.println(commands[4]+" already exists");
                    return;
                }
            }
            if(commands[5].equals("INT") || commands[5].equals("DECIMAL") || commands[5].matches("^CHAR\\([1-9][0-9]?\\)$"))
            {
                tableHeader.add(commands[4]);
                tableHeader.add(commands[5]);
            } 
            //* "^CHAR\\(-[1-9][0-9]?\\)$" is used to detect Negative numbers e.g. CHAR(-20)
            else if (commands[5].matches("^CHAR\\(-[1-9][0-9]?\\)$"))
            {
                System.out.println("Negative Length Columns are not allowed");
                return;
            } 
            //* "^CHAR\\(0\\)$" is used to detect 0 number e.g. CHAR(0)
            else if (commands[5].matches("^CHAR\\(0\\)$"))
            {
                System.out.println("Zero Length Columns are not allowed");
                return;
            } 
            //* If anyone of the above conditions do not match that means the Datatype is Unknown 
            else {
                System.out.println("Unknown Datatype - " + commands[5]);
                return;
            }
            
            String fileContents="";
            for(Map.Entry tableHeadRow:tableSchema.entrySet())
            {
                fileContents+=tableHeadRow.getKey()+"#";
                for(String entry:(List<String>)tableHeadRow.getValue())
                {
                    fileContents+=entry+"#";
                }
                fileContents = fileContents.substring(0,fileContents.length()-1);
                fileContents+="\n";
            }
            // System.out.println(fileContents);
            try {
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter("schema.txt"));
                fileWriter.write(fileContents);
                System.out.println("Column Added Successfully to "+tableName);
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error occurred while adding Column in "+tableName);
                e.printStackTrace();
            }
            
        } 
        //* 6 because ALTER TABLE table_name DROP COLUMN column_name;
        else if(commands[3].equals("DROP") && commands[4].equals("COLUMN"))
        {
            List<String> tableHeader = tableSchema.get(tableName);
            int columnIndex = 0;
            boolean columnExist = false;
            
            for(int i=0;i<tableHeader.size();i+=2)
            {
                if(commands[5].equals(tableHeader.get(i)))
                {
                    columnIndex = i/2;
                    columnExist=true;
                    tableHeader.remove(i);
                    tableHeader.remove(i);
                    break;
                    // System.out.println(commands[4]+" already exists");
                }
            }
            
            if(!columnExist)
            {
                System.out.println(commands[5]+" doesn't exist in "+tableName);
                return;
            }

            String fileContents="";
            for(Map.Entry tableHeadRow:tableSchema.entrySet())
            {
                fileContents+=tableHeadRow.getKey()+"#";
                for(String entry:(List<String>)tableHeadRow.getValue())
                {
                    fileContents+=entry+"#";
                }
                fileContents = fileContents.substring(0,fileContents.length()-1);
                fileContents+="\n";
            }
            // System.out.println(fileContents);

            loadTables(tableName);

            //* To remove the column Entries from tableName.txt file
            List<String> tableContent = tableDetails.get(tableName);
            // System.out.println(tableContent);
            String tableDetails="";
            for(int i=0;i<tableContent.size();++i)
            {
                String[] tempArray = tableContent.get(i).split("#");
                String temp="";
                for(int j=0;j<tempArray.length;++j)
                {
                    if(j != columnIndex)
                    {
                        temp+=tempArray[j]+"#";
                    }
                }
                temp = temp.substring(0,temp.length()-1);
                tableDetails+=temp+"\n";
                tableContent.set(i, temp);
            }
            // System.out.println(tableContent);
            
            //* For deleting Column from Schema File
            try {
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter("schema.txt"));
                fileWriter.write(fileContents);
                System.out.println("Column Removed Successfully from "+tableName);
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error occurred while removing Column from "+tableName);
                e.printStackTrace();
            }
            
            //* For deleting Column Values from tablename.txt File
            String fileName = tableName + ".txt";
            try {
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName));
                fileWriter.write(tableDetails);
                // System.out.println("Row Added Successfully in " + tableName);
                fileWriter.close();
            } catch (IOException e) {
                // System.out.println("Error occurred while adding Row in " + tableName);
                e.printStackTrace();
            }

        }
        else
        {
            System.out.println("Incorrect ALTER Command");
            return;
        }
        
    }
}
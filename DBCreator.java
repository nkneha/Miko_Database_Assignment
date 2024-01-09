import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DBCreator {
    private static final String TABLE_PATH= "tables";
    private static final String METADATA_PATH = "metadata.txt";

    public static void main(String[] args)  {
        System.out.println("DATABASE COMMANDS");
        System.out.println("CREATE TABLE <table_name> (col1 <datatype>,col2 <datatype>,...) to create a table");
        System.out.println("INSERT INTO <table_name> VALUES (val1,val2,...) to insert values in a table");
        System.out.println("SHOW <table_name> to Print all the Data in the table");
        System.out.println("EXIT or QUIT -- to terminate the program");
        boolean flag=true;
        Scanner scn = new Scanner(System.in);
        while(flag){
            String command = scn.nextLine();
            if(command.equalsIgnoreCase("EXIT")||command.equalsIgnoreCase("QUIT")) {
                flag = false;
                System.out.println("Program Terminated !!!!");
            }
            else
                processCommand(command);
        }
    }

    public static void processCommand(String command) {
        String[] parts = command.split("\\s");
        String keyword = parts[0];

        switch (keyword.toUpperCase()) {
            case "CREATE" -> createTable(command);
            case "INSERT" -> insertIntoTable(command);
            case "SHOW" -> showTableData(parts[1]);
            default -> System.out.println("Invalid command.");
        }
    }

    private static void createTable(String command) {

        // Implement the logic to parse and execute the create table command
        String[] tokens = command.split("[(\\s)]+");
        String tableName = tokens[2];
        if (tableExists(tableName)) {
            System.out.println("Table already exists: " + tableName);
            return;
        }
        String columnsDef = command.substring(command.indexOf("(") + 1, command.indexOf(")"));
        if(columnsDef.isEmpty() || columnsDef.matches("\\s+")){
            System.out.println("Columns cannot be empty");
            return;
        }

        try (PrintWriter metadataWriter = new PrintWriter(new FileWriter(METADATA_PATH, true));
             PrintWriter dataWriter = new PrintWriter(new FileWriter(TABLE_PATH + File.separator + tableName + ".txt", true))) {
            metadataWriter.println(tableName + "," + columnsDef);
            System.out.println("Table created successfully: " + "table: " + tableName + " ,columns: " + columnsDef);
        } catch (IOException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }

    private static void insertIntoTable(String command) {

        String[] tokens = command.split("[(\\s)]+");
        String tableName = tokens[2];

        if (!tableExists(tableName)) {
            System.out.println("Table not found: " + tableName);
            return;
        }

        Pattern pattern = Pattern.compile("\\((.*?)\\)");

        // Create an ArrayList to store the extracted values
        ArrayList<String> pickedValues = getStrings(command, pattern);

        if(pickedValues.isEmpty() || (pickedValues.size() == 1 && Objects.equals(pickedValues.get(0), ""))) {
            System.out.println("Empty, No value present");
            return;
        }

        for (String value : pickedValues) {
            String [] valSplit = value.split(",");
            int  valueLength= valSplit.length;
            if (columnsCount(tableName) < valueLength) {
                System.out.println("Invalid number of values: "+ value+ " Length: "+ valueLength + " ,Allowed: " + columnsCount(tableName));

            }
            else if(columnsCount(tableName) > valueLength){
                System.out.println("Invalid number of values: "+ value+ " Length: "+ valueLength + " ,Allowed: " + columnsCount(tableName));
            }
            else{
                try (PrintWriter tableWriter = new PrintWriter(new FileWriter(TABLE_PATH + File.separator + tableName + ".txt", true))) {
                    tableWriter.println(value);
                    System.out.println("Values inserted successfully: " + "table: " + tableName + " , values: " + value);
                } catch (IOException e) {
                    System.err.println("Error inserting into table: " + e.getMessage());
                }
            }
        }

    }

    private static ArrayList<String> getStrings(String command, Pattern pattern) {
        ArrayList<String> pickedValues = new ArrayList<>();

        // Create a Matcher for the input string
        Matcher matcher = pattern.matcher(command);

        // Find and store values in the ArrayList
        while (matcher.find()) {
            String extractedValue = matcher.group(1);// Group 1 contains the value between parentheses

            if( !extractedValue.matches("\\s+")) {
                pickedValues.add(extractedValue);
            }
        }
        return pickedValues;
    }

    private static int columnsCount(String tableName) {
        try (BufferedReader metadataReader = new BufferedReader(new FileReader(METADATA_PATH))) {
            String line;
            while ((line = metadataReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(tableName)) {
                    return parts.length - 1;
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking if table exists: " + e.getMessage());
        }
        return 0;
    }

    private static void showTableData(String tableName) {

        if(tableName.isEmpty()){
            System.out.println("Table name cannot be empty");
            return;
        }
        if(!tableExists(tableName)){
            System.out.println("Table not found: " + tableName);
            return;
        }

        try (BufferedReader metadataReader = new BufferedReader(new FileReader(METADATA_PATH));
             BufferedReader dataReader = new BufferedReader(new FileReader(TABLE_PATH + File.separator + tableName + ".txt"))) {

            String metadataLine;
            while ((metadataLine = metadataReader.readLine()) != null) {
                String[] metadataParts = metadataLine.split(",");
                if (metadataParts[0].equals(tableName)) {

                    printTableData(dataReader, tableName, metadataParts);
                    return;
                }
            }
            System.out.println("Table not found: " + tableName);
        } catch (IOException e) {
            System.err.println("Error showing table metadata and data: " + e.getMessage());
        }
    }




    private static void printTableData(BufferedReader dataReader, String tableName, String[] metadataParts) throws IOException {

        System.out.println("Table: " + metadataParts[0]);
        StringBuilder columns= new StringBuilder("Columns :");
        for(int i = 1; i < metadataParts.length; i++){
            if(i==metadataParts.length-1){
                columns.append(metadataParts[i]);
            }else{
                columns.append(metadataParts[i]).append(", ");
            }
        }
        System.out.println(columns);

        String dataLine;
        boolean flag = true;
        while ((dataLine = dataReader.readLine()) != null) {
            String[] dataParts = dataLine.split(",");
            if (dataParts[0] != null) {
                flag = false;
                System.out.println("Data: " + Arrays.toString(dataParts));
            }
        }
        if (flag){
            System.out.println("Data: [null]");
        }
    }

    private static boolean tableExists(String tableName) {
        try (BufferedReader metadataReader = new BufferedReader(new FileReader(METADATA_PATH))) {
            String line;
            while ((line = metadataReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(tableName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

# Simple Database
This is the simple Java program that implements a basic database system.It allows you to create tables, insert values into the table and show the table contents.The program uses text file to save the metadata and records. 

## Usage
These are the commands which we use:
- CREATE TABLE <table_name> (col1 <datatype>,col2 <datatype>,...) - to create table
- INSERT INTO <table_name> VALUES (val1,val2,...) - Insert values into a table.
- SHOW <table_name> - Print all the Data in the table.
- EXIT or QUIT - Terminate the program.

## Tech Stack Used
  - Java
  - IntelliJ IDEA

## Example Commands
Here are some example commands you can try:

- #### Create a table:
    ```
    CREATE TABLE table_name (col1 INTEGER,col2 STRING)
    ```
  
- #### Insert values into a table:
    ```
    INSERT INTO table_name VALUES (1,'Neha')
    ```
- #### Show Table Data:
    ```
    SHOW table_name
    ```

- ### Terminate the program:
    ```
    EXIT
    ```
  
## File Structure
The program uses 1 files and 1 folder:<br>
- metadata.txt: Stores the metadata of the tables created.<br>
- tables directory: Stores the .txt files which are created by the CREATE Command for the Tables And inserts records in those files according to table name of each Table using INSERT Command and read data from <table_name>.txt files according to table name of each Table using SHOW Command<br>
- Make sure both metadata.txt and tables folder are present in the same directory as the Java source file.

 
    

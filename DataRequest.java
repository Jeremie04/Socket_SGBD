package sgbd;

import java.util.*;
import object.*;
import java.io.*;
import java.lang.reflect.Method;

public class DataRequest{
    String database;
    String request;

    public String getDatabase(){
        return this.database;
    }
    public void setDatabase(String value){
        this.database=value;
    }

    public String getRequest(){
        return this.request;
    }
    public void setRequest(String value){
        this.request=value;
    }

    public DataRequest(String dtb, String request){
        setDatabase(dtb);
        setRequest(request);
    }

/// Procedure method
    public String createTableProcedure()throws Exception{
        /* Create table table:col1;col2;... */
        String[] req = this.getRequest().split("dataCreate table ");
        String[] element = req[1].split(":"); // 0 for table name; 1 for columns
        TableManager tm = new TableManager(this.getDatabase());
        tm.Creer(element[1], element[0]);
        return "Table created";
    }

    public String insertTableProcedure()throws Exception{
        /* Insert into table:d1;d2;d3... */
        String[] req = this.getRequest().split("dataInsert into ");
        String[] element = req[1].split(":"); // 0 for table name; 1 for columns
        TableManager tm = new TableManager(this.getDatabase());
        tm.Insert(element[1], element[0]);
        return "Table created";

    }

    public String DeleteTableProcedure()throws Exception{
        /* Delete table */
        String[] req = this.getRequest().split("DataDelete ");
        TableManager tm = new TableManager(this.getDatabase());
        tm.Delete(req[1]);
        return "Table Deleted";
    }

    public String DropTableProcedure()throws Exception{
        /* Drop table */
        String[] req = this.getRequest().split("dataDrop ");
        TableManager tm = new TableManager(this.getDatabase());
        tm.Delete(req[1]);
        return "Table Dropped";        
    }    

    public String ExcecuteDataRequest()throws Exception{
        if(this.getRequest().startsWith("dataCreate table ")){
            return this.createTableProcedure();
        }else if(this.getRequest().startsWith("dataInsert into ")){
            return this.insertTableProcedure();
        }else if(this.getRequest().startsWith("dataDelete ")){
            return this.DeleteTableProcedure();
        }else if(this.getRequest().startsWith("dataDrop ")){
            return this.DropTableProcedure();
        }else{
            throw new Exception("Syntax error");
        }
    }
}
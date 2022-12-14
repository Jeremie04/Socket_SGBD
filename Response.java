package socket;

import java.io.*;
import java.net.*;
import java.util.*;

import sgbd.Table;

public class Response {
    ObjectInputStream ois;

    public Response(ObjectInputStream o){
        this.ois= o;
    }

    public void show (){
        try{
            while(true){
                Object result = ois.readObject();
                if(result instanceof Table){
                    Table t = (Table) result;
                    t.display();
                }
                if(result instanceof Exception){
                    Exception ex = (Exception) result;
                    System.out.println(ex.getMessage());
                }
                if(result instanceof String){
                    System.out.println(result);
                }
            }
        }catch(Exception e){
            e.getMessage();
            e.printStackTrace();
        }
    }
}

package socket;

import java.net.Socket;
import java.util.*;
import java.net.*;
import java.io.*;

import sgbd.DatabaseManager;
import sgbd.Table;
import sgbd.*;

public class Serveur {
/// Attributs
    Socket socket;
    String database=null;
    DataInputStream dis;
    ObjectOutputStream oos;

/// Setters & Getters
    public void setSocket(Socket value)throws Exception{
        if(value==null) throw new Exception("Socket null");
        this.socket=value;
    }

    public Socket getSocket(){
        return this.socket;
    }

    public void setDatabase(String value)throws Exception{
        DatabaseManager ddbManager = new DatabaseManager();
        if(ddbManager.DatabaseExist(value)){
            this.database=value;
        }else{
            throw new Exception("Database doesn't exist");
        }
    }

    public String getDatabase(){
        return this.database;
    }

/// Constructor
    public Serveur(Socket sk)throws Exception{
        this.setSocket(sk);
    }


/// Functions 
    public void SendResponse (ObjectOutputStream oos, String str)throws Exception{
        if(str.startsWith("data")){
            DataRequest d = new DataRequest(str, this.getDatabase());
            String result = d.ExcecuteDataRequest();
            oos.writeObject(result);
        }else{
            Request r = new Request(str, this.getDatabase());
            Table result = r.ExcecuteRequest();
            oos.writeObject(result);
        }
    }


    public void run()throws Exception{
        dis=new DataInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());

        String	str="";
        while(true){
            if(str.equals("exit")){
                oos.close();
                dis.close();
                socket.close();
                break;
            }
            str=(String)dis.readUTF();
            System.out.println("message= "+str);
            try{
                if(str.startsWith("use")){
                    String[] req = str.split("use ");
                    this.setDatabase(req[1]);
                    System.out.println("fsfq");
                    String msg = "Database changed";
                    oos.writeUTF(msg);
                    oos.flush();
                }else if(this.getDatabase()!=null){
                    SendResponse(oos, str);
                }else{
                    throw new Exception("Choose a database");   
                }
            }catch(Exception e){
                oos.writeObject(e);    
            }
            oos.flush();
        }
    }
}

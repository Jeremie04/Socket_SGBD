package socket;

import java.io.*;
import java.net.*;
import java.util.*;

import sgbd.Table;

public class Client extends Thread{

    public void client( )throws Exception{
        final Socket socket;
        final DataOutputStream dos;
        final ObjectInputStream ois;
        final Scanner scan = new Scanner(System.in);

        socket = new Socket("localhost", 777 );
        dos = new DataOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        
        try{
            
            String str="";
            while(true){
                System.out.printf(">");
                str = scan.nextLine();
                dos.writeUTF(str);
                dos.flush();
                if(ois.readUTF()!=null){
                    String msg = ois.readUTF();
                    System.out.println(msg);
                }else{
                    Response res = new Response(ois);
                    res.show();
                }
                dos.flush();
            }
            
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }finally{
            try {
                ois.close();
                dos.close();
                socket.close();
            } catch (Exception e) {
                //TODO: handle exception
            }
            
        }
    }
}
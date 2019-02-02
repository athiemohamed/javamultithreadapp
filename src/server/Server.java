package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author athie
 */

public class Server extends Thread {
    
    private static ArrayList<BufferedWriter>users;
    private static ServerSocket server;
    private String user;
    private Socket con;
    private InputStream is;
    private InputStreamReader ir;
    private BufferedReader br;

    
    public Server(Socket con){
        this.con = con;
        try {
            is  = con.getInputStream();
            ir = new InputStreamReader(is);
            br = new BufferedReader(ir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
    public void run(){

        try{

            String msg;
            OutputStream ou =  this.con.getOutputStream();
            Writer ow = new OutputStreamWriter(ou);
            BufferedWriter bw = new BufferedWriter(ow);
            users.add(bw);
            user = msg = br.readLine();

            while(!"Sortir".equalsIgnoreCase(msg) && msg != null)
            {
                msg = br.readLine();
                sendToAll(bw, msg);
                System.out.println(msg);
            }

        }catch (Exception e) {
            e.printStackTrace();

        }
    }

   
    public void sendToAll(BufferedWriter bwSaida, String msg) throws  IOException
    {
        BufferedWriter bwS;

        for(BufferedWriter bw : users){
            bwS = (BufferedWriter)bw;
            if(!(bwSaida == bwS)){
                bw.write(user + msg+"\r\n");
                bw.flush();
            }
        }
    }

   
    public static void main(String []args) {

        try{
           
            int Port = 12345;
           
            server = new ServerSocket(Port);
            users = new ArrayList<BufferedWriter>();
             System.out.println("Server started !");
           
            while(true){
                System.out.println("Waiting for new connection ...");
                Socket con = server.accept();
                System.out.println("Client connected ...");
                Thread t = new Server(con);
                t.start();
            }

        }catch (Exception e) {

            e.printStackTrace();
        }
        
        
    } 

} 


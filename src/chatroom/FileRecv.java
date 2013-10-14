/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroom;

import java.net.*;
import java.io.*;
import gui.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Simon
 */
public class FileRecv implements Runnable{
    private ServerSocket server;
    private Socket recvSocket;
    private final int port = 5577; // default
    private DataInputStream is;
    private DataOutputStream os;
    private FileOutputStream fos;
    private BufferedOutputStream bos;
    private File file;
    private int filesize;
    private int bytesRead;
    private int current;
    private byte [] bufferArray;
    
    public FileRecv(File f, int fsize){
        file = f;
        filesize = fsize;
    }
    
    @Override
    public void run() {
        //System.out.print( bytesRead+" read, " );
        try{
            server = new ServerSocket(port);
            recvSocket = server.accept();
            System.out.println("Flie transmission channel constructed!!");
            is = new DataInputStream(recvSocket.getInputStream());
            os = new DataOutputStream(recvSocket.getOutputStream());
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            current = 0;
            bufferArray = new byte [filesize];
            

            if(is.readUTF().equals("/r")){

                System.out.println("BEGIN");                
                do {
                        
                    bytesRead = is.read(bufferArray, current, (bufferArray.length-current));
                    if(bytesRead>0){
                        current += bytesRead;
                    }
                    System.out.println(current);
                    System.out.println(filesize);
                }while(current < filesize );
                bos.write(bufferArray, 0, current);
                bos.flush();
                bos.close();
                recvSocket.close();
                server.close();
                System.out.println("SAVE DONE");
            }

            recvSocket.close();
            server.close();
        }catch(IOException e){
             System.out.println(e.toString());
        }
    }
        
}
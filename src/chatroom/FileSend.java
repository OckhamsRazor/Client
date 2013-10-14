/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroom;

import java.net.*;
import java.io.*;
import gui.*;
/**
 *
 * @author Simon
 */
public class FileSend implements Runnable{
    
    private String recvIP;
    private int port = 5577;
    private Socket sendSocket;
    private DataInputStream is;
    private DataOutputStream os;
    private FileInputStream fis;
    private BufferedInputStream bis;
    private File file;
    private int filesize;
    private int bytesSent;
    private int current;
    private byte [] bufferArray;
    
    public FileSend(String IP, File f){
        char[] char_ip = IP.toCharArray();
        StringBuffer temp_ip = new StringBuffer("");
        for(int i = 1; i < IP.length(); ++i){
            temp_ip.append(char_ip[i]);
        }
        recvIP = temp_ip.toString();
         System.out.println(recvIP);
        file = f;
        filesize = (int)file.length();
        System.out.println(file.getAbsolutePath());
        
    }

    @Override
    public void run() {
        try{

            sendSocket = new Socket(recvIP,port);
         
            is = new DataInputStream(sendSocket.getInputStream());
            os = new DataOutputStream(sendSocket.getOutputStream());
            
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            bytesSent = 0;
            
            bufferArray = new byte [filesize];
            os.writeUTF("/r");
        //    bytesSent = is.read( bufferArray, 0, filesize );
            current = bytesSent;
            int len = bis.read( bufferArray, 0, bufferArray.length );
            System.out.println(len);
            os.write(bufferArray, 0, bufferArray.length);
            os.flush();
            os.close();
            sendSocket.close();
            System.out.println("SEND DONE");
        }catch(IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
}

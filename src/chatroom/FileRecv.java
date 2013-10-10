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
public class FileRecv implements Runnable{
    private ServerSocket server;
    private final int port = 5566; 
    
    public FileRecv(String filename, String dir){
        
    }
    public void run(){
        try{
            server = new ServerSocket(port);
        }catch(IOException e){
            
        }
        
        
    }
}

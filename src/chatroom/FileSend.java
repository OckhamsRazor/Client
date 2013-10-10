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
    private File fs;
    
    public FileSend(String IP, File f){
        recvIP = IP;
        fs = f;
    }

    @Override
    public void run() {
        
    }
}

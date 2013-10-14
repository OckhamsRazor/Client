/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sound;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

/**
 *
 * @author User
 */
public class revSound implements Runnable{
    private Thread thread;
    private Socket socket;
    private AudioFormat format;
    private AudioPlayStream listen;
    
    public revSound(Socket s) 
    {
        socket=s;
        format=AMAudioFormat.getLineAudioFormat(AMAudioFormat.FORMAT_CODE_GSM);
        listen=new AudioPlayStream(format);
    }
    public void connecting()
    {
        try {
            listen.open();
        } catch (Exception ex) {
            Logger.getLogger(tranSound.class.getName()).log(Level.SEVERE, null, ex);
        }
        thread=new Thread();
        thread.start();
    }
    @Override
    public void run() {
        System.out.println("asa2");
        byte[] data=new byte[1024];
        BufferedInputStream playbackInputStream = null;
        try {
            playbackInputStream=new BufferedInputStream(new AudioInputStream(socket.getInputStream(),format,2147483647));
        } catch (IOException ex) {
            Logger.getLogger(tranSound.class.getName()).log(Level.SEVERE, null, ex);
        }
        int numBytesRead=0;  
        try {
            listen.start();
        } catch (Exception ex) {
            Logger.getLogger(revSound.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(thread!=null)
        {
            try {
                numBytesRead=playbackInputStream.read(data);
                listen.write(data,0,numBytesRead);
            } catch (IOException ex) {
                break;
            }
        }
        try {
            listen.close();
        } catch (IOException ex) {
            Logger.getLogger(revSound.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void stop()
    {
        thread=null;
    }
}

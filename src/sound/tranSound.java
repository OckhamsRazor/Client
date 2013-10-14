/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sound;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;

/**
 *
 * @author User
 */
public class tranSound implements Runnable{
    private Thread thread;
    private Socket socket;
    private int port;
    private AudioCapture speak;
    //private AudioFormat format;
    private InputStream stream;
    
    public tranSound(Socket s) 
    {
        socket=s;
        speak=new AudioCapture(AMAudioFormat.FORMAT_CODE_GSM);
        //format=AMAudioFormat.getLineAudioFormat(AMAudioFormat.FORMAT_CODE_GSM);
    }
    public void connecting()
    {
        try {
            speak.open();
            stream=speak.getAudioInputStream();
        } catch (Exception ex) {
            Logger.getLogger(tranSound.class.getName()).log(Level.SEVERE, null, ex);
        }
        thread=new Thread();
        thread.start();
    }
    @Override
    public void run() {
        System.out.println("asa1");
        byte[] data=new byte[1024];
        BufferedOutputStream captureOutputStream = null;
        try {
            captureOutputStream=new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            return;
        }
        try {
            speak.start();
        } catch (Exception ex) {
            Logger.getLogger(tranSound.class.getName()).log(Level.SEVERE, null, ex);
        }
        int numBytesRead=0;
        while(thread!=null)
        {
            try {
                numBytesRead=stream.read(data);
                captureOutputStream.write(data,0,numBytesRead);
            } catch (IOException ex) {
                Logger.getLogger(tranSound.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        speak.close();
        try {
            captureOutputStream.flush();
            captureOutputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(tranSound.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        
    }
    public void stop()
    {
        thread=null;
    }

    
    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sound;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class Sound {
    private ServerSocket serve;
    private Socket inSocket;
    private Socket outSocket;
    private tranSound transfer;
    private revSound receive;
    
    public Sound()
    {
       
    }
    public void connect1(int p)
    {
        try {
            serve=new ServerSocket(p);
            System.out.println("asa0");
            inSocket=serve.accept();
            
            receive=new revSound(inSocket);
            receive.connecting();
        } catch (IOException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void connect2(String ip,int p_partner,int p_me)
    {
        try {
            outSocket= new Socket(InetAddress.getByName(ip), p_partner);
            System.out.println("asa1");
            transfer=new tranSound(outSocket);
            transfer.connecting();
            serve=new ServerSocket(p_me);
            inSocket=serve.accept();
            receive=new revSound(inSocket);
            receive.connecting();
        } catch (IOException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void connect3(String ip,int p_partner)
    {
        try {
            outSocket= new Socket(InetAddress.getByName(ip), p_partner);
            transfer=new tranSound(outSocket);
            transfer.connecting();
        } catch (IOException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void disconnect()
    {
        transfer.stop();
        receive.stop();
    }
    
    
}

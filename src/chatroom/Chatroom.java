/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import gui.ChatFrame;

/**
 *
 * @author OckhamsRazor
 */
public class Chatroom {

    /**
     * @param args the command line arguments
     */
    private static ChatFrame _FrameObject;
    
    public static void main(String[] args) throws IOException {
        _FrameObject = new ChatFrame();
        _FrameObject.setVisible(true);
        // TODO code application logic here
        System.out.println("asa");
        /*
        System.out.println("Please enter the server's ip:");
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader keyboard = new BufferedReader(in);
        String ip=keyboard.readLine();
        System.out.println("Please enter the port number:");
        InputStreamReader in2 = new InputStreamReader(System.in);
        BufferedReader keyboard2 = new BufferedReader(in2);
        String s2=keyboard2.readLine();
        int port=Integer.parseInt(s2);
        System.out.println("Please enter the username:");
        InputStreamReader in3 = new InputStreamReader(System.in);
        BufferedReader keyboard3 = new BufferedReader(in3);
        String username=keyboard3.readLine();
        System.out.println("Please enter the password:");
        InputStreamReader in4 = new InputStreamReader(System.in);
        BufferedReader keyboard4 = new BufferedReader(in4);
        String password=keyboard4.readLine();
         */

        
        
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import gui.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JList;

/**
 *
 * @author user
 */
public class Client implements Runnable{
    
    // room tab
    private ChatFrame frame;
    public ChatRoomHall chatHall;
    public ChatFrame getFrame(){return frame;} 
    
   
    // room information 
    public ArrayList roomList;
    public HashMap roomMap; 
    public int roomCount;
    public Vector<String> userList;
    
    //log & server setting
    private LogWindow logWindow;
    private SettingWindow settingWindow;
    private int port;
    private String serverIP;
    public String username;
    private String password;
   
    // socket 
    private Socket socket;
    private DataInputStream i;
    private DataOutputStream o;
    private Thread thread;
    
    // log & connection state
    public boolean isLoggedIn;
    private boolean isConnected;
    
    
    public Client(ChatFrame f)
    {
        frame = f;
        logWindow = new LogWindow(frame);
        logWindow.setLocationRelativeTo(frame);
        logWindow.setVisible(false);
        settingWindow = new SettingWindow(frame);
        settingWindow.setLocationRelativeTo(frame);
        settingWindow.setVisible(false);
        serverIP = "140.112.18.224";
        port = 5566;
        isLoggedIn=false;
        isConnected=false; 
        roomMap = new HashMap();
        roomList = new ArrayList();
        roomCount = 0;
        userList = new Vector<String>();
    }
  
    public void setServer(){
        settingWindow.setVisible(true);
        serverIP = settingWindow.serverIP;
        port = settingWindow.port;
        
    }
    public void connectServer() throws IOException
    {
        logWindow.setVisible(true);
        
        username = logWindow.username;
        password = logWindow.password; 
        try
        { 
            
            socket = new Socket(InetAddress.getByName(serverIP),port);
            
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            i=new DataInputStream(in);
            o=new DataOutputStream(out);
            
             sendName();
             
            isConnected=true;
            chatHall = new ChatRoomHall(this);
            thread=new Thread(this);               
            thread.start();
            
        }
        catch (IOException ex)
        {
            somethingWrong();
        }
        // should receive user list from server!!
        isLoggedIn = true;
        roomList.add(chatHall);
        roomMap.put(0,chatHall); // cannot add friends in Hall
        frame.addHall(chatHall);
        //chatHall.addUser(username);
        chatHall.enterMessage(username);

    }
    public void sendName() throws IOException
    {
            String state=i.readUTF();
            if(state.equals("\001EST\000\004"))
            {
                o.writeUTF("\001LOGIN\000"+username+"\000"+password+"\000\004");
                System.out.println("hi");
            }
            while(true)
            {
                state=i.readUTF();
                if(state.equals("\001LOGINACK\000\004"))
                    break;
                else
                {
                    logWindow.setVisible(true);
                    username = logWindow.username;
                    password = logWindow.password;
                    o.writeUTF("\001LOGIN\000"+username+"\000"+password+"\000\004");
                }
            }
            System.out.println("hi");
            /*then check the protocol*/
    }
    public void somethingWrong() throws IOException
    {
        System.out.print("error");
        /*if(isConnected)
        {   
            o.writeUTF("haha");
        }*/
        logWindow.setVisible(false);
        settingWindow.setVisible(false);
        chatHall.setVisible(false);
        System.out.print("false");
        logWindow = new LogWindow(frame);
        logWindow.setLocationRelativeTo(frame);
        logWindow.setVisible(false);
        settingWindow = new SettingWindow(frame);
        settingWindow.setLocationRelativeTo(frame);
        settingWindow.setVisible(false);
        serverIP = "140.112.18.222";
        port = 5566;
        isLoggedIn=false;
        isConnected=false; 
        roomMap = new HashMap();
        roomList = new ArrayList();
        roomCount = 0;
        userList = new Vector<String>();
        
    }
    public void send(String msg)
    {
        try {
            o.writeUTF("\001"+msg+"\000\004");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            try {
                somethingWrong();
            } catch (IOException ex1) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    
    public void sendAddRoom(int myRoomNumber)
    {
        send("NEWROOM\000"+username+"\000"+Integer.toString(myRoomNumber));
    }
    
    public void sendAcceptInvitation(int roomNumber)
    {
        send("IN\000"+username+"\000"+Integer.toString(roomNumber));
    }
    
    public void sendLeaveRoom(int roomNumber)
    {
        send("OUT\000"+username+"\000"+Integer.toString(roomNumber));
    }
    
    public void sendInvitation(int roomNumber, String receiver, String msg)
    {
        send("INVITE\000"+username+"\000"+Integer.toString(roomNumber)+"\000"+receiver+"\000"+msg);
    }
    
    private void sendWhisper(int roomNumber, String receiver, String msg)
    {
        send("MSG_P\000"+username+"\000"+Integer.toString(roomNumber)+"\000"+receiver+"\000"+msg);
    }
    
    public void sendRoomMsg(int roomNumber, String msg)
    {
        send("MSG\000"+username+"\000"+Integer.toString(roomNumber)+"\000"+msg);
    }
    
    private void sendChangeState(String status)
    {
        send("STAT\000"+username+"\000"+status);
    }
    
    public void sendLogOut()
    {
        send("LOGOUT");
    }
    
    private void rvRoomNumber(int myRoomNumber, int roomKeyAssigned)
    {
        ChatRoomPrivate c = (ChatRoomPrivate) roomList.get(myRoomNumber);
        roomMap.put(roomKeyAssigned,c);
        c.roomKey = roomKeyAssigned;
    }
    
    private void rvAddRoomUser(String roomUser, int roomKey)
    {
       ChatRoomPrivate c = (ChatRoomPrivate) roomMap.get(roomKey);
       c.addUser(roomUser);
       c.enterMessage(roomUser);
    }
    
    private void rvLeaveRoomUser(String roomUser, int roomKey)
    {
        ChatRoomPrivate c = (ChatRoomPrivate) roomMap.get(roomKey);
        c.deleteUser(roomUser);
        c.leaveMessage(roomUser);
    }
    
    private void rvAddHallUser(String user)
    {
        if(user==username)
            return;
        System.out.println("adddddddd");
        userList.add(user);
        chatHall.displayUserList();
        chatHall.enterMessage(user);
    }
    
    private void rvLeaveHallUser(String user)
    {
        chatHall.deleteUser(user);
        userList.remove(user);
        chatHall.leaveMessage(user);
    }
    
    private void rvChangeState(String user, String status)
    {
        
    }
    
    private void rvWhisper(String sender, int roomKey, String msg)
    {
        if(roomKey == 0){
            if(sender==username)
                return;
            chatHall.showRecvMessage(sender, msg, true);
        }else{
            if(sender==username)
                return;
            ChatRoomPrivate c = (ChatRoomPrivate) roomMap.get(roomKey);
            c.showRecvMessage(sender, msg, true);
        }
    }
    
    private void rvRoomMsg(String sender, int roomKey, String msg)
    {
        if(roomKey == 0){
            if(sender==username)
                return;
            chatHall.showRecvMessage(sender, msg, false);
        }else{
            if(sender==username)
                return;
            ChatRoomPrivate c = (ChatRoomPrivate) roomMap.get(roomKey);
            c.showRecvMessage(sender, msg, false);
        }
    }
    public void rvUserList(String[] userlist, int length)
    {
        Vector<String> tmp=new Vector<String>();
        System.out.print(length);
        int i=0;
        while(true)
        {
            if(i==length)
                break;
            System.out.println(userlist[i*2+2]);
            tmp.add(userlist[i*2+2]);
            i=i+1;
        }
        //System.out.print("hahahah1");
        userList=tmp;
        //System.out.print("hahahah2");
        chatHall.updateUserList(tmp);
        //System.out.print("hahahah3");
        //chatHall.displayUserList(userList);
    }
    
    public void rvRoomUserList(String[] userlist, int length)
    {
        Vector<String> tmp=new Vector<String>();
        System.out.print(length);
        for(int i=3;i<length;i=i+1)
        {
            if(userlist[i]==username)
                continue;
            tmp.add(userlist[i]);
        }

        ChatRoomPrivate c = (ChatRoomPrivate) roomMap.get(Integer.parseInt(userlist[1])); 
        c.updateUserList(tmp);
        //chatHall.displayUserList(tmp);
    }
    
    private void parseMsg(String msg)
    {
        String[] message=msg.split("\000");
        //String[] string2=message[0].split("\001");
        switch(message[0])
        {
            case("\001NEWROOM"):
                rvRoomNumber(Integer.parseInt(message[1]),Integer.parseInt(message[2]));
                break;
            case("\001SB_IN"):
                rvAddRoomUser(message[1],Integer.parseInt(message[2]));
                break;
            case("\001SB_OUT"):
                rvLeaveRoomUser(message[1],Integer.parseInt(message[2]));
                break;
            case("\001SB_LOGIN"):
                rvAddHallUser(message[1]);
                break;
            case("\001SB_LOGOUT"):
                rvLeaveHallUser(message[1]);
                break;
            case("\001SB_STAT"):
                rvChangeState(message[1],message[2]);
                break;
            case("\001MSG_GET"):
                rvRoomMsg(message[1],Integer.parseInt(message[2]),message[3]);
                break;
            case("\001MSG_P_GET"):
                rvWhisper(message[1],Integer.parseInt(message[2]),message[3]);
                break;
            case("\001USERLIST"):
                int length=message.length;
                System.out.print(msg);
                rvUserList(message,Integer.parseInt(message[1]));
                break;
            case("\001RM_USERLIST"):
                int length2=message.length;
                rvRoomUserList(message,length2);
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        System.out.println("im alrealy here");
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        while(true)
        {
            
            try {
                String msg=i.readUTF();
                System.out.println(msg+" msg");
                parseMsg(msg);
                
            } catch (IOException ex) {
                try {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    somethingWrong();
                } catch (IOException ex1) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
    }
}



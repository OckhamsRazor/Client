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
import java.awt.FileDialog;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import sound.Sound;

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
    private ConnectionWindow connecitonWindow;
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
    private boolean isLoggedIn;
    private boolean isConnected;
    public boolean connectionState(){return isConnected;}
    public void setLogState(boolean b){ isLoggedIn = b;}
    public boolean getLogState(){return isLoggedIn;}
    
    // file transmission
    private HashMap recvFileMap;
    
    public Sound sound;
    
    
    public Client(ChatFrame f)
    {
        frame = f;
        logWindow = new LogWindow(frame);
        logWindow.setLocationRelativeTo(frame);
        logWindow.setVisible(false);
        connecitonWindow = new ConnectionWindow(frame, this);
        connecitonWindow.setLocationRelativeTo(frame);
        connecitonWindow.setVisible(false);
        serverIP = "140.112.18.224";
        port = 5566;
        isLoggedIn=false;
        isConnected=false; 
        roomMap = new HashMap();
        roomList = new ArrayList();
        roomCount = 0;
        userList = new Vector<String>();
        // file transmission
        recvFileMap = new HashMap<String,File>();
    }
  
    public void setServer(){
        connecitonWindow.setVisible(true);
    }
    
    public void connect(String IP, int p){
         try
        { 
            socket = new Socket(InetAddress.getByName(serverIP),port);
            
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            i=new DataInputStream(in);
            o=new DataOutputStream(out);
           
            String state=i.readUTF();
            if(!state.equals("\001EST\000\004")) 
            {
                System.out.println("Connetion fail!!");
            }
                    thread=new Thread(this);               
        thread.start();
            isConnected=true;
            System.out.println("Connetion success!!");
        }
        catch (IOException ex)
        {
                JOptionPane.showMessageDialog(frame, "Someting wrong happened whend connecting server.\n" 
                                    + ex.toString()+ "\nPlease try it again.","Connection Error", JOptionPane.ERROR_MESSAGE);
        }
        // should receive user list from server!!
    }
   
    public void signUp(){
        SignUpWindow signUpWindow = new SignUpWindow(frame);
        signUpWindow.setVisible(true);
        if(!signUpWindow.continueToSignUp) return;
        username = signUpWindow.username;
        password = signUpWindow.password;
        send("NEWUSER\000" +username + "\000" +password);
    }
    public void logIn() 
    {
     
        if(!isConnected) {
            JOptionPane.showMessageDialog(frame, "Please connect to server first.","Log error",JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        logWindow.setVisible(true);
        if(!logWindow.continueToLog) return;
        username = logWindow.username;
        password = logWindow.password; 
        chatHall = new ChatRoomHall(this);
        
        try {
            sendName();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        isLoggedIn = true;
        roomList.add(chatHall);
        roomMap.put(0,chatHall); // cannot add friends in Hall
        frame.addHall(chatHall);
        chatHall.enterMessage(username);
      
    }
    public void sendName() throws IOException
    {       
            send("LOGIN\000"+ username+"\000"+password);
            System.out.println("a");
            
            while(true) {
            String state=i.readUTF();
            
            System.out.println(state);
            String[] message=state.split("\000");
            System.out.println(message[0]);
            if(state.equals("\001LOGINACK\000\004")) return;
                
            else
            {
                if(message[0].equals("\001ERROR"))
                {
                    JOptionPane.showMessageDialog(frame,
                                                message[1],
                                                "Please sign up first!!",
                                                JOptionPane.ERROR_MESSAGE);
                    signUp();
                }else{
                    JOptionPane.showMessageDialog(frame,
                                                message[1],
                                                "Please login again.",
                                                JOptionPane.INFORMATION_MESSAGE);
                    logIn();
                }
               // o.writeUTF("\001LOGIN\000"+username+"\000"+password+"\000\004");
            }
            System.out.println("hi");
            /*then check the protocol*/
            }
    }

    public void send(String msg)
    {
        try {
            o.writeUTF("\001"+msg+"\000\004");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            
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
    
    public void sendWhisper(int roomNumber, String receiver, String msg)
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
    
    // emit send file request for other client to server 
    public void sendFileSendReq(String target){
        // check if in sendlist
        if(recvFileMap.containsKey(target)){
            int c = JOptionPane.showConfirmDialog(frame, "One file request is sent to " + target 
                                                        + ".\nDo you want to send new request?",
                                                        "",
                                                        JOptionPane.YES_NO_OPTION);
            if(c==JOptionPane.YES_OPTION){
                JFileChooser openFile = new JFileChooser();
                openFile.showOpenDialog(frame);
                File fs = openFile.getSelectedFile();
                String filename = fs.getName();
                int filesize = (int)fs.length();
                recvFileMap.remove(target);
                recvFileMap.put(target, fs);
                send("FS_REQ\000"+username+"\000"+target+"\000"+filename+"\000"+filesize+"\000");
               
            }else{
                // do nothong
            }
        }
        else{
            JFileChooser openFile = new JFileChooser();
            openFile.showOpenDialog(frame);
            File fs = openFile.getSelectedFile();
            String filename = fs.getName();
            int filesize = (int)fs.length();
            recvFileMap.put(target,fs);
            send("FS_REQ\000"+username+"\000"+target+"\000"+filename+"\000"+filesize+"\000");
        }
    }
    
    // receiver client
    public void sendFileRecvReply(boolean readyToRecv, String sender){
        //send protocol to server
        if(readyToRecv){
            // send protocol
            send("FS_REP_Y\000"+username+"\000"+sender+"\000"+socket.getLocalAddress()+"\000");
        }else{
            // send protocol
            send("FS_REP_N\000"+username+"\000"+sender+"\000");
        }
    }
    
    public void sendSpeakInvite(String receiver)
    {
        int sender_port=5560;
        sound=new Sound();
        sound.connect1(sender_port);
        send("SPEAK\000"+receiver+"\000"+Integer.toString(sender_port)+"\000");
    }
    
    public void sendSpeakAck()
    {
        int sender_port=5570;
        send("SPEAK_ACK\000"+Integer.toString(sender_port)+"\000");
    }
    
    public void logOut() throws IOException
    {
        send("LOGOUT");
        // log state reset
        socket.close();
        isLoggedIn = false;
        isConnected = false;
        // user info reset
        username = "";
        password = "";
        // room info reset
        roomMap = new HashMap();
        roomList = new ArrayList();
        roomCount = 0;
        //
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
        userList.add(user);
        chatHall.updateUserList(userList);
        chatHall.enterMessage(user);
    }
    
    private void rvLeaveHallUser(String user)
    {
        userList.remove(user);
        chatHall.updateUserList(userList);
        chatHall.leaveMessage(user);
        // update private room user list
        frame.updateChatRoomUserList(user);
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
        userList=tmp;
        chatHall.updateUserList(tmp);
    }
    
    public void rvRoomUserList(String[] userlist, int length)
    {
        Vector<String> tmp=new Vector<String>();
        System.out.print(length);
        int i=0;
        while(true)
        {
            if(i==length)
                break;
            System.out.println(userlist[i*2+3]);
            tmp.add(userlist[i*2+3]);
            i=i+1;
        }
        System.out.println("hahaha");
        ChatRoomPrivate c = (ChatRoomPrivate) roomMap.get(Integer.parseInt(userlist[1])); 
        c.updateUserList(tmp);
        //chatHall.displayUserList(tmp);
    }
    
    public void rvInvitation(int roomNum)
    {
        roomCount++; 
        ChatRoomPrivate c = new ChatRoomPrivate(this);
        roomList.add(c);
        c.roomKey=roomNum;
        roomMap.put(roomNum,c);
        frame.addRoomTab(c);
    }
    
    // not sure protocol yet
    public void rvFileSendReq(String sender, String filename, String filesize){ // default port
       int option = JOptionPane.showConfirmDialog(frame, sender + " want to send a file to you.\n"
                                                        + "File Name: " + filename +'\n'
                                                        + "File Size: " + filesize
                                                ,"File request",JOptionPane.YES_NO_OPTION);
        if(option == JOptionPane.YES_OPTION){
            JFileChooser recvFileDialog = new JFileChooser();
            int result = recvFileDialog.showSaveDialog(frame);
            if( result == JFileChooser.APPROVE_OPTION){
                File file = recvFileDialog.getSelectedFile();
                Thread fsThread = new Thread (new FileRecv(frame, file,Integer.parseInt(filesize)));
                fsThread.start();
                sendFileRecvReply(true,sender);
            }else{
                
            }
           
        } else {
            sendFileRecvReply(false,sender);
        }
    }
    
    public void rvFileRecvReply(String receiver, String recvIP){
        System.out.println(receiver + " &&" + recvIP );
        File fs = (File)recvFileMap.get(receiver);
        recvFileMap.remove(receiver);
        Thread fsThread = new Thread(new FileSend(recvIP,fs));
        fsThread.start();
    }
    public void rvFileRecvReply(String receiver){
        JOptionPane.showMessageDialog(frame,receiver + " reuse to receive file.","Send fail.", JOptionPane.INFORMATION_MESSAGE);
        recvFileMap.remove(receiver);
    }
    public void rvSpeak(String sender, String sender_ip, int sender_port)
    {
        int reply = JOptionPane.showConfirmDialog(frame, "( " + sender + " ) wants to see you", "Audio request", JOptionPane.YES_NO_CANCEL_OPTION);
        if(reply == JOptionPane.OK_OPTION){
            int my_port=5570;
            sound=new Sound();
            sound.connect2(sender_ip, sender_port, my_port);
        }else return;
    }
    public void rvSpeakAck(String sender_ip, int sender_port)
    {
        sound.connect3(sender_ip, sender_port);
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
                //int length=message.length;
                //System.out.print(msg);
                rvUserList(message,Integer.parseInt(message[1]));
                break;
            case("\001RM_USERLIST"):
                //int length2=message.length;
                rvRoomUserList(message,Integer.parseInt(message[2]));
                break;
            case("\001IN"):
                rvInvitation(Integer.parseInt(message[1]));
                break;
            case("\001FS_REQ"):
                System.out.println("a");
                rvFileSendReq(message[1],message[2],message[3]);
                break;
            case("\001FS_REP_Y"):
                System.out.println("b");
                rvFileRecvReply(message[1],message[2]); // receiver/IP
                break;
            case("\001FS_REP_N"):
                System.out.println("c");
                rvFileRecvReply(message[1]);
				break;
            case("\001ERROR"):
                JOptionPane.showMessageDialog(
                    null,
                    message[1],
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                break;
            case("\001SPEAK"):
                rvSpeak(message[1],message[2],Integer.parseInt(message[3]));
                break;
            case("\001SPEAK_ACK"):
                rvSpeakAck(message[1], Integer.parseInt(message[2]));
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
                ex.toString();
            }
        }
    }
}



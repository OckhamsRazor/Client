/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import chatroom.*;
import java.awt.Component;
import java.awt.FileDialog;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;


/**
 *
 * @author eecamp
 */
public class ChatFrame extends javax.swing.JFrame {

    /**
     * Creates new form ChatFrame
     */
    private Client client;
     
    
    // client info is accessible
    public ChatFrame() {
        initComponents();
        client = new Client(this);
        client.setLogState(false);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        roomTab = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        setServer = new javax.swing.JMenuItem();
        logIn = new javax.swing.JMenuItem();
        logOut = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        newRoom = new javax.swing.JMenuItem();
        leaveRoom = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        saveConv = new javax.swing.JMenuItem();
        sendFile = new javax.swing.JMenuItem();

        jMenu2.setText("jMenu2");

        jMenu3.setText("jMenu3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenuBar1.setMinimumSize(new java.awt.Dimension(0, 10));
        jMenuBar1.setPreferredSize(new java.awt.Dimension(92, 30));

        jMenu1.setText("Option");

        setServer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        setServer.setText("Set server");
        setServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setServerActionPerformed(evt);
            }
        });
        jMenu1.add(setServer);

        logIn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.ALT_MASK));
        logIn.setText("Log in");
        logIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logInActionPerformed(evt);
            }
        });
        jMenu1.add(logIn);

        logOut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK));
        logOut.setText("Log out");
        logOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutActionPerformed(evt);
            }
        });
        jMenu1.add(logOut);

        jMenuBar1.add(jMenu1);

        jMenu4.setText("Room");

        newRoom.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_MASK));
        newRoom.setText("New room");
        newRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newRoomActionPerformed(evt);
            }
        });
        jMenu4.add(newRoom);

        leaveRoom.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.ALT_MASK));
        leaveRoom.setText("Leave room");
        leaveRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leaveRoomActionPerformed(evt);
            }
        });
        jMenu4.add(leaveRoom);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("File");

        saveConv.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        saveConv.setText("Save conversation");
        saveConv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveConvActionPerformed(evt);
            }
        });
        jMenu5.add(saveConv);

        sendFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.ALT_MASK));
        sendFile.setText("Send file to...");
        sendFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendFileActionPerformed(evt);
            }
        });
        jMenu5.add(sendFile);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roomTab, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roomTab, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logInActionPerformed
        // TODO add your handling code here:
        if(!client.getLogState()){
            try {
                client.connectServer();
            } catch (IOException ex) {
                Logger.getLogger(ChatFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            JOptionPane.showMessageDialog(this,"You are already logged in","",JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_logInActionPerformed

    private void newRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newRoomActionPerformed
        // TODO add your handling code here:
        if(client.getLogState()){
            addRoom();
        }
        else{
            JOptionPane.showMessageDialog(this,"Please log in first!!","New Room Warning",JOptionPane.INFORMATION_MESSAGE); 
        }
    }//GEN-LAST:event_newRoomActionPerformed

    private void setServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setServerActionPerformed
        // TODO add your handling code here:
        client.setServer();
    }//GEN-LAST:event_setServerActionPerformed

    private void leaveRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leaveRoomActionPerformed
        // TODO add your handling code here:
        // not in the hall
        if(roomTab.getSelectedIndex() != 0){
            deleteRoom();
        }
    }//GEN-LAST:event_leaveRoomActionPerformed

    private void logOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutActionPerformed
        // TODO add your handling code here:
        if(client.getLogState()){
            client.sendLogOut();
        }else{
            JOptionPane.showMessageDialog(this, "You haven't logged in !!", "Log Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_logOutActionPerformed

    private void saveConvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveConvActionPerformed
        // TODO add your handling code here:
        if(roomTab.getSelectedIndex() == 0){ 
            // at Hall; no action
            JOptionPane.showMessageDialog(this,"You are in the Hall.","Invitation Error",JOptionPane.INFORMATION_MESSAGE);
        }else{
            // show list
        }
    }//GEN-LAST:event_saveConvActionPerformed

    private void sendFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendFileActionPerformed
        // TODO add your handling code here:
        GuestListWindow sendTargetWindow = new GuestListWindow(this,"Select Recevier");
        sendTargetWindow.setVisible(true);
        if(!sendTargetWindow.continueToSend) return;
        client.sendFileSendReq(sendTargetWindow.getSelectedGuest());
    }//GEN-LAST:event_sendFileActionPerformed

    // should log in first!!
    public void addHall(ChatRoomHall hall){
        roomTab.addTab("Hall", hall);
    }
    
    public void addRoom(){
        // test
         client.roomCount++; 
        int roomNum = client.roomCount;
        ChatRoomPrivate newRoom = new ChatRoomPrivate(client);
        client.roomList.add(newRoom);
        newRoom.addUser(client.username);
        roomTab.addTab("New Room", newRoom);
        System.out.println(client.roomMap.size());
        client.sendAddRoom(client.roomCount);
    }
    public void addRoomTab(ChatRoomPrivate room){
        roomTab.addTab("New Room", room);
    }
    // leave room or being kicked out
    public void deleteRoom(){
         ChatRoomPrivate c = (ChatRoomPrivate) roomTab.getSelectedComponent();
         roomTab.remove(roomTab.getSelectedIndex());
         client.sendLeaveRoom(c.roomKey);
    }
    public void clearTabbedRoom(){
        // delete all rooms except Hall
    }
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem leaveRoom;
    private javax.swing.JMenuItem logIn;
    private javax.swing.JMenuItem logOut;
    private javax.swing.JMenuItem newRoom;
    private javax.swing.JTabbedPane roomTab;
    private javax.swing.JMenuItem saveConv;
    private javax.swing.JMenuItem sendFile;
    private javax.swing.JMenuItem setServer;
    // End of variables declaration//GEN-END:variables
}

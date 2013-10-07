/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.lang.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.*;
import chatroom.*;

/**
 *
 * @author eecamp
 */
public class ChatRoomHall extends javax.swing.JPanel {

    /**
     * Creates new form ChatRoomHall
     */

    private Client client;
    // msgTextPane elements
    private StyledDocument inputDoc;    
    private StyledDocument dialogDoc;
    private String inputText;
    private String dialogText;
    private boolean whisper;
    public int roomKey;

    // guestList elements;
    private Vector<String> userList;    // all users who connect to server
    
    public ChatRoomHall(Client c) {
        initComponents();
        roomKey = 0;
        client = c;
        inputDoc = inputTextPane.getStyledDocument();
        dialogDoc = dialogTextPane.getStyledDocument();
        inputText = new String();
        dialogText = new String();              // empty string
        userList = new Vector<String>();        // get list form server!!    
        userListPanel.setListData(userList);
        whisper = false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        inputTextPane = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        dialogTextPane = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        userListPanel = new javax.swing.JList();
        sendToCombo = new javax.swing.JComboBox();
        cryButton = new javax.swing.JButton();

        inputTextPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                inputTextPaneKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputTextPaneKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(inputTextPane);

        jScrollPane2.setViewportView(dialogTextPane);

        userListPanel.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        userListPanel.setToolTipText("");
        jScrollPane3.setViewportView(userListPanel);

        sendToCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"To All"}));
        sendToCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendToComboActionPerformed(evt);
            }
        });

        cryButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/cry.png"))); // NOI18N
        cryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cryButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(sendToCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(sendToCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void inputTextPaneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputTextPaneKeyPressed
        // TODO add your handling code here:
        String s = (String) sendToCombo.getSelectedItem() ;
        if(!s.equals("To All")){ 
            whisper = true;
        }
        else{
            whisper = false;
        }
        // for test
        if(evt.getKeyChar( )== '\n'&& client.isLoggedIn){
          //  client.send(parseInputText(inputTextPane.getText()));
            inputText = inputTextPane.getText();
            refreshInputPane();
            System.out.println(inputText);
            if(!inputText.equals("") ){
                showMessage();
                client.sendRoomMsg(0,inputText);
            }            
        }
         if(!whisper){
            client.sendRoomMsg(roomKey,inputText);
        
         }else{
            String receiver = (String)sendToCombo.getSelectedItem();
            client.sendWhisper(roomKey,receiver,inputText);
        }
    }//GEN-LAST:event_inputTextPaneKeyPressed

    private void inputTextPaneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputTextPaneKeyReleased
        // TODO add your handling code here:
        if(evt.getKeyChar() == '\n' ){
            inputTextPane.setText("");
        }
    }//GEN-LAST:event_inputTextPaneKeyReleased

    private void sendToComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendToComboActionPerformed
        // TODO add your handling code here:
        // should change color of user
    }//GEN-LAST:event_sendToComboActionPerformed

    private void cryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cryButtonActionPerformed
        // TODO add your handling code here:
        inputTextPane.insertIcon(new ImageIcon("cry.png"));
        System.out.println("QQ");
    }//GEN-LAST:event_cryButtonActionPerformed
    private String parseInputText(String str){
        StringBuffer msgBuffer = new StringBuffer(""); // must create a empty buffer first
        char[] strCharArray = str.toCharArray();
        for(int i = 0; i < str.length(); ++i){ 
            msgBuffer.append(strCharArray[i]);
        }
        return msgBuffer.toString();
    }
    
    private void refreshInputPane () {
        try {
            inputDoc.insertString( inputDoc.getLength(),
                              inputTextPane.getText(),
                              null );
        }
        catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text into text pane.");
        }
        inputTextPane.setText("");
    }
    private void showMessage(){
        if(whisper){
            String receiver = (String)sendToCombo.getSelectedItem();
            dialogText = dialogText + "\n" + "( TO "+ receiver + " )"+client.username + " :" + inputText;
        }
        else{
            dialogText = dialogText + "\n" + client.username + " :" + inputText;
        }
        dialogTextPane.setText(dialogText);
    }
    public void showRecvMessage(String sender, String msg, boolean whisper_recv){
     
        if(whisper_recv){
                dialogText = dialogText + "\n" + "( FROM "+ sender+ " )" + " :" + msg;
            }
            else{
                dialogText = dialogText + "\n" + sender + " :" + msg;
            }
            dialogTextPane.setText(dialogText);
    }
    public void enterMessage(String user){
        dialogText = dialogText + "\n" +"( " + user +  " )"+" enters this room";
        dialogTextPane.setText(dialogText);
    };
    public void leaveMessage(String user){
        dialogText = dialogText + "\n" + user + " leave this room";
        dialogTextPane.setText(dialogText);
    }
    // set original user list from client
    
    // when other user enter/leave
    public void addUser(String user){
        /*
        userList.add(user);
        userListPanel.setListData(userList);    
        */
    }
    public void deleteUser(String user){
        /*
        userList.remove(user);
        userListPanel.setListData(userList);
        client.userList.remove(user);
        sendToCombo.removeItem(user);
        * */
    }
   
    public void updateUserList(Vector<String> updateList)
    {
        userList=updateList;
        userListPanel.setListData(userList);
        for(int i = 0; i < userList.size();++i){
            if(!userList.get(i).equals(client.username))
                sendToCombo.addItem(userList.get(i));
        }
    }
    public void updateUser(Vector<String> userlist){
        userList=userlist;
        userListPanel.setListData(userList);
    }
    // no room deletion in hall
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cryButton;
    private javax.swing.JTextPane dialogTextPane;
    private javax.swing.JTextPane inputTextPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JComboBox sendToCombo;
    private javax.swing.JList userListPanel;
    // End of variables declaration//GEN-END:variables
}

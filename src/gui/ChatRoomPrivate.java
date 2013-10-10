/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import chatroom.Client;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 *
 * @author eecamp
 */
public class ChatRoomPrivate extends javax.swing.JPanel {

    /**
     * Creates new form ChatRoomPrivate
     */

    private Client client;
    private StyledDocument doc;         // record properties
    private String inputText;
    private String dialogText;
    public int roomKey;
    private boolean whisper;
    private GuestListWindow inviteList;

    // guestList elements;
    private Vector<String> userList;    // all users who connect to server

    
    public ChatRoomPrivate(Client c) {
        initComponents();
        client = c;
        doc = inputTextPane.getStyledDocument();
        inputText = new String();
        dialogText = new String();              // empty string
        userList = new Vector<String>();        // get list form server!!    
        userListPanel.setListData(userList);
        inviteList = new GuestListWindow(client.getFrame(), "Select Guest");
        inviteList.setLocationRelativeTo(client.getFrame());
        inviteList.setVisible(false);
        roomKey = -1;
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

        jScrollPane2 = new javax.swing.JScrollPane();
        dialogTextPane = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        userListPanel = new javax.swing.JList();
        jScrollPane1 = new javax.swing.JScrollPane();
        inputTextPane = new javax.swing.JTextPane();
        inviteButton = new javax.swing.JButton();
        sendToCombo = new javax.swing.JComboBox();
        cryButton1 = new javax.swing.JButton();

        jScrollPane2.setViewportView(dialogTextPane);

        userListPanel.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(userListPanel);

        inputTextPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                inputTextPaneKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputTextPaneKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(inputTextPane);

        inviteButton.setText("Add Friend");
        inviteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inviteButtonActionPerformed(evt);
            }
        });

        sendToCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "To All"}));
        sendToCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendToComboActionPerformed(evt);
            }
        });

        cryButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/cry.png"))); // NOI18N
        cryButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cryButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inviteButton)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addContainerGap())
                            .addGroup(layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(cryButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendToCombo, 0, 72, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 14, Short.MAX_VALUE)
                        .addComponent(inviteButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cryButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(sendToCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(21, 21, 21))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void inputTextPaneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputTextPaneKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyChar( )== '\n'&& client.getLogState()){
            String s = (String) sendToCombo.getSelectedItem() ;
            if(!s.equals("To All") ){ 
                whisper = true;
            }
            else{
                whisper = false;
            }
            //  client.send(parseInputText(inputTextPane.getText()));
            inputText = inputTextPane.getText();
            refreshInputPane();
            System.out.println(inputText);
            if(!inputText.equals("") ){
                showMessage();
                 if(!whisper){
                    client.sendRoomMsg(roomKey,inputText);
                 }else{
                    String receiver = (String)sendToCombo.getSelectedItem();
                    client.sendWhisper(roomKey,receiver,inputText);
                }
            }

        }
    }//GEN-LAST:event_inputTextPaneKeyPressed

    private void inputTextPaneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputTextPaneKeyReleased
        // TODO add your handling code here:
        if(evt.getKeyChar() == '\n' ){
            inputTextPane.setText(null);
        }
    }//GEN-LAST:event_inputTextPaneKeyReleased

    private void sendToComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendToComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sendToComboActionPerformed

    private void inviteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inviteButtonActionPerformed
        // TODO add your handling code here:
       inviteList.setList(client.userList);
       inviteList.setTitle("Guest List");
       inviteList.setVisible(true);
       System.out.println(inviteList.getSelectedGuest());
       if(userList.contains(inviteList.getSelectedGuest())){
           JOptionPane.showMessageDialog(this,inviteList.getSelectedGuest()+" is already in this room.",
                   "Initation Error",JOptionPane.INFORMATION_MESSAGE);
       }
       else if(inviteList.getSelectedGuest().equals(""))
       {
           
       }
       else{
           
           client.sendInvitation(roomKey, inviteList.getSelectedGuest() , "");      
           inviteList.resetGuest();
       }
    }//GEN-LAST:event_inviteButtonActionPerformed

    private void cryButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cryButton1ActionPerformed
        // TODO add your handling code here:
        inputTextPane.insertIcon(new ImageIcon("cry.png"));
    }//GEN-LAST:event_cryButton1ActionPerformed

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
            doc.insertString( doc.getLength(),
                              inputTextPane.getText(),
                              null );
        }
        catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text into text pane.");
        }
        inputTextPane.setText("");
    }
    private void showMessage(){
        // test
        /*
        if(dialogText.isEmpty())    
            dialogText = inputText;
        else
            dialogText = dialogText + "\n" + inputText;
        dialogTextPane.setText(dialogText);
        System.out.println("input: "+ inputText);
        * */
        // if 
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
    }
    
    public void leaveMessage(String user){
        dialogText = dialogText + "\n" + user + " leave this room";
        dialogTextPane.setText(dialogText);
    }
    
    public void updateUserList(Vector<String> updateList){
        userList=updateList;
        userListPanel.setListData(userList);
        sendToCombo.removeAllItems();
        sendToCombo.addItem("To All");
        for(int i = 0; i < userList.size();++i){
            if(!userList.get(i).equals(client.username))
                sendToCombo.addItem(userList.get(i));
        }
    }           
    
   public void addUser(String user){
        userList.add(user);
        userListPanel.setListData(userList);
        sendToCombo.removeAllItems();
        if(!user.equals(client.username) )
            sendToCombo.addItem(user);
    }
   
    public void deleteUser(String user){
        userList.remove(user);
        userListPanel.setListData(userList);
        //client.userList.remove(user);
        sendToCombo.removeItem(user);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cryButton1;
    private javax.swing.JTextPane dialogTextPane;
    private javax.swing.JTextPane inputTextPane;
    private javax.swing.JButton inviteButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JComboBox sendToCombo;
    private javax.swing.JList userListPanel;
    // End of variables declaration//GEN-END:variables
}

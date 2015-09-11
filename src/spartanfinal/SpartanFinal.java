/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartanfinal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javafx.application.Platform;
import javafx.util.Duration;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;


/**
 *
 * @author noe
 */
public class SpartanFinal extends JFrame{
   
    
    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private JPanel contentPane;
    private JPanel panel;
    private JTextField textField;
    private JPasswordField passwordField;
    private esri_map map;
    public  dbConnect connect;
    private ImageIcon img;
    private JButton btnNewButton;
    
    public SpartanFinal()
    {
        img = new ImageIcon("global.png");
        setIconImage(img.getImage());
        connect = new dbConnect();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Spartan Login");
        
        setVisible(true);
        setResizable(false);
        
        setBounds(5, 5, 400, 267);
        setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 128, 128));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
         
        panel = new JPanel();
        panel.setBounds(57, 42, 300, 167);
        panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Login", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 255)));
        panel.setLayout(null);
        contentPane.add(panel);
        
       textField = new JTextField();
       textField.setBounds(90, 35, 192, 26);
       panel.add(textField);
       
       passwordField = new JPasswordField();
       passwordField.addKeyListener(new MyKeyListener());
       
       passwordField.setBounds(90, 72, 192, 26);
       panel.add(passwordField);
       
       JLabel lblNewLabel = new JLabel("Username:");
       lblNewLabel.setBounds(4, 41, 80, 14);
       panel.add(lblNewLabel);

       JLabel lblNewLabel_1 = new JLabel("Password:");
       lblNewLabel_1.setBounds(4, 78, 80, 14);
       panel.add(lblNewLabel_1);

       btnNewButton = new JButton("Login");
       btnNewButton.setBounds(71, 120, 89, 23);
       btnNewButton.addActionListener(new check());
       panel.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Cancel");
        btnNewButton_1.setBounds(174, 120, 89, 23);
        panel.add(btnNewButton_1);
        panel.repaint();
    }
   
    public static void main(String[] args){
        
        
        SpartanFinal spartan = new SpartanFinal();
      
    }
     private class MyKeyListener implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {
            
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == 10)
            {
                 if(connect.checkAccount(textField.getText(), passwordField.getText()))
                 {
                     map = new esri_map(img);
                     map.setLoginSuccess();
                     connect.closeConnect();
                     //dispose();//dispose of the Jframe from login... 
                     
                     //Get Credentials information... 
                    map.login.setUser(textField.getText());
                    map.login.setPass(passwordField.getText());
                    map.login.getAccountInformation();
                    map.createWindow(dim);
                    map.setEsriFrame();
                    dispose();   
                 }
                 else{
                     JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Failed, Login");
                      //success = false;
                 }
                
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            
        }
         
         
     }
    
     private class check implements ActionListener{
          public void actionPerformed(ActionEvent e)
        {
             
            if(e.getSource() == btnNewButton)
            {
              if(textField.getText().isEmpty() || passwordField.getText().isEmpty())
              {
                 JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Pleaset Fill Out Form");
              }
              else{
                  if(connect.checkAccount(textField.getText(), passwordField.getText()))
                  {
                    //JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Success, Login");
                      
                     map = new esri_map(img);
                      map.setLoginSuccess();
                    map.login.setUser(textField.getText());
                    map.login.setPass(passwordField.getText());
                    map.login.getAccountInformation();
                    map.createWindow(dim);
                     //connect.closeConnect();
                     //dispose();//dispose of the Jframe from login... 
                      
                      //Create new frame for esri map...
                     
                      //map.setGoogleFrame("test", "test");
                      map.setEsriFrame();
                      dispose();
                     
                 }
                 else{
                      JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Failed, Login");
                      //success = false;
                  }
                }
            }
        }
    }

  
    
   
    
    
}

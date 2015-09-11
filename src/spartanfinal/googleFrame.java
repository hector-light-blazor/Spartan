/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartanfinal;

import com.sun.glass.events.KeyEvent;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.*;

/**
 *
 * @author noe
 */
public class googleFrame extends JFrame{
    private WebEngine webEngine;
    private JFXPanel panel = new JFXPanel();
    private  WebView webView;
    private String google = "https://www.google.com/maps/@";
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenu mapMenu = new JMenu("Maps");
   
    public JMenuItem closeItem, basemap, satellite; //Here we add more items;
    public googleFrame(ImageIcon i)
    {
        this.setIconImage(i.getImage()); 
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        this.setTitle("Spartan Map Application");
        fileMenu.setMnemonic(KeyEvent.VK_F);
    fileMenu.getAccessibleContext().setAccessibleDescription("Dealing with Files");
    this.setJMenuBar(menuBar);
    
  
    closeItem = new JMenuItem("Close", new ImageIcon("img/close.png"));
    closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
    
    fileMenu.add(closeItem);
    
    
    basemap = new JMenuItem("BaseMap", new ImageIcon("img/map.png"));
    basemap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
    satellite = new JMenuItem("Satellite", new ImageIcon("img/satellite.png"));
    satellite.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
   
    mapMenu.add(basemap);
    mapMenu.add(satellite);
    
        
    menuBar.add(fileMenu);
    menuBar.add(mapMenu);   
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               
          
         
         add(panel);
           Platform.runLater(new Runnable() {
           @Override
           public void run(){
            
            webView = new WebView();
            webEngine = webView.getEngine();
             panel.setScene(new Scene(webView));
           
            
           }
           });
       }
   });
}
    public void changeWebView(String X, String Y)
    {
        google = google + Y + "," + X + ",15z";
        System.out.println(google);
        
         Platform.runLater(new Runnable() {
           @Override
           public void run(){
              
               //webEngine.loadContent(google);
               webEngine.load(google);
               google = "https://www.google.com/maps/@";
           }
         });
       
       
       
    
    }
   
    public void setDimension(Dimension dim)
    {
        this.setSize(dim);
    }
     //Class For Clicking..
   
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartanfinal;

import com.esri.core.runtime.LicenseResult;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.esri.map.JMap;
import com.esri.map.ArcGISTiledMapServiceLayer;
import com.esri.map.ArcGISDynamicMapServiceLayer;
import com.esri.map.LocationOnMap;
import com.esri.core.geometry.*;
import com.esri.core.io.UserCredentials;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureEditResult;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.*;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.map.ArcGISFeatureLayer;
import com.esri.map.GraphicsLayer;
import com.esri.map.MapEvent;
import com.esri.map.MapEventListenerAdapter;
import com.esri.map.MapOptions;
import com.esri.map.MapOptions.MapType;
import com.esri.map.QueryMode;
import com.esri.runtime.ArcGISRuntime;
import com.esri.toolkit.overlays.HitTestOverlay;
import com.esri.toolkit.overlays.NavigatorOverlay;
import com.esri.map.MapOverlay;
import com.esri.toolkit.overlays.HitTestEvent;
import com.esri.toolkit.overlays.HitTestListener;

import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.application.Platform;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;



import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;


public class esri_map {
    public Login login;
    private boolean run_once = true;
    private Dimension dim;
    private Point old;
    public JMap map;
    private MapOptions mapOptions;
    public googleFrame googleF;
    private static SpatialReference wgs84 = SpatialReference.create(4326);
    private JFrame window;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenu mapMenu = new JMenu("Maps");
    private JMenu toolsMenu = new JMenu("Tools");
    private JMenu profileMenu = new JMenu("Profile");
    private JMenuItem newItem, closeItem, basemap, satellite, googlem, addShapeFile,
    identifyFeature, moveSpartan, changeProfile, newProfile; //Here we add more items;
    private ImageIcon img;
   
    private boolean runonce = true;
    private ArcGISTiledMapServiceLayer TileLayer;
    private ArcGISTiledMapServiceLayer SatLayer;
    public ArcGISFeatureLayer spartanLayer;
    public ArcGISFeatureLayer flagsLayer;
    private Symbol special;
    private ArcGISDynamicMapServiceLayer StreetLayer;
    private JPanel contentPane;
    private int [] layer = {1,2,3};
    private int layer_loc = 0;
    private int getId;
    private int count;
    private int count2;
    private boolean moveOn = false;
    private boolean newMoveOn = false;
    private boolean readyMove  = false;
    private boolean readyMove2 = false;
    private boolean tablelist = false;
    private boolean tablelist2 = false;
    private boolean tablelist3 = false;
    private boolean tablelist4 = false;
    private int moveNum = 0;
    private String objectid = null;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();
   // private Image image = toolkit.getImage("img/cursor_hand.png");
    private Image identifyImage = toolkit.getImage("img/identify_cursor.png");
    private Cursor c;
    private Cursor id;
    private Point p;
    private newUser addP;
    private updateUser updateP;
    private newTicket ticket = null;
    private QueryParameters query = new QueryParameters();
    private DownloadInformation download;
    private GraphicsLayer temporary;
    JTextField findText = new HintTextField("Search Address");
    private Timer timer;
    private Timer t;
    private Timer subTimer;
    private Timer teamTimer;
    private Timer flagsTimer;
    private parcelSearch parcelsearch = new parcelSearch();
    private parcelSearch subSearh = new parcelSearch();
    private geocoderSearch geocodeS = new geocoderSearch();
    private ticketSearch ticketS = new ticketSearch();
    private searchDirectory directoryS = new searchDirectory();
    private AddressingForm addressing = new AddressingForm(); //Cool Stuff for each department.
    private boolean ticketopen = false;
    private tableList list;
    private tableList ownerList;
    private tableList directorySearch;
    private tableList subParcel;
    
    //URL STATIC FINAL PRIVATE
    //1.) streets, ranges, points....
    //2.) Basemap primary...
    //3.) Satellite View...
    //4.) Spartan Layer...
    //5.) Picture Flags...
    private final static String dynamic_url = 
            "http://gis.lrgvdc911.org/arcgis/rest/services/Dynamic/dynammic_layers_important/MapServer";
    private final static String basemap_url = 
            "http://gis.lrgvdc911.org/arcgis/rest/services/basemap/basemap_new_cache_once/MapServer";
    private final static String sat_url =
            "http://psap.lrgvdc911.org/arcgis/rest/services/Imagery/MapServer";
    private final static String spartan_url =
            "http://gis.lrgvdc911.org/arcgis/rest/services/Dynamic/spartan_dynamic_feature/FeatureServer/0";
    private final static String flags = 
            "http://gis.lrgvdc911.org/arcgis/rest/services/identify/cam_final_features/FeatureServer/0";
    public esri_map(ImageIcon i)
    {
        img = i;
        
        findText.setSize(2000, 10);
        
        login = new Login();
        googleF = new googleFrame(img);
        googleF.basemap.addActionListener(new actionClick());
        googleF.closeItem.addActionListener(new actionClick());
        googleF.satellite.addActionListener(new actionClick());
        
       setLicense();
    }
    public void setDefinition()
    {
      if(login.getRole().equalsIgnoreCase("GIS"))
      {
          login.setRole("MAPPING");
          spartanLayer.setDefinitionExpression("status = '" + login.getRole() + "' and region = '" + login.getRegion() + "' or status = 'HOLD'");
      }else{
          spartanLayer.setDefinitionExpression("status = '" + login.getRole() + "' or status = 'HOLD'");
          
      }
          
     
       
    }
    public void setLoginSuccess()
            {
                 Platform.runLater(new Runnable() {
            @Override
            public void run() {
                TrayNotification tray = new TrayNotification(); 
                 tray.setMessage("Login was a success");
        tray.setTitle("Welcome to Spartan Database");
        tray.setNotificationType(NotificationType.SUCCESS);
        tray.setAnimationType(AnimationType.POPUP);
       
            tray.showAndDismiss(Duration.seconds(2));
            }
       });
                
            }
  
    public JComponent createUI() throws Exception {

    //Application north search..
        
      JLabel findLabel = new JLabel("Find ");
    findLabel.setForeground(Color.WHITE);
    findLabel.setFont(new Font(findLabel.getFont().getName(), findLabel
        .getFont().getStyle(), 14));
    findText.setFont(new Font(findLabel.getFont().getName(), findLabel
        .getFont().getStyle(), 14));
    findText.setMinimumSize(new Dimension(150, 25));
    findText.setMaximumSize(new Dimension(150, 25));
    findText.setColumns(10);
    
    JButton findButton = new JButton("Find");
    findButton.setFont(new Font(findLabel.getFont().getName(), findLabel
        .getFont().getStyle(), 14));
     JPanel topPanel = new JPanel();
    topPanel.setBackground(Color.decode("#0CA3D2"));
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
    topPanel.add(Box.createHorizontalGlue());
  // topPanel.add(findLabel);
   // topPanel.add(findText);
 
  // topPanel.add(findButton);
   //topPanel.add(Box.createHorizontalGlue());
        
    // application content
    contentPane = new JPanel(new BorderLayout());
    contentPane.add(topPanel, BorderLayout.NORTH);
    // map options: topographic map, centered at lat-lon 41.9, 12.5 (Rome), zoom level 12
    mapOptions = new MapOptions(MapType.TOPO, 26.3825161, -98.756449, 8);
    //Create Arcgis Dynamic Service
    TileLayer = new ArcGISTiledMapServiceLayer(
        basemap_url);
    SatLayer = new ArcGISTiledMapServiceLayer(sat_url);
    
    StreetLayer = new ArcGISDynamicMapServiceLayer(dynamic_url);
    final UserCredentials credentials = new UserCredentials();
    
    credentials.setUserAccount("hchapa", "cangri1989");
    spartanLayer = new ArcGISFeatureLayer(spartan_url, credentials);
    spartanLayer.setOperationMode(QueryMode.ON_DEMAND);
    flagsLayer = new ArcGISFeatureLayer(flags, credentials);
    flagsLayer.setOperationMode(QueryMode.ON_DEMAND);
    //Set Query Definitaion on Spartan Layer..
    //Called function....
    setDefinition();
    // create the map using MapOptions
    map = new JMap(mapOptions);
    temporary = new GraphicsLayer();
    
    map.addMapOverlay(new MouseOverLay(map));
    map.getLayers().clear();
   // map.zoomTo(26.3825161, -97.9756449, 40);
    
    map.getLayers().add(TileLayer);
    map.getLayers().add(StreetLayer);
    map.getLayers().add(spartanLayer);
    map.getLayers().add(flagsLayer);
    map.getLayers().add(temporary);
    
    
    contentPane.add(map, BorderLayout.CENTER);

    // add marker graphics directly to the map
  

    
    NavigatorOverlay navigatorOverlay = new NavigatorOverlay();
    navigatorOverlay.setLocation(LocationOnMap.TOP_LEFT);
    map.addMapOverlay(navigatorOverlay);
    map.addMapEventListener(new MapEventListenerAdapter(){
        @Override
        public void mapExtentChanged(MapEvent arg0)
        {
            if(login.getRole().equalsIgnoreCase("addressing"))
            {
                
            }
        }
    });
    addHitTestOverlay(map, spartanLayer);
    addHitTestOverlay2(map, flagsLayer);
    // option to use a custom marker image by creating a BufferedImage
   

    return contentPane;
  }
  
   public void setLicense()
   {
      
       LicenseResult lice =  ArcGISRuntime.setClientID("DUrBj4n7d8YXh4GM");
       //ArcGISRuntime.License.setLicense("DUrBj4n7d8YXh4GM");
   }
   
   public void createWindow(Dimension d) {
    window = new JFrame("Spartan Map Application");
   p = new Point(window.getX(), window.getY());
  
    
   // c = toolkit.createCustomCursor(image,p.getPoint(), "test");
   
   id = toolkit.createCustomCursor(identifyImage,window.getLocation(),"Identify");
    // window.setCursor(c);
    window.setExtendedState(JFrame.MAXIMIZED_BOTH);
    window.setIconImage(img.getImage());
    
    dim = d;
    window.setSize(dim);
    googleF.setSize(dim);
    
    //Setting Menu...
    fileMenu.setMnemonic(KeyEvent.VK_F);
    fileMenu.getAccessibleContext().setAccessibleDescription("Dealing with Files");
    window. setJMenuBar(menuBar);
    newItem = new JMenuItem("New Ticket", new ImageIcon("img/document.png"));
    newItem.setMnemonic(KeyEvent.VK_P);
    newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
    newItem.addActionListener(new actionClick());
    fileMenu.add(newItem);
    closeItem = new JMenuItem("Exit", new ImageIcon("img/close.png"));
    closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
    closeItem.addActionListener(new actionClick());
    fileMenu.add(closeItem);
    
    mapMenu.setMnemonic(KeyEvent.VK_M);
    basemap = new JMenuItem("BaseMap", new ImageIcon("img/map.png"));
    basemap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
    basemap.addActionListener(new actionClick());
    satellite = new JMenuItem("Satellite", new ImageIcon("img/satellite.png"));
    satellite.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
    satellite.addActionListener(new actionClick());
    googlem = new JMenuItem("Google Map", new ImageIcon("img/google.png"));
    googlem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
    googlem.addActionListener(new actionClick());
    mapMenu.add(basemap);
    mapMenu.add(satellite);
    mapMenu.add(googlem);
    moveSpartan = new JMenuItem("Move Graphic", new ImageIcon("img/arrow_move2.png"));
    moveSpartan.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
    moveSpartan.addActionListener(new actionClick());
    toolsMenu.add(moveSpartan);
    //More tools menu..
    identifyFeature = new JMenuItem("Identify", new ImageIcon("img/identify_display.png"));
    identifyFeature.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
    identifyFeature.addActionListener(new actionClick());
    toolsMenu.add(identifyFeature);
    profileMenu.setMnemonic(KeyEvent.VK_P);
    changeProfile = new JMenuItem("Update Profile", new ImageIcon("img/Add_User_24.png"));
    changeProfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
    changeProfile.addActionListener(new actionClick());
    profileMenu.add(changeProfile);
    if(login.getRights().equals("YES"))
    {
        newProfile = new JMenuItem("New Profile", new ImageIcon("img/Add_User_24.png"));
        newProfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        newProfile.addActionListener(new actionClick());
        profileMenu.add(newProfile);
    }
    toolsMenu.setMnemonic(KeyEvent.VK_T);
    
    //Adding Menu to the MenuBar..
    
    menuBar.add(fileMenu);
    menuBar.add(mapMenu);
    menuBar.add(profileMenu);
    menuBar.add(toolsMenu);
    menuBar.add(new JSeparator());
    menuBar.add(findText);
   
    window.setLocationRelativeTo(null);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.getContentPane().setLayout(new BorderLayout());
    window.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent windowEvent) {
        super.windowClosing(windowEvent);
        try{
             map.dispose();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
       
      }
    });
    
  }
  public void setEsriFrame()
  {
     
     try{
         window.add(createUI());
         window.setVisible(true);
         parcelsearch.setFeatureLayer(spartanLayer);
         timer = new Timer(200, new actionClick());
         timer.setRepeats(false);
         timer.start();
         //Open what department form
         if(login.getRole().equalsIgnoreCase("addressing"))
         {
             count = 0;
             this.addressing.setVisible(true);
             this.addressing.setMessage("Please wait.... Setting Some Stuff");
             this.addressing.runQuery();
             this.addressing.addClick(new actionClick());
             this.addressing.addTableClick(new table());
             this.addressing.addFocusListener(new focusList());
             t = new Timer(3000, new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            if(download.getFound() && download.getFound2())
                            {
                                count++;
                                if(count == 1)
                                {
                                   
                                    addressing.setRegion(download.setRegionArray());
                                    addressing.setMsag(download.setMsagArray());
                                    count = 0;
                                }
                            }
                            if(addressing.ready())
                            {
                                t.stop();
                                count++;
                                if(count == 1)
                                {
                                   
                                    proccessBuffer();
                                }
                                
                            }
                        }
                    });
              t.start();
          }    
     }catch(Exception t)
     {
            t.printStackTrace();
     }  
      
   }
  public void proccessBuffer(){
      zoomToCreateBuffer();
  }
  
   public void setMap(int i)
   {
       map.getLayers().clear();
       if(layer[i] != layer[layer_loc])
         {
             layer_loc = i;
             
             if(layer[i] == 1)
             {
                
                 map.getLayers().add(TileLayer);
                 map.getLayers().add(StreetLayer);
                 map.getLayers().add(spartanLayer);
                 map.getLayers().add(flagsLayer);
                 map.getLayers().add(temporary);
                 
                 spartanLayer.requery();
             }
             else if(layer[i] == 2)
             {
                 
                 map.getLayers().add(SatLayer);
                 map.getLayers().add(StreetLayer);
                 map.getLayers().add(spartanLayer);
                 map.getLayers().add(flagsLayer);
                 map.getLayers().add(temporary);
                 spartanLayer.requery();
             }
             else{
                 old = map.getExtent().getCenter();
                 
                 old = (Point) GeometryEngine.project(old, map.getSpatialReference(), wgs84);
                 //if(map.getResolution())
                 window.setVisible(false);
                 googleF.setVisible(true);
                 googleF.changeWebView(String.valueOf(old.getX()), String.valueOf(old.getY()));
             }
             
         }else
         {
             
             System.out.println("There are the same");
         }
                 
         
   }
   public void updateInfomation()
   {
       
       moveOn = false;
       moveNum = 0;
       Graphic up = spartanLayer.getGraphic(getId);
       Point p = (Point) GeometryEngine.project(spartanLayer.getGraphic(getId).getGeometry(), map.getSpatialReference(), wgs84);
       Map<String, Object> attributes = up.getAttributes();
       attributes.put("longy", p.getX());
       attributes.put("lat", p.getY());
       attributes.put("msag_comm", download.checkMsagContains(spartanLayer.getGraphic(getId).getGeometry()));
       attributes.put("exch", download.checkExchContains(spartanLayer.getGraphic(getId).getGeometry()));
       attributes.put("esn", download.checkEsnContains(spartanLayer.getGraphic(getId).getGeometry()));
       attributes.put("region", download.checkRegionContains(spartanLayer.getGraphic(getId).getGeometry()));
       if(!ticketopen)
       {
            System.out.println("I ran1");
           parcelsearch.setObjectId(attributes.get("OBJECTID").toString());
            parcelsearch.searchGeometry(spartanLayer.getGraphic(getId).getGeometry());
       }
       else if(ticket.checkObjectid(objectid))
       {
            System.out.println("I ran2");
            
           ticket.runGeometry(spartanLayer.getGraphic(getId).getGeometry());
       }
       spartanLayer.updateGraphic(getId, attributes);
       Graphic[] update = {spartanLayer.getGraphic(getId)};
       spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{},update , new CallbackListener<FeatureEditResult[][]>(){

    @Override
    public void onCallback(FeatureEditResult[][] result) {
      // do something with the feature edit result object
        
       for(int i = 0; i < result.length; i++)
       {
           //System.out.println("I ran what " + i);
           FeatureEditResult []t = result[i];
          // System.out.println("T length " + t.length);
           for(int y = 0; y < t.length; y++)
           {
              // System.out.println("I ran something testing this out " + y);
               //System.out.println("Global Id " + t[y].getObjectId());
                       
           }
           
           
       }
     
    }

        @Override
         public void onError(Throwable e) {
         // handle the error
        }								
     });
   }
    public void updateInfomation2()
   {
       
       getId = findId();
       Graphic[] update = {spartanLayer.getGraphic(getId)};
       spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{},update , new CallbackListener<FeatureEditResult[][]>(){

    @Override
    public void onCallback(FeatureEditResult[][] result) {
      // do something with the feature edit result object
        objectid = null;
       for(int i = 0; i < result.length; i++)
       {
          // System.out.println("I ran what " + i);
           FeatureEditResult []t = result[i];
          // System.out.println("T length " + t.length);
           for(int y = 0; y < t.length; y++)
           {
              // System.out.println("I ran something testing this out " + y);
              // System.out.println("Global Id " + t[y].getObjectId());
                       
           }
           
           
       }
     
    }

        @Override
         public void onError(Throwable e) {
         // handle the error
        }								
     });
   }
   public void insertNewTicket()
   {
       //Create new ticket submit to the database...
       
       Point p = (Point)GeometryEngine.project(map.getExtent().getCenter(), map.getSpatialReference(), SpatialReference.create(4326));
       DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
       Calendar now = Calendar.getInstance();
      // System.out.println("Calendar Month: " + dateFormat.format(now.getTime()));
      
       Map<String, Object> attributes = new HashMap<String, Object>();
       attributes.put("started_ticket", login.getInit());
       attributes.put("created_date",dateFormat.format(now.getTime()));
       attributes.put("state", "TX");
       attributes.put("status", "HOLD");
       attributes.put("longy", p.getX());
       attributes.put("lat", p.getY());
       PictureMarkerSymbol symbol = null;
       File f = new File("img/user_location_32.png");
       try{
           BufferedImage symbolI = ImageIO.read(f);
            symbol = new PictureMarkerSymbol(symbolI);
       }
       catch(Exception t)
       {
           
       }
      
       attributes.put("msag_comm", download.checkMsagContains(map.getExtent().getCenter()));
       attributes.put("exch", download.checkExchContains(map.getExtent().getCenter()));
       attributes.put("esn", download.checkEsnContains(map.getExtent().getCenter()));
       attributes.put("region", download.checkRegionContains(map.getExtent().getCenter()));
      
       Graphic insert = new Graphic(map.getExtent().getCenter(), symbol, attributes);
       Graphic [] add = {insert};
       
       spartanLayer.applyEdits(add, new Graphic[]{},new Graphic[]{} , new CallbackListener<FeatureEditResult[][]>(){

    @Override
    public void onCallback(FeatureEditResult[][] result) {
      // do something with the feature edit result object
        
         if(ticket == null || !ticket.isDisplayable())
         {
             try{    
             ticket = new newTicket(img, String.valueOf(result[0][0].getObjectId()), spartanLayer);
             ticketopen = true;
             ticket.packOnce();
             ticket.mainT.addActionListener(new actionClick());
             ticket.alt1.addActionListener(new actionClick());
             ticket.save.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK), "Save");
             ticket.save.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK), "Save");
             ticket.save.getActionMap().put("Save", new abClick());
             ticket.save.addActionListener(new actionClick());
             ticket.userF.addActionListener(new actionClick());
             ticket.sub.addActionListener(new actionClick());
             ticket.zoom.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK), "Zoom");
             ticket.zoom.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK), "Zoom");
             ticket.zoom.getActionMap().put("Zoom", new abClick());
             ticket.zoom.addActionListener(new actionClick());
             ticket.delete.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK), "Delete");
             ticket.delete.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK), "Delete");
             ticket.delete.getActionMap().put("Delete", new abClick());
             ticket.delete.addActionListener(new actionClick());
             ticket.next.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK), "New");
             ticket.next.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK), "New");
             ticket.next.getActionMap().put("New", new abClick());
             ticket.next.addActionListener(new actionClick());
             ticket.previous.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK), "Prev");
             ticket.previous.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK), "Prev");
             ticket.previous.getActionMap().put("Prev", new abClick());
             ticket.previous.addActionListener(new actionClick());
             ticket.GraphicsId = ticket.layer.addGraphic(insert);
             ticket.position = ticket.list.size() - 1;
             ticket.list.get(ticket.position).setPosition(map.getExtent().getCenter());
             ticket.list.get(ticket.position).setGeometry(insert.getGeometry());
             ticket.list.get(ticket.position).setSymbol(insert.getSymbol());
             setUpFocus();
             map.getLayers().add(ticket.layer);
             ticket.setVisible(true);
             ticket.setUpTicket();
             ticket.addWindowListener(new WindowAdapter(){
                 @Override
                 public void windowClosed(WindowEvent e)
                 {
                     super.windowClosed(e);
                     ticketopen = false;
                     map.getLayers().remove(ticket.layer);
                    
                 }
             });
             
             ticket.runGeometry(map.getExtent().getCenter());
             spartanLayer.refresh();
             spartanLayer.requery();
             }catch(Exception e)
             {
                 
             }
         }
        
        }

        @Override
         public void onError(Throwable e) {
         // handle the error
        }								
     }); 
       
   }
   public void processTable()
   {
       //Get Results from teamTimer...
       //Add the data to the table...
       if(login.getRole().equalsIgnoreCase("addressing"))
       {
            addressing.setRelatedRest(ticketS.getResults());
            addressing.startAddingDataRelated();
            //Start adding the symbols in the map..
            //set the symbol information before loop starts
            SimpleMarkerSymbol marker = new SimpleMarkerSymbol(new Color(116, 209, 234), 10, SimpleMarkerSymbol.Style.SQUARE);
            addressing.clearGraphicId();
            for(int i = 0; i < addressing.getLengthRelated(); i++)
            {
                addressing.setIdGraphicRelated(temporary.addGraphic(new Graphic(addressing.getRelatedGeometry(i), marker)));
                
            }
       }
      
       
       
   }
   public void zoomToCreateBuffer()
   {
       if(login.getRole().equalsIgnoreCase("addressing"))
       {
                Polygon p = GeometryEngine.buffer(addressing.getGeometry(), map.getSpatialReference(), 500, map.getSpatialReference().getUnit());
                temporary.removeAll();
                SimpleLineSymbol outline = new SimpleLineSymbol(Color.BLACK, 2, SimpleLineSymbol.Style.SOLID);
                         SimpleFillSymbol symbol = new SimpleFillSymbol(new Color(160, 32, 240, 50), outline);
                addressing.setGraphicId(temporary.addGraphic(new Graphic(p, symbol)));
                map.zoomTo(p);
                ticketS.queryGeometry(p);
                ticketS.setFoundTicket(false);
                addressing.destroyAttachments();
                addressing.getAttachments();
              
                teamTimer = new Timer(800, new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            if(ticketS.getFoundTicket())
                            {
                                System.out.println("found gracious data hear....");
                                System.out.println("Check out my inventory" + ticketS.getResults().featureCount());
                               
                                processTable();
                                ticketS.setFoundTicket(false);
                                parcelsearch.searchGeometry2(addressing.getGeometry());
                            }
                            if(parcelsearch.getFoundParcel())
                            {
                                teamTimer.stop();
                                addressing.getParcelModel().setRowCount(0);
                                addressing.getParcelModel().addRow(new Object[]{parcelsearch.getParcelData().get(0).getFull(),
                                parcelsearch.getParcelData().get(0).getTax(), parcelsearch.getParcelData().get(0).getProp(),
                                parcelsearch.getParcelData().get(0).getLegal(), parcelsearch.getParcelData().get(0).getSitus()});
                               
                                addressing.getSubModel().setRowCount(0);
                                addressing.searchDirectorySub(parcelsearch.getParcelData().get(0).getLegal());
                                
                              
                            }
                        }
                 });
                teamTimer.start();
           
       }
   }
   public void zoomTOFullExtent()
   {
       map.zoomTo(map.getFullExtent());
   }
    public void insertNextTicket()
   {
       //Create new ticket submit to the database...
       //
       System.out.println(ticket.position);
       System.out.println(ticket.list.size());
        Point p = (Point)GeometryEngine.project(map.getExtent().getCenter(), map.getSpatialReference(), SpatialReference.create(4326));
       DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
       Calendar now = Calendar.getInstance();
       //System.out.println("Calendar Month: " + dateFormat.format(now.getTime()));
       Map<String, Object> attributes = new HashMap<String, Object>();
       attributes.put("started_ticket", login.getInit());
       attributes.put("created_date",dateFormat.format(now.getTime()));
       attributes.put("state", "TX");
       attributes.put("status", "HOLD");
       attributes.put("msag_comm", download.checkMsagContains(map.getExtent().getCenter()));
       attributes.put("exch", download.checkExchContains(map.getExtent().getCenter()));
       attributes.put("esn", download.checkEsnContains(map.getExtent().getCenter()));
       attributes.put("region", download.checkRegionContains(map.getExtent().getCenter()));
        attributes.put("longy", p.getX());
       attributes.put("lat", p.getY());
        
       PictureMarkerSymbol symbol = null;
       File f = new File("img/user_location_32.png");
       try{
           BufferedImage symbolI = ImageIO.read(f);
            symbol = new PictureMarkerSymbol(symbolI);
       }
       catch(Exception t)
       {
           
       }
      
       Graphic insert = new Graphic(map.getExtent().getCenter(), symbol, attributes);
       Graphic [] add = {insert};
       System.out.println(insert.getAttributes().toString());
       System.out.println("I ran here waht happend.");
       spartanLayer.applyEdits(add, new Graphic[]{},new Graphic[]{} , new CallbackListener<FeatureEditResult[][]>(){

    @Override
    public void onCallback(FeatureEditResult[][] result) {
      // do something with the feature edit result object
         try{
        
                       
             
             //System.out.println("ID is " + result[0][0].getObjectId() + " positino is " + ticket.position);
             System.out.println("I ran here as well ");
             ticket.setLayer(spartanLayer); //Reset layer with new information...
             ticket.setIdText(String.valueOf(result[0][0].getObjectId()), insert);
             ticket.refreshForm();
             ticket.runGeometry(map.getExtent().getCenter());
            // System.out.println("new position " + ticket.position);
        }catch(Exception t)
         {
         
             
         }
        }

        @Override
         public void onError(Throwable e) {
         // handle the error
        }								
     }); 
       
   }
     public int findId()//This function will loop until find the id matching that object id..
 {
    
     int temp = 0;
     if(ticket.list.size() > 0)
     { 
        
         System.out.println("size of the list is " + ticket.list.size());
        for(int i = 0; i < spartanLayer.getGraphicIDs().length; i++)
         {
            temp = spartanLayer.getGraphicIDs()[i];
            System.out.println("CURRENT ID CHECKING IS " + temp);
            System.out.print("OBJECTID IS " + ticket.list.get(ticket.position).getId());
         if(spartanLayer.getGraphic(temp).getAttributeValue("OBJECTID").toString().equalsIgnoreCase(ticket.list.get(ticket.position).getId()))
         {
             System.out.println("I found something");
             return temp;
         }
             
         
         }
     }
     return 0;
 }
     public int findId2()
     {
         int temp = 0;
     
        
       try{
           for(int i = 0; i < spartanLayer.getGraphicIDs().length; i++)
         {
         temp = spartanLayer.getGraphicIDs()[i];
        
         if(spartanLayer.getGraphic(temp).getAttributeValue("OBJECTID").toString().equalsIgnoreCase(objectid))
             return temp;
         }
       }catch(Exception e)
       {
           
       }
     return 0;
     }
    
     public void changeLocation(Point p)
     {
           Envelope extent = map.getExtent();
            extent.centerAt(p);
            map.zoomTo(extent);
            updateInfomation();
            
     }
     public void deleteRecordN()
     {
         Graphic up = null; 
         Map<String, Object> attributes = new HashMap<String, Object>();
         if(login.getRole().equalsIgnoreCase("addressing"))
         {
              attributes.put("OBJECTID", Integer.parseInt(addressing.getId()));
              up = new Graphic(addressing.getGeometry(), null, attributes);
         }
        
          spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{up}, new Graphic[]{} , new CallbackListener<FeatureEditResult[][]>(){
            @Override
            public void onCallback(FeatureEditResult[][] result) {
              // do something with the feature edit result object
               if(result[1] != null && result[1][0] != null && result[1][0].isSuccess())
               {
                    JOptionPane.showMessageDialog(null, "Success Delete Record ");
                    
                    addressing.removeArraylist();
                    addressing.removeRecordTable();
                   
                    if(addressing.getPosition() > addressing.getPositionLength())
                    {
                        addressing.subPosition();
                    }
                     addressing.setMessageText();
                    addressing.moveTable();
                    addressing.setTablePosition();
               }

            }

                @Override
                 public void onError(Throwable e) {
                 // handle the error
                      JOptionPane.showMessageDialog(null, "Sorry, No more Records To delete");
                }								
             });
   
    }
     public void saveRecord()
  {
      ticket.startProgressBar();
      Graphic [] up = ticket.getInfo();
      
     spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{}, up, new CallbackListener<FeatureEditResult[][]>(){

    @Override
    public void onCallback(FeatureEditResult[][] result) {
      // do something with the feature edit result object
        
             if(result[2] != null && result[2][0] != null && result[2][0].isSuccess())
             {
                 JOptionPane.showMessageDialog(null, "Saved Record");   
                ticket.stopProgressBar();            
             }
            
        }

        @Override
         public void onError(Throwable e) {
         // handle the error
              JOptionPane.showMessageDialog(null, e.getMessage());   
        }								
     });   
     }
     public void deleteRecord()
    {
        int temp = findId();
        System.out.println(temp);
        System.out.println(ticket.list.get(ticket.position).getId());
        Graphic up = null;
     if(temp != 0)
     {
       System.out.println(spartanLayer.getCapabilities());
       
       System.out.println("I got id " + temp);
        up = spartanLayer.getGraphic(temp);
        System.out.println(up.getAttributes().toString());
       
     }else{
         Map<String, Object> attributes = new HashMap<String, Object>();
         attributes.put("OBJECTID", Integer.parseInt(ticket.list.get(ticket.position).getId()));
         up = new Graphic(ticket.list.get(ticket.position).getGeometry(), null, attributes);
     }
         spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{up}, new Graphic[]{} , new CallbackListener<FeatureEditResult[][]>(){
    @Override
    public void onCallback(FeatureEditResult[][] result) {
      // do something with the feature edit result object
       if(result[1] != null && result[1][0] != null && result[1][0].isSuccess())
       {
            JOptionPane.showMessageDialog(null, "Success Delete Record " + ticket.list.get(ticket.position).getId());
            //update list...
           ticket.updateList();
            //System.out.println("Lenth of list " + ticket.list.size());
            ticket.subTrackPosition();
           // System.out.println("New position is " + ticket.position);
            //refresh Form..
            ticket.removeLayer();
            ticket.refreshForm();
           // System.out.println("I ran second");
            map.panTo(ticket.list.get(ticket.position).getGeometry()); 
       }
     
    }

        @Override
         public void onError(Throwable e) {
         // handle the error
              JOptionPane.showMessageDialog(null, "Sorry, No more Records To delete");
        }								
     });
   
    }
      private void addHitTestOverlay2(final JMap jMap, final ArcGISFeatureLayer flags) { //For Regular tickets not new...

    // create a hit test overlay for the feature layer
        
    final HitTestOverlay hitTestOverlay = new HitTestOverlay(flags);
   
    // add a listener to perform some action when a feature was hit
    hitTestOverlay.addHitTestListener(new HitTestListener() {

     
      @Override
      public void featureHit(HitTestEvent event) {
          
          if(login.getRole().equalsIgnoreCase("addressing"))
          {
                flagsLayer.clearSelection();
                flagsLayer.setSelectionColor(Color.yellow);
                flagsLayer.select((int)hitTestOverlay.getHitFeatures().get(0).getId());
                addressing.getPicturesModel().setNumRows(0);
                addressing.setTabPane(6);
                for(int i = 0; i < hitTestOverlay.getHitFeatures().size(); i++)
                {
                    addressing.getPicturesModel().addRow(new Object[]{hitTestOverlay.getHitFeatures().get(i).getAttributeValue("FilePath")});
                }
          }
      }
      });
     map.addMapOverlay(hitTestOverlay);
     }
    
    private void addHitTestOverlay(final JMap jMap, final ArcGISFeatureLayer spartan) { //For Regular tickets not new...

    // create a hit test overlay for the feature layer
        
    final HitTestOverlay hitTestOverlay = new HitTestOverlay(spartan);
   
    // add a listener to perform some action when a feature was hit
    hitTestOverlay.addHitTestListener(new HitTestListener() {

     
      @Override
      public void featureHit(HitTestEvent event) {
        // reset the feature layer to remove previously selected cities
        spartan.clearSelection();
        if(readyMove)
        {
            Feature t = hitTestOverlay.getHitFeatures().get(0);
           
           objectid = t.getAttributeValue("OBJECTID").toString();
           //System.out.println("Object id is " + objectid);
           getId = (int) t.getId();
           
           moveOn = true;
       }
       else{
            if(login.getRole().equalsIgnoreCase("addressing"))
            {
                Feature t = hitTestOverlay.getHitFeatures().get(0);
                System.out.println(map.getSpatialReference().getUnit());
                Polygon p = GeometryEngine.buffer(t.getGeometry(), map.getSpatialReference(), 500, map.getSpatialReference().getUnit());
                temporary.removeAll();
                SimpleLineSymbol outline = new SimpleLineSymbol(Color.BLACK, 3, SimpleLineSymbol.Style.SOLID);
                         SimpleFillSymbol symbol = new SimpleFillSymbol(new Color(160, 32, 240, 50), outline);
                temporary.addGraphic(new Graphic(p, symbol));
                map.zoomTo(p);
            }
        }
      }
      
    });
    map.addMapOverlay(hitTestOverlay);
    
   }
 
    
   //Class For Clicking..
    public class actionClick implements ActionListener{ //Click Button

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if(login.getRole().equalsIgnoreCase("addressing"))
            {
                    if(e.getSource() == addressing.getButtonPrev())
                    {
                            if(addressing.getPosition() == 0)
                            {

                                addressing.endList();
                                addressing.setTablePosition();
                                addressing.scrollTable();
                                addressing.setMessageText();
                                addressing.updateId();
                                zoomToCreateBuffer();
                                
                            }else{

                                addressing.subPosition();
                                addressing.setTablePosition();
                                addressing.scrollTable();
                                addressing.setMessageText();
                                addressing.updateId();
                                zoomToCreateBuffer();
                                
                            }
                            return;
                    }
                    
                    else if(e.getSource() == addressing.getButtonNext())
                    {
                            if(addressing.getPosition() == addressing.getPositionLength())
                            {
                                addressing.begList();
                                addressing.setTablePosition();
                                addressing.scrollTable();
                                addressing.setMessageText();
                                addressing.updateId();
                                zoomToCreateBuffer();
                            }
                            else{
                                  addressing.addPosition();
                                  addressing.setTablePosition();
                                  addressing.scrollTable();
                                  addressing.setMessageText();
                                  addressing.updateId();
                                 zoomToCreateBuffer();
                            }
                            return;
           
                    }
                    else if(e.getSource() == addressing.getButtonDel())
                    {
                      System.out.println("I ran delete button yay");
                        deleteRecordN();
                        return;
                    }
                    else if(e.getSource() == addressing.getButtonZoom())
                    {
                        zoomToCreateBuffer();
                        return;
                    }
                    else if(e.getSource() == addressing.getButtonAttach())
                    {
                        JFileChooser fileChooser = new javax.swing.JFileChooser();
                        int returnValue = fileChooser.showOpenDialog(null);
                        if(returnValue == JFileChooser.APPROVE_OPTION)
                        {
                            try
                            {
                               final long startTime = System.currentTimeMillis();
                            ProcessFiles files = new ProcessFiles(fileChooser.getSelectedFile());
                            dbConnect con = new dbConnect();
                           
                            int num = con.insertAttachments(addressing.getId(), files);
                           
                            final long endTime = System.currentTimeMillis();
                           
                           // Vector newRow = new Vector();
                            if(num > 0)
                                addressing.getAttachModel().addRow(new Object[]{con.getFiles().getId(), con.getFiles().getName(), con.getFiles().getType(), con.getFiles().getImage()});
                            else
                                JOptionPane.showMessageDialog(null, "Sorry can't upload to big of a file even with compression");
                            //jTable1.addR
                            }catch(Exception t)
                            {

                                 JOptionPane.showMessageDialog(null, t);   
                            }
                        }


                        return;
                     }
                    else if(e.getSource() == addressing.getButtonSave())
                    {
                        String full_address = addressing.getJTextFieldNum().getText();
                        full_address += addressing.getJComboxPre().getSelectedItem().toString().equalsIgnoreCase("?") ? "" : " " + addressing.getJComboxPre().getSelectedItem().toString();
                        full_address += addressing.getJTextFieldStreet().getText().equalsIgnoreCase("?") ? "" : " " + addressing.getJTextFieldStreet().getText();
                        full_address += addressing.getJComboxType().getSelectedItem().toString().equalsIgnoreCase("?") ? "" : " " + addressing.getJComboxType().getSelectedItem().toString();
                        full_address += addressing.getJComboxPost().getSelectedItem().toString().equalsIgnoreCase("?") ? "" : " " + addressing.getJComboxPost().getSelectedItem().toString();
                        
                        System.out.println(full_address);
                        
                        Map<String, Object> attributes = new HashMap<String, Object>();
                        attributes.put("OBJECTID", Integer.parseInt(addressing.getId()));
                        attributes.put("dmi_beg", addressing.getJTextFieldDmi().getText());
                        attributes.put("structure", addressing.getJTextFieldStruct().getText());
                        attributes.put("dmi_end", addressing.getJTextFieldEnd().getText());
                        attributes.put("full_address", full_address);
                        attributes.put("add_num", Double.parseDouble(addressing.getJTextFieldNum().getText()));
                        attributes.put("prd", addressing.getJComboxPre().getSelectedItem().toString());
                        attributes.put("rd", addressing.getJTextFieldStreet().getText());
                        attributes.put("sts", addressing.getJComboxType().getSelectedItem().toString());
                        attributes.put("pod", addressing.getJComboxPost().getSelectedItem().toString().equalsIgnoreCase("?") ? "" : addressing.getJComboxPost().getSelectedItem().toString());
                        attributes.put("unit", addressing.getJTextFieldUnit().getText());
                        attributes.put("msag_comm",addressing.getJComboxMsag().getSelectedItem().toString());
                        attributes.put("status", addressing.getJComboxStatus().getSelectedItem().toString());
                        Graphic up = new Graphic(addressing.getGeometry(), null, attributes);
                        Graphic [] updates = {up};
                        spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{}, updates, new CallbackListener<FeatureEditResult[][]>(){

                            @Override
                            public void onCallback(FeatureEditResult[][] result) {
                                    if(result[2][0].isSuccess()){
                                        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Update Record");
                                    }
                            }

                        @Override
                         public void onError(Throwable e) {
                         // handle the error
                        }								
                     });
                        return;
                    }
                   
            }
           if(e.getSource() == newItem)
           {
              if(ticket == null || !ticket.isDisplayable())
              {
                     
                  insertNewTicket();
                }
              
              return;
           }
           else if(e.getSource() == identifyFeature)
           {
               System.out.println("I ran identify");
               window.setCursor(id);
               return;
           }
           else if(e.getSource() == googleF.basemap)
           {
               window.setVisible(true);
               setMap(0);
               googleF.setVisible(false);
               return;
           }
           else if(e.getSource() == googleF.satellite)
           {
               window.setVisible(true);
               setMap(1);
               googleF.setVisible(false);
               return;
           }
           else if(e.getSource() == closeItem || e.getSource() == googleF.closeItem)
           {
               map.dispose();
               googleF.dispose();
               System.exit(1);
               return;
           }
           else if(e.getSource() == basemap)
           {
              // System.out.println("I ran 1");
               setMap(0);
               return;
           }
           else if(e.getSource() == satellite)
           {
               setMap(1);
               return;
           }
           else if(e.getSource() == googlem)
           {
               setMap(2);
               return;
           }
           else if(e.getSource() == newProfile)
           {
              
             // System.out.println("Array is " + Arrays.toString(download.setRegionArray()));
                addP = new newUser(img, download.setRegionArray());
                return;
           }
         
           else if(e.getSource() == changeProfile)
           {
               
           
               updateP = new updateUser(img, download.setRegionArray(), login);
                updateP.addWindowListener(new WindowAdapter() {
        @Override
      public void windowClosing(WindowEvent windowEvent) {
        super.windowClosing(windowEvent);
        try{
           login = updateP.getNewInfo();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
       
      }
    });
             
                   
           }
           else if(e.getSource() == moveSpartan)
           {
              
                    readyMove = readyMove ? false : true;
                        if(readyMove)
                            window.setCursor(Cursor.HAND_CURSOR);
                        else
                            window.setCursor(Cursor.DEFAULT_CURSOR);
                        if(!readyMove)
                        {
                            updateInfomation();
                        }
                   
               
               //System.out.println("Read equals " + readyMove);
               return;
           }
           else if(e.getSource() == timer)
           {
               final int THREADS = Runtime.getRuntime().availableProcessors();
               ExecutorService executor = Executors.newFixedThreadPool(THREADS);
               Future<Object> future = executor.submit(new Callable<Object>()
                       {
                          public Object call()
                          {
                              download = new DownloadInformation();
                             
                              return null;
                          }
                       });
               if(future.isDone())
               {
                   System.out.println("FINISH");
               }
               return;
                         
           }
           else if(e.getSource() == ticket.sub)
           {
                ticket.startProgressBar();
                 directoryS.searchWhat(ticket.list.get(ticket.position).getSub());
               subSearh.searchSubdivision(ticket.list.get(ticket.position).getSub());
              
               count = 0;
               count2 = 0;
                t = new Timer(950, new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            if(count == 10)
                            {
                                if(subSearh.getOwnerData().size() > 0)
                                {
                                    subSearh.setFoundParcel(true);
                                    count = 0;
                                }
                            }
                            else{
                                count++;
                            }
                            if(subSearh.getFoundParcel())
                            {
                                System.out.println("FOUnd data");
                                t.stop();
                                System.out.println(count);
                                count = 0;
                                count++;
                                if(count == 1)
                                {
                                  if(tablelist4)
                                   {
                                       subParcel.clearData();
                                       subParcel.addData(subSearh.getOwnerData());
                                       
                                   }else{
                                       subParcel = new tableList();
                                      tablelist4 = true;
                                    subParcel.setVisible(true);
                                    subParcel.addWindowListener(new closing());
                                    subParcel.setUpNewColums();
                                    subParcel.addData(subSearh.getOwnerData());
                                      subParcel.customerT.addMouseListener(new table());
                                    count = 0;
                                    
                                    subSearh.setFoundParcel(false);
                                    
                                   }
                                }
                            }
                            
                        }
                    });
                t.start();
                subTimer = new Timer(950, new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            if(directoryS.getFound1() && directoryS.getFound2() && directoryS.getFound3() && directoryS.getFound4())
                            {
                                subTimer.stop();
                                count2++;
                                if(count2 == 1)
                                {
                                    if(tablelist3)
                                    {
                                        directorySearch.clearData();
                                        directorySearch.addDirecData(directoryS.getMainData());
                                       

                                    }else{
                                        tablelist3 = true;
                                        ticket.startProgressBar();
                                        directorySearch = new tableList();
                                        directorySearch.setTitle("Local Subdivisions");
                                        directorySearch.setVisible(true);
                                        directorySearch.addWindowListener(new closing());
                                        directorySearch.setUpColumns();
                                        directorySearch.addDirecData(directoryS.getMainData());
                                        directorySearch.customerT.addMouseListener(new table());
                                        ticket.stopProgressBar();
                                    }
                                }
                               
                            }
                        }
                    });
                subTimer.start();
                return;
           }
            else if(e.getSource() == ticket.save)
           {
               saveRecord();
           }
           else if(e.getSource() == ticket.delete)
           {
               ticket.startProgressBar();
                deleteRecord();
                ticket.stopProgressBar();
           }
           else if(e.getSource() == ticket.userF)
           {
               ticket.startProgressBar();
               parcelsearch.searchPropertyOwner(ticket.list.get(ticket.position).getOfname(), ticket.list.get(ticket.position).getOlname());
               count = 0;
                t = new Timer(950, new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            if(count == 10)
                            {
                                if(parcelsearch.getOwnerData().size() > 0)
                                {
                                    parcelsearch.setFoundParcel(true);
                                    count = 0;
                                }
                            }
                            else{
                                count++;
                            }
                            if(parcelsearch.getFoundParcel())
                            {
                                System.out.println("FOUnd data");
                                t.stop();
                                count++;
                                if(count == 1)
                                {
                                   if(tablelist2)
                                   {
                                       ownerList.clearData();
                                       ownerList.addData(parcelsearch.getOwnerData());
                                       
                                   }else{
                                       tablelist2 = true;
                                       ownerList = new tableList();
                                       ownerList.addWindowListener(new closing());
                                       ownerList.setUpNewColums();
                                        ownerList.addData(parcelsearch.getOwnerData());
                                      ownerList.setVisible(true);
                                      ownerList.customerT.addMouseListener(new table());
                                      count = 0;
                                      ticket.stopProgressBar();
                                      parcelsearch.setFoundParcel(false);
                                    
                                   }
                                    
                                }
                            }
                            
                        }
                    });
                t.start();
              
           }
           else if(e.getSource() == ticket.next)
           {
               ticket.startProgressBar();
              if(ticket.position == ticket.list.size() - 1)
                {
                    
                    System.out.println("I ran once");
                    insertNextTicket();
                    ticket.stopProgressBar();
                }
                else{
                    //Update the ticket to the next level
                  System.out.println("I ran once again");
                    ticket.addPosition();
                    ticket.removeLayer();
                   
                   //Refresh information....
                   ticket.refreshForm();
                  
                   map.panTo(ticket.list.get(ticket.position).getGeometry()); 
                }
              ticket.stopProgressBar();
               return;
           }
           else if(e.getSource() == ticket.previous)
           {
               //Try to go back in the tickets..
               ticket.startProgressBar();
               //First Check the current position..
               if(ticket.getPosAndSize()[1] > 1 && ticket.getPosAndSize()[0] != 0)//if greater than zero subtract one to the current position.. and refresh form..
               {
                   ticket.subTrackPosition();
                   ticket.removeLayer();
                   //Refresh information....
                   ticket.refreshForm();
                    
                   //System.out.println("X and Y " + ticket.list.get(ticket.position).getLocation().toString());
                   map.panTo(ticket.list.get(ticket.position).getGeometry()); 
               }else if(ticket.getPosAndSize()[0] == 0 && ticket.getPosAndSize()[1] > 1)
               {
                   ticket.goToBegining();
                   ticket.removeLayer();
                   ticket.refreshForm();
                   
                   map.panTo(ticket.list.get(ticket.position).getGeometry());
               }
               ticket.stopProgressBar();
               return;
           }
           else if(e.getSource() == ticket.zoom)
           {
               map.panTo(ticket.list.get(ticket.position).getGeometry());
               map.zoomTo(ticket.list.get(ticket.position).getGeometry());
           }
        }
    
    }
    //Class for shortcut keys listner
     private class abClick extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == ticket.next)
            {
                ticket.startProgressBar();
                //System.out.println("I ran two");
                //Before Insert check that you are the last record on the position..
                if(ticket.position == ticket.list.size() - 1)
                {
                    //then insert new ticket();
                    insertNextTicket();
                    ticket.stopProgressBar();
                }
                else{
                    //Update the ticket to the next level
                    ticket.addPosition();
                    ticket.layer.removeAll();
                  ticket.GraphicsId = ticket.layer.addGraphic(new Graphic(ticket.list.get(ticket.position).getGeometry(), ticket.list.get(ticket.position).getSymbol()));
                   //Refresh information....
                   ticket.refreshForm();
                   //System.out.println("X and Y " + ticket.list.get(ticket.position).getLocation().toString());
                   map.panTo(ticket.list.get(ticket.position).getGeometry()); 
                   ticket.stopProgressBar();
                }
                return;
            }
            else if(e.getSource() == ticket.zoom)
            {
               ticket.startProgressBar();
               ticket.setMessage("Zooming in to Ticket");
               map.panTo(ticket.list.get(ticket.position).getGeometry());
               map.zoomTo(ticket.list.get(ticket.position).getGeometry());
               ticket.stopProgressBar();
               ticket.setMessage("");
            }
            else if(e.getSource() == ticket.save)
            {
                saveRecord();
            }
            else if(e.getSource() == ticket.delete)
            {
                ticket.startProgressBar();
                 deleteRecord();
                 ticket.stopProgressBar();
            }
            else if(e.getSource() == ticket.previous)
            {
                  //Try to go back in the tickets..
                ticket.startProgressBar();
               //First Check the current position..
               if(ticket.getPosAndSize()[1] > 1 && ticket.getPosAndSize()[0] != 0)//if greater than zero subtract one to the current position.. and refresh form..
               {
                   ticket.subTrackPosition();
                   ticket.layer.removeAll();
                   //insert symbol..
                   ticket.GraphicsId = ticket.layer.addGraphic(new Graphic(ticket.list.get(ticket.position).getGeometry(), ticket.list.get(ticket.position).getSymbol()));
                   //Refresh information....
                   ticket.refreshForm();
                   //System.out.println("X and Y " + ticket.list.get(ticket.position).getLocation().toString());
                   map.panTo(ticket.list.get(ticket.position).getGeometry()); 
               }else if(ticket.getPosAndSize()[0] == 0 && ticket.getPosAndSize()[1] > 1)
               {
                   ticket.goToBegining();
                   ticket.layer.removeAll();
                   ticket.GraphicsId = ticket.layer.addGraphic(new Graphic(ticket.list.get(ticket.position).getGeometry(), ticket.list.get(ticket.position).getSymbol()));
                   ticket.refreshForm();
                   map.panTo(ticket.list.get(ticket.position).getGeometry());
               }
               ticket.stopProgressBar();
               return;
                
            }
        }
     }
    class HintTextField extends JTextField implements FocusListener {

  private final String hint;
  private boolean showingHint;

  public HintTextField(final String hint) {
    super(hint);
    this.hint = hint;
    this.showingHint = true;
    
    super.addFocusListener(this);
  }

  @Override
  public void focusGained(FocusEvent e) {
    if(this.getText().isEmpty()) {
      super.setText("");
      showingHint = false;
    }
  }
  @Override
  public void focusLost(FocusEvent e) {
    if(this.getText().isEmpty()) {
      super.setText(hint);
      showingHint = true;
    }
  }

  @Override
  public String getText() {
    return showingHint ? "" : super.getText();
  }
}
    public class MouseOverLay extends MapOverlay{
            private JMap map;
             private  DecimalFormat decimalFormat = new DecimalFormat("##.#######");
        public MouseOverLay(JMap map)
        {
             this.map = map;
         }
    @Override
    public void onMouseMoved(MouseEvent event) {
        
        try{
            java.awt.Point p = event.getPoint();
            com.esri.core.geometry.Point mapPoint = map.toMapPoint(p.x, p.y);
           // mapPoint = (Point) GeometryEngine.project(mapPoint, map.getSpatialReference(), SpatialReference.create(4326));
           // System.out.println("Map Coordinates: X = " + decimalFormat.format(mapPoint.getX()) 
          //+ ", Y = " + decimalFormat.format(mapPoint.getY()));
            if(moveOn)
            {
                getId = findId2();
                moveNum++;
                spartanLayer.updateGraphic(getId, map.toMapPoint(p.x, p.y));
               if(ticketopen)
               {
                   
                   
                    ticket.layer.updateGraphic(ticket.GraphicsId, map.toMapPoint(p.x, p.y));
                    //Update the ObjectData as well...
                    ticket.list.get(ticket.position).setGeometry(map.toMapPoint(p.x, p.y).copy());
               }
               if(moveNum == 30)
               {
                    moveNum = 0;
                    Graphic [] update = {spartanLayer.getGraphic(getId)};
                    spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{}, update, null);
               }
               
            }
           
        }finally{
            
        super.onMouseMoved(event); 
        
    }
   }
}
     public void setUpFocus()
 {
     ticket.fname.addFocusListener(new focusList());
     ticket.lname.addFocusListener(new focusList());
     ticket.mainT.addFocusListener(new focusList());
     ticket.alt1.addFocusListener(new focusList());
     ticket.ofname.addFocusListener(new focusList());
     ticket.olname.addFocusListener(new focusList());
     ticket.subdivision.addFocusListener(new focusList());
     ticket.block.addFocusListener(new focusList());
     ticket.lotnum.addFocusListener(new focusList());
     ticket.taxT.addFocusListener(new focusList());
     ticket.propertyT.addFocusListener(new focusList());
     ticket.streetT.addFocusListener(new focusList());
     ticket.intersectionT.addFocusListener(new focusList());
     ticket.description.addFocusListener(new focusList());
     ticket.notes.addFocusListener(new focusList());
 }
     
     public class focusList implements FocusListener{

        @Override
        public void focusGained(FocusEvent e) {
            
        }

        @Override
        public void focusLost(FocusEvent e) { //focusLost tickets
            
            if(login.getRole().equalsIgnoreCase("addressing"))
            {
                int enter = 0;
                if(e.getSource() == addressing.getJTextFieldFname())
                {
                    //get information and submit to the server after focusLost...
                    System.out.println("I ran");
                     Map<String, Object> attributes = new HashMap<String, Object>();
                     attributes.put("OBJECTID", Integer.parseInt(addressing.getId()));
                     attributes.put("cfirst_name", addressing.getJTextFieldFname().getText().toUpperCase());
                     addressing.getData().get(addressing.getPosition()).setFname(addressing.getJTextFieldFname().getText().toUpperCase());
                     Graphic up = new Graphic(addressing.getGeometry(),null, attributes);
                     Graphic [] updates = {up};
                     spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{}, updates, null);
                     
                }
                else if(e.getSource() == addressing.getJTextFieldLname())
                {
                    enter = 1;
                     Map<String, Object> attributes = new HashMap<String, Object>();
                     attributes.put("OBJECTID", Integer.parseInt(addressing.getId()));
                     attributes.put("clast_name", addressing.getJTextFieldLname().getText().toUpperCase());
                     addressing.getData().get(addressing.getPosition()).setLname(addressing.getJTextFieldLname().getText().toUpperCase());
                     Graphic up = new Graphic(addressing.getGeometry(),null, attributes);
                     Graphic [] updates = {up};
                     spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{}, updates, null);
                }
                else if(e.getSource() == addressing.getJTextFieldMainT())
                {
                     enter = 1;
                     Map<String, Object> attributes = new HashMap<String, Object>();
                     attributes.put("OBJECTID", Integer.parseInt(addressing.getId()));
                     attributes.put("telephone_land_line", addressing.getJTextFieldMainT().getText().toUpperCase());
                     addressing.getData().get(addressing.getPosition()).setMainT(addressing.getJTextFieldMainT().getText().toUpperCase());
                     Graphic up = new Graphic(addressing.getGeometry(),null, attributes);
                     Graphic [] updates = {up};
                     spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{}, updates, null);
                }
                else if(e.getSource() == addressing.getJTextFieldAlt1())
                {
                     enter = 1;
                     Map<String, Object> attributes = new HashMap<String, Object>();
                     attributes.put("OBJECTID", Integer.parseInt(addressing.getId()));
                     attributes.put("alt_telephone", addressing.getJTextFieldAlt1().getText().toUpperCase());
                     addressing.getData().get(addressing.getPosition()).setAlt1(addressing.getJTextFieldAlt1().getText().toUpperCase());
                     Graphic up = new Graphic(addressing.getGeometry(),null, attributes);
                     Graphic [] updates = {up};
                     spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{}, updates, null);
                }
                else if(e.getSource() == addressing.getJTextFieldBlock())
                {
                     enter = 1;
                     Map<String, Object> attributes = new HashMap<String, Object>();
                     attributes.put("OBJECTID", Integer.parseInt(addressing.getId()));
                     attributes.put("block_num", addressing.getJTextFieldBlock().getText().toUpperCase());
                     addressing.getData().get(addressing.getPosition()).setBlock(addressing.getJTextFieldBlock().getText().toUpperCase());
                     Graphic up = new Graphic(addressing.getGeometry(),null, attributes);
                     Graphic [] updates = {up};
                     spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{}, updates, null);
                    
                }
                else if(e.getSource() == addressing.getJTextFieldDescription())
                {
                     enter = 1;
                     Map<String, Object> attributes = new HashMap<String, Object>();
                     attributes.put("OBJECTID", Integer.parseInt(addressing.getId()));
                     attributes.put("building_description", addressing.getJTextFieldDescription().getText().toUpperCase());
                     addressing.getData().get(addressing.getPosition()).setBuild(addressing.getJTextFieldDescription().getText().toUpperCase());
                     Graphic up = new Graphic(addressing.getGeometry(),null, attributes);
                     Graphic [] updates = {up};
                     spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{}, updates, null);
                    
                }
                else if(e.getSource() == addressing.getJTextFieldIntersection())
                {
                     enter = 1;
                     Map<String, Object> attributes = new HashMap<String, Object>();
                     attributes.put("OBJECTID", Integer.parseInt(addressing.getId()));
                     attributes.put("intersection", addressing.getJTextFieldIntersection().getText().toUpperCase());
                     addressing.getData().get(addressing.getPosition()).setInter(addressing.getJTextFieldIntersection().getText().toUpperCase());
                     Graphic up = new Graphic(addressing.getGeometry(),null, attributes);
                     Graphic [] updates = {up};
                     spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{}, updates, null);
                    
                }
                else if(e.getSource() == addressing.getJTextFieldLot())
                {
                     enter = 1;
                     Map<String, Object> attributes = new HashMap<String, Object>();
                     attributes.put("OBJECTID", Integer.parseInt(addressing.getId()));
                     attributes.put("lot_num", addressing.getJTextFieldLot().getText().toUpperCase());
                     addressing.getData().get(addressing.getPosition()).setLot(addressing.getJTextFieldLot().getText().toUpperCase());
                     Graphic up = new Graphic(addressing.getGeometry(),null, attributes);
                     Graphic [] updates = {up};
                     spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{}, updates, null);
                }
                else if(e.getSource() == addressing.getJTextFieldNotes())
                {
                     enter = 1;
                     Map<String, Object> attributes = new HashMap<String, Object>();
                     attributes.put("OBJECTID", Integer.parseInt(addressing.getId()));
                     attributes.put("comments", addressing.getJTextFieldNotes().getText().toUpperCase());
                     addressing.getData().get(addressing.getPosition()).setNotes(addressing.getJTextFieldNotes().getText().toUpperCase());
                     Graphic up = new Graphic(addressing.getGeometry(),null, attributes);
                     Graphic [] updates = {up};
                     spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{}, updates, null);
                }
                else if(e.getSource() == addressing.getJTextFieldOfname())
                {
                    enter = 1;
                    Map<String, Object> attributes = new HashMap<String, Object>();
                    attributes.put("OBJECTID", Integer.parseInt(addressing.getId()));
                    attributes.put("pfirst_name", addressing.getJTextFieldOfname().getText().toUpperCase());
                    addressing.getData().get(addressing.getPosition()).setOfname(addressing.getJTextFieldOfname().getText().toUpperCase());
                    Graphic up = new Graphic(addressing.getGeometry(),null, attributes);
                    Graphic [] updates = {up};
                    spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{}, updates, null);    
                }
                else if(e.getSource() == addressing.getJTextFieldOlname())
                {
                    enter = 1;
                    Map<String, Object> attributes = new HashMap<String, Object>();
                    attributes.put("OBJECTID", Integer.parseInt(addressing.getId()));
                    attributes.put("plast_name", addressing.getJTextFieldOlname().getText().toUpperCase());
                    addressing.getData().get(addressing.getPosition()).setOlname(addressing.getJTextFieldOlname().getText().toUpperCase());
                    Graphic up = new Graphic(addressing.getGeometry(),null, attributes);
                    Graphic [] updates = {up};
                    spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{}, updates, null); 
                }
                else if(e.getSource() == addressing.getJTextFieldSubdivision())
                {
                    enter = 1;
                    Map<String, Object> attributes = new HashMap<String, Object>();
                    attributes.put("OBJECTID", Integer.parseInt(addressing.getId()));
                    attributes.put("subdivision", addressing.getJTextFieldSubdivision().getText().toUpperCase());
                    addressing.getData().get(addressing.getPosition()).setSub(addressing.getJTextFieldSubdivision().getText().toUpperCase());
                    Graphic up = new Graphic(addressing.getGeometry(),null, attributes);
                    Graphic [] updates = {up};
                    spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{}, updates, null); 
                }
                else if(e.getSource() == addressing.getJTextFieldTaxT())
                {
                    enter = 1;
                    Map<String, Object> attributes = new HashMap<String, Object>();
                    attributes.put("OBJECTID", Integer.parseInt(addressing.getId()));
                    attributes.put("tax_account_num", addressing.getJTextFieldTaxT().getText().toUpperCase());
                    addressing.getData().get(addressing.getPosition()).setTax(addressing.getJTextFieldTaxT().getText().toUpperCase());
                    Graphic up = new Graphic(addressing.getGeometry(),null, attributes);
                    Graphic [] updates = {up};
                    spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{}, updates, null); 
                }
                else if(e.getSource() == addressing.getJTextFieldProperty())
                {
                    enter = 1;
                    Map<String, Object> attributes = new HashMap<String, Object>();
                    attributes.put("OBJECTID", Integer.parseInt(addressing.getId()));
                    attributes.put("property_id", addressing.getJTextFieldProperty().getText().toUpperCase());
                    addressing.getData().get(addressing.getPosition()).setProp(addressing.getJTextFieldProperty().getText().toUpperCase());
                    Graphic up = new Graphic(addressing.getGeometry(),null, attributes);
                    Graphic [] updates = {up};
                    spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{}, updates, null);
                }
                else if(e.getSource() == addressing.getJTextFieldStreetT())
                {
                    enter = 1;
                    Map<String, Object> attributes = new HashMap<String, Object>();
                    attributes.put("OBJECTID", Integer.parseInt(addressing.getId()));
                    attributes.put("street", addressing.getJTextFieldStreetT().getText().toUpperCase());
                    addressing.getData().get(addressing.getPosition()).setStreet(addressing.getJTextFieldStreetT().getText().toUpperCase());
                    Graphic up = new Graphic(addressing.getGeometry(),null, attributes);
                    Graphic [] updates = {up};
                    spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{}, updates, null);
                }
                if(enter == 1)
                {
                    
                    return;
                }
            }
            if(ticketopen)
            {
            if(e.getSource() == ticket.fname)
                ticket.list.get(ticket.position).setFname(ticket.fname.getText().toUpperCase());
            else if(e.getSource() == ticket.lname)
                ticket.list.get(ticket.position).setLname(ticket.lname.getText().toUpperCase());
            else if(e.getSource() == ticket.mainT)
            {
                ticket.list.get(ticket.position).setMainT(ticket.mainT.getText().toUpperCase());
                if(ticket.mainT.getText().replace(" ", "").length() > 3)
                {
                 ticketS.searchTelephone(ticket.mainT.getText());
                    count = 0;
                     t = new Timer(800, new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            if(ticketS.getFoundTicket())
                            {
                                
                                t.stop();
                                count++;
                                if(count == 1)
                                {
                                    if(tablelist)
                                    {
                                          list.addNewList(ticketS.getListInfo());
                                    }else
                                    {
                                        tablelist = true;
                                        list = new tableList();
                                        list.setList(ticketS.getListInfo());
                                         list.setVisible(true);
                                         list.setFocusable(true);
                                         ticketS.setFoundTicket(false);
                                            list.customerT.addMouseListener(new table());
                                            list.addWindowListener(new closing());
                                         System.out.println("I ran from maint");
                                    }
                                }
                            }
                            
                        }
                      
                  });
                   t.start();
                }
            }
            else if(e.getSource() == ticket.alt1)
            {
                ticket.list.get(ticket.position).setAlt1(ticket.alt1.getText().toUpperCase());
                if( ticket.alt1.getText().replace(" ", "").length() > 3)
                {
                     count = 0;
                    ticketS.searchTelephone(ticket.alt1.getText());
                     t = new Timer(800, new ActionListener()
                     {
                         public void actionPerformed(ActionEvent e)
                         {
                              if(ticketS.getFoundTicket())
                             {
                                
                                t.stop();
                                count++;
                                if(count == 1)
                                {
                                if(tablelist)
                                {
                                   
                                    list.addNewList(ticketS.getListInfo());
                                }else{
                                    tablelist = true;
                                    list = new tableList();
                                    list.setList(ticketS.getListInfo());
                                    list.setVisible(true);
                                    list.setFocusable(true);
                                    list.customerT.addMouseListener(new table());
                                    list.addWindowListener(new closing());
                                }
                                }
                             }
                            
                        }
                      
                  });
                   t.start();
                }
            }
            else if(e.getSource() == ticket.ofname)
                ticket.list.get(ticket.position).setOfname(ticket.ofname.getText().toUpperCase());
            else if(e.getSource() == ticket.olname)
                ticket.list.get(ticket.position).setOlname(ticket.olname.getText().toUpperCase());
            else if(e.getSource() == ticket.subdivision)
                ticket.list.get(ticket.position).setSub(ticket.subdivision.getText().toUpperCase());
            else if(e.getSource() == ticket.block)
                ticket.list.get(ticket.position).setBlock(ticket.block.getText());
            else if(e.getSource() == ticket.lotnum)
                ticket.list.get(ticket.position).setLot(ticket.lotnum.getText());
            else if(e.getSource() == ticket.taxT)
            {
               ticket.startProgressBar();
               ticket.list.get(ticket.position).setTax(ticket.taxT.getText().toUpperCase());
               ticket.runTaxQuery();
                t = new Timer(800, new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            if(ticket.list.get(ticket.position).getFound())
                            {
                                System.out.println("I ran action performed found geometry");
                                t.stop();
                                map.panTo(ticket.list.get(ticket.position).getGeometry());
                                map.zoomTo(ticket.list.get(ticket.position).getGeometry());
                                
                            }
                            
                        }
                      
                  });
                   t.start();
                    
            }
            else if(e.getSource() == ticket.propertyT)
            {
               
                ticket.list.get(ticket.position).setProp(ticket.propertyT.getText().toUpperCase());
                ticket.runPropQuery();
                 t = new Timer(800, new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            if(ticket.list.get(ticket.position).getFound())
                            {
                                
                                t.stop();
                                map.zoomTo(ticket.list.get(ticket.position).getGeometry());
                                
                            }
                            
                        }
                      
                  });
                   t.start();
            }
            else if(e.getSource() == ticket.streetT)
            {
                ticket.list.get(ticket.position).setStreet(ticket.streetT.getText().toUpperCase());
                if(!ticket.intersectionT.getText().trim().equals("") && !ticket.list.get(ticket.position).getFound())
                {
                    System.out.println("I ran starting to geocode");
                    geocodeS.setMessage("Intersection Search");
                    geocodeS.search10Stuff(ticket.streetT.getText() + " & " + ticket.intersectionT.getText());
                    t = new Timer(500, new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            if(geocodeS.getReady())
                            {
                                
                                t.stop();
                                getId = findId();
                                spartanLayer.updateGraphic(getId, geocodeS.getResult().getLocation());
                                 ticket.layer.updateGraphic(ticket.GraphicsId, geocodeS.getResult().getLocation());
                                changeLocation(geocodeS.getResult().getLocation());
                                
                            }
                            
                        }
                      
                  });
                   t.start();
                }
            }
            else if(e.getSource() == ticket.intersectionT)
            {
                  ticket.list.get(ticket.position).setInter(ticket.intersectionT.getText().toUpperCase());
                  if(!ticket.streetT.getText().trim().equals("")  && !ticket.list.get(ticket.position).getFound())
                  {
                    System.out.println("I ran starting to geocode");
                    geocodeS.setMessage("Intersection Search");
                    geocodeS.search10Stuff(ticket.streetT.getText() + " & " + ticket.intersectionT.getText());
                    t = new Timer(500, new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            if(geocodeS.getReady())
                            {
                                
                                t.stop();
                                getId = findId();
                               spartanLayer.updateGraphic(getId, geocodeS.getResult().getLocation());
                               ticket.layer.updateGraphic(ticket.GraphicsId, geocodeS.getResult().getLocation());
                                changeLocation(geocodeS.getResult().getLocation());
                                
                            }
                            
                        }
                      
                  });
                   t.start();
                  }
            }
            else if(e.getSource() == ticket.description)
                ticket.list.get(ticket.position).setBuild(ticket.description.getText().toUpperCase());
            else if(e.getSource() == ticket.notes)
            {
                ticket.list.get(ticket.position).setNotes(ticket.notes.getText().toUpperCase());
            }
        }
        }
     }
     
     //Create a mouse adapter for the tableList...
     public class table extends MouseAdapter{ //JTABLE CLICKS
         public void mouseClicked(MouseEvent e)
         {
            if(login.getRole().equalsIgnoreCase("addressing"))
            {
                if(e.getSource() == addressing.getJtableMain())
                {
                   addressing.setPosition(addressing.getJtableMain().getSelectedRow());
                   addressing.setMessageText();
                   addressing.updateId();
                    zoomToCreateBuffer();
                    return;
                }
                else if(e.getSource() == addressing.getJtableRelated())
                {
                    //Diferent process this zooms in to that particular square...
                    if(e.getClickCount() == 1)
                    {
                    map.zoomTo(addressing.getRelatedGeometry(addressing.getTableRelatedRow()));
                    //Select the currect square...
                    //clear first then select..
                    temporary.clearSelection();
                    temporary.setSelectionColor(Color.yellow);
                    temporary.select(addressing.getIdGraphicRelated(addressing.getTableRelatedRow()));
                    return;
                    }
                }
                else if(e.getSource() == addressing.getJtableAttachment())
                {
                     if(e.getButton() == java.awt.event.MouseEvent.BUTTON3)
                    {
                        dbConnect con = new dbConnect();
                        if(con.deleteAttachment(addressing.getJtableAttachment().getValueAt(addressing.getJtableAttachment().getSelectedRow(), 0).toString()) > 0)
                        {
                            addressing.getAttachModel().removeRow(addressing.getJtableAttachment().getSelectedRow());
                            JOptionPane.showMessageDialog(null, "Attachment deleted");
                        }else{
                            JOptionPane.showMessageDialog(null, "Sorry, couldn't delete your attachment?");

                        }


                        return;
                    }
                     else if(e.getClickCount() > 1){
                    try{
                        ProcessFiles open = new ProcessFiles();
                        //this.startProgressBar();
                            if(new File("d:/").exists())
                                if(!new File("d:/download").exists())
                                {
                                    new File("d:/download").mkdir();
                                     try{
                                     dbConnect con = new dbConnect();
                                     ProcessFiles data = con.getBlobInfo(addressing.getJtableAttachment().getValueAt(addressing.getJtableAttachment().getSelectedRow(), 0).toString());

                                     //Here is oging to create the file...
                                     if(!new File("d:/download/"+ data.getName()).exists())
                                     {
                                         File f = new File("d:/download/"+ data.getName());

                                         data.writeByteArraysToFile("d:/download/" + data.getName(),  data.decodeBase64(data.getBase64(), data.getName()));
                                         data.openFile(f);

                                     }
                                     else{
                                         new File("d:/download/"+data.getName()).delete();
                                         File f = new File("d:/download/"+ data.getName());

                                         data.writeByteArraysToFile("d:/download/" + data.getName(),  data.decodeBase64(data.getBase64(), data.getName()));
                                         data.openFile(f);
                                     }

                                    }catch(Exception error)
                                    {
                                         JOptionPane.showMessageDialog(null, error);
                                    }
                                }
                                else{
                                    //check if the download file exists in that particular locaiton...
                                    try{
                                     dbConnect con = new dbConnect();
                                     ProcessFiles data = con.getBlobInfo(addressing.getJtableAttachment().getValueAt(addressing.getJtableAttachment().getSelectedRow(), 0).toString());

                                     //Here is oging to create the file...
                                     if(!new File("d:/download/"+ data.getName()).exists())
                                     {
                                         File f = new File("d:/download/"+ data.getName());
                                         data.writeByteArraysToFile("d:/download/" + data.getName(),  data.decodeBase64(data.getBase64(), data.getName()));
                                         data.openFile(f);
                                     }
                                     else{
                                         new File("d:/download/"+data.getName()).delete();
                                         File f = new File("d:/download/"+ data.getName());
                                         data.writeByteArraysToFile("d:/download/" + data.getName(),  data.decodeBase64(data.getBase64(), data.getName()));
                                         data.openFile(f);
                                     }

                                    }catch(Exception error)
                                    {
                                         JOptionPane.showMessageDialog(null, error);
                                    }

                                }
                            else{
                                //Move to c drive//

                            }
                    }catch(Exception t)
                    {
                        
                    }
                     }
                    return;
                }
                else if(e.getSource() == addressing.getJtableSubdivision())
                {
                    if(e.getClickCount() > 1)
                    {
                        try{
                        ProcessFiles open = new ProcessFiles();
                        open.openFile(new File(addressing.getJtableSubdivision().getValueAt(addressing.getJtableSubdivision().getSelectedRow(), 0).toString()));
                    }catch(Exception t)
                    {
                        
                    }
                        
                    }
                }
                else if(e.getSource() == addressing.getJtablePictures())
                {
                      try{
                        ProcessFiles open = new ProcessFiles();
                      
                                     ProcessFiles data = new ProcessFiles();
                                     data.openFile(new File(addressing.getJtablePictures().getValueAt(addressing.getJtablePictures().getSelectedRow(), 0).toString()));    
                    }catch(Exception t)
                    {
                        
                    }
                }
            }
             if(tablelist2)
             {
           
                if(e.getSource() == ownerList.customerT)
                {
                 if(e.getClickCount() > 1)
                 {
                    
                     SimpleLineSymbol outline = new SimpleLineSymbol(Color.BLACK, 3, SimpleLineSymbol.Style.SOLID);
                     SimpleFillSymbol symbol = new SimpleFillSymbol(new Color(160, 32, 240, 50), outline);
                      temporary.removeAll();
                      map.panTo(ownerList.getGeometry2(ownerList.customerT.getSelectedRow()));
                      map.zoomTo(ownerList.getGeometry2(ownerList.customerT.getSelectedRow()));
                     temporary.addGraphic(new Graphic(ownerList.getGeometry2(ownerList.customerT.getSelectedRow()), 
                             symbol));
                 }
                }
             
             }
             if(tablelist4)
             {
                 if(e.getSource() == subParcel.customerT)
                 {
                 if(e.getClickCount() > 1)
                 {
                    
                     SimpleLineSymbol outline = new SimpleLineSymbol(Color.BLACK, 3, SimpleLineSymbol.Style.SOLID);
                     SimpleFillSymbol symbol = new SimpleFillSymbol(new Color(160, 32, 240, 50), outline);
                      temporary.removeAll();
                      map.panTo(subParcel.getGeometry2(subParcel.customerT.getSelectedRow()));
                      map.zoomTo(subParcel.getGeometry2(subParcel.customerT.getSelectedRow()));
                     temporary.addGraphic(new Graphic(subParcel.getGeometry2(subParcel.customerT.getSelectedRow()), 
                             symbol));
                     
                 }
                 }
                 
             }
             if(tablelist)
             {
             if(e.getSource() == list.customerT){
                 if(e.getClickCount() > 1)
                 {
                     System.out.println("I ran table ");
                     System.out.println("SELECTED ROW " + list.customerT.getSelectedRow());
                     map.panTo(list.getGeometry(list.customerT.getSelectedRow()));
                     map.zoomTo(list.getGeometry(list.customerT.getSelectedRow()));
                }
             }
             }
             if(tablelist3)
             {
                  if(e.getSource() == directorySearch.customerT){
                 if(e.getClickCount() > 1)
                 {
                     System.out.println("I ran table ");
                     System.out.println("SELECTED ROW " + directorySearch.customerT.getSelectedRow());
                     
                     try{
                          ProcessFiles data = new ProcessFiles();
                         data.openFile(new File(directorySearch.getPath(directorySearch.customerT.getSelectedRow())));   
                     }catch(Exception t)
                     {
                         
                     }
                    
                     
                }
             }
         }
     }
     }
     public class closing extends WindowAdapter
     {

        @Override
        public void windowClosed(WindowEvent e) {
            super.windowClosed(e);
            
            if(e.getSource() == ownerList)
            {
                tablelist2 = false;
            }
            else if(e.getSource() ==  subParcel)
            {
                tablelist4 = false;
            }
            else if(e.getSource() == list)
            {
                tablelist = false;
            }
            else if(e.getSource() == directorySearch)
            {
                tablelist3 = false;
            }
        }
         
     }
}

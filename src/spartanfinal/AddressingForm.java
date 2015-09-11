/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartanfinal;

import com.esri.core.geometry.Geometry;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;





/**
 *
 * @author hchapa
 */
public class AddressingForm extends javax.swing.JFrame {

    /**
     * Creates new form AddressingForm
     */
    private ticketSearch search = new ticketSearch();
    private Timer timer;
    private Timer dTimer;
    private DefaultTableModel model;
    private DefaultTableModel relatedModel;
    private DefaultTableModel attachModel;
    private DefaultTableModel parcelModel;
    private DefaultTableModel subdivisionModel;
    private DefaultTableModel picturesModel;
    private int count = 0;
    private ArrayList<ObjectData> dataArray = new ArrayList();
    private ArrayList<ObjectData> dataRelated = new ArrayList();
    private ArrayList<String[]> attachments = new ArrayList();
    private ArrayList<Integer> idOfGraphics = new ArrayList();
    private ArrayList<FlagObject> flagArray = new ArrayList();
    private FeatureResult res;
    private FeatureResult relatedRes;
    private ObjectData data = new ObjectData();
    private ObjectData relatedData = new ObjectData();
    private searchDirectory searchSub = new searchDirectory();
    private boolean ready = false;
    private int position;
    private int count2;
    private int graphicId;
    
    public AddressingForm() {
        initComponents();
        counting.setText("");
        attachmentT.setRowHeight(30);
        icon.setIcon(new ImageIcon("img/create_new_file.png"));
        more.setIcon(new ImageIcon("img/cell_phone_32.png"));
        this.subR.setIcon(new ImageIcon("img/enter_key_32.png"));
        this.previous.setIcon(new ImageIcon("img/previous_32.png"));
        this.next.setIcon(new ImageIcon("img/next_32.png"));
        this.save.setIcon(new ImageIcon("img/save_32.png"));
        this.delete.setIcon(new ImageIcon("img/delete_32.png"));
        this.zoom.setIcon(new ImageIcon("img/center_direction_32.png"));
        this.addAttachment.setIcon(new ImageIcon("img/attach_32.png"));
        model = (DefaultTableModel)addressingView.getModel();
        relatedModel = (DefaultTableModel) relatedTable.getModel();
        attachModel = (DefaultTableModel) attachmentT.getModel();
        parcelModel = (DefaultTableModel) parcelTable.getModel();
        subdivisionModel = (DefaultTableModel)directoryTable.getModel();
        picturesModel = (DefaultTableModel)picturesTable.getModel();
       /// this.progress.setVisible(false);
        this.setTitle("Addressing Department");
    }
    
    public void setGraphicId(int i)
    {
        graphicId = i;
    }
    public void clearFlagInfo(){flagArray.clear();}
    public void addFlagInfo(String l, Geometry g)
    {
        flagArray.add(new FlagObject(l, g));
        picturesModel.addRow(new Object[]{l});
    }
    public int getGraphicId(){return graphicId;}
    public void destroyAttachments()
    {
       attachModel.setRowCount(0);
        
    }
    public void getAttachments()
    {
        dbConnect  attachment = new dbConnect();
        attachments = attachment.getAttachmentsInfo(dataArray.get(position).getId());
        System.out.println("size is " + attachments.size());
        try{
        ProcessFiles pro = new ProcessFiles();
      if(attachments.size() > 0)//Greater than one add to the table...
      {
          attachments.stream().map((table1) -> {
              pro.setType(table1[2]);
              return table1;
          }).forEach((table1) -> {
              attachModel.addRow(new Object[]{table1[0], table1[1], table1[2], pro.getImage()});
          });
           
      }
        }catch(Exception e)
        {
            
        }
        
    }
    public void clearGraphicId(){idOfGraphics.clear();}
    public void setIdGraphicRelated(int i){idOfGraphics.add(i);}
    public int getIdGraphicRelated(int i){return idOfGraphics.get(i);}
    public void setRelatedRest(FeatureResult r){relatedRes = r;}
    public boolean ready(){return ready;}
    public String getId(){return dataArray.get(position).getId();}
    public Geometry getGeometry(){return dataArray.get(position).getGeometry();}
    public int getPosition(){return position;}
    public void setPosition(int i){position = i;}
    public Geometry getRelatedGeometry(int i){return dataRelated.get(i).getGeometry();}
    public int getTableRelatedRow(){return relatedTable.getSelectedRow();}
    public DefaultTableModel getAttachModel(){return attachModel;}
    public DefaultTableModel getParcelModel(){return parcelModel;}
    public DefaultTableModel getSubModel(){return subdivisionModel;}
    public DefaultTableModel getPicturesModel(){return picturesModel;}
    public void runQuery()
    {
         final int THREADS = Runtime.getRuntime().availableProcessors();
               ExecutorService executor = Executors.newFixedThreadPool(THREADS);
               Future<Object> future = executor.submit(new Callable<Object>()
                       {
                          public Object call()
                          {
                                search.searchAddressing("ADDRESSING");
                               return null;
                          }
                       });
         timer = new Timer(900, new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            if(search.getFoundTicket())
                            {
                                count++;
                                timer.stop();
                                if(count == 1)
                                {
                                    System.out.println("I have records");
                                    res = search.getResults();
                                    setUpRecords();
                                }
                            }
                                }
        });
         timer.start();
               
    }
    public void addClick(esri_map.actionClick n) 
    {
        this.next.addActionListener(n);
        this.save.addActionListener(n); 
        this.previous.addActionListener(n);
        this.delete.addActionListener(n);
        this.zoom.addActionListener(n);
        this.addAttachment.addActionListener(n);
        this.subR.addActionListener(n);
    }
    public void addTableClick(esri_map.table n)
    {
        this.addressingView.addMouseListener(n);
        this.relatedTable.addMouseListener(n);
        this.attachmentT.addMouseListener(n);
        this.directoryTable.addMouseListener(n);
        this.picturesTable.addMouseListener(n);
    }
    public void addFocusListener(esri_map.focusList n)
    {
        this.fname.addFocusListener(n);
        this.lname.addFocusListener(n);
        this.mainT.addFocusListener(n);
        this.alt1.addFocusListener(n);
        this.ofname.addFocusListener(n);
        this.olname.addFocusListener(n);
        this.subdivision.addFocusListener(n);
        this.block.addFocusListener(n);
        this.lotnum.addFocusListener(n);
        this.taxT.addFocusListener(n);
        this.propertyT.addFocusListener(n);
        this.streetT.addFocusListener(n);
        this.intersectionT.addFocusListener(n);
        this.description.addFocusListener(n);
        this.notes.addFocusListener(n);
        
    }
    public JTextField getJTextFieldFname(){return this.fname;}
    public JTextField getJTextFieldLname(){return this.lname;}
    public JTextField getJTextFieldMainT(){return this.mainT;}
    public JTextField getJTextFieldAlt1(){return this.alt1;}
    public JTextField getJTextFieldOfname(){return this.ofname;}
    public JTextField getJTextFieldOlname(){return this.olname;}
    public JTextField getJTextFieldSubdivision(){return this.subdivision;}
    public JTextField getJTextFieldBlock(){return this.block;}
    public JTextField getJTextFieldLot(){return this.lotnum;}
    public JTextField getJTextFieldTaxT(){return this.taxT;}
    public JTextField getJTextFieldProperty(){return this.propertyT;}
    public JTextField getJTextFieldStreetT(){return this.streetT;}
    public JTextField getJTextFieldIntersection(){return this.intersectionT;}
    public JTextArea getJTextFieldDescription(){return this.description;}
    public JTextArea getJTextFieldNotes(){return this.notes;}
    public JTextField getJTextFieldDmi(){return this.dmiText;}
    public JTextField getJTextFieldStruct(){return this.structureText;}
    public JTextField getJTextFieldEnd(){return this.endText;}
    public JTextField getJTextFieldNum(){return this.numText;}
    public JTextField getJTextFieldStreet(){return this.streetText;}
    public JTextField getJTextFieldUnit(){return this.unitText;}
    public JComboBox getJComboxStatus(){return this.statusCombo;}
    public JComboBox getJComboxPre(){return this.preCombo;}
    public JComboBox getJComboxType(){return this.typeCombo;}
    public JComboBox getJComboxPost(){return this.postCombo;}
    public JComboBox getJComboxMsag(){return this.msagCombo;}
    public JButton getButtonSubmit(){return subR;}
    public JButton getButtonNext(){return next;}
    public JButton getButtonPrev(){return previous;}
    public JButton getButtonDel(){return delete;}
    public JButton getButtonZoom(){return zoom;}
    public JButton getButtonSave(){return save;}
    public JButton getButtonAttach(){return addAttachment;}
    public JTable getJtableMain(){return this.addressingView;}
    public JTable getJtableRelated(){return this.relatedTable;}
    public JTable getJtableAttachment(){return this.attachmentT;}
    public JTable getJtableSubdivision(){return this.directoryTable;}
    public JTable getJtablePictures(){return this.picturesTable;}
    public void setTabPane(int i){
        mainTab.setSelectedIndex(i);
    }
    public void searchDirectorySub(String name)
    {
        final int THREADS = Runtime.getRuntime().availableProcessors();
        ExecutorService excu = Executors.newFixedThreadPool(THREADS);
               Future<Object> futu = excu.submit(new Callable<Object>(){
                   public Object call()
                   {
                       searchSub.searchWhat2(name);
                       dTimer  = new Timer(900, new ActionListener()
                             {
                        public void actionPerformed(ActionEvent e)
                        {
                             if(searchSub.getFound1() && searchSub.getFound2() && searchSub.getFound3() && searchSub.getFound4())
                             {
                                 System.out.println("I ran");
                                 dTimer.stop();
                                 for (ObjectDirectory mainData : searchSub.getMainData()) {
                                     subdivisionModel.addRow(new Object[]{mainData.getPath(), mainData.getLocation()});
                                 }
                                
                                 searchSub.setFound(false);
                             }
                        }
                       
                   });
                       dTimer.start();
                       return null;
                   }
                   
               });
        
    }
    public void addPosition(){position++; System.out.println("Position is ");}
    public int getLengthRelated(){return dataRelated.size();}
    public int getPositionLength(){return dataArray.size() - 1;}
    public void begList(){position = 0;}
    public void endList(){position = dataArray.size() - 1;}
    public void subPosition(){position--;}
    public void removeArraylist(){dataArray.remove(position); System.out.println(position);}
    public void removeRecordTable(){model.removeRow(position);}
    public void moveTable(){addressingView.setRowSelectionInterval(position, position);}
    public ArrayList<ObjectData>  getData(){return dataArray;}
    public void changeColorBlue(){this.message.setForeground(Color.BLUE);}
    public void changeColorRed(){this.message.setForeground(Color.RED);}
    public boolean checkIfOld(){return dataArray.get(position).oldRecord();}
    public String getOldId(){return dataArray.get(position).getOldId();}
    public void updateId()
    {
        this.setMessage(dataArray.get(position).getId());
        this.fname.setText(dataArray.get(position).getFname());
        this.lname.setText(dataArray.get(position).getLname());
        this.mainT.setText(dataArray.get(position).getMainT());
        this.alt1.setText(dataArray.get(position).getAlt1());
        this.ofname.setText(dataArray.get(position).getOfname());
        this.olname.setText(dataArray.get(position).getOlname());
        this.subdivision.setText(dataArray.get(position).getSub());
        this.block.setText(dataArray.get(position).getBlock());
        this.lotnum.setText(dataArray.get(position).getLot());
        this.taxT.setText(dataArray.get(position).getTax());
        this.propertyT.setText(dataArray.get(position).getProp());
        this.streetT.setText(dataArray.get(position).getStreet());
        this.intersectionT.setText(dataArray.get(position).getInter());
        this.description.setText(dataArray.get(position).getBuild());
        this.notes.setText(dataArray.get(position).getNotes());
        this.dmiText.setText(dataArray.get(position).getDmiBeg());
        this.structureText.setText(dataArray.get(position).getStruct());
        this.endText.setText(dataArray.get(position).getDmiEnd());
        this.numText.setText(dataArray.get(position).getAddNum());
        this.streetText.setText(dataArray.get(position).getRd());
        this.unitText.setText(dataArray.get(position).getUnit());
        
        if(this.checkIfOld())
        {
            this.changeColorBlue();
        }
        else{
            this.changeColorRed();
        }
       
      
        try{
            this.preCombo.setSelectedItem(dataArray.get(position).getPrd().toString());
        }catch(Exception e)
        {
            
            dataArray.get(position).setPrd("?");
        }
        try{
            this.typeCombo.setSelectedItem(dataArray.get(position).getSts().toString());
        }catch(Exception e)
        {
           dataArray.get(position).setSts("?");
          
        }
         try{
             this.postCombo.setSelectedItem(dataArray.get(position).getPod().toString());
        }catch(Exception e)
        {
           dataArray.get(position).setPod("?");
           
        }
        try{
            msagCombo.setSelectedItem(dataArray.get(position).getMsageCom().toString());
        }catch(Exception e)
        {
           dataArray.get(position).setMsageCom("?");
           
        }
       
        //System.out.println(hey);
//        this.preCombo.setSelectedItem();
//        this.typeCombo.setSelectedItem(dataArray.get(position).getSts());
//        this.postCombo.setSelectedItem(dataArray.get(position).getPod());
//         msagCombo.setSelectedItem(dataArray.get(position).getMsageCom());
//         statusCombo.setSelectedItem(dataArray.get(position).getStatus());
        
    }
   
    public void setTablePosition(){addressingView.setRowSelectionInterval(position, position);}
    public void setMessageText(){counting.setText(String.valueOf(position + 1) + " Of " + String.valueOf(dataArray.size()));}
    public void scrollTable(){addressingView.scrollRectToVisible(addressingView.getCellRect(position, 0, true));}
    public void setUpRecords()
    {
       // System.out.println("Size of the array is " + res.featureCount());
        count2 = 0;
        counting.setText(String.valueOf(position + 1) + " Of " + String.valueOf(res.featureCount()));
        if(res.featureCount() > 0)
        {
            position = 0;
        }
        for (Object data2 : res) 
        {
            Feature f = (Feature)data2;
            data = new ObjectData();
            count2++;
            if(count2 == 1)
            {
               setMessage(f.getAttributeValue("OBJECTID").toString());
               
            }
            data.setId(f.getAttributeValue("OBJECTID").toString());
            data.setFullName((String)f.getAttributeValue("cfull_name"));
            data.setFname((String)f.getAttributeValue("cfirst_name"));
            data.setLname((String)f.getAttributeValue("clast_name"));
            data.setMainT((String)f.getAttributeValue("telephone_land_line"));
            data.setAlt1((String)f.getAttributeValue("alt_telephone"));
            data.setAlt2((String)f.getAttributeValue("alt2_telephone"));
            data.setAlt3((String)f.getAttributeValue("alt3_telephone"));
            data.setAlt4((String)f.getAttributeValue("alt4_telephone"));
            data.setOfname((String)f.getAttributeValue("pfirst_name"));
            data.setOlname((String)f.getAttributeValue("plast_name"));
            data.setSub((String)f.getAttributeValue("subdivision"));
            data.setBlock((String)f.getAttributeValue("block_num"));
            data.setLot((String)f.getAttributeValue("lot_num"));
            data.setTax((String)f.getAttributeValue("tax_account_num"));
            data.setProp((String)f.getAttributeValue("property_id"));
            data.setStreet((String)f.getAttributeValue("street"));
            data.setInter((String)f.getAttributeValue("intersection"));
            data.setBuild((String)f.getAttributeValue("building_description"));
            data.setNotes((String)f.getAttributeValue("comments"));
            data.setMsageCom((String)f.getAttributeValue("msag_comm"));
            data.setRegion((String)f.getAttributeValue("region"));
            data.setStatus((String)f.getAttributeValue("status"));
            data.setOldId((String)f.getAttributeValue("spartan_old_ticket"));
            data.setAddNum(String.valueOf(f.getAttributeValue("add_num")).replace(".0", ""));
            data.setDmiBeg((String)f.getAttributeValue("dmi_beg"));
            data.setDmiEnd((String)f.getAttributeValue("dmi_end"));
            data.setStruct((String)f.getAttributeValue("structure"));
            data.setPrd((String)f.getAttributeValue("prd"));
            data.setRd((String)f.getAttributeValue("rd"));
            data.setSts((String)f.getAttributeValue("sts"));
            data.setPod((String)f.getAttributeValue("pod"));
            data.setUnit((String)f.getAttributeValue("unit"));
            data.setGeometry(f.getGeometry());
            dataArray.add(data);
            model.addRow(new Object[]{f.getAttributeValue("OBJECTID"), 
                f.getAttributeValue("cfull_name"), 
                f.getAttributeValue("msag_comm"), 
                f.getAttributeValue("region")}); 
        }
        ready = true;
        addressingView.setRowSelectionInterval(position, position);
        updateId();
       
        
        
      
    }
    public void startAddingDataRelated()
    {
        //Delete the whole table first...
        relatedModel.setRowCount(0);
        dataRelated.clear();
        for (Object data2 : relatedRes) 
        {
            Feature f = (Feature)data2;
            if(!f.getAttributeValue("OBJECTID").toString().equalsIgnoreCase(dataArray.get(position).getId()))
            {
            relatedData = new ObjectData();
            relatedData.setId(f.getAttributeValue("OBJECTID").toString());
            relatedData.setFullName((String)f.getAttributeValue("cfull_name"));
            relatedData.setProp((String)f.getAttributeValue("property_id"));
            relatedData.setFullAddress((String)f.getAttributeValue("full_address"));
            relatedData.setMsageCom((String)f.getAttributeValue("msag_comm"));
            relatedData.setStatus((String)f.getAttributeValue("status"));
            relatedData.setGeometry(f.getGeometry());
            dataRelated.add(relatedData);
             relatedModel.addRow(new Object[]{f.getAttributeValue("cfull_name"), 
                f.getAttributeValue("property_id"), 
                f.getAttributeValue("full_address"), 
                f.getAttributeValue("msag_comm"),
                f.getAttributeValue("status")});
            }
        }
        
    }
    public void setRegion(String [] arr)
    {
       String [] arr1 = {"See All"};
       int aLen = arr1.length;
       int bLen = arr.length;
       String [] c = new String[aLen + bLen];
       c[0] = arr1[0];
       for(int i = 0; i < arr.length; i++)
       {
           
               c[i+ 1] = arr[i];

          
       }
       
        // System.out.println(Arrays.toString(arr));
        region.setModel(new javax.swing.DefaultComboBoxModel(c));
    }
    public void setMsag(String [] arr)
    {
        String [] arr1 = {"?"};
        int aLen = arr1.length;
        int bLen = arr.length;
        String [] c = new String[aLen + bLen];
        c[0] = arr1[0];
        for(int i = 0; i < arr.length; i++)
        {
            c[i + 1] = arr[i];
        }
        
        msagCombo.setModel(new javax.swing.DefaultComboBoxModel(c));
        msagCombo.setSelectedItem(dataArray.get(position).getMsageCom());
        System.out.println("Hello");
        System.out.println(dataArray.get(position).getStatus());
        statusCombo.setSelectedItem(dataArray.get(position).getStatus());
    }
   
    public void setMessage(String m)
    {
        this.message.setText(m);
    }
           

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        icon = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        message = new javax.swing.JLabel();
        mainTab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        fnameL = new javax.swing.JLabel();
        fname = new javax.swing.JTextField();
        lname = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        try{
            mainT = new javax.swing.JFormattedTextField(new MaskFormatter("'(###')' ###'-####"));
            jLabel5 = new javax.swing.JLabel();
            try{
                alt1 = new javax.swing.JFormattedTextField(new MaskFormatter("'(###')' ###'-####"));
                more = new javax.swing.JButton();
                ofname = new javax.swing.JTextField();
                jLabel6 = new javax.swing.JLabel();
                jLabel7 = new javax.swing.JLabel();
                olname = new javax.swing.JTextField();
                jLabel8 = new javax.swing.JLabel();
                subdivision = new javax.swing.JTextField();
                jLabel9 = new javax.swing.JLabel();
                block = new javax.swing.JTextField();
                streetT = new javax.swing.JTextField();
                jLabel11 = new javax.swing.JLabel();
                propertyT = new javax.swing.JTextField();
                jLabel10 = new javax.swing.JLabel();
                jLabel12 = new javax.swing.JLabel();
                taxT = new javax.swing.JTextField();
                lotnum = new javax.swing.JTextField();
                jLabel13 = new javax.swing.JLabel();
                jLabel14 = new javax.swing.JLabel();
                intersectionT = new javax.swing.JTextField();
                jLabel15 = new javax.swing.JLabel();
                jScrollPane3 = new javax.swing.JScrollPane();
                description = new javax.swing.JTextArea();
                jLabel16 = new javax.swing.JLabel();
                jScrollPane4 = new javax.swing.JScrollPane();
                notes = new javax.swing.JTextArea();
                jPanel2 = new javax.swing.JPanel();
                jScrollPane2 = new javax.swing.JScrollPane();
                attachmentT = new javax.swing.JTable();
                jPanel3 = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                jLabel17 = new javax.swing.JLabel();
                jLabel18 = new javax.swing.JLabel();
                dmiText = new javax.swing.JTextField();
                structureText = new javax.swing.JTextField();
                endText = new javax.swing.JTextField();
                jLabel19 = new javax.swing.JLabel();
                numText = new javax.swing.JTextField();
                jLabel20 = new javax.swing.JLabel();
                preCombo = new javax.swing.JComboBox();
                jLabel21 = new javax.swing.JLabel();
                streetText = new javax.swing.JTextField();
                jLabel22 = new javax.swing.JLabel();
                typeCombo = new javax.swing.JComboBox();
                jLabel23 = new javax.swing.JLabel();
                postCombo = new javax.swing.JComboBox();
                jLabel24 = new javax.swing.JLabel();
                msagCombo = new javax.swing.JComboBox();
                jLabel25 = new javax.swing.JLabel();
                statusCombo = new javax.swing.JComboBox();
                jLabel26 = new javax.swing.JLabel();
                unitText = new javax.swing.JTextField();
                jPanel4 = new javax.swing.JPanel();
                jScrollPane5 = new javax.swing.JScrollPane();
                relatedTable = new javax.swing.JTable();
                jPanel5 = new javax.swing.JPanel();
                jPanel6 = new javax.swing.JPanel();
                jSplitPane1 = new javax.swing.JSplitPane();
                jScrollPane6 = new javax.swing.JScrollPane();
                directoryTable = new javax.swing.JTable();
                jScrollPane7 = new javax.swing.JScrollPane();
                parcelTable = new javax.swing.JTable();
                jPanel7 = new javax.swing.JPanel();
                jScrollPane8 = new javax.swing.JScrollPane();
                picturesTable = new javax.swing.JTable();
                jScrollPane1 = new javax.swing.JScrollPane();
                addressingView = new javax.swing.JTable();
                region = new javax.swing.JComboBox();
                subR = new javax.swing.JButton();
                previous = new javax.swing.JButton();
                next = new javax.swing.JButton();
                save = new javax.swing.JButton();
                delete = new javax.swing.JButton();
                counting = new javax.swing.JLabel();
                zoom = new javax.swing.JButton();
                addAttachment = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

                icon.setIcon(new javax.swing.ImageIcon("911dbastr\\911_DEPT\\04_SYSTEMS\\00_STAFF_PROFILES\\hchapa\\Documents\\NetBeansProjects\\spartan\\img\\create_new_file.png")); // NOI18N

                jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
                jLabel3.setText("Ticket Number: ");

                message.setForeground(new java.awt.Color(255, 51, 51));
                message.setText("number");

                fnameL.setText("First Name");

                jLabel2.setText("Last Name");

                jLabel4.setText("9-1-1 Telephone");

            }catch(Exception e)
            {
            }

            jLabel5.setText("ALT Telephone");

        }catch(Exception e)
        {

        }

        more.setIcon(new javax.swing.ImageIcon("911dbastr\\911_DEPT\\04_SYSTEMS\\00_STAFF_PROFILES\\hchapa\\Documents\\NetBeansProjects\\spartan\\img\\cell_phone_32.png")); // NOI18N
        more.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        more.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moreActionPerformed(evt);
            }
        });

        jLabel6.setText("Owner First Name");

        jLabel7.setText("Owner Last Name");

        jLabel8.setText("Subdivision");

        jLabel9.setText("Block Number");

        jLabel11.setText("Street");

        jLabel10.setText("Property ID");

        jLabel12.setText("Tax Account");

        jLabel13.setText("Lot Number");

        jLabel14.setText("Intersection");

        jLabel15.setText("Building Description");

        description.setColumns(20);
        description.setRows(5);
        jScrollPane3.setViewportView(description);

        jLabel16.setText("Notes");

        notes.setColumns(20);
        notes.setRows(5);
        jScrollPane4.setViewportView(notes);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14)
                                    .addComponent(intersectionT, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lotnum, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(taxT, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addGap(102, 102, 102)
                                        .addComponent(jLabel10))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(ofname, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(olname))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(fname, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6)
                                            .addComponent(fnameL))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel7)
                                            .addComponent(lname, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(95, 95, 95)
                                        .addComponent(jLabel5))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(subdivision)
                                            .addComponent(mainT)
                                            .addComponent(jLabel8)
                                            .addComponent(propertyT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(streetT, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                                                .addComponent(jLabel11))
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel9)
                                                .addComponent(block, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                                                .addComponent(alt1)))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(more, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(fnameL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(more, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(fname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(mainT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(alt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ofname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(olname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subdivision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(block, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel10)
                    .addComponent(jLabel13)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(taxT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(propertyT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lotnum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(streetT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(intersectionT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        mainTab.addTab("Customer Information", jPanel1);

        attachmentT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NAME", "TYPE", "ICON"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(attachmentT);
        if (attachmentT.getColumnModel().getColumnCount() > 0) {
            attachmentT.getColumnModel().getColumn(3).setCellRenderer(new MyRenderer());
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1438, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
        );

        mainTab.addTab("Attachments", jPanel2);

        jLabel1.setText("DMI Beg");

        jLabel17.setText("Structure");

        jLabel18.setText("End");

        jLabel19.setText("Address Number");

        jLabel20.setText("Pre Dir");

        preCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "?", "N", "S", "E", "W" }));

        jLabel21.setText("Street Name");

        jLabel22.setText("Street Type");

        typeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "?", "CIR", "DR", "AVE", "LN", "CT", "RD", "ST", "TRL" }));

        jLabel23.setText("Post Direction");

        postCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "?", "N", "S", "E", "W" }));

        jLabel24.setText("Msag Comm");

        msagCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel25.setText("Status");

        statusCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "?", "ADDRESSING", "DATABASE", "MAPPING", "COMPLETED" }));

        jLabel26.setText("Unit Number");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(statusCombo, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dmiText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                    .addComponent(jLabel25))
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(118, 118, 118)
                                .addComponent(jLabel18)
                                .addGap(147, 147, 147)
                                .addComponent(jLabel19)
                                .addGap(51, 51, 51)
                                .addComponent(jLabel20)
                                .addGap(78, 78, 78)
                                .addComponent(jLabel21)
                                .addGap(76, 76, 76)
                                .addComponent(jLabel22))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(structureText, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(endText, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(numText, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(preCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(streetText, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(typeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addGap(57, 57, 57)
                                .addComponent(jLabel24))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(postCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                                .addComponent(msagCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(103, 103, 103))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addComponent(unitText, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dmiText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(structureText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(preCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(streetText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(postCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(msagCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(241, Short.MAX_VALUE))
        );

        mainTab.addTab("Addressing Department", jPanel3);

        relatedTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Full Name", "Property Id", "Full Address", "Msag Comm", "Department"
            }
        ));
        jScrollPane5.setViewportView(relatedTable);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1438, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
        );

        mainTab.addTab("Related Records", jPanel4);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1438, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 355, Short.MAX_VALUE)
        );

        mainTab.addTab("Old Record", jPanel5);

        directoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Path", "File Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(directoryTable);

        jSplitPane1.setLeftComponent(jScrollPane6);

        parcelTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Owner Name", "Tax Id", "Prop Id", "Legal", "Situs"
            }
        ));
        jScrollPane7.setViewportView(parcelTable);

        jSplitPane1.setRightComponent(jScrollPane7);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1438, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 21, Short.MAX_VALUE)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        mainTab.addTab("Parcel/Subdivision", jPanel6);

        picturesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "FilePath"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(picturesTable);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 1438, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        mainTab.addTab("Pictures", jPanel7);

        addressingView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "OBJECTID", "Full Name", "Msag Comm", "Region"
            }
        ));
        jScrollPane1.setViewportView(addressingView);

        subR.setIcon(new javax.swing.ImageIcon("911dbastr\\911_DEPT\\04_SYSTEMS\\00_STAFF_PROFILES\\hchapa\\Documents\\NetBeansProjects\\spartan\\img\\enter_key_32.png")); // NOI18N
        subR.setText("Submit");

        previous.setIcon(new javax.swing.ImageIcon("911dbastr\\911_DEPT\\04_SYSTEMS\\00_STAFF_PROFILES\\hchapa\\Documents\\NetBeansProjects\\spartan\\img\\previous_32.png")); // NOI18N
        previous.setText("Previous");

        next.setIcon(new javax.swing.ImageIcon("911dbastr\\911_DEPT\\04_SYSTEMS\\00_STAFF_PROFILES\\hchapa\\Documents\\NetBeansProjects\\spartan\\img\\next_32.png")); // NOI18N
        next.setText("Next");
        next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextActionPerformed(evt);
            }
        });

        save.setIcon(new javax.swing.ImageIcon("911dbastr\\911_DEPT\\04_SYSTEMS\\00_STAFF_PROFILES\\hchapa\\Documents\\NetBeansProjects\\spartan\\img\\save_32.png")); // NOI18N
        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        delete.setIcon(new javax.swing.ImageIcon("911dbastr\\911_DEPT\\04_SYSTEMS\\00_STAFF_PROFILES\\hchapa\\Documents\\NetBeansProjects\\spartan\\img\\delete_32.png")); // NOI18N
        delete.setText("Delete");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        counting.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        counting.setText("jLabel1");

        zoom.setIcon(new javax.swing.ImageIcon("911dbastr\\911_DEPT\\04_SYSTEMS\\00_STAFF_PROFILES\\hchapa\\Documents\\NetBeansProjects\\spartan\\img\\center_direction_32.png")); // NOI18N
        zoom.setText("Zoom");

        addAttachment.setIcon(new javax.swing.ImageIcon("911dbastr\\911_DEPT\\04_SYSTEMS\\00_STAFF_PROFILES\\hchapa\\Documents\\NetBeansProjects\\spartan\\img\\attach_32.png")); // NOI18N
        addAttachment.setText("Attachments");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(mainTab, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(icon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(message)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(previous)
                .addGap(2, 2, 2)
                .addComponent(next, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(save, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(delete)
                .addGap(2, 2, 2)
                .addComponent(zoom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(region, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subR)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addAttachment)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(counting))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(icon)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(message)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainTab, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(previous, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(next)
                            .addComponent(save)
                            .addComponent(delete))
                        .addComponent(zoom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(addAttachment, javax.swing.GroupLayout.Alignment.TRAILING)))
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(counting)
                    .addComponent(region, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subR)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void moreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moreActionPerformed
   

    }//GEN-LAST:event_moreActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saveActionPerformed

    private void nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nextActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteActionPerformed

    /**
     * @param args the command line arguments
     */
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addAttachment;
    public javax.swing.JTable addressingView;
    public javax.swing.JFormattedTextField alt1;
    private javax.swing.JTable attachmentT;
    public javax.swing.JTextField block;
    private javax.swing.JLabel counting;
    public javax.swing.JButton delete;
    public javax.swing.JTextArea description;
    private javax.swing.JTable directoryTable;
    private javax.swing.JTextField dmiText;
    private javax.swing.JTextField endText;
    public javax.swing.JTextField fname;
    private javax.swing.JLabel fnameL;
    private javax.swing.JLabel icon;
    public javax.swing.JTextField intersectionT;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSplitPane jSplitPane1;
    public javax.swing.JTextField lname;
    public javax.swing.JTextField lotnum;
    public javax.swing.JFormattedTextField mainT;
    private javax.swing.JTabbedPane mainTab;
    private javax.swing.JLabel message;
    private javax.swing.JButton more;
    private javax.swing.JComboBox msagCombo;
    public javax.swing.JButton next;
    public javax.swing.JTextArea notes;
    private javax.swing.JTextField numText;
    public javax.swing.JTextField ofname;
    public javax.swing.JTextField olname;
    private javax.swing.JTable parcelTable;
    private javax.swing.JTable picturesTable;
    private javax.swing.JComboBox postCombo;
    private javax.swing.JComboBox preCombo;
    public javax.swing.JButton previous;
    public javax.swing.JTextField propertyT;
    private javax.swing.JComboBox region;
    private javax.swing.JTable relatedTable;
    public javax.swing.JButton save;
    private javax.swing.JComboBox statusCombo;
    public javax.swing.JTextField streetT;
    private javax.swing.JTextField streetText;
    private javax.swing.JTextField structureText;
    private javax.swing.JButton subR;
    public javax.swing.JTextField subdivision;
    public javax.swing.JTextField taxT;
    private javax.swing.JComboBox typeCombo;
    private javax.swing.JTextField unitText;
    public javax.swing.JButton zoom;
    // End of variables declaration//GEN-END:variables
}

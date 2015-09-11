/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartanfinal;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.io.EsriSecurityException;
import com.esri.core.io.UserCredentials;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author hchapa
 */
public class ticketSearch {
    private final static String url = 
            "http://gis.lrgvdc911.org/arcgis/rest/services/Dynamic/spartan_dynamic_feature/FeatureServer/0";
    private final static String url2 = "http://gis.lrgvdc911.org/arcgis/rest/services/identify/cam_final_features/MapServer/0";
    private QueryParameters query;
    private QueryTask task;
    private ArrayList<ObjectData> dataArray = new ArrayList();
    private String OBJECTID;
    private SpatialReference in = SpatialReference.create(3857);
    private SpatialReference out = SpatialReference.create(2279);
    private FeatureResult res;
    private FeatureResult flagRes;
    private Geometry geometry;
    private boolean isTicket = false;
    private boolean foundticket = false;
    private boolean flagsfound = false;
    private String first;
    private String last;
    private ObjectData data = new ObjectData();
    public boolean getFoundTicket(){return foundticket;}
    public boolean getFoundFlags(){return flagsfound;}
    public FeatureResult getFlagResult(){return flagRes;}
    public void setFlagResult(){flagRes = null;}
    public void setFoundFlags(boolean t){flagsfound = t;}
    public void setFoundTicket(boolean t){foundticket = t; dataArray.clear();}
    public ArrayList<ObjectData> getListInfo(){return dataArray;}
    public void searchAddressing(String find)
    {
        
        try{
             final UserCredentials credentials = new UserCredentials();
    
        credentials.setUserAccount("hchapa", "cangri1989");
             task = new QueryTask(url, credentials);
             dataArray.clear();
              query = new QueryParameters();
        query.setOutSpatialReference(in);
        query.setReturnGeometry(true);
        query.setOutFields(new String[] {"OBJECTID", "cfull_name","cfirst_name","clast_name","telephone_land_line","alt_telephone",
            "alt2_telephone","alt3_telephone","alt4_telephone","pfirst_name","plast_name","subdivision",
            "block_num","lot_num","tax_account_num","property_id","street","intersection","building_description","comments","add_num","prd", "rd", "sts","pod", "unit",
            "msag_comm", "region", "status","spartan_old_ticket"});
         query.setWhere("status = '" + find + "'");
         
         task.execute(query, new CallbackListener<FeatureResult>(){
               @Override
               public void onError(Throwable e){
                  
                  
               }
               @Override
               public void onCallback(FeatureResult result){
                  if(result.featureCount() != 0)
                  {
                      res = result;
                      setUpInfo();
                     
                 
               }else{
                     
               }
               
               }   
           });
        }catch(Exception e)
        {
            
        }
    }
    public void queryFlagsGeometry(Geometry g)
    {
        try{
            //System.out.println("I ran this query");
            final UserCredentials credentials = new UserCredentials();
    
             credentials.setUserAccount("hchapa", "cangri1989");
            
             QueryTask task2 = new QueryTask(url2, credentials);
             QueryParameters query2 = new QueryParameters();
             query2.setOutSpatialReference(in);
             query2.setReturnGeometry(true);
             query2.setGeometry(g);
             query2.setOutFields(new String[] {"*"});
             task2.execute(query2, new CallbackListener<FeatureResult>(){
               @Override
               public void onError(Throwable e){
                  
                  
               }
               @Override
               public void onCallback(FeatureResult result){
                  // System.out.println("I have callback flags..");
                  // System.out.println("Size of count " + result.featureCount());
                  if(result.featureCount() != 0)
                  {
                     flagsfound = true;
                      flagRes = result;
                     // System.out.println("Size of flag records is " + result.featureCount());
                     // System.out.println("I ran after that ");
                }
                  else{
                     
               }
               
               }   
           });
             
        }catch(Exception e)
        {
            
        }
    }
    public void queryGeometry(Geometry g)
    {
         try{
             final UserCredentials credentials = new UserCredentials();
    
        credentials.setUserAccount("hchapa", "cangri1989");
             task = new QueryTask(url, credentials);
             dataArray.clear();
              query = new QueryParameters();
        query.setOutSpatialReference(in);
        query.setReturnGeometry(true);
        query.setOutFields(new String[] {"OBJECTID", "cfull_name","property_id","full_address",
            "msag_comm","status"});
       
        query.setGeometry(g);
         
         task.execute(query, new CallbackListener<FeatureResult>(){
               @Override
               public void onError(Throwable e){
                  
                  
               }
               @Override
               public void onCallback(FeatureResult result){
                  if(result.featureCount() != 0)
                  {
                     foundticket = true;
                      res = result;
                      
                }
                  else{
                     
               }
               
               }   
           });
          }catch(Exception e)
        {
            
        }
    
    }
    public void setUpInfo()
    {
        
        foundticket = true;
     
    }
    public FeatureResult getResults()
    {
        return res;
    }
    public void searchTelephone(String number)
    {
        
        first = number.substring(6, 9);
        last = number.substring(10);
        dataArray.clear();
        data = new ObjectData();
        final UserCredentials credentials = new UserCredentials();
    
        credentials.setUserAccount("hchapa", "cangri1989");
        try{
             task = new QueryTask(url, credentials);
              query = new QueryParameters();
        query.setOutSpatialReference(in);
        query.setReturnGeometry(true);
        query.setOutFields(new String[] {"OBJECTID", "cfull_name", "full_address", "msag_comm", "status"});
        String queryS = "telephone_land_line like '%" + first + "%' and telephone_land_line like '%" + last + "%' " + 
                " or alt_telephone like '%" + first + "%' and alt_telephone like '%" + last + "%' "
                + " or alt2_telephone like '%" + first + "%' and alt2_telephone like '%" + last + "%' "
                + " or alt3_telephone like '%" + first + "%' and alt3_telephone like '%" + first + "%' or " + 
                " alt4_telephone like '%" + first + "%' and alt4_telephone like '%" + last + "%'";
         query.setWhere(queryS);
         task.execute(query, new CallbackListener<FeatureResult>(){
               @Override
               public void onError(Throwable e){
                   System.out.println(e.getMessage());
                    JOptionPane.showMessageDialog(null, e.getMessage());
                  
               }
               @Override
               public void onCallback(FeatureResult result){
                  if(result.featureCount() != 0)
                  {
                      foundticket = true;
                  for(Object record : result)
                  {
                      Feature feature = (Feature)record;
                      data.setId(feature.getAttributeValue("OBJECTID").toString());
                      data.setFullName(feature.getAttributeValue("cfull_name").toString());
                      data.setFullAddress(feature.getAttributeValue("full_address").toString());
                      data.setMsageCom(feature.getAttributeValue("msag_comm").toString());
                      data.setStatus(feature.getAttributeValue("status").toString());
                      data.setGeometry(feature.getGeometry());
                      dataArray.add(data);
                  }
               }else{
                     
                  }
               
               }   
           });
             
             
        }catch(EsriSecurityException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        
    }
    
   
    
}

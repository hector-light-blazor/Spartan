/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartanfinal;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.io.UserCredentials;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureEditResult;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;
import com.esri.map.ArcGISFeatureLayer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hchapa
 * purpose of this class is to search parcel as well search address point.. and update the information of the ticket..
 */
public class parcelSearch {
    private final static String url = "http://propaccess.hidalgoad.org:6080/arcgis/rest/services/HidalgoWeb/MapServer/1";
    private final static String url2 = "http://gis.lrgvdc911.org/arcgis/rest/services/feature/address_points_streets_feature/MapServer/0";
    private final static String url3 = "http://propaccess.hidalgoad.org:6080/arcgis/rest/services/HidalgoWeb/MapServer/4";
    private QueryParameters query;
    private QueryTask task;
    private ArrayList<dataObject> dataArray = new ArrayList();
    private ArrayList<ObjectParcel> parcelArray = new ArrayList();
    private String OBJECTID;
    private SpatialReference in = SpatialReference.create(3857);
    private SpatialReference out = SpatialReference.create(2279);
    private ArcGISFeatureLayer spartanLayer;
    private Geometry geometry;
    private boolean isTicket = false;
    private boolean foundparcel = false;
    private newTicket ticket;
    private int count;
    public boolean getFoundParcel(){return foundparcel;}
    public void setFoundParcel(boolean f){foundparcel = f;}
    public void setFeatureLayer(ArcGISFeatureLayer layer){spartanLayer = layer;}
    public void setObjectId(String id){OBJECTID = id;}
    public ArrayList<ObjectParcel> getOwnerData(){return parcelArray;}
    public ArrayList<dataObject> getParcelData(){return dataArray;}
    public void searchPropertyId(String id)
    {
        
        task = new QueryTask(url);
         final UserCredentials credentials = new UserCredentials();
    
        credentials.setUserAccount("hchapa", "cangri1989");
                
        System.out.println("OBJECTID IS " + OBJECTID);
         dataArray.clear();
         isTicket = true;
        query = new QueryParameters();
        query.setOutSpatialReference(in);
        query.setReturnGeometry(true);
        query.setOutFields(new String[] {"hcad.SDE.Parcel.PROP_ID", "hcad.sde.map_property_data_vw.geo_id", "hcad.sde.map_property_data_vw.legal_desc"});
        query.setWhere("hcad.SDE.Parcel.PROP_ID = " + id);
         task.execute(query, new CallbackListener<FeatureResult>(){
               @Override
               public void onError(Throwable e){
                   System.out.println(e.getMessage());
                   ticket.setMessage("Error, on the property id");
                   ticket.stopProgressBar();
               }
               @Override
               public void onCallback(FeatureResult result){
                  if(result.featureCount() != 0)
                  {
                  for(Object record : result)
                  {
                      foundparcel = true;
                      Feature feature = (Feature)record;
                      //Update information
                     System.out.println("Feature type " + feature.getGeometry().getType().ENVELOPE);
                      dataArray.add(new dataObject(feature.getAttributeValue("hcad.sde.map_property_data_vw.geo_id").toString(), 
                              feature.getAttributeValue("hcad.SDE.Parcel.PROP_ID").toString(), feature.getGeometry()));
                      updateInformation2();
                      return;
                  }
               }else{
                      ticket.stopProgressBar();
                      ticket.setMessage("ZERO Results with that property id");
                  }
               
               }   
           });
        
    }
    public void searchTaxId(String tax)
    {
        task = new QueryTask(url);
        isTicket = true;
        dataArray.clear();
        foundparcel = false;
        query = new QueryParameters();
        query.setOutSpatialReference(in);
        query.setReturnGeometry(true);
        query.setOutFields(new String[] {"hcad.SDE.Parcel.PROP_ID", "hcad.sde.map_property_data_vw.geo_id", "hcad.sde.map_property_data_vw.legal_desc"});
        query.setWhere("hcda.SDE.map_property_data_vw.geo_id = '" + tax + "'");
        task.execute(query, new CallbackListener<FeatureResult>(){
               @Override
               public void onError(Throwable e){
                   System.out.println(e.getMessage());
               }
               @Override
               public void onCallback(FeatureResult result){
                  
                if(result.featureCount() != 0)
                {
                  for(Object record : result)
                  {
                      foundparcel = true;
                      Feature feature = (Feature)record;
                      //Update information
                      dataArray.add(new dataObject(feature.getAttributeValue("hcad.sde.map_property_data_vw.geo_id").toString(), 
                              feature.getAttributeValue("hcad.SDE.Parcel.PROP_ID").toString(), feature.getGeometry()));
                      updateInformation2();
                      
                      return;
                  }
               }else{
                    ticket.stopProgressBar();
                    ticket.setMessage("ZERO Results with that property id");
                }
            }
               
               
           });
        
    
    }
    public void searchGeometry(Geometry g)
    {
        g = GeometryEngine.project(g, in, out);
        geometry = g;
        task = new QueryTask(url);
        dataArray.clear();
        foundparcel = false;
        query = new QueryParameters();
        query.setOutSpatialReference(SpatialReference.create(3857));
        query.setReturnGeometry(true);       
        query.setOutFields(new String[] {"hcad.SDE.Parcel.PROP_ID", "hcad.sde.map_property_data_vw.geo_id", "hcad.sde.map_property_data_vw.legal_desc"});
        query.setGeometry(g);
         System.out.println(query.getGeometry());
        task.execute(query, new CallbackListener<FeatureResult>(){
               @Override
               public void onError(Throwable e){
                   System.out.println(e.getMessage());
               }
               @Override
               public void onCallback(FeatureResult result){
                  for(Object record : result)
                  {
                      
                      Feature feature = (Feature)record;
                      //Update information
                      dataArray.add(new dataObject(feature.getAttributeValue("hcad.sde.map_property_data_vw.geo_id").toString(), 
                              feature.getAttributeValue("hcad.SDE.Parcel.PROP_ID").toString(), feature.getGeometry()));
                      updateInformation();
                      return;
                  }
                  
               
               }
           });
        
    
     }
     public void searchGeometry2(Geometry g)
    {
        g = GeometryEngine.project(g, in, out);
        geometry = g;
        task = new QueryTask(url);
        dataArray.clear();
        foundparcel = false;
        query = new QueryParameters();
        query.setOutSpatialReference(SpatialReference.create(3857));
        query.setReturnGeometry(true);       
        query.setOutFields(new String[] {"hcad.sde.map_property_data_vw.owner_name",
            "hcad.SDE.Parcel.PROP_ID", "hcad.sde.map_property_data_vw.geo_id",
            "hcad.sde.map_property_data_vw.legal_desc", "hcad.sde.map_property_data_vw.situs"});
        query.setGeometry(g);
         System.out.println(query.getGeometry());
        task.execute(query, new CallbackListener<FeatureResult>(){
               @Override
               public void onError(Throwable e){
                   System.out.println(e.getMessage());
               }
               @Override
               public void onCallback(FeatureResult result){
                  for(Object record : result)
                  {
                      
                      Feature feature = (Feature)record;
                      //Update information
                      dataArray.add(new dataObject(
                              (String) feature.getAttributeValue("hcad.sde.map_property_data_vw.owner_name"),
                              (String) feature.getAttributeValue("hcad.sde.map_property_data_vw.geo_id"), 
                              String.valueOf(feature.getAttributeValue("hcad.SDE.Parcel.PROP_ID")), 
                              (String) feature.getAttributeValue("hcad.sde.map_property_data_vw.legal_desc"),
                              (String) feature.getAttributeValue("hcad.sde.map_property_data_vw.situs"),
                              feature.getGeometry()));
                      
                      foundparcel = true;
                      return;
                  }
                  
               
               }
           });
        
    
     }
    public void searchPropertyOwner(String f, String l)
    {
        parcelArray.clear();
        count = 0;
         task = new QueryTask(url);
        isTicket = true;
        query = new QueryParameters();
        query.setOutSpatialReference(in);
        query.setReturnGeometry(true);
        query.setOutFields(new String[] {"hcad.sde.map_property_data_vw.owner_name",
            "hcad.SDE.Parcel.PROP_ID", 
            "hcad.sde.map_property_data_vw.geo_id"
            ,"hcad.sde.map_property_data_vw.legal_desc",
             "hcad.sde.map_property_data_vw.situs"});
        query.setWhere("hcad.sde.map_property_data_vw.owner_name like '%" + f + "%' and hcad.sde.map_property_data_vw.owner_name like '%" + l + "%'");
        task.execute(query, new CallbackListener<FeatureResult>(){
             @Override
               public void onError(Throwable e){
                   System.out.println(e.getMessage());
               }
               @Override
               public void onCallback(FeatureResult result){
                 
                   int temp = (int) result.featureCount() - 4;
                   int size = (int) result.featureCount();
                   for(Object record : result)
                  {
                      Feature feature = (Feature)record;
                      count++;
                      if(count == 4)
                      {
                          System.out.println("Processing data");
                        parcelArray.add(new ObjectParcel((String) feature.getAttributeValue("hcad.sde.map_property_data_vw.owner_name"),
                        (String) feature.getAttributeValue("hcad.sde.map_property_data_vw.geo_id"),
                         (String) feature.getAttributeValue("hcad.SDE.Parcel.PROP_ID"),
                        (String) feature.getAttributeValue("hcad.sde.map_property_data_vw.legal_desc"), 
                         (String) feature.getAttributeValue("hcad.sde.map_property_data_vw.situs"), feature.getGeometry()));  
                        count = 0;
                        
                      }
                      
                      
                  }
                  
                  if(result.isExceededTransferLimit())
                  {
                      
                      updateFound();
                  }
                  else{
                      updateFound();
                  }
                 
               }
        
        });
    }
     public void searchSubdivision(String sub)
    {
        parcelArray.clear();
        sub = sub.replaceAll(" ", "%");
        count = 0;
         task = new QueryTask(url3);
        isTicket = true;
        System.out.println(sub);
        query = new QueryParameters();
        query.setOutSpatialReference(in);
        query.setReturnGeometry(true);
        query.setOutFields(new String[] {"hcad.sde.map_property_data_vw.owner_name",
            "hcad.SDE.Parcel.PROP_ID", 
            "hcad.sde.map_property_data_vw.geo_id"
            ,"hcad.sde.map_property_data_vw.legal_desc",
             "hcad.sde.map_property_data_vw.situs"});
        query.setWhere("hcad.sde.map_property_data_vw.legal_desc like '%" + sub + "%'");
        task.execute(query, new CallbackListener<FeatureResult>(){
             @Override
               public void onError(Throwable e){
                   System.out.println(e.getMessage());
               }
               @Override
               public void onCallback(FeatureResult result){
                 
                   
                   for(Object record : result)
                  {
                      Feature feature = (Feature)record;
                      count++;
                      if(count == 4)
                      {
                          //System.out.println("Processing data");
                         
                        parcelArray.add(new ObjectParcel((String) feature.getAttributeValue("hcad.sde.map_property_data_vw.owner_name"),
                        (String) feature.getAttributeValue("hcad.sde.map_property_data_vw.geo_id"),
                         String.valueOf(feature.getAttributeValue("hcad.SDE.Parcel.PROP_ID")),
                        (String) feature.getAttributeValue("hcad.sde.map_property_data_vw.legal_desc"), 
                         (String) feature.getAttributeValue("hcad.sde.map_property_data_vw.situs"), feature.getGeometry()));  
                        count = 0;
                        
                      }
                      
                      
                  }
                  
                  if(result.isExceededTransferLimit())
                  {
                      // System.out.println("FOund parcel is ");
                      updateFound();
                  }
                  else{
                      updateFound();
                  }
                 
               }
        
        });
    }
    public void setTicket(newTicket ti){ticket = ti; isTicket = true;}
    public String getTax(){return dataArray.get(0).getTax();}
    public String getProp(){return dataArray.get(0).getProp();}
    public Geometry getGeometry(){return dataArray.get(0).getGeometry();}
    private void updateFound()
    {
       // System.out.println("I am running fuck man");
        foundparcel = true;
    }
    private void updateInformation()
    {
       
        if(dataArray.size() > 0)
        {
            if(this.isTicket)
            {
               // System.out.println("UPdating info i hope");
                ticket.taxT.setText(dataArray.get(0).getTax());
                ticket.propertyT.setText(dataArray.get(0).getProp());
                ticket.update();
            }
            geometry = GeometryEngine.project(geometry, out, in);
            Symbol s = null;
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("tax_account_num", dataArray.get(0).getTax());
        attributes.put("OBJECTID", Integer.parseInt(OBJECTID));
        attributes.put("property_id", dataArray.get(0).getProp());
        Graphic up = new Graphic(geometry, s, attributes);
        Graphic [] update = {up};
        spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{},update , new CallbackListener<FeatureEditResult[][]>(){

    @Override
    public void onCallback(FeatureEditResult[][] result) {
      // do something with the feature edit result object
        
       
           
       if(result[2][0].isSuccess())
       {
           System.out.println("This is success ");
       }
     
    }

        @Override
         public void onError(Throwable e) {
         // handle the error
        }								
     });
        }
    }
     private void updateInformation2()
    {
       
        if(dataArray.size() > 0)
        {
            if(this.isTicket)
            {
                //System.out.println("UPdating info i hope");
                ticket.taxT.setText(dataArray.get(0).getTax());
                ticket.propertyT.setText(dataArray.get(0).getProp());
                
                Polygon polygon = (Polygon) dataArray.get(0).getGeometry();
                Point p = (Point) GeometryEngine.getLabelPointForPolygon(polygon, in);
                dataArray.get(0).setGeometry(p.copy());
                ticket.sendUpdate(foundparcel);
                ticket.update();
//                p = (Point) GeometryEngine.project(p, in, SpatialReference.create(4326));
//                System.out.println("Envelope is " + p.getX() + " " + p.getY());
                //ticket.taxT.setText(dataArray.get(0).getTax());
                //ticket.propertyT.setText(dataArray.get(0).getProp());
                //ticket.update();
            Symbol s = null;
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("tax_account_num", dataArray.get(0).getTax());
        attributes.put("OBJECTID", Integer.parseInt(OBJECTID));
        attributes.put("property_id", dataArray.get(0).getProp());
       
        System.out.println(p);
        Graphic up = new Graphic(p, s, attributes);
        Graphic [] update = {up};
        spartanLayer.applyEdits(new Graphic[]{}, new Graphic[]{},update , new CallbackListener<FeatureEditResult[][]>(){

    @Override
    public void onCallback(FeatureEditResult[][] result) {
      // do something with the feature edit result object
        
       
           
       if(result[2][0].isSuccess())
       {
           System.out.println("This is success ");
       }
     
    }

        @Override
         public void onError(Throwable e) {
         // handle the error
        }								
     });
        }
    }
    }
    class dataObject
    {
        private String fullName;
        private String situs;
        private String tax;
        private String prop;
        private String legal;
        private Geometry geometry;
         public dataObject(String t, String p, Geometry g)
        {
           
            this.tax = t;
            this.prop = p;
            this.geometry = g; 
        }
        public dataObject(String f, String t, String p, String l,String s, Geometry g)
        {
            this.fullName = f;
            this.tax = t;
            this.prop = p;
            this.legal = l;
            this.situs = s;
            this.geometry = g; 
        }
        public String getFull(){return fullName;}
        public String getLegal(){return legal;}
        public String getSitus(){return situs;}
        public String getTax(){return tax;}
        public String getProp(){return prop;}
        public Geometry getGeometry(){return geometry;}
        public void setGeometry(Geometry g){this.geometry = g;}
        
    }
     
}

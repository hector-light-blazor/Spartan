/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartanfinal;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.tasks.query.Order;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author hchapa
 */
public class DownloadInformation{
    private SpatialReference mapReference;
    private final static String msag = "http://gis.lrgvdc911.org/arcgis/rest/services/identify/Msag_Community/MapServer/0";
    private final static String region =
            "http://gis.lrgvdc911.org/arcgis/rest/services/identify/region/MapServer/0";
    private final static String exch = "http://gis.lrgvdc911.org/arcgis/rest/services/Dynamic/exchange_map/MapServer/0";
    private final static String esn = "http://gis.lrgvdc911.org/arcgis/rest/services/identify/esn/MapServer/0";
    private ArrayList<DownloadData> dataRegion = new ArrayList();
    private ArrayList<DownloadData> dataMsag = new ArrayList();
    private ArrayList<DownloadData> dataExch = new ArrayList();
    private ArrayList<DownloadData> dataEsn = new ArrayList();
    private boolean found1 = false;
    private boolean found2 = false;
     private QueryParameters query = new QueryParameters();
     public DownloadInformation()
     {
         try{
            
             
                      System.out.println("I ran first time...");
                      final int THREADS = Runtime.getRuntime().availableProcessors();
               ExecutorService executor = Executors.newFixedThreadPool(THREADS);
               Future<Object> future = executor.submit(new Callable<Object>()
                       {
                          public Object call()
                          {
                              try{
                                   startRegionDownload();
                              }catch(Exception e){
                              }
                              
                                return null;
                          }
                       });
                          
                      ExecutorService executor2 = Executors.newFixedThreadPool(THREADS);
               Future<Object> future2 = executor2.submit(new Callable<Object>()
                       {
                          public Object call()
                          {
                     
                                startMsagDownload();
                                 return null;
                          }
                       });
                          
                       ExecutorService executor3 = Executors.newFixedThreadPool(THREADS);
               Future<Object> future3 = executor3.submit(new Callable<Object>()
                       {
                          public Object call()
                          {
                              try{
                                  
                                  startEsnDownload();
                              }catch(Exception e)
                              {
                                  
                              }
                           
                            return null;
                          }
                       });
                            
                            
                        
                          
                     
                        ExecutorService executor4 = Executors.newFixedThreadPool(THREADS);
               Future<Object> future4 = executor4.submit(new Callable<Object>()
                       {
                          public Object call()
                          {
                             
                            try{startExchDownload();}
                            catch(Exception e){}
                           return null;
                            
                        }
                          
                      });
                     
                     
                 
                
        }catch(Exception e)
        {
            
        }
     }
    public void startRegionDownload() throws Exception
    {
        //First download region...
         QueryTask task = new QueryTask(region);
         Map<String, Order> orderF = new LinkedHashMap<String, Order>();
         orderF.put("REGION_NM", Order.ASC);
        query.setOutFields(new String[] {"REGION_NM"});
       
        query.setOutSpatialReference(SpatialReference.create(3857));
        query.setOrderByFields(orderF);
        query.setReturnGeometry(true);
        query.setWhere("1=1");
         task.execute(query, new CallbackListener<FeatureResult>(){
               @Override
               public void onError(Throwable e){
                  // System.out.println(e.getMessage());
               }
               @Override
               public void onCallback(FeatureResult result){
                  // System.out.println("I return something");
                 found1 = true;
                  for(Object record : result)
                  {
                      
                      Feature feature = (Feature)record;
                       dataRegion.add(new DownloadData(feature.getAttributeValue("REGION_NM").toString(), feature.getGeometry()));
                  }
               }
               
               
           });
  
          
    }
    public boolean getFound(){return found1;}
    public boolean getFound2(){return found2;}
     public void startMsagDownload() 
    {
        //First download region...
      //  System.out.println("I ran");
                     
          QueryTask task = new QueryTask(msag);
           Map<String, Order> orderF = new LinkedHashMap<String, Order>();
         orderF.put("MUNI_NM", Order.ASC);
          QueryParameters query2 = new QueryParameters();
          query2.setOutSpatialReference(SpatialReference.create(3857));
          query2.setOrderByFields(orderF);
          query2.setOutFields(new String[]{"MUNI_NM"});
          query2.setWhere("1=1");
           task.execute(query2, new CallbackListener<FeatureResult>(){
               @Override
               public void onError(Throwable e){
                  // System.out.println(e.getMessage() + " MSAG ");
               }
               @Override
               public void onCallback(FeatureResult result){
                  // System.out.println("I return something");
                 
                  for(Object record : result)
                  {
                      
                      Feature feature = (Feature)record;
                     dataMsag.add(new DownloadData(feature.getAttributeValue("MUNI_NM").toString(), feature.getGeometry()));
                  }
                  found2 = true;
               }
               
               
           });
           
           
       
          
    }
      public void startEsnDownload() throws Exception
    {
       QueryTask task = new QueryTask(esn);
          
          QueryParameters query2 = new QueryParameters();
           query2.setOutSpatialReference(SpatialReference.create(3857));
          query2.setOutFields(new String[]{"ESN_NUM"});
          query2.setWhere("1=1");
           task.execute(query2, new CallbackListener<FeatureResult>(){
               @Override
               public void onError(Throwable e){
                   //System.out.println(e.getMessage());
               }
               @Override
               public void onCallback(FeatureResult result){
                   //System.out.println("I return something");
                 
                  for(Object record : result)
                  {
                      
                      Feature feature = (Feature)record;
                      dataEsn.add(new DownloadData(feature.getAttributeValue("ESN_NUM").toString(), feature.getGeometry()));
                  }
               }
               
               
           });
          
    }
      public void startExchDownload() throws Exception
      {
           QueryTask task4 = new QueryTask(exch);
            QueryParameters query2 = new QueryParameters();
          query2.setOutFields(new String[]{"EXCH"});
          query2.setReturnGeometry(true);
          query2.setWhere("1=1");
           task4.execute(query2, new CallbackListener<FeatureResult>(){
               @Override
               public void onError(Throwable e){
                  // System.out.println(e.getMessage());
               }
               @Override
               public void onCallback(FeatureResult result){
                  // System.out.println("I return something");
                 
                  for(Object record : result)
                  {
                      
                      Feature feature = (Feature)record;
                      dataExch.add(new DownloadData(feature.getAttributeValue("EXCH").toString(), feature.getGeometry()));
                  }
               }
               
               
           });
    }
  
    public String [] setRegionArray()
    {
        String arr [] = new String[dataRegion.size()];
        for(int i = 0; i < dataRegion.size(); i++)
        {
            arr[i] = dataRegion.get(i).getName();
        }
        return arr;
    }
    public String [] setMsagArray()
    {
         String arr [] = new String[dataMsag.size()];
        for(int i = 0; i < dataMsag.size(); i++)
        {
            arr[i] = dataMsag.get(i).getName();
        }
        return arr;
        
    }
    public void setSpatialReference(SpatialReference m){mapReference = m;}
    public String checkRegionContains(Geometry g)
    {
       for(int i = 0; i < dataRegion.size(); i++)
       {
           if(GeometryEngine.contains(dataRegion.get(i).getGeometry(),g, mapReference))
           {
              // System.out.println("REGION is " + dataRegion.get(i).getName());
               return dataRegion.get(i).getName();
           }
          
       }
        return null;
    }
    public String checkEsnContains(Geometry g)
    {
       for(int i = 0; i < dataEsn.size(); i++)
       {
           if(GeometryEngine.contains(dataEsn.get(i).getGeometry(),g, mapReference))
           {
              // System.out.println("ESN is " + dataEsn.get(i).getName());
               return dataEsn.get(i).getName();
           }
          
       }
        return null;
    }
    public String checkExchContains(Geometry g)
    {
       for(int i = 0; i < dataExch.size(); i++)
       {
           if(GeometryEngine.contains(dataExch.get(i).getGeometry(),g, mapReference))
           {
               //System.out.println("ESN is " + dataExch.get(i).getName());
               return dataExch.get(i).getName();
           }
          
       }
        return null;
    }
     public String checkMsagContains(Geometry g)
    {
       for(int i = 0; i < dataMsag.size(); i++)
       {
           if(GeometryEngine.contains(dataMsag.get(i).getGeometry(),g, mapReference))
           {
              // System.out.println("ESN is " + dataMsag.get(i).getName());
               return dataMsag.get(i).getName();
           }
          
       }
        return null;
    }
class DownloadData {
  private String name = "";
  private Geometry geometry = null;
    public DownloadData(String n, Geometry g)
    {
        name = n;
        geometry = g;
    }
    public Geometry getGeometry(){return geometry;}
    public String getName(){return name;}
    
}
}


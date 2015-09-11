/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartanfinal;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author hchapa
 */
public class searchDirectory {
    private String name;
    private final String insideCity = "\\\\911dbastr\\911_DEPT\\08_OLD_RAID\\001_CITY_PLATS";
    private String tempcity = "";
    private boolean found1 = false;
    private ArrayList<ObjectDirectory> main = new ArrayList();
    private String play = "";
    private final String outsideCity = "\\\\911dbastr\\911_dept\\08_OLD_RAID\\000_FOOTNOTES_AND_PLATS";
    private String tempoutside = "";
    private boolean found2 = false;
    private boolean found3 = false;
    private boolean found4 = false;
    private String [] firstHalfInside;
    private String [] secondHalfInside;
    private String [] firstHalfOut;
    private String [] secondHalfOut;
    private File file;
    private int half;
    public void setFound(boolean f){found2 = f;}
    public void searchWhat(String name)
    {
       this.name = name;
       main.clear();
        file = new File(insideCity);
        half = file.list().length / 2;
        firstHalfInside = Arrays.copyOfRange(file.list(), 0, half);
        secondHalfInside = Arrays.copyOfRange(file.list(), half, file.list().length);
        file = new File(outsideCity);
        half = file.list().length / 2;
        firstHalfOut = Arrays.copyOfRange(file.list(), 0, half);
        secondHalfOut = Arrays.copyOfRange(file.list(), half, file.list().length);
        
        
       //Start Asynchronous Thread...
       final int THREADS = Runtime.getRuntime().availableProcessors();
               ExecutorService executor = Executors.newFixedThreadPool(THREADS);
               Future<Object> future = executor.submit(new Callable<Object>()
                       {
                          public Object call()
                          {
                              searchFirstHalfInside();
                              return null;
                          }
                       });
                ExecutorService executor2 = Executors.newFixedThreadPool(THREADS);
               Future<Object> future2 = executor2.submit(new Callable<Object>()
                       {
                          public Object call()
                          {
                              searchSecondHalfInside();
                              return null;
                          }
                       });
               ExecutorService excu = Executors.newFixedThreadPool(THREADS);
               Future<Object> futu = excu.submit(new Callable<Object>(){
                   public Object call()
                   {
                       searchFirstHalfOutSide();
                       return null;
                   }
                   
               });
                ExecutorService excu2 = Executors.newFixedThreadPool(THREADS);
               Future<Object> futu2 = excu.submit(new Callable<Object>(){
                   public Object call()
                   {
                       searchSecondHalfOutSide();
                       return null;
                   }
               });
                   
        
    }
     public void searchWhat2(String name)
    {
       this.name = name;
       System.out.println(name);
       main.clear();
        file = new File(insideCity);
        half = file.list().length / 2;
        firstHalfInside = Arrays.copyOfRange(file.list(), 0, half);
        secondHalfInside = Arrays.copyOfRange(file.list(), half, file.list().length);
        file = new File(outsideCity);
        half = file.list().length / 2;
        firstHalfOut = Arrays.copyOfRange(file.list(), 0, half);
        secondHalfOut = Arrays.copyOfRange(file.list(), half, file.list().length);
        
        
       //Start Asynchronous Thread...
       final int THREADS = Runtime.getRuntime().availableProcessors();
               ExecutorService executor = Executors.newFixedThreadPool(THREADS);
               Future<Object> future = executor.submit(new Callable<Object>()
                       {
                          public Object call()
                          {
                              searchFirstHalfInside2();
                              return null;
                          }
                       });
                ExecutorService executor2 = Executors.newFixedThreadPool(THREADS);
               Future<Object> future2 = executor2.submit(new Callable<Object>()
                       {
                          public Object call()
                          {
                              searchSecondHalfInside2();
                              return null;
                          }
                       });
               ExecutorService excu = Executors.newFixedThreadPool(THREADS);
               Future<Object> futu = excu.submit(new Callable<Object>(){
                   public Object call()
                   {
                       searchFirstHalfOutSide2();
                       return null;
                   }
                   
               });
                ExecutorService excu2 = Executors.newFixedThreadPool(THREADS);
               Future<Object> futu2 = excu.submit(new Callable<Object>(){
                   public Object call()
                   {
                       searchSecondHalfOutSide2();
                       return null;
                   }
               });
                   
        
    }
    public void searchFirstHalfInside()
    {
         //System.out.println("I started inside");
         found1 = false;
         
         
        tempcity = insideCity;
        for(int i = 0; i < secondHalfInside.length; i++)
        {
            play = tempcity + "/" + secondHalfInside[i];
            //System.out.println(play);
            File temp = new File(play);
            if(!temp.isFile())
            {
            for(int x = 0; x < temp.listFiles().length; x++)
            {
               
                processName(name, temp.listFiles()[x].getName(), temp.listFiles()[x].toString());
                
            }
            }else{
                 processName(name, temp.getName(), temp.getAbsolutePath());
            }
        }
       
            found1 = true; //this is to tell the program that it finish..

    }
    public void searchFirstHalfInside2()
    {
         //System.out.println("I started inside");
         found1 = false;
         
         
        tempcity = insideCity;
        for(int i = 0; i < secondHalfInside.length; i++)
        {
            play = tempcity + "/" + secondHalfInside[i];
            //System.out.println(play);
            File temp = new File(play);
            if(!temp.isFile())
            {
            for(int x = 0; x < temp.listFiles().length; x++)
            {
                 
                processName2(name, temp.listFiles()[x].getName(), temp.listFiles()[x].toString());
                
            }
            }else{
                 processName2(name, temp.getName(), temp.getAbsolutePath());
            }
        }
       
            found1 = true; //this is to tell the program that it finish..

    }
    public void searchSecondHalfInside()
    {
         tempcity = insideCity;
         found3 = false;
        for(int i = 0; i < firstHalfInside.length; i++)
        {
            play = tempcity + "/" + firstHalfInside[i];
            //System.out.println(play);
            File temp = new File(play);
            if(!temp.isFile())
            {
            for(int x = 0; x < temp.listFiles().length; x++)
            {
                
                processName(name, temp.listFiles()[x].getName(), temp.listFiles()[x].toString());
                
            }
            }else{
                 processName(name, temp.getName(), temp.getAbsolutePath());
            }
        }
       
            found3 = true; //this is to tell the program that it finish..
    }
    public void searchFirstHalfOutSide()
    {
             //System.out.println("I started outside");
             found2 = false;
       
         // System.out.println("Array list is " + Arrays.toString(firstHalfOut));
          
        tempoutside = outsideCity;
        
        for(int i = 0; i < firstHalfOut.length; i++)
        {
           // System.out.println(firstHalfOut[i]);
                File temp = new File(tempoutside + "/" + firstHalfOut[i]);
            if(!temp.isFile())
            {
            for(int x = 0; x < temp.listFiles().length; x++)
            {
                System.out.println(temp.listFiles()[x].getName());
                processName(name, temp.listFiles()[x].getName(), temp.listFiles()[x].toString());
                
            }
            }else{
                 processName(name, temp.getName(), temp.getAbsolutePath());
                
            }
        }
       
            found2 = true;
           // System.out.println("Found2s is " + found2); //to tell the program it finish as well..

        
        
    }
    public void searchSecondHalfOutSide()
    {
         found4 = false; 
        tempoutside = outsideCity;
        //  System.out.println("Array list is " + Arrays.toString(secondHalfOut));
          
        tempoutside = outsideCity;
        
        for(int i = 0; i < secondHalfOut.length; i++)
        {
           // System.out.println(secondHalfOut[i]);
                File temp = new File(tempoutside + "/" + secondHalfOut[i]);
            if(!temp.isFile())
            {
            for(int x = 0; x < temp.listFiles().length; x++)
            {
                System.out.println(temp.listFiles()[x].getName());
                processName(name, temp.listFiles()[x].getName(), temp.listFiles()[x].toString());
                
            }
            }else{
                 processName(name, temp.getName(), temp.getAbsolutePath());
                
            }
        }
       
            found4 = true;
           // System.out.println("Found2s is " + found2); //to tell the program it finish as well..

        
    }
     public void processName(String finding, String name, String path)
    {
        
       
        if(name.equalsIgnoreCase(finding))
        {
           // System.out.println("Found it");
        }
        else{
            String temp = name.toLowerCase();
            String [] parts = finding.toLowerCase().split(" ");
            int count = 0;
            for (String part : parts) {
                if(temp.contains(part))
                {
                    count++;
                }
            }
            if(parts.length == 1)
            {
                main.add(new ObjectDirectory(name, path));
            }else if(count > 1){
                main.add(new ObjectDirectory(name, path));
            }
        
        }
        
    }
      public void searchSecondHalfInside2()
    {
         tempcity = insideCity;
         found3 = false;
        for(int i = 0; i < firstHalfInside.length; i++)
        {
            play = tempcity + "/" + firstHalfInside[i];
            //System.out.println(play);
            File temp = new File(play);
            if(!temp.isFile())
            {
            for(int x = 0; x < temp.listFiles().length; x++)
            {
                
                processName2(name, temp.listFiles()[x].getName(), temp.listFiles()[x].toString());
                
            }
            }else{
                 processName2(name, temp.getName(), temp.getAbsolutePath());
            }
        }
       
            found3 = true; //this is to tell the program that it finish..
    }
    public void searchFirstHalfOutSide2()
    {
             //System.out.println("I started outside");
             found2 = false;
       
         // System.out.println("Array list is " + Arrays.toString(firstHalfOut));
          
        tempoutside = outsideCity;
        
        for(int i = 0; i < firstHalfOut.length; i++)
        {
           // System.out.println(firstHalfOut[i]);
                File temp = new File(tempoutside + "/" + firstHalfOut[i]);
            if(!temp.isFile())
            {
            for(int x = 0; x < temp.listFiles().length; x++)
            {
                
                processName2(name, temp.listFiles()[x].getName(), temp.listFiles()[x].toString());
                
            }
            }else{
                 processName2(name, temp.getName(), temp.getAbsolutePath());
                
            }
        }
       
            found2 = true;
           // System.out.println("Found2s is " + found2); //to tell the program it finish as well..

        
        
    }
    public void searchSecondHalfOutSide2()
    {
         found4 = false; 
        tempoutside = outsideCity;
        //  System.out.println("Array list is " + Arrays.toString(secondHalfOut));
          
        tempoutside = outsideCity;
        
        for(int i = 0; i < secondHalfOut.length; i++)
        {
           // System.out.println(secondHalfOut[i]);
                File temp = new File(tempoutside + "/" + secondHalfOut[i]);
            if(!temp.isFile())
            {
            for(int x = 0; x < temp.listFiles().length; x++)
            {
                
                processName2(name, temp.listFiles()[x].getName(), temp.listFiles()[x].toString());
                
            }
            }else{
                 processName2(name, temp.getName(), temp.getAbsolutePath());
                
            }
        }
       
            found4 = true;
           // System.out.println("Found2s is " + found2); //to tell the program it finish as well..

        
    }
     public void processName2(String finding, String name, String path)
    {
        
       
        if(name.equalsIgnoreCase(finding))
        {
           // System.out.println("Found it");
        }
        else{
            String temp = name.toLowerCase();
            finding = finding.replace("#", "");
            String [] parts = finding.toLowerCase().split(" ");    
                    if(temp.contains(parts[0]) && temp.contains(parts[1]))
                    {
                        main.add(new ObjectDirectory(name, path));
                    }
                   
            }
            
        
        }
    

     public boolean getFound1(){return found1;}
     public boolean getFound2(){return found2;}
     public boolean getFound3(){return found3;}
     public boolean getFound4(){return found4;}
     public ArrayList<ObjectDirectory> getMainData(){return main;}
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartanfinal;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author hchapa
 */
public class ProcessFiles{
    private int size;
    private String name;
    private String type;
    private byte [] base64;
    private String base;
    private boolean send = false;
    private String id;
    private String [] pieces;
    private File f;
    private String [] location = {"img/document_32.png","img/image_file_32.png", "img/ms_access_32.png", "img/ms_excel_32.png", "img/ms_word_32.png", "img/ms_powerpoint_32.png", "img/pdf_32.png"};
    public ProcessFiles() throws Exception
    {
        name = "";
    }
    public ProcessFiles(File f) throws Exception
    {
        type = null;
        name = f.getName();
        this.f = f;
        setType();
         encodeBase64();
                setUpString();
      
    }
    public void setFile(File file){f = file;name = f.getName();}
    public void setType(String t){type = t;}
    public void setBase64(byte [] b){base64 =b;}
    public void setName(String n){name = n;}
    public void setId(String i){id = i;}
    public String getId(){return id;}
    public void setType()
    {
        int i = name.lastIndexOf(".");
        if(i > 0 && i <  name.length() - 1)
        {
            type = name.substring(i + 1).toLowerCase();
        }
    }
    public void encodeBase64() throws Exception
    {
        byte bytes [] = compress(loadFileAsByteArray(f));
        base64 = Base64.encodeBase64(bytes, true);
         
      
    }
    public byte[] compress(byte[] data) throws IOException{
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        deflater.finish();
        byte[] buffer = new byte[1024];
        while(!deflater.finished())
        {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        byte[] output = outputStream.toByteArray();
       
        return output;
    }
    public byte [] decodeBase64(byte [] sourceFile, String targetFile) throws Exception
    {
        byte [] decodeBytes = decompress(Base64.decodeBase64(sourceFile));
       decodeBytes = decompress(decodeBytes);
        return decodeBytes;
        
    }
    public byte [] decompress(byte [] data) throws IOException, DataFormatException{
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream  = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while(!inflater.finished())
        {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
            
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();
       
        return output;
    }
    public void writeByteArraysToFile(String fileName, byte [] content) throws Exception
    {
        File file = new File(fileName);
        BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file));
        writer.write(content);
        writer.flush();
        writer.close();
    }
    public byte[] loadFileAsByteArray(File file) throws Exception
    {
        
        size = (int) file.length(); 
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
        
        byte [] bytes = new byte[size];
        reader.read(bytes, 0, size);
        reader.close();
        System.out.println("Original bytes " + bytes.length / 1024 + " kb");
        bytes = compress(bytes);
        return bytes;
    }
    
    public void setUpString() throws Exception
    {
        
        if(base64.length < 15728640)
        {
            base = new String(base64);
            send = true;
        }
        else{ //Here we need to break the pieces into many pieces if need too...
           pieces = new String[2];
           base = new String(base64);
            pieces[0] = base.substring(0, base.length()/2);
            pieces[1] = base.substring(base.length()/2, base.length());
        }
    }
    public boolean getSend(){return send;}
    public String getBase(){return base;}
    public byte [] getBase64(){return base64;}
    public String getType(){return type;}
    public String getName(){return name;}
    public int getSize(){return size;}
    public String getImage(){
        
        if(type.equalsIgnoreCase("pdf"))
            return location[6];
        else if(type.equalsIgnoreCase("xlsx") || type.equalsIgnoreCase("xls") || type.equalsIgnoreCase("csv"))
            return location[3];
        else if(type.equalsIgnoreCase("png") || type.equalsIgnoreCase("tif") || type.equalsIgnoreCase("tiff") || type.equalsIgnoreCase("jpg"))
            return location[1];
        else if(type.equalsIgnoreCase("doc") || type.equalsIgnoreCase("docx"))
            return location[4];
        else if(type.equalsIgnoreCase("mdb") || type.equalsIgnoreCase("accdb"))
            return location[2];
        else if(type.equalsIgnoreCase("ppt") || type.equalsIgnoreCase("pptx"))
            return location[5];
        else
            return location[0];
   
    }
    
    public void openFile(File file)
    {
         Desktop desktop = Desktop.getDesktop();
                    if(desktop.isSupported(Desktop.Action.OPEN))
                    {
                       for(Desktop.Action action : Desktop.Action.values())
                       {
                          
                       }
                        try{
                            
                            desktop.open(file);
                        }catch(Exception t)
                        {
                            String [] commands = { "cmd.exe", "/c", "start", "\"test\"", "\"" + file.getAbsolutePath() + "\""};
                            try{
                            Process p = Runtime.getRuntime().exec(commands);
                            p.waitFor();
                            
                            }catch(Exception l)
                            {
                            }
                        }
                    }else{
                        
                        
                    }
        
    }
    
}

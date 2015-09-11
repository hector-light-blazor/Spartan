/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartanfinal;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;

import com.esri.core.symbol.Symbol;

/**
 *
 * @author hchapa
 */
public class ObjectData {
    private String id;
    private String started_ticket;
    private String created_date;
    private String fname;
    private String lname;
    private String full_name;
    private String maint;
    private String alt1;
    private String alt2;
    private String alt3;
    private String alt4;
    private String ofname;
    private String olname;
    private String ofull_name;
    private String subdivision;
    private String blocknumber;
    private String lotnumber;
    private String taxaccount;
    private String propertyid;
    private String street;
    private String intersection;
    private String buildingdescription;
    private String notes;
    private String full_address;
    private String dmi_beg;
    private String dmi_end;
    private String structure;
    private String add_num;
    private String prd;
    private String rd;
    private String sts;
    private String pod;
    private String unit;
    private String msag_com;
    private String state;
    private String region;
    private String status;
    private String oldId;
    private Point location;
    private Geometry zoom;
    private Symbol symbol;
    private boolean oldrecord;
    private boolean startedUpload;
    private boolean foundparcel;

    public ObjectData()
    {
        this.id = "";
        this.started_ticket = "";
        this.created_date = "";
        this.fname = "";
        this.lname = "";
        this.full_name = "";
        this.maint = "";
        this.alt1 = "";
        this.alt2 = "";
        this.alt3 = "";
        this.alt4 = "";
        this.ofname = "";
        this.olname = "";
        this.ofull_name = "";
        this.subdivision = "";
        this.blocknumber = "";
        this.lotnumber = "";
        this.taxaccount = "";
        this.propertyid = "";
        this.street = "";
        this.intersection = "";
        this.buildingdescription = "";
        this.notes = "";
        this.full_address = "";
        this.dmi_beg = "";
        this.dmi_end = "";
        this.structure = "";
        this.add_num = "";
        this.prd = "";
        this.rd = "";
        this.sts = "";
        this.pod = "";
        this.unit = "";
        this.msag_com = "";
        this.state = "";
        this.status = "";
        this.region = "";
        this.oldId = "";
        this.oldrecord = false;
        this.startedUpload = false;
        this.foundparcel = false;
        this.location = new Point();
        
    }
    public ObjectData(String i)
    {
        this.id = i;
        this.started_ticket = "";
        this.created_date = "";
        this.fname = "";
        this.lname = "";
        this.full_name = "";
        this.maint = "";
        this.alt1 = "";
        this.alt2 = "";
        this.alt3 = "";
        this.alt4 = "";
        this.ofname = "";
        this.olname = "";
        this.ofull_name = "";
        this.subdivision = "";
        this.blocknumber = "";
        this.lotnumber = "";
        this.taxaccount = "";
        this.propertyid = "";
        this.street = "";
        this.intersection = "";
        this.buildingdescription = "";
        this.notes = "";
        this.full_address = "";
        this.dmi_beg = "";
        this.dmi_end = "";
        this.structure = "";
        this.add_num = "";
        this.prd = "";
        this.rd = "";
        this.sts = "";
        this.pod = "";
        this.unit = "";
        this.msag_com = "";
        this.state = "";
        this.status = "";
        this.region = "";
        this.oldId = "";
        this.oldrecord = false;
        this.startedUpload = false;
        this.foundparcel = false;
        this.location = new Point();
    }
    
    
    //Set Methods
    public void setId(String i){id = i;}
    public void setStarted(String s){started_ticket = s;}
    public void setCreatedDate(String d){created_date = d;}
    public void setFname(String f){fname = f;}
    public void setLname(String l){lname = l;}
    public void setFullName(String f){full_name = f;}
    public void setMainT(String t){maint = t;}
    public void setAlt1(String a){alt1 = a;}
    public void setAlt2(String a){alt2 = a;}
    public void setAlt3(String a){alt3 = a;}
    public void setAlt4(String a){alt4 = a;}
    public void setOfname(String o){ofname = o;}
    public void setOlname(String o){olname = o;}
    public void setOFullName(String f){ofull_name = f;}
    public void setSub(String s){subdivision = s;}
    public void setBlock(String b){blocknumber = b;}
    public void setLot(String l){lotnumber = l;}
    public void setTax(String t){taxaccount = t;}
    public void setProp(String p){propertyid = p;}
    public void setStreet(String t){street = t;}
    public void setInter(String i){intersection = i;}
    public void setBuild(String b){buildingdescription = b;}
    public void setNotes(String n){notes = n;}
    public void setFullAddress(String a){full_address = a;}
    public void setMsageCom(String m){msag_com = m;}
    public void setState(String s){state = s;}
    public void setStatus(String s){status = s;}
    public void setStartedUpload(boolean s){startedUpload = s;}
    public void setPosition(Point p){location = p;}
    public void setGeometry(Geometry g){g.copyTo(location);zoom = g;}
    public void setSymbol(Symbol s){symbol = s;}
    public void setFound(boolean f){this.foundparcel = f;}
    public void setRegion(String r){this.region = r;}
    public void setOldId(String r)
    {this.oldId = r; 
      
    }
    public void setDmiBeg(String d){this.dmi_beg = d;}
    public void setDmiEnd(String e){this.dmi_end = e;}
    public void setStruct(String s){this.structure = s;}
    public void setAddNum(String a){this.add_num = a;}
    public void setPrd(String p){this.prd = p;}
    public void setRd(String r){this.rd = r;}
    public void setSts(String s){this.sts = s;}
    public void setPod(String p){this.pod = p;}
    public void setUnit(String u){this.unit = u;}
    //Get Methods
    public String getId(){return id;}
    public String getFname(){return fname;}
    public String getLname(){return lname;}
    public String getFullName(){return full_name;}
    public String getMainT(){return maint;}
    public String getAlt1(){return alt1;}
    public String getAlt2(){return alt2;}
    public String getAlt3(){return alt3;}
    public String getAlt4(){return alt4;}
    public String getOfname(){return ofname;}
    public String getOlname(){return olname;}
    public String getOFullName(){return ofull_name;}
    public String getSub(){return subdivision;}
    public String getBlock(){return blocknumber;}
    public String getLot(){return lotnumber;}
    public String getTax(){return taxaccount;}
    public String getProp(){return propertyid;}
    public String getStreet(){return street;}
    public String getInter(){return intersection;}
    public String getBuild(){return buildingdescription;}
    public String getNotes(){return notes;}
    public String getFullAddress(){return full_address;}
    public String getMsageCom(){return msag_com;}
    public String getStatus(){return status;}
    public boolean getUpload(){return startedUpload;}
    public Point getLocation(){return location;}
    public Geometry getGeometry(){return zoom;}
    public Symbol getSymbol(){return symbol;}
    public String getOldId(){return oldId;}
    public String getDmiBeg(){return this.dmi_beg;}
    public String getDmiEnd(){return this.dmi_end;}
    public String getStruct(){return this.structure;}
    public String getAddNum(){return this.add_num;}
    public String getPrd(){return this.prd;}
    public String getRd(){return this.rd;}
    public String getSts(){return this.sts;}
    public String getPod(){return this.pod;}
    public String getUnit(){return this.unit;}
    
    public boolean oldRecord(){
       try{
        if(this.oldId.contains("W") || this.oldId.contains("H"))
        {
            oldrecord = true;
            
        }
       }catch(Exception e)
       {
           oldrecord = false;
       }
        return oldrecord;
    }
    public boolean getFound(){return foundparcel;}
    public String getRegion(){return region;}
    
    //Other Methods..
    
    
}

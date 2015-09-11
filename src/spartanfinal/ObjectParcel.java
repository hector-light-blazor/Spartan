/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartanfinal;

import com.esri.core.geometry.Geometry;
import com.esri.core.symbol.Symbol;

/**
 *
 * @author hchapa
 */
public class ObjectParcel {
    private String ownerName;
    private String geoId;
    private String propid;
    private String legal;
    private String situs;
    private Geometry geometry;
    private Symbol symbol;
    
    public ObjectParcel(String o, String g, String p, String l, String s, Geometry ge)
    {
        this.ownerName = o;
        this.geoId = g;
        this.propid = p;
        this.legal = l;
        this.situs = s;
        this.geometry = ge;
        //this.symbol = sym;
    }
    public String getOwner(){return ownerName;}
    public String getGeo(){return geoId;}
    public String getProp(){return propid;}
    public String getLegal(){return legal;}
    public String getSitus(){return situs;}
    public Geometry getGeometry(){return geometry;}
    public Symbol getSymbol(){return symbol;}
    
}

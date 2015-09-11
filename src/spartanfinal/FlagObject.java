/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartanfinal;

import com.esri.core.geometry.Geometry;

/**
 *
 * @author hchapa
 */
public class FlagObject {
    
    private String location;
    private Geometry geometry;
    public Geometry getGeometry(){return geometry;}
    public String getLocation(){return location;}
    public void setGeometry(Geometry g){geometry = g;}
    public void setLoction(String l){location = l;}
    
    public FlagObject(String l, Geometry g)
    {
        this.location = l;
        this.geometry = g;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartanfinal;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.tasks.geocode.Locator;
import com.esri.core.tasks.geocode.LocatorFindParameters;
import com.esri.core.tasks.geocode.LocatorGeocodeResult;
import com.esri.map.ArcGISFeatureLayer;
import java.util.List;
import javafx.application.Platform;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author hchapa
 */
public class geocoderSearch {
    private final static String geoURL = "http://gis.lrgvdc911.org/arcgis/rest/services/GEOCODERS/Main_Composite/GeocodeServer";
    Locator locator = Locator.createOnlineLocator(geoURL);
    private String message;
    private ArcGISFeatureLayer spartanLayer;
    private final static String url = "";
    LocatorGeocodeResult zoom;
    private boolean ready = false;
    public geocoderSearch()
    {
        message = "";
    }
    public void search10Stuff(String s)
    {
        LocatorFindParameters parameters = new LocatorFindParameters(s);
        parameters.setMaxLocations(10);
        parameters.setOutSR(SpatialReference.create(3857));
        locator.find(parameters, new CallbackListener<List<LocatorGeocodeResult>>(){
             @Override
             public void onError(Throwable e)
             {
            
                }
        @Override
        public void onCallback(List<LocatorGeocodeResult> results)
        {
            if(results != null)
            {
                displayResults(results);
                return;
            }
            
            
        }
    });
        
    }
    public void displayResults(List<LocatorGeocodeResult> results)
    {
        if(results.size() == 0)
        {
            errorTray();
            return;
        }
        ready = true;
        zoom = results.get(0);
        
    }
    public LocatorGeocodeResult getResult()
    {
        ready = false;
        
        return zoom;
        
    }
    public boolean getReady(){return ready;}
    public void errorTray()
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                TrayNotification tray = new TrayNotification(); 
                 tray.setMessage("Sorry Couldn't find " + message);
        tray.setTitle("Geocoder Information");
        tray.setNotificationType(NotificationType.ERROR);
        tray.setAnimationType(AnimationType.POPUP);
       
            tray.showAndDismiss(Duration.seconds(2));
            }
       });
    }
    public void setMessage(String m){message = m;}
    
}

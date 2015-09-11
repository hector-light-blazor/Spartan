package spartanfinal;

public class ObjectDirectory {
    private String name;
    private String path;
    public ObjectDirectory()
    {
        name = "";
        path = "";
    }
    public ObjectDirectory(String name, String pa)
    {
        this.name = name;
        this.path = pa;
    }
    public void setPath(String p){path = p;}
    public void setLocation(String l){name = l;}
    public String getPath(){return path;}
    public String getLocation(){return name;}
    
    
}

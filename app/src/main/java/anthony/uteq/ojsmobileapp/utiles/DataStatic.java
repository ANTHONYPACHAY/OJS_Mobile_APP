package anthony.uteq.ojsmobileapp.utiles;

public class DataStatic {

    private static String urlDomain = "https://revistas.uteq.edu.ec";

    private static String webservices = "/ws/";

    public static String gerUrlApi(String servicePath){
        return urlDomain + webservices + servicePath;
    }

}

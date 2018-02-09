package registration.network;

public class NetworkConfig {
    private NetworkConfig() { }

    // 192.168.8.100
    public final static String SERVER_ADDRESS = "127.0.0.1";
    public final static String USERNAME = "root";
    public final static String PASSWORD = "raniel1206";
    public final static String URL = "jdbc:mysql://" + SERVER_ADDRESS + ":3306/";
    public final static String DATABASE_NAME = "ie_convention";

}

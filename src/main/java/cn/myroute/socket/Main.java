package cn.myroute.socket;
import java.net.ServerSocket;  
import java.net.Socket;  
  
public class Main {  
	public static boolean printLog = "1".equals(System.getenv("printLog"));
	
    /** 
     * @param args 
     */  
    public static void main(String[] args) throws Exception {  
    	int port = 8888;
    	if(args.length >= 1){
    		port = Integer.valueOf(args[0]);
    	}
        ServerSocket serverSocket = new ServerSocket(port);  
        TLogger.log("listing on port :" + port + ", printLog:"+printLog);
        DnsUtil.initDns();
        while (true) {  
            Socket socket = null;  
            try {  
                socket = serverSocket.accept();  
                new ProxyController(socket).start();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
}  
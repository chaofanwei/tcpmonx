package cn.myroute.socket;
import java.net.ServerSocket;  
import java.net.Socket;  
  
public class Main {  
    /** 
     * @param args 
     */  
    public static void main(String[] args) throws Exception {  
    	int port = 98;
    	if(args.length >= 1){
    		port = Integer.valueOf(args[0]);
    	}
        ServerSocket serverSocket = new ServerSocket(port);  
        System.out.println("listing on port :" + port);
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
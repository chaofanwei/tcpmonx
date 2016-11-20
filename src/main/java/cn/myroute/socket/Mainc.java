package cn.myroute.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Mainc {

	public static void main(String[] args) throws Exception {
		//args = new String[]{"13307","127.0.0.1","12207"};
		int port = 98;
		String targetIP = "127.0.0.1";
		int targetPort= 90;
    	if(args.length >= 3){
    		port = Integer.valueOf(args[0]);
    		targetIP = args[1];
    		targetPort = Integer.valueOf(args[2]);
    	}
    	
    	
        ServerSocket serverSocket = new ServerSocket(port);  
        TLogger.log("listing on port :" + port+" , targetIP:"+targetIP+",targetPort:"+targetPort);
        while (true) {  
            try {  
            	final Socket socket = serverSocket.accept(); 
            	System.out.println("new connection:"+socket);
                new Controller(socket,targetIP,targetPort).start();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  

	}

	static class Controller extends Thread{
		Socket inSocket;
		String targetIP;
		int targetPort;
		public Controller(Socket socket,String targetIP,int targetPort) {
			this.inSocket = socket;
			this.targetIP=targetIP;
			this.targetPort=targetPort;
		}

		@Override
		public void run() {
			Socket outSocket=null;
			try {
				InputStream isIn = inSocket.getInputStream();
				OutputStream isOut =inSocket.getOutputStream();
				
				outSocket= new Socket(targetIP, targetPort);
				
				InputStream osIn = outSocket.getInputStream();
				OutputStream osOut =outSocket.getOutputStream();
				
				SocketThreadOutput out= new SocketThreadOutput(isIn, osOut);
				SocketThreadInput in = new SocketThreadInput(osIn, isOut);
				out.start();
				in.start();
				out.join();
				in.join();
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(null != inSocket){
					try {
						System.out.println("exit connection:"+inSocket);
						inSocket.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != outSocket){
					try {
						outSocket.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
}

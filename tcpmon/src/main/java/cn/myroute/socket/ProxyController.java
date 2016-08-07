package cn.myroute.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ProxyController  extends Thread{
	protected Socket socketIn;  
    
    public ProxyController(Socket socket) {  
        this.socketIn = socket;  
    }  

    public void run() {  
    	Socket socketOut = null;
    	byte[] buffer = new byte[4096];
        try {  
            System.out.println("\n\na client connect " + socketIn.getInetAddress() + ":" + socketIn.getPort());  
            InputStream isIn = socketIn.getInputStream();  
            OutputStream osIn = socketIn.getOutputStream();  
            int len = isIn.read(buffer);  
            System.out.println("< " + Util.bytesToHexString(buffer, 0, len));
            
            SocketProxy socketProxy = null;
            
            if(buffer[0] == 4){
            	socketProxy = new SocketProxy4Impl();
            }else if (buffer[0] == 5) {
            	socketProxy = new SocketProxy5Impl();
			}else{
				throw new IllegalArgumentException("proxy version not supported ! " + buffer[0]);
			}
            
            socketOut = socketProxy.authCheck(isIn,osIn,buffer);
            
            InputStream isOut = socketOut.getInputStream();  
            OutputStream osOut = socketOut.getOutputStream(); 
             
            SocketThreadOutput out = new SocketThreadOutput(isIn, osOut);  
            out.start();  
            SocketThreadInput in = new SocketThreadInput(isOut, osIn);  
            in.start();  
            out.join();  
            in.join();  
        } catch (Exception e) {  
            System.out.println("a client leave"); 
            e.printStackTrace();
        } finally {  
            try {  
                if (socketIn != null) {  
                    socketIn.close();  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
            try {  
            	if (socketOut != null) {  
            		socketOut.close();  
            	}  
            } catch (Exception e) {  
            	e.printStackTrace();  
            }  
        }  
        System.out.println("socket close");  
    }  
}

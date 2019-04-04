package cn.myroute.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class ProxyController  extends Thread{
	protected Socket socketIn;  
    
    public ProxyController(Socket socket) {  
        this.socketIn = socket;  
    }  

    public void run() {  
    	Socket socketOut = null;
    	byte[] buffer = new byte[4096];
        try {  
            TLogger.log("\n\na client connect " + socketIn.getInetAddress() + ":" + socketIn.getPort());  
            InputStream isIn = socketIn.getInputStream();  
            OutputStream osIn = socketIn.getOutputStream();  
            int len = isIn.read(buffer);  
            TLogger.log("< " + Util.bytesToHexString(buffer, 0, len));
            
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
            CountDownLatch countDownLatch = new CountDownLatch(1);
            SocketThreadOutput out = new SocketThreadOutput(isIn, osOut, countDownLatch);  
            out.start();  
            SocketThreadInput in = new SocketThreadInput(isOut, osIn, countDownLatch);  
            in.start();  
            
            countDownLatch.await();
        	if(!out.isClosed()){
        		Thread.sleep(20);
            	out.interrupt();
            }
        	if(!in.isClosed()){
        		Thread.sleep(20);
        		out.interrupt();
        	}
        	TLogger.log("\n\na client connect end " + socketIn.getInetAddress() + ":" + socketIn.getPort());  
        } catch (Exception e) {  
            TLogger.log("a client leave,e:"+e.getLocalizedMessage()); 
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
        TLogger.log("socket close");  
    }  
}

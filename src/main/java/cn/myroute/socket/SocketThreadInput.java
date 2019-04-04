package cn.myroute.socket;
/** 
 * * 从外部读取，向内部发送信息 
 */  
import java.io.InputStream;  
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;  
  
public class SocketThreadInput extends Thread {  
    private InputStream isOut;  
    private OutputStream osIn;  
    private boolean isClosed =false;
    CountDownLatch countDownLatch;
  
    public SocketThreadInput(InputStream isOut, OutputStream osIn, CountDownLatch countDownLatch) {  
        this.isOut = isOut;  
        this.osIn = osIn;  
        this.countDownLatch = countDownLatch;
    }  
    public SocketThreadInput(InputStream isIn, OutputStream osOut) {  
    	this(isIn, osOut, null);
    } 
  
    private byte[] buffer = new byte[409600];  
  
    public void run() {  
    	long totalBytes = 0; 
        try {  
            int len; 
            boolean first = true;
            while ((len = isOut.read(buffer)) != -1) {  
                if (len > 0) {  
                	//TLogger.log(new String(buffer, 0, len));
                	//if(Main.printLog){
                	totalBytes += len;
                	if(first){
                		first = false;
                		int printMaxBytes = len > 512 ? 512 : len;
                		System.out.println("in hex:"+Util.bytesToHexString(buffer,printMaxBytes));
                    	System.out.println("in string:\r\n"+new String(buffer,0,printMaxBytes));
                	}
                	//}
                    osIn.write(buffer, 0, len);  
                    osIn.flush();  
                }  
            }  
        } catch (Exception e) {  
        	TLogger.log("SocketThreadInput leave,e:"+e.getLocalizedMessage());  
        }  
        System.out.println("totalBytes:"+totalBytes);
        setClosed(true);
        if(countDownLatch != null){
            countDownLatch.countDown();
        }
    }  
    
	public boolean isClosed() {
		return isClosed;
	}

	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}
}  

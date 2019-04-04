package cn.myroute.socket;
import java.io.InputStream;  
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;  
  
/** 
 * 从内部读取，向外部发送信息 
 *  
 * @author zxq 
 *  
 */  
public class SocketThreadOutput extends Thread {  
    private InputStream isIn;  
    private OutputStream osOut;  
    private boolean isClosed =false;
    CountDownLatch countDownLatch;
    public SocketThreadOutput(InputStream isIn, OutputStream osOut, CountDownLatch countDownLatch) {  
        this.isIn = isIn;  
        this.osOut = osOut;  
        this.countDownLatch = countDownLatch;
    }  
    public SocketThreadOutput(InputStream isIn, OutputStream osOut) {  
    	this(isIn, osOut, null);
    }  
  
    private byte[] buffer = new byte[409600];  
  
    public void run() {  
        try {  
            int len;  
            while ((len = isIn.read(buffer)) != -1) {  
                if (len > 0) {  
                	//TLogger.log(new String(buffer, 0, len));
                	//if(Main.printLog){
            		System.out.println("out hex:"+Util.bytesToHexString(buffer,len));
                	System.out.println("out string:\r\n"+new String(buffer,0,len));
                	//}
                    osOut.write(buffer, 0, len);  
                    osOut.flush();  
                }  
            }  
        } catch (Exception e) {  
        	TLogger.log("SocketThreadOutput leave,e:"+e.getLocalizedMessage());  
        }  
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
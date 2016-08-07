package cn.myroute.socket;
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;  
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
  
public class SocketThread5 extends Thread {  
    private Socket socketIn;  
    private InputStream isIn;  
    private OutputStream osIn;  
    //  
    private Socket socketOut;  
    private InputStream isOut;  
    private OutputStream osOut;  
  
    public SocketThread5(Socket socket) {  
        this.socketIn = socket;  
    }  
  
    private byte[] buffer = new byte[4096];  
    private static final byte[] VER = { 0x5, 0x0 };  
    private static final byte[] CONNECT_OK = { 0x5, 0x0, 0x0, 0x1, 0, 0, 0, 0, 0, 0 };  
  
    public void run() {  
        try {  
            System.out.println("\n\na client connect " + socketIn.getInetAddress() + ":" + socketIn.getPort());  
            isIn = socketIn.getInputStream();  
            osIn = socketIn.getOutputStream();  
            int len = isIn.read(buffer);  
            System.out.println("< " + bytesToHexString(buffer, 0, len));  
            authCheck(buffer, 0, len);
            osIn.write(VER);  
            osIn.flush();  
            System.out.println("> " + bytesToHexString(VER, 0, VER.length));  
            len = isIn.read(buffer);  
            System.out.println("< " + bytesToHexString(buffer, 0, len));  
            // 查找主机和端口  
          //  String host = findHost(buffer, 4, 7);  
          //  int port = findPort(buffer, 8, 9);  
            String host = findHost(buffer, 0,len);  
            int port = findPort(buffer,0,len);  
            System.out.println("host=" + host + ",port=" + port);  
            
            socketOut = new Socket();
            bindSocket(socketOut);
            System.out.println("target:"+host + ":" + port);
            socketOut.connect(new InetSocketAddress(host, port));
            
            isOut = socketOut.getInputStream();  
            osOut = socketOut.getOutputStream();  
            //  
            for (int i = 4; i <= 9; i++) {  
                CONNECT_OK[i] = buffer[i];  
            }  
            osIn.write(CONNECT_OK);  
            osIn.flush();  
            System.out.println("> " + bytesToHexString(CONNECT_OK, 0, CONNECT_OK.length));  
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
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        System.out.println("socket close");  
    }  
  
    void authCheck(byte[] bArray, int begin, int end){
    	if(bArray[begin] != 0x05){
    		System.out.println("version : " + bArray[0] +",not supported");
    		System.exit(-4);
    	}
    }
    
    private void bindSocket(Socket outSocket2) {
    	String bindIp = System.getProperty("bindIp");
    	if ((bindIp == null) || bindIp.trim().equals("")) {
	        try {
	            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
	            NetworkInterface networkInterface;
	            Enumeration<InetAddress> inetAddresses;
	            InetAddress inetAddress;
	            String ip;
	            while (networkInterfaces.hasMoreElements()) {
	                networkInterface = networkInterfaces.nextElement();
	                inetAddresses = networkInterface.getInetAddresses();
	                while (inetAddresses.hasMoreElements()) {
	                    inetAddress = inetAddresses.nextElement();
	                    if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
	                    	if(!inetAddress.isLoopbackAddress()){
	                    		 ip = inetAddress.getHostAddress();
	                             if(ip.startsWith("10.10.17")){
	                            	 bindIp = ip;
	                             }
	                    	}
	                    }
	                }
	            }
	        } catch (SocketException e) {
	            e.printStackTrace();
	        }
        }
    	if ((bindIp != null) && !bindIp.trim().equals("")) {
    		try {
				outSocket2.bind(new InetSocketAddress(bindIp, 0));
				System.out.println("success bind ip : " + bindIp);
			} catch (IOException e) {
				System.err.println("bindIp : " + bindIp);
				e.printStackTrace();
			}
    	}
	}
    
    public static String findHost(byte[] bArray, int begin, int end) {  
    	
    	switch (bArray[3]) {
		case 1://ipv4
			return findHost2(bArray,4,7);
		case 3://dns name
			
			return findHostName(bArray,5,5 + bArray[4]);

		default:
			break;
		}
    	System.err.println("not supported " + bArray[3] + ",maybe ipv6");
        return "error";  
    }  
    public static String findHost2(byte[] bArray, int begin, int end) {  
    	StringBuffer sb = new StringBuffer();  
    	for (int i = begin; i <= end; i++) {  
    		sb.append(Integer.toString(0xFF & bArray[i]));  
    		sb.append(".");  
    	}  
    	sb.deleteCharAt(sb.length() - 1);  
    	return sb.toString();  
    }  
    public static String findHostName(byte[] bArray, int begin, int end) {  
    	StringBuffer sb = new StringBuffer();  
    	for (int i = begin; i < end; i++) {  
    		sb.append((char)bArray[i]);  
    	}  
    	String domain= sb.toString();
    	try {
    		
			return InetAddress.getByName(domain).getHostAddress();
		} catch (UnknownHostException e) {
			System.err.println("domain:"+domain);
			e.printStackTrace();
		}  
    	return "127.0.0.1";
    }  
  
    public static int findPort(byte[] bArray, int begin, int end) {  
        int port = 0;  
        begin = end -2;
        end = end -1;
        for (int i = begin; i <= end; i++) {  
            port <<= 16;  
            port += bArray[i];  
        }  
        return port;  
    }  
  
    // 4A 7D EB 69  
    // 74 125 235 105  
    public static final String bytesToHexString(byte[] bArray, int begin, int end) {  
        StringBuffer sb = new StringBuffer(bArray.length);  
        String sTemp;  
        for (int i = begin; i < end; i++) {  
            sTemp = Integer.toHexString(0xFF & bArray[i]);  
            if (sTemp.length() < 2)  
                sb.append(0);  
            sb.append(sTemp.toUpperCase());  
            sb.append(" ");  
        }  
        return sb.toString();  
    }  
}  
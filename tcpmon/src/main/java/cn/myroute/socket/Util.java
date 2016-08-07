package cn.myroute.socket;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Util {
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
    
    public static void bindSocket(Socket outSocket2) {
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
    
    public static String findHostIp(byte[] bArray, int begin, int end) {  
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
        for (int i = begin; i <= end; i++) {  
            port <<= 16;  
            port += bArray[i];  
        }  
        return port;  
    }  
    
}

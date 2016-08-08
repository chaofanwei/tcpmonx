package cn.myroute.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

//http://www.ietf.org/rfc/rfc1928.txt

public class SocketProxy5Impl implements SocketProxy{

	 private static final byte[] VER = { 0x5, 0x0 };  
	 private static final byte[] CONNECT_OK = { 0x5, 0x0, 0x0, 0x1, 0, 0, 0, 0, 0, 0 };
	    
	@Override
	public Socket authCheck(InputStream isIn, OutputStream osIn,byte[] buffer) throws Exception  {
		
		int aSize = buffer[1];
		List<Byte> aMethod= new LinkedList<Byte>();
		for(int i = 2;i < 2 + aSize;i++){
			aMethod.add(buffer[i]);
		}
		if(!aMethod.contains((byte)0)){
			throw new IllegalArgumentException("aMethod:"+aMethod);
		}
		
		osIn.write(VER);  
        osIn.flush();  
        System.out.println("> " + Util.bytesToHexString(VER, 0, VER.length));  
        int len = isIn.read(buffer);  
        System.out.println("< " + Util.bytesToHexString(buffer, 0, len));  
        // 查找主机和端口  
      //  String host = findHost(buffer, 4, 7);  
      //  int port = findPort(buffer, 8, 9);  
        String host = findHost(buffer, 0,len);
        int port = Util.findPort(buffer,len-2,len-1);  
        System.out.println("host=" + host + ",port=" + port);  
        
        Socket socketOut = new Socket();
        Util.bindSocket(socketOut);
        System.out.println("target:"+host + ":" + port);
        socketOut.connect(new InetSocketAddress(host, port));
        
        for (int i = 4; i <= 9; i++) {  
            CONNECT_OK[i] = buffer[i];  
        }  
        osIn.write(CONNECT_OK);  
        osIn.flush();  
        System.out.println("> " + Util.bytesToHexString(CONNECT_OK, 0, CONNECT_OK.length)); 
        
		return socketOut;
	}

	 public static String findHost(byte[] bArray, int begin, int end) {  
	    	
	    	switch (bArray[3]) {
			case 1://ipv4
				return Util.findHostIp(bArray,4,7);
			case 3://dns name
				return Util.findHostName(bArray,5,5 + bArray[4]);
			default:
				break;
			}
	    	throw new IllegalArgumentException("ATYP:" + bArray[3]) ;
	    }  
	    
}

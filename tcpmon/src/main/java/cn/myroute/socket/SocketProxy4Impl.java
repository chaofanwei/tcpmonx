package cn.myroute.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

//http://www.openssh.com/txt/socks4.protocol

public class SocketProxy4Impl implements SocketProxy {

	@Override
	public Socket authCheck(InputStream isIn, OutputStream osIn, byte[] buffer)
			throws Exception {

		if (buffer[1] == 0x1) {// connect

			int port = Util.findPort(buffer, 2, 3);
			String host = Util.findHostIp(buffer, 4, 7);

			System.out.println("host=" + host + ",port=" + port);

			Socket socketOut = new Socket();
			Util.bindSocket(socketOut);
			System.out.println("target:" + host + ":" + port);
			socketOut.connect(new InetSocketAddress(host, port));

			byte[] CONNECT_OK = new byte[8];
			CONNECT_OK[0] = 0;
			CONNECT_OK[1] = 90;
			for (int i = 2; i <= 7; i++) {  
	            CONNECT_OK[i] = buffer[i];  
	        } 
			osIn.write(CONNECT_OK);  
	        osIn.flush();  
	        System.out.println("> " + Util.bytesToHexString(CONNECT_OK, 0, CONNECT_OK.length)); 
	        
	        return socketOut;
		} else if (buffer[1] == 0x2) { // bind

		} else {
			throw new IllegalArgumentException("CD:" + buffer[1]);
		}

		return null;
	}

}

package cn.myroute.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public interface SocketProxy {

	Socket authCheck(InputStream isIn,OutputStream osIn,byte[] buffer) throws Exception ;
	
}

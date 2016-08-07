package org.apache.ws.commons.tcpmon.tcpmon.socket4;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
  
/**
 */
public class Sock4Socket extends Socket {
  
    // 版本
    static byte VERSION4 = 0X04;
    // 请求
    static byte CONNECT_REQUEST = 0X01;
    static byte BIND_REQUEST = 0X02;
    // 结束
    static byte END = 0X00;
  
    private String targetHost;
  
    private int targetPort;
  
    public Sock4Socket(String proxyHost, int proxyPort, String targetHost,
            int targetPort) throws UnknownHostException, IOException {
        super(proxyHost, proxyPort);
        this.targetHost = targetHost;
        this.targetPort = targetPort;
        this.connect();
    }
  
    /**
     * @param host
     * @param port
     * @return
     * @throws UnknownHostException
     */
    private byte[] connByte(String host, int port) throws UnknownHostException {
        String[] subIP = host.split("\\.");
        String portStr = Integer.toHexString(port);
        byte subP1B = 0;
        byte subP2B = 0;
        if (portStr.length() <= 2) {
            subP2B = (byte) Byte.valueOf(portStr, 16);
        } else if (portStr.length() == 3) {
            String p1 = portStr.charAt(0) + "";
            subP1B = (byte) Byte.valueOf(p1, 16);
            String p2 = portStr.charAt(1) + "" + portStr.charAt(2);
            subP2B = (byte) Byte.valueOf(p2, 16);
        } else if (portStr.length() == 4) {
            String p1 = portStr.charAt(0) + "" + portStr.charAt(1);
            subP1B = (byte) Byte.valueOf(p1, 16);
            String p2 = portStr.charAt(2) + "" + portStr.charAt(3);
            subP2B = (byte) Byte.valueOf(p2, 16);
        }
        byte[] bt = new byte[9];
        bt[0] = VERSION4;
        bt[1] = CONNECT_REQUEST;
        bt[2] = subP1B;
        bt[3] = subP2B;
        bt[4] = (byte) Integer.parseInt(subIP[0]);
        bt[5] = (byte) Integer.parseInt(subIP[1]);
        bt[6] = (byte) Integer.parseInt(subIP[2]);
        bt[7] = (byte) Integer.parseInt(subIP[3]);
        bt[8] = END;
        return bt;
    }
  
    /**
     * 连接
     */
    private void connect() throws IOException {
        byte[] data = connByte(this.targetHost, this.targetPort);
        OutputStream os = this.getOutputStream();
        // 握手字节序列
        os.write(data);
        os.flush();
        // 服务端返回的字节 version,command,ip,port,message
        byte[] receive = new byte[8];
        InputStream is = this.getInputStream();
        is.read(receive);
        byte b = receive[1];
        if (b == 0) {
            throw new IOException("");
        } else if (b == 92) {
            throw new IOException("server time out");
        } else if (b == 90) {
            // success
        }
    }
  
}
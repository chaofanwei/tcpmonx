package org.apache.ws.commons.tcpmon.tcpmon.socket4;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
 
public class Socket4ProxyClientDemo {
 
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // Socket4代理服务地址
        String proxyHost = "127.0.0.1";
        // Socket4代理服务IP
        int proxyPort = 98;
        // 创建Socket4代理Socket
        Socket socket = new Sock4Socket(proxyHost, proxyPort, "10.210.81.10",9080);
        OutputStream output = socket.getOutputStream();
        InputStreamReader isr = new InputStreamReader(socket.getInputStream(),
                "GBK");
        BufferedReader br = new BufferedReader(isr);
        // 请求内容
        StringBuilder request = new StringBuilder();
        request.append("GET /casp/ HTTP/1.1\r\n");
        request.append("Accept-Language: zh-cn\r\n");
        request.append("Host: 10.210.81.10\r\n");
        request.append("\r\n");
        output.write(request.toString().getBytes());
        output.flush();
        // 响应内容
        StringBuilder sb = new StringBuilder();
        String str = null;
        while ((str = br.readLine()) != null) {
            sb.append(str + "\n");
        }
        System.out.println(sb.toString());
        br.close();
        isr.close();
        output.close();
    }
     
}
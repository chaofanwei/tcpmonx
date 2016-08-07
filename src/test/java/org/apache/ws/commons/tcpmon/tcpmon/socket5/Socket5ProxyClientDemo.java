package org.apache.ws.commons.tcpmon.tcpmon.socket5;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Socket;
 
public class Socket5ProxyClientDemo {
 
    /**
     * 设置代理
     * 
     * @param proxyAddr
     * @param proxyPort
     */
    private static Proxy createProxy(String proxyAddr, int proxyPort) {
        // 设置认证
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("username", "password"
                        .toCharArray());
            }
        });
        // 设置HTTP代理
        Proxy proxy = new Proxy(Proxy.Type.SOCKS,new InetSocketAddress(proxyAddr, proxyPort));
        return proxy;
    }
     
    /**
     * 测试调用
     */
    private static void invoke(){
        // 创建代理对象
        Proxy proxy = createProxy("127.0.0.1",98);
        try{
            Socket socket = new Socket(proxy);
            socket.connect(new InetSocketAddress("www.baidu.com", 80));
            OutputStream output = socket.getOutputStream();
            InputStreamReader isr = new InputStreamReader(socket.getInputStream(),
                    "GBK");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder request = new StringBuilder();
            request.append("GET /index.php HTTP/1.1\r\n");
            request.append("Accept-Language: zh-cn\r\n");
            request.append("Host: www.baidu.com\r\n");
            request.append("\r\n");
            output.write(request.toString().getBytes());
            output.flush();
      
            StringBuilder sb = new StringBuilder();
            String str = null;
            while ((str = br.readLine()) != null) {
                sb.append(str + "\n");
            }
            System.out.println(sb.toString());
            br.close();
            isr.close();
            output.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
  
     
    /**
     * @param args
     */
    public static void main(String[] args) {
        invoke();
 
    }
 
}
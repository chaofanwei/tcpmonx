package org.apache.ws.commons.tcpmon.tcpmon.http;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
 
public class HttpProxyClientDemo2 {
 
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
        Proxy proxy = new Proxy(Proxy.Type.HTTP,
                new InetSocketAddress(proxyAddr, proxyPort));
        return proxy;
    }
     
    /**
     * 测试调用
     */
    private static void invoke() {
        String urlStr = "http://www.baidu.com/";
        // 创建代理对象
        Proxy proxy = createProxy("127.0.0.1",21802);;
         
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = null;
            if (proxy != null) {
                // 使用代理代开资源
                conn = (HttpURLConnection) url.openConnection(proxy);
                System.out.println("使用了代理代开资源");
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
            // 超时时间
            conn.setConnectTimeout(10000);
            InputStream is = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            StringBuffer text = new StringBuffer();
            String line = null;
            while ((line = in.readLine()) != null) {
                text.append(line);
            }
            if (is != null) {
                is.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
            // 打印内容
            System.out.println(text.toString());
        } catch (Exception e) {
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
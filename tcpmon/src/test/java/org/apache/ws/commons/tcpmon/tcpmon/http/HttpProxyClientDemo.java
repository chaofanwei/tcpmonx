package org.apache.ws.commons.tcpmon.tcpmon.http;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URL;
import java.util.List;
 
public class HttpProxyClientDemo {
 
    /**
     * 通过系统属性设置代理
     * 
     * @param proxyAddr
     * @param proxyPort
     */
    private static void setProxy(String proxyAddr, int proxyPort) {
        System.setProperty("java.net.useSystemProxies", "true");
        // HTTP代理
        System.setProperty("http.proxyHost", proxyAddr);
        System.setProperty("http.proxyPort", proxyPort + "");
        // HTTPS代理
        System.setProperty("https.proxyHost", proxyAddr);
        System.setProperty("https.proxyPort", proxyPort + "");
    }
 
    /**
     * @param uri
     * @return
     */
    private static Proxy getSystemProxy(String uri) {
        Proxy proxy = null;
        try {
            ProxySelector ps = ProxySelector.getDefault();
            List<Proxy> proxyList = ps.select(new URI(uri));
            for (Proxy p : proxyList) {
                // System.out.println("代理类型 : " + p.type());
                InetSocketAddress addr = (InetSocketAddress) p.address();
                if (addr == null) {
                    // System.out.println("没有代理");
                } else {
                    proxy = p;
                    // System.out.println("代理主机 : " + addr.getHostName());
                    // System.out.println("代理端口 : " + addr.getPort());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proxy;
    }
 
    /**
     * 测试调用
     */
    private static void invoke() {
        // 通过系统属性设置代理
        setProxy("127.0.0.1",21802);
        String urlStr = "http://www.baidu.com/";
        // 获取代理
        Proxy proxy = getSystemProxy(urlStr);
         
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
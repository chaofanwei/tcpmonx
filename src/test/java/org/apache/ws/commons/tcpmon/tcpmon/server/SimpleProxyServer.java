package org.apache.ws.commons.tcpmon.tcpmon.server;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
 
/**
 * 简单Socket代理服务器实现
 */
public class SimpleProxyServer {
     
    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        try {
            String host = "";
            int remoteport = 100;
            int localport = 1001;
            runServer(host, remoteport, localport); // never returns
        } catch (Exception e) {
            System.err.println(e);
        }
    }
 
      
    /**
     * @param host
     * @param remoteport
     * @param localport
     * @throws IOException
     */
    public static void runServer(String host, int remoteport, int localport)
            throws IOException {
        final ServerSocket ss = new ServerSocket(localport);
        final byte[] request = new byte[1024];
        byte[] reply = new byte[4096];
        while (true) {
            Socket client = null, server = null;
            try {
                client = ss.accept();
                final InputStream streamFromClient = client.getInputStream();
                final OutputStream streamToClient = client.getOutputStream();
                try {
                    server = new Socket(host, remoteport);
                } catch (IOException e) {
                    PrintWriter out = new PrintWriter(streamToClient);
                    out.print("error.");
                    out.flush();
                    client.close();
                    continue;
                }
                final InputStream streamFromServer = server.getInputStream();
                final OutputStream streamToServer = server.getOutputStream();
                Thread t = new Thread() {
                    public void run() {
                        int bytesRead;
                        try {
                            while ((bytesRead = streamFromClient.read(request)) != -1) {
                                streamToServer.write(request, 0, bytesRead);
                                streamToServer.flush();
                            }
                        } catch (IOException e) {
                        }
                        try {
                            streamToServer.close();
                        } catch (IOException e) {
                        }
                    }
                };
                t.start();
                int bytesRead;
                try {
                    while ((bytesRead = streamFromServer.read(reply)) != -1) {
                        streamToClient.write(reply, 0, bytesRead);
                        streamToClient.flush();
                    }
                } catch (IOException e) {
                }
                streamToClient.close();
            } catch (IOException e) {
                System.err.println(e);
            } finally {
                try {
                    if (server != null)
                        server.close();
                    if (client != null)
                        client.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
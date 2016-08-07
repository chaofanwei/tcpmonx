package cn.myroute.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class DnsUtil {

	public static void main(String[] args) {
		initDns();
		try {
			System.out.println(InetAddress.getByName("weichaofan.com"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void initDns(){
		String jreVersion = "1.6";
		Method cacheAddressMethod = null;
		final Class<InetAddress> clazz = java.net.InetAddress.class;
		try {
			jreVersion=System.getProperties().getProperty("java.specification.version");
			
			cacheAddressMethod = null;
			if("1.6".equals(jreVersion)){
				cacheAddressMethod=clazz.getDeclaredMethod("cacheAddress", new Class[]{String.class,Object.class,boolean.class});
			}else{
				cacheAddressMethod = clazz.getDeclaredMethod("cacheAddresses", new Class[] { String.class, InetAddress[].class, boolean.class });
			}
			cacheAddressMethod.setAccessible(true);
			
		}catch (Exception e1) {
			e1.printStackTrace();
		}
		
		BufferedReader br = null;
		try {
			br = new BufferedReader( new InputStreamReader(DnsUtil.class.getClassLoader().getResourceAsStream("dns.conf")));
			String line = null;
			while(null != (line=br.readLine())){
				line = line.replaceAll("#.*", "");
				line=line.trim();
				line= line.replaceAll("\\s+", " ");
				if(line.length() > 5 && line.split(" ").length == 2){
					String[] fs= line.split(" ");
					
					InetAddress[] adds= addDns(fs[0].trim(),fs[1].trim());
					if(null != adds){
						cacheAddressMethod.invoke(clazz, new Object[] { fs[1], adds, true });
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	private static InetAddress[] addDns(String ip,String domain){
		String[] ipt = ip.split("\\.");
		byte[] ipb = new byte[4];
		for (int j = 0; j < ipt.length; j++) {
			ipb[j] = (byte) Integer.valueOf(ipt[j]).intValue();
		}
		try {
			InetAddress addr = InetAddress.getByAddress(domain, ipb);
			InetAddress[] addrs = new InetAddress[] { addr };
			return addrs;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

package org.apache.ws.commons.tcpmon.tcpmon.http;

import org.junit.Test;

public class CommonTest {

	@Test
	public void test1(){
		byte[] CONNECT_OK = { 0x6D, 0x61, 0x69, 0x6c, 0x2e, 0x79, 0x6f, 0x75, 0x6b, 0x75,0x2e,0x63,0x6f,0x6d }; 
		
		for(byte b: CONNECT_OK){
			System.out.print((char)b);
		}
	}
	@Test
	public void test12(){//  11111    10010000
		int[] bArray = new int[]{0x1f,0x90};
		int port = findPort(bArray, 0, 1);
		System.out.println(port);
		
		System.out.println(Integer.toBinaryString((byte)0x1f));
		System.out.println(Integer.toBinaryString(0x90));
		System.out.println(Integer.toBinaryString(port));
		System.out.println(Integer.toBinaryString(8080));
	}
	
	public static int findPort(int[] bArray, int begin, int end) {  
//        int port = 0;  
//        System.out.println(Integer.toBinaryString(port));
//        for (int i = begin; i <= end; i++) {  
//            port <<= 8;  
//            System.out.println(Integer.toBinaryString(port));
//            System.out.println(Integer.toBinaryString(bArray[i]));
//            port += bArray[i]; 
//            System.out.println(Integer.toBinaryString(port));
//        }  
		
		byte a = (byte)234;
		System.out.println(a);
		int i = a;	
		System.out.println(a);
		 System.out.println(Integer.toBinaryString(i));
		i = a&0xff;
		System.out.println(i);
		System.out.println(Integer.toBinaryString(i));
		
		  int port = 0;  
	        int pre= bArray[begin];
	        int sec= bArray[begin + 1];
	        port = pre << 8;
	        port = port | sec;
        
        return port;  
    }  
	
}

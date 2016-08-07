package cn.myroute.socket;

import org.junit.Test;

public class CommonTest {

	@Test
	public void test1(){
		byte[] CONNECT_OK = { 0x6D, 0x61, 0x69, 0x6c, 0x2e, 0x79, 0x6f, 0x75, 0x6b, 0x75,0x2e,0x63,0x6f,0x6d }; 
		
		for(byte b: CONNECT_OK){
			System.out.print((char)b);
		}
	}
}

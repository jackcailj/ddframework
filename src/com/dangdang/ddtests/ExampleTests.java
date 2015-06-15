package com.dangdang.ddtests;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;



public class ExampleTests {
	
	private static Logger logger = Logger.getLogger(ExampleTests.class);
	
	@Test
	public void test1(){
		logger.error("error inf");
		logger.info("info msg");
	}

    public static void main(String[] args){
        ExampleTests test= new ExampleTests();
        test.test1();
    }
	
}

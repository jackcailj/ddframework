package com.dangdang.ddtests;

import org.slf4j.Logger;  import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;



public class ExampleTests {
	
	private static Logger logger = LoggerFactory.getLogger(ExampleTests.class);
	
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

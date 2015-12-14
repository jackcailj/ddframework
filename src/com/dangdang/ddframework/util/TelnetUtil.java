package com.dangdang.ddframework.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.apache.commons.net.telnet.TelnetClient;
import org.apache.log4j.Logger;

 
public class TelnetUtil {
	static Logger logger = Logger.getLogger(TelnetUtil.class);
    /** 新建一个TelnetClient对象 */
    private TelnetClient telnetClient = new TelnetClient();
    /** 输入流，接收返回信息 */
    private InputStream in;
    /** 向 服务器写入 命令 */
    private PrintStream out;
    private String  ip = "10.255.223.155";//155开发环境 //208测试环境
    private Integer port = 6379;
 
    /**
     * @param ip : telnet的IP地址
     * @param port : 端口号
     */
    public void connect(String ip, Integer port) {
       try {
           telnetClient.connect(ip, port);
           in = telnetClient.getInputStream();
           out = new PrintStream(telnetClient.getOutputStream());
       } catch (Exception e) {
           System.out.println("[telnet] connect error: connect to [" + ip + ":" + port + "] fail!");
       }
    }
    
    /**
     * 执行telnet命令
     *
     * @param command
     * @return
     */
    public String execute(String command) {
       write(command);
       StringBuffer sb = new StringBuffer();
       return sb.toString();
    }
 
    /**
     * 向telnet命令行输入命令
     *
     * @param command
     */
    public void write(String command) {
       try {
           out.println(command);
           out.flush();
           System.out.println("[telnet] 打印本次执行的telnet命令:" + command);
       } catch (Exception e) {
           e.printStackTrace();
       }
    }
 
    /**
     * 关闭Telnet连接
     */
    public void disconnect() {
       try {
           Thread.sleep(10);
           telnetClient.disconnect();
       } catch (InterruptedException e1) {
           e1.printStackTrace();
       } catch (IOException e2) {
           e2.printStackTrace();
       }
    }
 
    public  String sendCommand(String command) {
    	 logger.info("----------------------------" + ip + ":" + port + "----------------------------");
         connect(ip, port);
         logger.info("Command is "+command);
         String result = execute(command);
         logger.info(result);  
         disconnect();
		 return result;
    }
    
    public String[] sendCommands(String... command) {
   	    logger.info("----------------------------" + ip + ":" + port + "----------------------------");
     	connect(ip, port);
     	String[] result = new String[command.length];
        logger.info("Command is "+command);
        for(int i=0; i<command.length; i++){
        	result[i] = execute(command[i]);
        }
        logger.info(result); 
        disconnect();
		return result;
   }
    
    public String sendCommand(String ip, Integer port, String command) {
   	    logger.info("----------------------------" + ip + ":" + port + "----------------------------");
   	    connect(ip, port);
        logger.info("Command is "+command);
        String result = execute(command);
        logger.info(result);  
        disconnect();
		 return result;
   }
   
    /**
     * 存储服务器正在清空缓存服务器缓存
     * @param url
     * @param port
     */
    public void clearCache(String url,Integer port){
    	logger.info("[telnet] 存储服务器正在清空缓存服务器缓存[" + url + ":" + port + "]----------------------------");
       connect(ip, port);
       String result = execute("flush_all");
       logger.info(result);  
       disconnect();
    }
   
}
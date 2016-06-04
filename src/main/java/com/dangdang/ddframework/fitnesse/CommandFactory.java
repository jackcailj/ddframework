package com.dangdang.ddframework.fitnesse;

/**
 * Created by cailianjie on 2016-5-19.
 */
public class CommandFactory{


    public static ICommand createCommand(FitnesseKey key, String commandStr) throws Exception {
        String keyStr = key.toString().toLowerCase();
        String className = "com.dangdang.ddframework.fitnesse."+keyStr.replaceFirst(keyStr.substring(0,1),keyStr.substring(0,1).toUpperCase())+"Command";

        try{
            Class c = Class.forName(className);
            ICommand command = (ICommand) c.newInstance();

            command.parseCommand(commandStr);

            return command;
        }catch (Exception e){
            throw e;
        }
    }
}

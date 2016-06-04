package com.dangdang.ddframework.fitnesse;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cailianjie on 2016-5-19.
 */
public abstract class CommandBase implements ICommand{

    String commandStr;

    public  void parseCommand(String command) throws Exception {
        Matcher matcher = Pattern.compile("^(.*?):(.*?)$").matcher(command);
        if(matcher.find()) {
            this.commandStr = matcher.group(2).toString().trim();
            parseCommandIml();
        }
        else{
            throw new Exception("命令格式不正确["+command+"]");
        }
    }

    public abstract void parseCommandIml() throws Exception;

    @Override
    public void execute(Object... param) throws Exception {

    }
}

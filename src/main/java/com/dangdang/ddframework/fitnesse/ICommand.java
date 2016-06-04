package com.dangdang.ddframework.fitnesse;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by cailianjie on 2016-5-19.
 */
public interface ICommand {

    public void parseCommand(String commandStr) throws Exception;

    public void execute(Object... param) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, Exception;
}

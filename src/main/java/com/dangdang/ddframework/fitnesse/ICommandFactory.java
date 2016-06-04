package com.dangdang.ddframework.fitnesse;

/**
 * Created by cailianjie on 2016-5-19.
 */
public interface ICommandFactory {

    public abstract   ICommand createCommand(FitnesseKey key,String commandStr) throws Exception;
}

package com.dangdang.ddframework.util;

import java.lang.reflect.Method;

/**
 * Created by cailianjie on 2016-4-29.
 */
public class PropertyMethod {

    String property;
    Method getMethod;
    Method setMethod;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Method getGetMethod() {
        return getMethod;
    }

    public void setGetMethod(Method getMethod) {
        this.getMethod = getMethod;
    }

    public Method getSetMethod() {
        return setMethod;
    }

    public void setSetMethod(Method setMethod) {
        this.setMethod = setMethod;
    }
}

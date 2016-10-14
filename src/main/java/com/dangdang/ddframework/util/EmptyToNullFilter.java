package com.dangdang.ddframework.util;

import com.alibaba.fastjson.serializer.PropertyFilter;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Administrator on 2016/9/20.
 */
public class EmptyToNullFilter implements PropertyFilter {
    @Override
    public boolean apply(Object o, String s, Object o1) {
        if(o1 instanceof String){
            if(StringUtils.isBlank(o1.toString())){
                return false;
            }
        }
        return true;
    }
}

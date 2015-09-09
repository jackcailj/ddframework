package com.dangdang.ddframework.dataverify.verify_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by cailianjie on 2015-8-29.
 * 标记在FIELD上，表示此字段不能为NULL
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {

}

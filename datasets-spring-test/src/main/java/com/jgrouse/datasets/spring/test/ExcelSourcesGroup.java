package com.jgrouse.datasets.spring.test;

import org.springframework.test.context.jdbc.SqlConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExcelSourcesGroup {
    ExcelSource[] value();
    SqlConfig sqlConfig() default @SqlConfig;
}

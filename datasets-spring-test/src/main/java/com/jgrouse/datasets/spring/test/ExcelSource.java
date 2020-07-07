package com.jgrouse.datasets.spring.test;

import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.jdbc.SqlConfig;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Repeatable(ExcelSourcesGroup.class)
public @interface ExcelSource {
    @AliasFor("workbookName")
    String value() default "";
    String workbookName() default "";
    String[] sheetNames() default {};
    SqlConfig config() default @SqlConfig;
}

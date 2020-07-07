package com.jgrouse.datasets.spring.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class ExcelListener extends AbstractTestExecutionListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public int getOrder() {
        return 6000; //after SqlScriptsTestExecutionListener
    }

    @Override
    public void beforeTestExecution(final TestContext testContext) throws Exception {


    }

}

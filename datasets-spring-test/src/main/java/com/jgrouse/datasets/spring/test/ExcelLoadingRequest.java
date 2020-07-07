package com.jgrouse.datasets.spring.test;

import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExcelLoadingRequest {
    private final Map<String, List<String>> workbooksToSheetNames;
    private final MergedSqlConfig mergedSqlConfig;

    public ExcelLoadingRequest(final Map<String, List<String>> workbooksToSheetNames, final MergedSqlConfig mergedSqlConfig) {
        this.workbooksToSheetNames = workbooksToSheetNames;
        this.mergedSqlConfig = mergedSqlConfig;
    }

    public Map<String, List<String>> getWorkbooksToSheetNames() {
        return workbooksToSheetNames;
    }


    public MergedSqlConfig getMergedSqlConfig() {
        return mergedSqlConfig;
    }


    static class Builder {
        private final Class<?> testClass;
        private final Method testMethod;

        public Builder(final Class<?> testClass, final Method testMethod) {
            this.testClass = testClass;
            this.testMethod = testMethod;
        }

        ExcelLoadingRequest build() {
            Set<ExcelSource> excelSources = AnnotatedElementUtils.getMergedRepeatableAnnotations(testMethod, ExcelSource.class, ExcelSourcesGroup.class);
            
            return null;
        }
    }
}

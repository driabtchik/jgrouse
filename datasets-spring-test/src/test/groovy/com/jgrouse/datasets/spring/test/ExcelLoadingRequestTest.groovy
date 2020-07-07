package com.jgrouse.datasets.spring.test

import com.jgrouse.util.AssertionException
import groovy.transform.CompileStatic
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.springframework.util.ReflectionUtils

@CompileStatic
class ExcelLoadingRequestTest implements WithAssertions {

    @Test
    void requireWorkbook() {
        assertThatExceptionOfType(AssertionException).isThrownBy({
            new ExcelLoadingRequest.Builder(Foo.class, ReflectionUtils.findMethod(Foo, "willFailBecauseWorkbookNotSpecified")).build()
        })
    }


    static class Foo {

        @ExcelSource
        void willFailBecauseWorkbookNotSpecified() {
            //no-op
        }

    }

}

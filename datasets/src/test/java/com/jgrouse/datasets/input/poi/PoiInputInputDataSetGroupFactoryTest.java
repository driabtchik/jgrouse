package com.jgrouse.datasets.input.poi;

import com.jgrouse.datasets.input.InputDataSet;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class PoiInputInputDataSetGroupFactoryTest {


    @Test
    void createAndValidateContent() {
        final PoiInputDataSetGroupFactory factory = new PoiInputDataSetGroupFactory();
        final PoiInputDataSetGroup dataSetGroup =
                factory.create(() -> this.getClass().getResourceAsStream("/com/jgrouse/datasets/input/poi/PoiDatasetFactoryTest.xlsx"));

        final InputDataSet table1 = dataSetGroup.get("table1");

        assertThat(table1.stream()).usingRecursiveFieldByFieldElementComparator().containsExactly(
                Arrays.asList(1L, "foo", 2.3),
                Arrays.asList(2L, "bar", 4.5)
        );
    }

}
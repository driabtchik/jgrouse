package com.jgrouse.datasets.input;

import com.jgrouse.datasets.DataSetMetadata;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Stream;

public interface InputDataSet {
    @NotNull
    Stream<List<Object>> stream();

    @NotNull
    DataSetMetadata getMetadata();
}

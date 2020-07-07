package com.jgrouse.datasets.output;

import com.jgrouse.datasets.input.InputDataSet;

import java.util.List;

@FunctionalInterface
public interface OutputDataSetGroup {
    <T extends InputDataSet> void save(List<T> dataSets);
}

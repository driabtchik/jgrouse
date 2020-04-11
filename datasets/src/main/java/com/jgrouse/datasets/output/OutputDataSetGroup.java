package com.jgrouse.datasets.output;

import com.jgrouse.datasets.input.InputDataSet;

import java.util.List;

@FunctionalInterface
public interface OutputDataSetGroup {
    void save(List<InputDataSet> dataSets);
}

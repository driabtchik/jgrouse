package com.jgrouse.datasets.input;

import javax.validation.constraints.NotNull;

@FunctionalInterface
public interface InputDataSetGroup {
    @NotNull
    InputDataSet get(@NotNull String dataSetName);
}

package com.jgrouse.datasets.input;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface InputDataSetGroup {
    @NotNull
    InputDataSet get(@NotNull String dataSetName);

    List<String> getDataSetNames();
}

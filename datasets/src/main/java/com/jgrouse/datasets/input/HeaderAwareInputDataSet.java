package com.jgrouse.datasets.input;

import java.util.List;

public interface HeaderAwareInputDataSet extends InputDataSet {
    List<String> getHeaders();
}

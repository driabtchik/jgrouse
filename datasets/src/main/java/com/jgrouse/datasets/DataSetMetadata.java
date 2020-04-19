package com.jgrouse.datasets;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.jgrouse.util.Assert.notEmpty;
import static com.jgrouse.util.Assert.notNullElements;


public class DataSetMetadata {
    private final List<DataSetMetadataElement> elements;
    private final String name;

    public DataSetMetadata(@NotNull final List<DataSetMetadataElement> elements, @NotEmpty final String name) {
        this.name = notEmpty(name, () -> "dataSet name must be provided");
        notEmpty(elements, "At least one element must be provided");
        notNullElements(elements,
                indexWithNull -> "All metadata elements must be not null; got null at position " + indexWithNull);
        this.elements = Collections.unmodifiableList(elements);
    }

    @NotNull
    public String getName() {
        return name;
    }

    public int getElementsCount() {
        return elements.size();
    }

    @NotNull
    public DataSetMetadataElement getElement(final int index) {
        return elements.get(index);
    }


    public Stream<DataSetMetadataElement> stream() {
        return elements.stream();
    }
}

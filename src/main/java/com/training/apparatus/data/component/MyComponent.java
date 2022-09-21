package com.training.apparatus.data.component;

import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.shared.ui.LoadMode;

import java.lang.annotation.Annotation;

public class MyComponent implements JavaScript {
    @Override
    public String value() {
        return null;
    }

    @Override
    public LoadMode loadMode() {
        return null;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}

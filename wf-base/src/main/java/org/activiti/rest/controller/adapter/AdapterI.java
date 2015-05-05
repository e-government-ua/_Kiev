package org.activiti.rest.controller.adapter;

/**
 * Created by diver on 4/8/15.
 */
public interface AdapterI<Input, Output> {

    Output apply(Input input);
}

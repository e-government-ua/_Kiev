package org.activiti.rest.controller.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by diver on 4/12/15.
 */
@JsonSerialize(as = Process.class)
@JsonDeserialize(as = Process.class)
public interface ProcessI {

    /**
     * @return Идентификатор процесса
     */
    String getId();
}

package org.activiti.rest.controller.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by diver on 4/12/15.
 */
@JsonSerialize(as = ProcDefinition.class)
@JsonDeserialize(as = ProcDefinition.class)
public interface ProcDefinitionI {

    /**
     * @return Уникальный идентификатор
     */
    String getId();

    /**
     * @return Наименование категории процесса
     */
    String getCategory();

    /**
     * @return Наименование для отображения
     */
    String getName();

    /**
     * @return Уникальное имя всех версий процесса
     */
    String getKey();

    /**
     * @return Описание процесса
     */
    String getDescription();

    /**
     * @return Версия процесса
     */
    int getVersion();

    /**
     * @return Наименование ресурса
     */
    String getResourceName();

    /** The deployment in which this process definition is contained. */
    /**
     * @return Идентификатор деплоя
     */
    String getDeploymentId();

    /**
     * @return Наименование ресурса диаграммы при установке приложения
     */
    String getDiagramResourceName();

    /**
     * @return true - если процесс в приостановленном состоянии
     */
    boolean isSuspended();

    /**
     * @return Идентификатор владельца процесса
     */
    String getTenantId();
}

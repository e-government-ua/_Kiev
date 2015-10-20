package org.activiti.rest.controller.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by diver on 4/8/15.
 */
@JsonSerialize(as = TaskAssignee.class)
@JsonDeserialize(as = TaskAssignee.class)
public interface TaskAssigneeI {

    /**
     * @return Состояние делегирования
     */
    String getDelegationState();

    /**
     * @return true - если процесс в приостановленном состоянии
     */
    boolean isSuspended();

    /**
     * @return Идентификатор
     */
    String getId();

    /**
     * @return Наименование
     */
    String getName();

    /**
     * @return Описание
     */
    String getDescription();

    /**
     * @return Приоритет
     */
    int getPriority();

    /**
     * @return Владелец
     */
    String getOwner();

    /**
     * @return Исполнитель
     */
    String getAssignee();

    /**
     * @return Идентификатор образца процесса
     */
    String getProcessInstanceId();

    /**
     * @return Идентификатор выполнения
     */
    String getExecutionId();

    /**
     * @return Идентификатор процесса
     */
    String getProcessDefinitionId();

    /**
     * @return Время созадния
     */
    String getCreateTime();

    /**
     * @return Ключ определения задачи
     */
    String getTaskDefinitionKey();

    /**
     * @return Скрок
     */
    String getDueDate();

    /**
     * @return Категория
     */
    String getCategory();

    /**
     * @return Идентификатор родительской задачи
     */
    String getParentTaskId();

    /**
     * @return Идентификатор владельца
     */
    String getTenantId();

    /**
     * @return Ключ формы
     */
    String getFormKey();
}

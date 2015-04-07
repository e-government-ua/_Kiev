package org.activiti.rest.data_access.entity;

/**
 * Created by diver on 4/6/15.
 */
public interface ClientInfoI {

    /**
     *
     * @return Имя
     */
    String getFirstName();

    /**
     *
     * @return Фамилия
     */
    String getLastName();

    /**
     *
     * @return Идентификационный номер налогоплательщика
     */
    String getINN();

    /**
     *
     * @return Серия паспорта
     */
    String getPassportSeries();

    /**
     *
     * @return Номер паспорта
     */
    String getPassportNumber();

    /**
     *
     * @return Дата выдачи паспорта
     */
    String getPassportIssueDate();

    /**
     *
     * @return Кем выдан паспорт
     */
    String getPassportIssueAuthor();

    /**
     *
     * @return Уникальный идентификатор пользователя
     */
    String getUserIdentifier();
}

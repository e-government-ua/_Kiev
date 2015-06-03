package org.wf.dp.dniprorada.constant;

public enum HistoryEventType {

    CUSTOM(0L,
            "custom",
            "Частный тип",
            ""),
    GET_SERVICE(1L,
            "getService",
            "Пользователь воспользовался услугой на портале",
            "Ви подали заявку на послугу %Назва послуги%. \n Cтатус: %статус%" ),
    SET_DOCUMENT_INTERNAL(2L,
            "setDocument_internal",
            "В Мои документы пользователя загружен новый документ – через наш портал",
            "%Назва органу% завантажує %Назва документу% у Ваш розділ Мої документи"),
    SET_DOCUMENT_EXTERNAL(3L,
            "setDocument_external",
            "В Мои документы пользователя загружен новый документ – внешняя организация",
            "%Назва органу% завантажує %Назва документу% у Ваш розділ Мої документи"),
    SET_DOCUMENT_ACCESS_LINK(4L,
            "setDocumentAccessLink",
            "Пользователь предоставил доступ к своему документу",
            "Ви надаєте доступ до документу %Назва документу% іншій людині: %Ім’я того, кому надають доступ% (телефон: %телефон%)"),
    SET_DOCUMENT_ACCESS(5L,
            "setDocumentAccess",
            "Кто-то воспользовался доступом к документу через OTP, который ему предоставил пользователь",
            "%Ім’я того, кому надають доступ% скористався доступом, який Ви надали, та переглянув документ %Назва документу%"),
    ACTIVITY_STATUS_NEW(6L,
            "ActivitiStatusNew",
            "Изменение статуса заявки",
            "Ваша заявка №%nTask% изменила свой статус на %StatusNameNew%");

    private Long nID;
    private String sID;
    private String sName;
    private String sTemplate;

    public Long getnID() {
        return nID;
    }

    public String getsID() {
        return sID;
    }

    public String getsName() {
        return sName;
    }

    public String getsTemplate() {
        return sTemplate;
    }


    public static HistoryEventType getById(Long id) {
        if (id != null) {
            for (HistoryEventType eventType : values()) {
                if (eventType.nID.equals(id)) {
                    return eventType;
                }
            }
        }
        return null;
    }

    private HistoryEventType(Long nID, String sID, String sName, String sTemplate) {
        this.nID = nID;
        this.sID = sID;
        this.sName = sName;
        this.sTemplate = sTemplate;
    }

}


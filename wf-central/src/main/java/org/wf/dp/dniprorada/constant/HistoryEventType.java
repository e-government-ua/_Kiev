package org.wf.dp.dniprorada.constant;

public enum HistoryEventType {

    CUSTOM(0),
    GET_SERVICE(1),
    SET_DOCUMENT_INTERNAL(2),
    SET_DOCUMENT_EXTERNAL(3),
    SET_DOCUMENT_ACCESS_LINK(4),
    SET_DOCUMENT_ACCESS(5),
    ACTIVITY_STATUS_NEW(6);

    private Long nID;
    private String sID;
    private String sName;
    private String sTemplate;

    public Long getnID() {
        return nID;
    }

    public void setnID(Long nID) {
        this.nID = nID;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsTemplate() {
        return sTemplate;
    }

    public void setsTemplate(String sTemplate) {
        this.sTemplate = sTemplate;
    }

    HistoryEventType(int id) {

        switch (id) {
            case (0): {
                setsID("custom");
                setsName("Частный тип");
                setsTemplate("");
            }
            case (1): {
                setsID("getService");
                setsName("Пользователь воспользовался услугой на портале");
                setsTemplate("Ви подали заявку на послугу %Назва послуги%. \n Cтатус: %статус%");
            }
            case (2): {
                setsID("setDocument_internal");
                setsName("В Мои документы пользователя загружен новый документ – через наш портал");
                setsTemplate("%Назва органу% завантажує %Назва документу% у Ваш розділ Мої документи");
            }
            case (3): {
                setsID("setDocument_external");
                setsName("В Мои документы пользователя загружен новый документ – внешняя организация");
                setsTemplate("%Назва органу% завантажує %Назва документу% у Ваш розділ Мої документи");
            }
            case (4): {
                setsID("setDocumentAccessLink");
                setsName("Пользователь предоставил доступ к своему документу");
                setsTemplate("Ви надаєте доступ до документу %Назва документу% іншій людині: %Ім’я того, кому надають доступ% (телефон: %телефон%)");
            }
            case (5): {
                setsID("setDocumentAccess");
                setsName("Кто-то воспользовался доступом к документу через OTP, который ему предоставил пользователь");
                setsTemplate("%Ім’я того, кому надають доступ% скористався доступом, який Ви надали, та переглянув документ %Назва документу%");
            }
            case (6): {
                setsID("ActivitiStatusNew");
                setsName("Изменение статуса заявки");
                setsTemplate("Ваша заявка №%nTask% изменила свой статус на %StatusNameNew%");
            }
        }
    }
}

package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.wf.dp.dniprorada.base.model.Entity;

import javax.persistence.Column;

@javax.persistence.Entity
public class Country extends Entity{

    /**
     * nID_UA - ИД-номер Код, в Украинском классификкаторе (уникальный-ключ, long)
     */
    @JsonProperty(value="nID_UA")
    @Column(unique = true)
    private Long nID_UA;

    /**
     *sID_Two - ИД-строка Код-двухсимвольный, международный (уникальный-ключ, String 2 символа)
     */
    @JsonProperty(value="sID_Two")
    @Column(length = 2, unique = true)
    private String sID_Two;

    /**
     * sID_Three - ИД-строка Код-трехсимвольный, международный (уникальный-ключ, String 3 символа)
     */
    @JsonProperty(value="sID_Three")
    @Column(length = 3, unique = true)
    private String sID_Three;

    /**
     * sNameShort_UA - Назва країни, коротка, Украинская (уникальный, String < 100 символов)
     */
    @JsonProperty(value="sNameShort_UA")
    @Column(length = 100, unique = true)
    private String sNameShort_UA;

    /**
     * sNameShort_EN - Назва країни, коротка, англійською мовою (уникальный, String < 100 символов)
     */
    @JsonProperty(value="sNameShort_EN")
    @Column(length = 100, unique = true)
    private String sNameShort_EN;

    /**
     * sReference_LocalISO - Ссылка на локальный ISO-стандарт, с названием (a-teg с href) (String < 100 символов)
     */
    @JsonProperty(value="sReference_LocalISO")
    @Column(length = 100)
    private String sReference_LocalISO;


    public Long getnID_UA() {
        return nID_UA;
    }

    public void setnID_UA(Long nID_UA) {
        this.nID_UA = nID_UA;
    }

    public String getsID_Two() {
        return sID_Two;
    }

    public void setsID_Two(String sID_Two) {
        this.sID_Two = sID_Two;
    }

    public String getsID_Three() {
        return sID_Three;
    }

    public void setsID_Three(String sID_Three) {
        this.sID_Three = sID_Three;
    }

    public String getsNameShort_UA() {
        return sNameShort_UA;
    }

    public void setsNameShort_UA(String sNameShort_UA) {
        this.sNameShort_UA = sNameShort_UA;
    }

    public String getsNameShort_EN() {
        return sNameShort_EN;
    }

    public void setsNameShort_EN(String sNameShort_EN) {
        this.sNameShort_EN = sNameShort_EN;
    }

    public String getsReference_LocalISO() {
        return sReference_LocalISO;
    }

    public void setsReference_LocalISO(String sReference_LocalISO) {
        this.sReference_LocalISO = sReference_LocalISO;
    }
}

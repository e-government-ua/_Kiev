package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.wf.dp.dniprorada.base.model.Entity;

import javax.persistence.Column;

@javax.persistence.Entity
public class Country extends Entity {

    /**
     * nID_UA - ИД-номер Код, в Украинском классификкаторе (уникальный-ключ, long)
     */
    @JsonProperty(value = "nID_UA")
    @Column(unique = true)
    private Long nID_UA;

    /**
     * sID_Two - ИД-строка Код-двухсимвольный, международный (уникальный-ключ, String 2 символа)
     */
    @JsonProperty(value = "sID_Two")
    @Column(length = 2, unique = true)
    private String sID_Two;

    /**
     * sID_Three - ИД-строка Код-трехсимвольный, международный (уникальный-ключ, String 3 символа)
     */
    @JsonProperty(value = "sID_Three")
    @Column(length = 3, unique = true)
    private String sID_Three;

    /**
     * sNameShort_UA - Назва країни, коротка, Украинская (уникальный, String < 100 символов)
     */
    @JsonProperty(value = "sNameShort_UA")
    @Column(length = 100, unique = true)
    private String sNameShort_UA;

    /**
     * sNameShort_EN - Назва країни, коротка, англійською мовою (уникальный, String < 100 символов)
     */
    @JsonProperty(value = "sNameShort_EN")
    @Column(length = 100, unique = true)
    private String sNameShort_EN;

    /**
     * sReference_LocalISO - Ссылка на локальный ISO-стандарт, с названием (a-teg с href) (String < 100 символов)
     */
    @JsonProperty(value = "sReference_LocalISO")
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

    @Override
    public String toString() {
        return "Country{" +
                "nID=" + getId() +
                ",nID_UA=" + nID_UA +
                ", sID_Two='" + sID_Two + '\'' +
                ", sID_Three='" + sID_Three + '\'' +
                ", sNameShort_UA='" + sNameShort_UA + '\'' +
                ", sNameShort_EN='" + sNameShort_EN + '\'' +
                ", sReference_LocalISO='" + sReference_LocalISO + '\'' +
                "} ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Country country = (Country) o;

        if (nID_UA != null ? !nID_UA.equals(country.nID_UA) : country.nID_UA != null)
            return false;
        if (sID_Three != null ? !sID_Three.equals(country.sID_Three) : country.sID_Three != null)
            return false;
        if (sID_Two != null ? !sID_Two.equals(country.sID_Two) : country.sID_Two != null)
            return false;
        if (sNameShort_EN != null ? !sNameShort_EN.equals(country.sNameShort_EN) : country.sNameShort_EN != null)
            return false;
        if (sNameShort_UA != null ? !sNameShort_UA.equals(country.sNameShort_UA) : country.sNameShort_UA != null)
            return false;
        if (sReference_LocalISO != null ?
                !sReference_LocalISO.equals(country.sReference_LocalISO) :
                country.sReference_LocalISO != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nID_UA != null ? nID_UA.hashCode() : 0;
        result = 31 * result + (sID_Two != null ? sID_Two.hashCode() : 0);
        result = 31 * result + (sID_Three != null ? sID_Three.hashCode() : 0);
        result = 31 * result + (sNameShort_UA != null ? sNameShort_UA.hashCode() : 0);
        result = 31 * result + (sNameShort_EN != null ? sNameShort_EN.hashCode() : 0);
        result = 31 * result + (sReference_LocalISO != null ? sReference_LocalISO.hashCode() : 0);
        return result;
    }
}

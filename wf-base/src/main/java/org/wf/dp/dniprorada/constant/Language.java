package org.wf.dp.dniprorada.constant;

public enum Language {

    UKRAINIAN("ua"),
    RUSSIAN("ru"),
    ENGLISH("en");

    private String shortName;

    private Language(String shortName) {
        this.shortName = shortName;
    }

    public static Language fromString(String shortName) {
        if (shortName != null) {
            for (Language b : Language.values()) {
                if (shortName.equalsIgnoreCase(b.shortName)) {
                    return b;
                }
            }
        }
        throw new IllegalArgumentException("No Language with short name \"" + shortName + "\" found");
    }

    public String getShortName() {
        return this.shortName;
    }

}

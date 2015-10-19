package org.wf.dp.dniprorada.constant;

public enum KOATUU {

    KYIVSKA_OBLAST(3200000000L),
    KYIV(8000000000L);

    private final long id;

    KOATUU(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
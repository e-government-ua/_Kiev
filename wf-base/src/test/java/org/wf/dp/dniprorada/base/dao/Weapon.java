package org.wf.dp.dniprorada.base.dao;

import org.wf.dp.dniprorada.base.model.Entity;

/**
 * @see Jedi
 */
@javax.persistence.Entity
public class Weapon extends Entity {
    private String name;

    public Weapon() {
    }

    public Weapon(String name) {
        this.name = name;
    }

    public Weapon(Long id, String name) {
        this(name);
        setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package org.wf.dp.dniprorada.base.dao;

import org.wf.dp.dniprorada.base.model.Entity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Test entity used in {@link GenericEntityDaoTest}
 */
@javax.persistence.Entity
public class Jedi extends Entity {
    private String name;

    @ManyToOne
    @JoinColumn(name = "weapon")
    private Weapon weapon;

    public Jedi() {
    }

    public Jedi(Long id) {
        setId(id);
    }

    public Jedi(String name, Weapon weapon) {
        this.name = name;
        this.weapon = weapon;
    }

    public Jedi(Long id, String name, Weapon weapon) {
        this(name, weapon);
        setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
}

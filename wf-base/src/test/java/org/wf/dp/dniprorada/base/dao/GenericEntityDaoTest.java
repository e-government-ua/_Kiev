package org.wf.dp.dniprorada.base.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/org/wf/dp/dniprorada/base/dao/testContext.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DatabaseSetup("/org/wf/dp/dniprorada/base/dao/dataset.xml")
public class GenericEntityDaoTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    @Qualifier("jediDao")
    private EntityDao<Jedi> testedObject;

    @Test
    public void shouldFindJediById() throws Exception {
        //when
        Optional<Jedi> jedi = testedObject.findById(1L);

        //then
        assertThat(jedi.isPresent()).isTrue();
        assertThat(jedi.get().getName()).isEqualTo("Yoda");
        assertThat(jedi.get().getWeapon().getName()).isEqualTo("Lightsaber");
    }

    @Test
    public void shouldFindJediByExpectedId() throws Exception {
        //when
        Jedi jedi = testedObject.findByIdExpected(1L);

        //then
        assertThat(jedi.getName()).isEqualTo("Yoda");
        assertThat(jedi.getWeapon().getName()).isEqualTo("Lightsaber");
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowExceptionIfJediNotFound() throws Exception {
        //when
        testedObject.findByIdExpected(777L);
    }

    @Test
    public void shouldFindJediByName() throws Exception {
        //when
        Optional<Jedi> jedi = testedObject.findBy("name", "Obi-Wan");

        //then
        assertThat(jedi.isPresent()).isTrue();
    }

    @Test
    public void shouldFindExpectedJediByName() throws Exception {
        //when
        Jedi jedi = testedObject.findByExpected("name", "Obi-Wan");

        //then
        assertThat(jedi).isNotNull();
    }

    @Test
    public void shouldNotFindExpectedJediByName() throws Exception {
        //given
        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage("Entity with name='Obi-Wannn' not found");

        //when
        testedObject.findByExpected("name", "Obi-Wannn");
    }

    @Test
    public void shouldNotFindJediByWeaponName() throws Exception {
        //when
        Optional<Jedi> jedi = testedObject.findBy("weapon.name", "Death Star");

        //then
        assertThat(jedi.isPresent()).isFalse();
    }

    @Test
    public void shouldFindJediByWeaponName() throws Exception {
        //when
        Optional<Jedi> jedi = testedObject.findBy("weapon.name", "Blaster");

        //then
        assertThat(jedi.get().getName()).isEqualTo("Han Solo");
    }

    @Test
    public void shouldFindAllJediByWeaponName() throws Exception {
        //when
        Collection<Jedi> jedi = testedObject.findAllBy("weapon.name", "Lightsaber");

        //then
        assertThat(jedi).hasSize(2);
        assertThat(jedi).extracting("name").contains("Yoda", "Obi-Wan");
    }

    @Test
    public void shouldFindAllJedi() throws Exception {
        //when
        Collection<Jedi> jedi = testedObject.findAll();

        //then
        assertThat(jedi).hasSize(3);
        assertThat(jedi).extracting("name").contains("Yoda", "Obi-Wan", "Han Solo");
    }

    @Test
    public void shouldFindAllJediByIds() throws Exception {
        //when
        Collection<Jedi> jedi = testedObject.findAll(Arrays.asList(1L, 3L));

        //then
        assertThat(jedi).hasSize(2);
        assertThat(jedi).extracting("name").contains("Yoda", "Han Solo");
    }

    @Test
    @ExpectedDatabase("/org/wf/dp/dniprorada/base/dao/shouldUpdateJediName-dataset.xml")
    public void shouldUpdateJediName() throws Exception {
        //given
        Jedi jedi = new Jedi(2L, "Obi-Wan", new Weapon(1L, "Lightsaber"));

        //when
        jedi.setName("Obi-Wan Kenobi");
        Jedi updatedJedi = testedObject.saveOrUpdate(jedi);

        //then
        assertThat(updatedJedi.getId()).isEqualTo(jedi.getId());
        assertThat(updatedJedi.getName()).isEqualTo("Obi-Wan Kenobi");
    }

    @Test
    @DatabaseSetup("/org/wf/dp/dniprorada/base/dao/shouldSaveJedi-setup.xml")
    @ExpectedDatabase("/org/wf/dp/dniprorada/base/dao/shouldSaveJedi-dataset.xml")
    public void shouldSaveJedi() throws Exception {
        //given
        Jedi jedi = new Jedi("Luke Skywalker", new Weapon(1L, "Lightsaber"));

        //when
        Jedi updatedJedi = testedObject.saveOrUpdate(jedi);

        //then
        assertThat(updatedJedi.getId()).isEqualTo(112L);
        assertThat(updatedJedi.getName()).isEqualTo("Luke Skywalker");
    }

    @Test
    @ExpectedDatabase("/org/wf/dp/dniprorada/base/dao/shouldUpdate2Jedi-dataset.xml")
    public void shouldUpdate2Jedi() throws Exception {
        //given
        Jedi jedi1 = new Jedi(1L, "Yoda", new Weapon(1L, "Lightsaber"));
        Jedi jedi2 = new Jedi(2L, "Obi-Wan", new Weapon(1L, "Lightsaber"));

        //when
        jedi1.setName("Grand Master Yoda");
        jedi2.setName("Obi-Wan Kenobi");
        Collection<Jedi> updatedJedi = testedObject.saveOrUpdate(Arrays.asList(jedi1, jedi2));

        //then
        assertThat(updatedJedi).hasSize(2);
        assertThat(updatedJedi).extracting("name").contains("Grand Master Yoda", "Obi-Wan Kenobi");
    }

    @Test
    @DatabaseSetup("/org/wf/dp/dniprorada/base/dao/shouldSave2Jedi-setup.xml")
    @ExpectedDatabase("/org/wf/dp/dniprorada/base/dao/shouldSave2Jedi-dataset.xml")
    public void shouldSave2Jedi() throws Exception {
        //given
        Jedi jedi1 = new Jedi("Luke Skywalker", new Weapon(1L, "Lightsaber"));
        Jedi jedi2 = new Jedi("Darth Vader", new Weapon(1L, "Lightsaber"));

        //when
        Collection<Jedi> updatedJedi = testedObject.saveOrUpdate(Arrays.asList(jedi1, jedi2));

        //then
        assertThat(updatedJedi).hasSize(2);
        assertThat(updatedJedi).extracting("name").contains("Luke Skywalker", "Darth Vader");
    }

    @Test
    @ExpectedDatabase("/org/wf/dp/dniprorada/base/dao/shouldDeleteJediById-dataset.xml")
    public void shouldDeleteJediById() throws Exception {
        //when
        testedObject.delete(1L);
    }

    @Test
    @ExpectedDatabase("/org/wf/dp/dniprorada/base/dao/shouldDeleteJediById-dataset.xml")
    public void shouldDeleteJediByEntity() throws Exception {
        //given
        Jedi jedi = new Jedi(1L);

        //when
        testedObject.delete(jedi);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowExceptionIfJediDoesNotExist() throws Exception {
        //given
        Jedi jedi = new Jedi(777L);

        //when
        testedObject.delete(jedi);
    }

    @Test
    @ExpectedDatabase("/org/wf/dp/dniprorada/base/dao/shouldDeleteJediById-dataset.xml")
    public void shouldDeleteJediByName() throws Exception {
        //when
        int deleted = testedObject.deleteBy("name", "Yoda");

        //then
        assertThat(deleted).isEqualTo(1);
    }

    @Test
    @ExpectedDatabase("/org/wf/dp/dniprorada/base/dao/shouldDeleteByWeaponName-dataset.xml")
    public void shouldDeleteJediByWeaponName() throws Exception {
        //when
        int deleted = testedObject.deleteBy("weapon.name", "Blaster");

        //then
        assertThat(deleted).isEqualTo(1);
    }

    @Test
    @ExpectedDatabase("/org/wf/dp/dniprorada/base/dao/shouldDelete2JediById-dataset.xml")
    public void shouldDelete2JediByEntity() throws Exception {
        //given
        Jedi jedi1 = new Jedi(1L);
        Jedi jedi2 = new Jedi(2L);

        //when
        testedObject.delete(Arrays.asList(jedi1, jedi2));
    }

    @Test
    @ExpectedDatabase("/org/wf/dp/dniprorada/base/dao/shouldDelete2JediById-dataset.xml")
    public void shouldDelete2Of3JediByEntity() throws Exception {
        //given
        Jedi jedi1 = new Jedi(1L);
        Jedi jedi2 = new Jedi(2L);
        Jedi jedi3 = new Jedi(777L);

        //when
        Collection<Jedi> notDeleted = testedObject.delete(Arrays.asList(jedi1, jedi2, jedi3));

        //then
        assertThat(notDeleted).hasSize(1);
        assertThat(Iterables.getFirst(notDeleted, null).getId()).isEqualTo(777L);
    }

    @Test
    @ExpectedDatabase("/org/wf/dp/dniprorada/base/dao/shouldDeleteAllJedi-dataset.xml")
    public void shouldDeleteAllJedi() throws Exception {
        //when
        testedObject.deleteAll();
    }

    @Test
    public void shouldReturnTrueForExistingJedi() throws Exception {
        //when
        boolean exists = testedObject.exists(1L);

        //then
        assertThat(exists).isTrue();
    }

    @Test
    public void shouldReturnFalseForNotExistingJedi() throws Exception {
        //when
        boolean exists = testedObject.exists(999L);

        //then
        assertThat(exists).isFalse();
    }
}
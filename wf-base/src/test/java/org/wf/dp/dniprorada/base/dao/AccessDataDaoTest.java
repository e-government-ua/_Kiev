/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wf.dp.dniprorada.base.dao;

import org.activiti.rest.controller.IntegrationTestsApplicationConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.wf.dp.dniprorada.util.Util;

/**
 * @author olya
 */
//@Ignore
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestsApplicationConfiguration.class)
public class AccessDataDaoTest {

    @Autowired
    private AccessDataDao accessDataDao;

    @Test //@Ignore
    public void workWithAccessData() {
        byte[] content = new byte[] { 1, 2, 3 };
        String contentString = Util.contentByteToString(content);
        String key = accessDataDao.setAccessData(contentString);
        Assert.assertNotNull(key);
        String contentReturn = accessDataDao.getAccessData(key);
        Assert.assertEquals(contentString, contentReturn);
        Assert.assertTrue(accessDataDao.removeAccessData(key));
    }
}

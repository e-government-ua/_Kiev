package org.wf.dp.dniprorada.base.util.queryloader;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author dgroup
 * @since 02.08.2015
 */
public class QueryLoaderTest {

    private static final String SQL_FILE = "query1.sql";
    private static final String MISSING_SQL_FILE = "ababagalamaga.sql";
    private static final String SQL_HOME_DIRECTORY = "/queryloader/sql-files/";

    @Test
    public void shouldDetectQuery() {
        String expectedSQL = "select current_time";
        String actualSQL = new QueryLoader(SQL_HOME_DIRECTORY + "PostgreSQL/")
                .get(SQL_FILE);

        assertNotNull(SQL_FILE + " not found", actualSQL);
        assertEquals("SQL queries aren't match", expectedSQL, actualSQL.trim());

        String currTimeSQL = "curr_time.sql";
        actualSQL = new QueryLoader().get(currTimeSQL);
        assertNotNull(currTimeSQL + " not found through default constructor", actualSQL);
        assertEquals("SQL queries aren't match through default constructor", expectedSQL, actualSQL.trim());
    }

    @Test(expected = MissingResourceException.class)
    public void queryShouldBeAbsent() {
        new QueryLoader(SQL_HOME_DIRECTORY + "H2/")
                .get(MISSING_SQL_FILE);
    }

    @Test
    public void shouldDetectQueryWithProfile() {
        String expectedSQL = "select CURRENT_TIME()";
        String actualSQL = new QueryLoader(SQL_HOME_DIRECTORY + "H2/")
                .get(SQL_FILE);

        assertNotNull(SQL_FILE + " not found", actualSQL);
        assertEquals("SQL queries aren't match", expectedSQL, actualSQL.trim());
    }
}
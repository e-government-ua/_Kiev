package org.org.wf.dp.dniprorada.util.queryloader;

import org.junit.Test;
import org.wf.dp.dniprorada.util.queryloader.MissingResourceException;
import org.wf.dp.dniprorada.util.queryloader.QueryLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author dgroup
 * @since  02.08.2015
 */
public class QueryLoaderTest {

    private static final String SQL_FILE = "query1.sql";
    private static final String MISSING_SQL_FILE = "ababagalamaga.sql";
    private static final String SQL_HOME_DIRECTORY = "/queryloader/sql-files/";

    @Test
    public void shouldDetectQuery(){
        String expectedSQL  = "select * from dual";
        String actualSQL    = new QueryLoader(SQL_HOME_DIRECTORY + "PostgreSQL/")
            .get(SQL_FILE);

        assertNotNull(SQL_FILE + " not found", actualSQL);
        assertEquals("SQL queries aren't match", expectedSQL, actualSQL.trim());
    }

    @Test(expected = MissingResourceException.class)
    public void queryShouldBeAbsent(){
        new QueryLoader(SQL_HOME_DIRECTORY + "H2/")
            .get(MISSING_SQL_FILE);
    }

    @Test
    public void shouldDetectQueryWithProfile(){
        String expectedSQL  = "select CURRENT_TIME()";
        String actualSQL = new QueryLoader(SQL_HOME_DIRECTORY + "H2/")
            .get(SQL_FILE);

        assertNotNull(SQL_FILE + " not found", actualSQL);
        assertEquals("SQL queries aren't match", expectedSQL, actualSQL.trim());
    }


    @Test
    public void shouldDetectQueryViaDefaultProfile(){
        String expectedSQL  = "\"nID_Place\" = :placeId";
        String sqlFile      = "get_PlaceTree_by_id.sql";
        String actualSQL    = new QueryLoader().get(sqlFile);

        assertNotNull(sqlFile + " not found",  actualSQL);
        assertTrue("SQL queries aren't match", actualSQL.trim().contains(expectedSQL));
    }
}
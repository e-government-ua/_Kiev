package org.org.wf.dp.dniprorada.util.queryloader;

import org.junit.Ignore;
import org.junit.Test;
import org.wf.dp.dniprorada.util.queryloader.MissingResourceException;
import org.wf.dp.dniprorada.util.queryloader.QueryLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.wf.dp.dniprorada.util.queryloader.QueryLoader.TypeDB.H2;

/**
 * @author dgroup
 * @since  02.08.2015
 */
public class QueryLoaderTest {

    private static final String SQL_FILE = "query1.sql";
    private static final String MISSING_SQL_FILE = "ababagalamaga.sql";
    private static final String SQL_HOME_DIRECTORY = "/queryloader/sql-files/";

    @Test @Ignore
    public void shouldDetectQuery(){
        String expectedSQL  = "select * from dual";
        String actualSQL    = new QueryLoader(SQL_HOME_DIRECTORY).get(SQL_FILE);

        assertNotNull(SQL_FILE + " not found", actualSQL);
        assertEquals("SQL queries aren't match", expectedSQL, actualSQL.trim());
    }

    @Test(expected = MissingResourceException.class) @Ignore
    public void queryShouldBeAbsent(){
        new QueryLoader(SQL_HOME_DIRECTORY).get(MISSING_SQL_FILE);
    }

    @Test @Ignore
    public void shouldDetectQueryWithProfile(){
        String expectedSQL  = "select CURRENT_TIME()";
        String actualSQL = new QueryLoader( H2, SQL_HOME_DIRECTORY )
            .get(SQL_FILE);

        assertNotNull(SQL_FILE + " not found", actualSQL);
        assertEquals("SQL queries aren't match", expectedSQL, actualSQL.trim());
    }
}
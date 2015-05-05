package ua.org.bankid.oauth.systest

import groovy.util.logging.Slf4j
import groovyx.net.http.RESTClient
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.springframework.http.HttpStatus

import static org.junit.Assert.assertEquals
import static org.junit.Assert.fail

@Slf4j(value = "logger")
public class OAuthNotAuthorizedFlowTest {

    RESTClient rest
    ConfigObject conf;

    @Before
    public void setUp() throws Exception {
        logger.info("setup rest client")
        ConfigObject conf = Utils.loadConfig("oauth-config.groovy")
        rest = new RESTClient(conf.server.egov.url)
    }

    @Test
    public void testPublicResourceAvailable() {
        def resp = rest.get(path:"/public/citizen/stub")
        assertEquals(HttpStatus.OK.value(), resp.status)
    }

    @Test
    public void testNotProtectedURLResourceAvailable() {
        def resp = rest.get(path:"/not_protected/citizen/stub")
        assertEquals(HttpStatus.OK.value(), resp.status)
    }

    @Test
    public void testNotProtectedURLProtectedResourceForbiden() {
        try {
            rest.get(path:"/not_protected/citizen/protected_stub")
            fail("expected unauthrized exception")
        } catch (Exception resp) {
            assertEquals(HttpStatus.UNAUTHORIZED.value(), resp.statusCode)
        }
    }

    @Test
    public void testProtectedURLResourceForbiden() {
        try {
            rest.get(path:"/protected/citizen/stub")
            fail("expected unauthrized exception")
        } catch (Exception resp) {
            assertEquals(HttpStatus.UNAUTHORIZED.value(), resp.statusCode)
        }

        try {
            rest.get(path:"/protected/citizen/protected_stub")
            fail("expected unauthrized exception")
        } catch (Exception resp) {
            assertEquals(HttpStatus.UNAUTHORIZED.value(), resp.statusCode)
        }
    }

}
package ua.org.bankid.oauth.systest

import groovy.util.logging.Slf4j
import groovyx.net.http.RESTClient
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

@Slf4j(value = "logger")
public class OAuthFlowTest {

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
        assert resp.status == 200
    }

    @Test
    public void testNotProtectedURLResourceAvailable() {
        def resp = rest.get(path:"/not_protected/citizen/stub")
        assert resp.status == 200
    }

    @Test
    public void testNotProtectedURLProtectedResourceForbiden() {
        def resp = rest.get(path:"/not_protected/citizen/stub")
        assert resp.status == 403
    }

    @Test
    public void testProtectedURLResourceForbiden() {
        def resp = rest.get(path:"/protected/citizen/stub")
        assert resp.status == 403

        def resp2 = rest.get(path:"/protected/citizen/protected_stub")
        assert resp2.status == 403
    }

    @Ignore
    @Test
    public void testOAuthSuccessFlow() {
        logger.info("test oauth success flow {}", client)
    }
}
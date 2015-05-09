package ua.org.bankid.oauth.systest

/**
 * @author zora.borys
 */
public class Utils {

    public static ConfigObject loadConfig(String configName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(configName)
        // TODO: remove hardcode local (dev, uat, local)
        return new ConfigSlurper("local").parse(url)
    }
}

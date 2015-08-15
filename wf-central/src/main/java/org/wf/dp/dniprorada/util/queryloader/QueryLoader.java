package org.wf.dp.dniprorada.util.queryloader;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.qatools.properties.Property;
import ru.qatools.properties.PropertyLoader;
import ru.qatools.properties.Resource;

import java.io.IOException;
import java.io.InputStream;

import static org.springframework.util.Assert.notNull;

/**
 * This class allow to store sql/hql queries in external files
 *
 * @author dgroup
 * @since  02.08.15
 */
@Component
@Resource.Classpath("config.properties")
public class QueryLoader {
    private static final Logger LOG = LoggerFactory.getLogger(QueryLoader.class);

    @Property("db.profile")
    private String dbProfile;

    @Property("root.folder")
    private String rootFolder;

    private String homeDirectory;


    public QueryLoader(){
        PropertyLoader.newInstance().populate(this);
        homeDirectory = "/queryloader/PostgreSQL/"; // TODO remove hard code

        LOG.info("Root: {}, DB profile: {}", rootFolder, dbProfile);
//        homeDirectory = rootFolder + TypeDB.define(dbProfile).getPath();
    }

    public QueryLoader(String directory) {
        this.homeDirectory = directory;
    }



    /**
     * @param fileWithQuery             file's name which contains sql/hql query.
     * @throws QueryLoadingException    when unable to load the file.
     * @throws MissingResourceException when unable to find the file in classpath.
     * @return                          sql/hql query from external file.
     */
    public String get(String fileWithQuery){
        notNull(fileWithQuery, "Key can't be a null");
        try(InputStream in = assertLoading(homeDirectory + fileWithQuery)){
            return IOUtils.toString(in);
        } catch (IOException e){
            throw new QueryLoadingException(fileWithQuery, e);
        }
    }



    private InputStream assertLoading(String file) {
        InputStream in = QueryLoader.class.getResourceAsStream(file);
        if (in == null){
            throw new MissingResourceException("File not found " + file);
        }
        return in;
    }



    public String getHomeDirectory(){
        return homeDirectory;
    }


    public String getDbProfile() {
        return dbProfile;
    }
    public void setDbProfile(String dbProfile) {
        this.dbProfile = dbProfile;
    }


    public String getRootFolder() {
        return rootFolder;
    }
    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }


    public enum TypeDB {
        Postgres("PostgreSQL"), H2("H2");

        private String profile;

        private TypeDB(String profile) {
            this.profile = profile;
        }

        public String getPath() {
            return profile + '/';
        }

        public static final TypeDB define(String profile){
            notNull(profile, "Profile can't be empty");
            for(TypeDB type : values())
                if (type.getPath().startsWith(profile))
                    return type;
            throw new IllegalArgumentException("Database type " + profile + " not found");
        }
    }
}
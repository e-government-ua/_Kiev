package org.wf.dp.dniprorada.util.queryloader;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import ru.qatools.properties.Property;
import ru.qatools.properties.PropertyLoader;
import ru.qatools.properties.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class allow to store sql/hql queries in external files
 *
 * @author dgroup
 * @since  02.08.15
 */
@Component
@Resource.Classpath("queryloader.properties")
public class QueryLoader {

    @Property("db.profile")
    private String dbProfile;
    private String homeDirectory;


    public QueryLoader(){
        PropertyLoader.newInstance().populate(this);
        calculatePath( TypeDB.define(dbProfile), "/queryloader/");
    }
    public QueryLoader(String directory) {
        this.homeDirectory = directory;
    }
    public QueryLoader(TypeDB type) {
        calculatePath(type, "/queryloader/");
    }
    public QueryLoader(TypeDB type, String directory) {
        calculatePath(type, directory);
    }

    private final void calculatePath(TypeDB type, String directory) {
        homeDirectory = directory + type.getPath();
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



    private static void notNull(Object obj, String errMsg){
        if (obj == null) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    public String getHomeDirectory(){
        return homeDirectory;
    }

    public String getDbProfile() {
        return dbProfile;
    }

    public enum TypeDB {
        Postgres("PostgreSQL"), H2("H2");

        private String path;
        private String name;

        TypeDB(String name) {
            this(name, name + '/');
        }
        TypeDB(String name, String path) {
            this.name = name;
            this.path = path;
        }

        public String getPath() {
            return path;
        }
        public String getName() {
            return name;
        }
        public static final TypeDB define(String name){
            for(TypeDB type : values())
                if (type.getName().equals(name))
                    return type;
            throw new IllegalArgumentException("Database type " + name + " not found");
        }
    }
}
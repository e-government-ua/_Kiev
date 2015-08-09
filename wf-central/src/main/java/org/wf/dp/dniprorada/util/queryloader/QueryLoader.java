package org.wf.dp.dniprorada.util.queryloader;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * This class allow to store sql/hql queries in external files
 *
 * @author dgroup
 * @since  02.08.15
 */
@Component
public class QueryLoader {
    private static final Logger LOG = LoggerFactory.getLogger(QueryLoader.class);

    private String homeDirectory;


    public QueryLoader(){
        this("/queryloader/");
    }

    public QueryLoader(String directory) {
        homeDirectory = directory;
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
}
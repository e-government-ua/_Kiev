package org.wf.dp.dniprorada.dao.place;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.base.util.caching.EnableCaching;
import org.wf.dp.dniprorada.base.util.queryloader.QueryLoader;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.wf.dp.dniprorada.dao.place.PlaceDaoImpl.valid;

/**
 * @author dgroup
 * @since 17.08.15
 */
@Component
public class PlaceQueryResolver {
    private static final Logger LOG = LoggerFactory.getLogger(PlaceQueryResolver.class);

    @Autowired
    private QueryLoader sqlStorage;

    @EnableCaching
    public String getTreeDown(PlaceHibernateHierarchyRecord root) {
        return load(valid(root.getPlaceId())
                ? "get_PlaceTree_down_by_id.sql"
                : "get_PlaceTree_down_by_UA-id.sql");
    }

    @EnableCaching
    public String getTreeUp(Long placeId, String uaId, boolean tree) {
        LOG.debug("Got {}, {}, {}.", placeId, uaId, tree);

        if (valid(placeId) && tree)
            return load("get_PlaceTree_up_by_id.sql");

        if (isNotBlank(uaId) && tree)
            return load("get_PlaceTree_up_by_UA-id.sql");

        if (valid(placeId))
            return load("get_PlaceTree_by_id.sql");

        if (isNotBlank(uaId))
            return load("get_PlaceTree_by_UA-id.sql");

        throw new IllegalArgumentException(format(
                "Unexpected set of parameters: %s, %s, %s.", placeId, uaId, tree));
    }

    private String load(String sqlFile) {
        String sqlQuery = sqlStorage.get(sqlFile);
        LOG.debug("SQL file {} contains '{}' query.", sqlFile, sqlQuery);
        return sqlQuery;
    }
}

package org.wf.dp.dniprorada.dao.place;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.base.util.queryloader.QueryLoader;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author dgroup
 * @since  17.08.15
 */
@Component
public class PlaceQueryDaoBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(PlaceQueryDaoBuilder.class);

    @Autowired
    private QueryLoader sqlStorage;

    static boolean specified(Long value) {
        return value != null && value > 0;
    }
    static boolean specified(Boolean value) {
        return value != null;
    }


    @Cacheable("ext-file-getTreeDown")
    public String getTreeDown(PlaceHierarchyRecord root) {
        String sql = load( root.getPlaceId() > 0
            ? "get_PlaceTree_down_by_id.sql" : "get_PlaceTree_down_by_UA-id.sql");

        if (specified(root.getTypeId()) ||
            specified(root.isArea()) ||
            specified(root.isRoot()) ||
            specified(root.getDeep()))
            sql = sql + " where ";

        if (specified(root.getTypeId()))
            sql += " type_id = :typeId";

        if (specified(root.isArea()) && specified(root.getTypeId()))
            sql += " and ";

        if (specified(root.isArea()))
            sql += " area = :area";

        if (specified(root.isRoot()) && (
            specified(root.getTypeId()) ||
            specified(root.isArea())))
            sql += " and ";

        if (specified(root.isRoot()))
            sql += " root = :root";

        if (specified(root.getDeep()) && (
            specified(root.getTypeId()) ||
            specified(root.isArea()) ||
            specified(root.isRoot())))
            sql += " and ";

        if (specified(root.getDeep()))
            sql += " level <= :deep";

        LOG.debug("SQL query {}", sql);

        return sql;
    }


    @Cacheable("ext-file-getTreeUp")
    public String getTreeUp(Long placeId, String uaId, Boolean tree) {
        if (specified(placeId) && specified(tree))
            return load("get_PlaceTree_up_by_id.sql");

        String sqlFile = "get_PlaceTree_by_id.sql";

        if (specified(placeId) && isNotBlank(uaId))
            return load(sqlFile);

        if (specified(placeId))
            return load(sqlFile);

        if (isNotBlank(uaId))
            sqlFile = "get_PlaceTree_by_UA-id.sql";

        if (isNotBlank(uaId) && specified(tree))
            sqlFile = "get_PlaceTree_up_by_UA-id.sql";

        return load(sqlFile);
    }

    private String load(String sqlFile) {
        String sqlQuery = sqlStorage.get(sqlFile);
        LOG.debug("SQL query [{}] loaded.", sqlQuery);
        return sqlQuery;
    }
}

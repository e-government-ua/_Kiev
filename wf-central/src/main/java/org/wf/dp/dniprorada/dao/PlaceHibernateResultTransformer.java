package org.wf.dp.dniprorada.dao;

import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wf.dp.dniprorada.model.Place;
import org.wf.dp.dniprorada.util.Tree;

import java.util.List;

import static java.util.Arrays.binarySearch;

/**
 * @author dgroup
 * @since  11.08.2015.
 */
public class PlaceHibernateResultTransformer implements ResultTransformer {
    private static final Logger LOG = LoggerFactory.getLogger(PlaceHibernateResultTransformer.class);


    @Override
    public PlaceHierarchyRecord transformTuple(Object[] objects, String[] strings) {
        PlaceHierarchyRecord phr = new PlaceHierarchyRecord();

        LOG.info("Labels {}", strings);
        LOG.info("Data {}", objects);

//        phr.setPlaceId( longVal(objects, strings, "id"));
//        phr.setTypeId( longVal(objects, strings, "type_id"));
//        phr.setUaID( stringVal(objects, strings, "ua_id"));
//        phr.setName( stringVal(objects, strings, "name"));
//        phr.setOriginalName( stringVal(objects, strings, "original_name"));
//        phr.setParentId( longVal(objects, strings, "parent_id"));
//        phr.setArea( boolVal(objects, strings, "area"));
//        phr.setRoot( boolVal(objects, strings, "root"));
//        phr.setDeep( longVal(objects, strings, "level") );

        return phr;
    }


    @Override
    public List transformList(List list) {
        LOG.info("Got {}", list);
        return list;
    }


    public static Tree<Place> toTree(List dataRows) {
        // #################################################
        // ###  TODO: Just for debug, delete it later   ####
        LOG.info("Result {}", dataRows);
        // #################################################
        return new Tree<>();
    }


    private boolean boolVal(Object[] objects, String[] strings, String column) {
        return Boolean.valueOf( stringVal(objects,strings,column) );
    }

    private String stringVal(Object[] objects, String[] strings, String column) {
        return objects[binarySearch(strings, column)].toString();
    }

    private long longVal(Object[] objects, String[] strings, String column) {
        return Long.valueOf( stringVal(objects, strings, column) );
    }
}
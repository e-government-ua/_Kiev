package org.wf.dp.dniprorada.dao;

import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wf.dp.dniprorada.util.Tree;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author dgroup
 * @since  11.08.2015.
 */
public class PlaceHibernateResultTransformer implements ResultTransformer {
    private static final Logger LOG = LoggerFactory.getLogger(PlaceHibernateResultTransformer.class);


    @Override
    public PlaceHierarchyRecord transformTuple(Object[] objects, String[] strings) {
        PlaceHierarchyRecord phr = new PlaceHierarchyRecord();

        LOG.info("Labels {}", Arrays.toString(strings));
        LOG.info("Data {}", Arrays.toString(objects));

        phr.setPlaceId( longVal(objects, strings, "id"));
        phr.setTypeId( longVal(objects, strings, "type_id"));
        phr.setUaID( stringVal(objects, strings, "ua_id"));
        phr.setName( stringVal(objects, strings, "name"));
        phr.setOriginalName( stringVal(objects, strings, "original_name"));
        phr.setParentId( longVal(objects, strings, "parent_id"));
        phr.setAreaId( longVal(objects, strings, "area_id"));
        phr.setRootId(longVal(objects, strings, "root_id"));
        phr.setDeep( longVal(objects, strings, "level") );

        return phr;
    }


    @Override
    public List transformList(List list) {
        return list;
    }


    private int getIndex(String[] labels, String key){
        for(int i=0; i < labels.length; i++)
            if (key.equals( labels[i] ))
                return i;
        return -1;
    }

    private String stringVal(Object[] objects, String[] labels, String column) {
        int index = getIndex(labels, column);
        return index >= 0 ? objects[index].toString() : "";
    }

    private long longVal(Object[] objects, String[] labels, String column) {
        String val = stringVal(objects, labels, column);
        return isNotBlank(val) ? Long.valueOf( val ) : 0L;
    }

    private boolean boolVal(Object[] objects, String[] labels, String column) {
        String val = stringVal(objects,labels,column);
        return isNotBlank(val) ? Boolean.valueOf( val ) : false;
    }

    public static Tree<PlaceHierarchyRecord> toTree(List dataRows) {
        // #################################################
        // ###  TODO: Just for debug, delete it later   ####
        LOG.info("Result {}", dataRows);
        // #################################################
        Tree<PlaceHierarchyRecord> tree = new Tree<>();
        tree.setChildren(dataRows);
        return tree;
    }

}
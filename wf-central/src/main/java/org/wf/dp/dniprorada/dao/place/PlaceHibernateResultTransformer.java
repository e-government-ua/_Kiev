package org.wf.dp.dniprorada.dao.place;

import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.contains;
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


    // TODO: Implement algorithm for transformation list to tree....
    public static PlaceHierarchy toTree(List<PlaceHierarchyRecord> dataRows) {
        LOG.info("Got {}", dataRows);

        PlaceHierarchy tree = new PlaceHierarchy();

        for(int i=0; i<dataRows.size(); i++){
            PlaceHierarchyRecord phr = dataRows.get(i);
            if (i==0){
                tree.setPlace( phr.toPlace() );
                tree.setLevel( phr.getDeep() );
            } else {
                long currentPlaceId = phr.getPlaceId();
                List<PlaceHierarchy> children = new ArrayList<>();

                for(int j=i; j<dataRows.size(); j++){
                    PlaceHierarchyRecord hr = dataRows.get(j);

                    if (hr.isAlreadyIncluded())
                        continue;

                    if (currentPlaceId == hr.getParentId()) {
                        PlaceHierarchy ph = new PlaceHierarchy();
                        ph.setPlace( hr.toPlace() );
                        ph.setLevel(hr.getDeep());
                        hr.setAlreadyIncluded(true);
                        children.add(ph);
                    }
                }



            }
            phr.setAlreadyIncluded(true);
        }

        return tree;
    }

}
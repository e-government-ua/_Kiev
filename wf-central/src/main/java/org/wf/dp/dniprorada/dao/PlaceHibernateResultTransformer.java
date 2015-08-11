package org.wf.dp.dniprorada.dao;

import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wf.dp.dniprorada.model.Place;
import org.wf.dp.dniprorada.util.Tree;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author dgroup
 * @since  11.08.2015.
 */
public class PlaceHibernateResultTransformer implements ResultTransformer {
    private static final Logger LOG = LoggerFactory.getLogger(PlaceHibernateResultTransformer.class);


    @Override
    public Object transformTuple(Object[] objects, String[] strings) {
        return asList(objects).addAll( asList(strings) );
    }

    @Override
    public List transformList(List list) {
        return list;
    }


    public static Tree<Place> toTree(List dataRows) {
        // #################################################
        // ###  TODO: Just for debug, delete it later   ####
        LOG.info("Result {}", dataRows);
        // #################################################
        return new Tree<>();
    }
}
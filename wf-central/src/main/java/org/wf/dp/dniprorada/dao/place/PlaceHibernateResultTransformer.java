package org.wf.dp.dniprorada.dao.place;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.ArrayUtils.indexOf;

/**
 * @author dgroup
 * @since  11.08.2015.
 */
public class PlaceHibernateResultTransformer implements ResultTransformer {
    private static final Logger LOG = LoggerFactory.getLogger(PlaceHibernateResultTransformer.class);


    @Override
    public PlaceHibernateHierarchyRecord transformTuple(Object[] objects, String[] strings) {
        PlaceHibernateHierarchyRecord phr = new PlaceHibernateHierarchyRecord();

        phr.setPlaceId  ( toLong(objects, strings, "id"));
        phr.setTypeId   ( toLong(objects, strings, "type_id"));
        phr.setParentId ( toLong(objects, strings, "parent_id"));
        phr.setDeep     ( toLong(objects, strings, "level") );
        phr.setUaID     (    toString(objects, strings, "ua_id"));
        phr.setName     (    toString(objects, strings, "name"));
        phr.setOriginalName( toString(objects, strings, "original_name"));

        return phr;
    }


    @Override
    public List transformList(List list) {
        return list;
    }


    private static String toString(Object[] objects, String[] labels, String column) {
        int index = indexOf(labels, column);
        return index >= 0 ? objects[index].toString() : "";
    }

    private static long toLong(Object[] objects, String[] labels, String column) {
        return NumberUtils.toLong(toString(objects, labels, column));
    }


    /**
     * Transform the list of Hibernate result entities. Each entity from that list should be build via
     *  {@link org.wf.dp.dniprorada.dao.place.PlaceHibernateResultTransformer#transformTuple}.
     *
     * @return unexpected result for list if it wasn't created via build method above
     **/
    public static PlaceHierarchyTree toTree(List<PlaceHibernateHierarchyRecord> dataRows) {
        Assert.isTrue(!dataRows.isEmpty(), "Entity not found");
        LOG.debug("Got {}", dataRows);

        Map<Long, PlaceHierarchyTree> tempParents = new HashMap<>();
        PlaceHierarchyTree tree = new PlaceHierarchyTree();

        // We want to transform the list of Hibernate entities into hierarchy tree
        for(int i=0; i<dataRows.size(); i++){
            PlaceHibernateHierarchyRecord node = dataRows.get(i);
            if (i==0){
                tree = handleRootElement(node, tempParents);

            } else if ( !node.isAlreadyIncluded() ){
                handleGenericNode(node, tempParents, dataRows, i+1);
            }
            node.setAlreadyIncluded(true);                                      // Disable node for the next iteration
        }
        LOG.debug("Result tree {}", tree);
        tempParents.clear();                                                    // We don't need it anymore because
        return tree;                                                            // the hierarchy was build successfully
    }


    public static PlaceHierarchy toList(List<PlaceHibernateHierarchyRecord> dataRows) {
        Assert.isTrue(!dataRows.isEmpty(), "Entity not found");
        LOG.debug("Got {}", dataRows);

        PlaceHierarchyList phl = new PlaceHierarchyList();
        for(PlaceHibernateHierarchyRecord phr : dataRows)
            if (phr != null)
                phl.add(phr.toPlace());

        LOG.debug("Result list {}", phl);
        return phl;
    }

    /**
     * General node (of our tree) isn't:
     * - Root node
     * - Included in result tree yet,
     * hence it should be included (with children)
     *
     * @param node              - it's our general node
     * @param tempParents       - contains our parents (just for quick access)
     * @param dataRows          - our node list
     * @param nextElementIndex  - index of the next element in our list
     **/
    private static void handleGenericNode(PlaceHibernateHierarchyRecord node,
                                          Map<Long, PlaceHierarchyTree> tempParents,
                                          List<PlaceHibernateHierarchyRecord> dataRows,
                                          int nextElementIndex) {
        LOG.debug("Got {}, start from {}", node, nextElementIndex);
        PlaceHierarchyTree parent  = tempParents.get( node.getParentId() ); // Get the parent node
        PlaceHierarchyTree current = node.toTreeElement();                  // Get the current node

        register(current, tempParents);                                     // Register curnt node in temp. storage
        current.setChildren( getChildren(dataRows, nextElementIndex, current) );
        parent.addChild(current);
    }

    /**
     * Root element should be registered as parent only.
     * We don't need to handle it in specific way.
     **/
    private static PlaceHierarchyTree handleRootElement(PlaceHibernateHierarchyRecord node,
                                                        Map<Long, PlaceHierarchyTree> tempParents) {
        LOG.debug("Got {}", node);
        register(node.toTreeElement(), tempParents );
        return tempParents.get( node.getPlaceId() );
    }

    /**
     * Allows to find all children for current node.
     * @param dataRows collection of all nodes
     * @param startElement its a start position. We need to check only unread nodes.
     * @param node our current node
     **/
    private static List<PlaceHierarchyTree> getChildren( List<PlaceHibernateHierarchyRecord> dataRows,
                                                     int startElement,
                                                     PlaceHierarchyTree node ){
        List<PlaceHierarchyTree> children = new ArrayList<>();
        for(int j=startElement; j<dataRows.size(); j++){
            PlaceHibernateHierarchyRecord phr = dataRows.get(j);

            if (phr.isAlreadyIncluded()) // It's already belongs to our result tree
                continue;

            if (node.getPlace().getId().equals(phr.getParentId())) {    // One child was found
                PlaceHierarchyTree itsMySon = phr.toTreeElement();      // Get child
                phr.setAlreadyIncluded(true);                           // Disable child for the next iteration
                children.add( itsMySon );                               // Append child to the children list
            }
        }
        return children;
    }


    /**
     * This method "register" the current node in collection of temporary parents.
     * During tree build process we need to detect known node because parent will be handled before child.
     * The reason why we have such order (parent before child) is very simple:
     *  this logic implemented in Native SQL query.
     *
     * So, finally, we have a result of sql hierarchy query where parents are on top,
     * and they should be registered somewhere just for quick detection.
     *
     * @param node which should be registered in temporary storage
     * @param asTemporaryParents is a temporary storage just for quick detection of known parent node.
     * */
    private static void register(PlaceHierarchyTree node, Map<Long, PlaceHierarchyTree> asTemporaryParents) {
        asTemporaryParents.put(node.getPlace().getId(), node);
        LOG.debug("Node {} registered in temp. storage", node);
    }
}
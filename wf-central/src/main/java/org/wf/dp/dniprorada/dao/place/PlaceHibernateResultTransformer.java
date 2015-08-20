package org.wf.dp.dniprorada.dao.place;

import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    /**
     * Transform the list of Hibernate result entities. Each entity from that list should be build via
     *  {@link org.wf.dp.dniprorada.dao.place.PlaceHibernateResultTransformer#transformTuple}.
     *
     * @return unexpected result for list if it wasn't created via build method above
     **/
    public static PlaceHierarchyTree toTree(List<PlaceHierarchyRecord> dataRows) {
        LOG.debug("Got {}", dataRows);

        Map<Long, PlaceHierarchyTree> tempParents = new HashMap<>();
        PlaceHierarchyTree tree = new PlaceHierarchyTree();

        // We want to transform the list of Hibernate entities into hierarchy tree
        for(int i=0; i<dataRows.size(); i++){
            PlaceHierarchyRecord node = dataRows.get(i);
            LOG.debug("Handling of {} started", node);
            if (i==0){ // It's a root element
                register( node.toTree(), tempParents );
                tree = tempParents.get( node.getPlaceId() );
            } else if ( !node.isAlreadyIncluded() ){
                /*
                    It means that:
                    - it's general node of our tree (isn't a root)
                    - this node isn't included in result tree yet, hence, it should be included (with children)
                 */
                PlaceHierarchyTree parent  = tempParents.get( node.getParentId() ); // Get the parent node
                PlaceHierarchyTree current = node.toTree();                         // Get the current node
                register(current, tempParents);                                  // Register curnt node in temp. storage
                current.setChildren(getChildren(dataRows, i + 1, current));
                parent.addChild(current);
            }
            node.setAlreadyIncluded(true);                                      // Disable node for the next iteration
        }
        LOG.debug("Whole tree {}", tree);
        tempParents.clear();                                                    // We don't need it anymore because
        return tree;                                                            // the hierarchy was build successfully
    }

    /**
     * Allows to find all children for current node.
     * @param dataRows collection of all nodes
     * @param startElement its a start position. We need to check only unread nodes.
     * @param node our current node
     **/
    private static List<PlaceHierarchyTree> getChildren( List<PlaceHierarchyRecord> dataRows,
                                                     int startElement,
                                                     PlaceHierarchyTree node ){
        List<PlaceHierarchyTree> children = new ArrayList<>();
        for(int j=startElement; j<dataRows.size(); j++){
            PlaceHierarchyRecord hr = dataRows.get(j);

            if (hr.isAlreadyIncluded()) // It's already belongs to our result tree
                continue;

            if (node.getPlace().getId().equals(hr.getParentId())) {     // One child was found
                PlaceHierarchyTree itsMySon = hr.toTree();              // Get child
                hr.setAlreadyIncluded(true);                            // Disable child for the next iteration
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
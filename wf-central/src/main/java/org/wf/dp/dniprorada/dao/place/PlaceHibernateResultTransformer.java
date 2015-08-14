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


    /**
     * Transform the list of Hibernate result entities. Each entity from that list should be build via
     *  {@link org.wf.dp.dniprorada.dao.place.PlaceHibernateResultTransformer#transformTuple}.
     *
     * @return unexpected result for list if it wasn't created via build method above
     **/
    public static PlaceHierarchy toTree(List<PlaceHierarchyRecord> dataRows) {
        LOG.info("Got {}", dataRows);

        Map<Long, PlaceHierarchy> tempParents = new HashMap<>();
        PlaceHierarchy tree = new PlaceHierarchy();

        // We want to transform the list of Hibernate entities into hierarchy tree
        for(int i=0; i<dataRows.size(); i++){
            PlaceHierarchyRecord node = dataRows.get(i);
            LOG.info("Handling of {} started", node);
            if (i==0){ // It's a root element
                register( node.toTree(), tempParents);
            } else if (!node.isAlreadyIncluded()) {
                /*
                    It means that:
                    - it's general node of our tree (isn't a root)
                    - this node isn't included in result tree yet, hence, it should be included (with children)
                 */
                PlaceHierarchy parent = tempParents.get( node.getParentId() );   // Get the parent node
                PlaceHierarchy currnt = node.toTree();                           // Get the current node
                parent.addChild(currnt);                                        // Link them as parent-child
                register(currnt, tempParents);                                  // Register curnt node in temp. storage

                List<PlaceHierarchy> children = new ArrayList<>();              // Create a container for children
                currnt.setChildren(children);                                   // of our current element

                // Now we need to find all children of current node. Thus, start from the next element in data rows
                for(int j=i+1; j<dataRows.size(); j++){
                    PlaceHierarchyRecord hr = dataRows.get(j);
                    LOG.info("Index of current node {}, index of next node {}, the node {}", i, j, hr);

                    if (hr.isAlreadyIncluded()) // It's already belongs to our result tree
                        continue;

                    if (currnt.getPlace().getId().equals(hr.getParentId())) {   // One child was found
                        PlaceHierarchy itsMySon = hr.toTree();                  // Get child
                        hr.setAlreadyIncluded(true);                            // Disable child for the next iteration
                        children.add( itsMySon );                               // Append child to the children list
                    }
                }
            }
            node.setAlreadyIncluded(true);                                       // Disable node for the next iteration
            LOG.info("Node handled {}", node);
            LOG.info("Current tree {}", tree);
        }
        LOG.info("Whole tree {}", tree);
        LOG.info("Temp. parent storage {}", tempParents);
        tempParents.clear();                                                    // We don't need it anymore because
        return tree;                                                            // the hierarchy was build successfully
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
    private static void register(PlaceHierarchy node, Map<Long, PlaceHierarchy> asTemporaryParents) {
        asTemporaryParents.put(node.getPlace().getId(), node);
        LOG.info("Node {} registered in temp. storage", node);
    }

}
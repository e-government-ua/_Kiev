package org.wf.dp.dniprorada.util;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.List;

/**
 * @author dgroup
 * @since  22.07.2015
 */
public class Tree<T> extends AbstractCollection<T> {
    // where 1 = root, oblast

    private Integer levelOfArea;
    private Integer level;
    private T node;
    private List<Tree<T>> children;


    public Integer getLevelOfArea() {
        return levelOfArea;
    }
    public void setLevelOfArea(Integer levelOfArea) {
        this.levelOfArea = levelOfArea;
    }

    public Integer getLevel() {
        return level;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }

    public T getNode() {
        return node;
    }
    public void setNode(T node) {
        this.node = node;
    }

    public List<Tree<T>> getChildren() {
        return children;
    }
    public void setChildren(List<Tree<T>> children) {
        this.children = children;
    }


    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int size() {
        int size = 1;
        for(Tree<T> c : children)
            size += c.size();
        return size;
    }
}
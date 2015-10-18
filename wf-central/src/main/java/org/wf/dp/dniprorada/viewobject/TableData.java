package org.wf.dp.dniprorada.viewobject;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * User: goodg_000
 * Date: 30.05.2015
 * Time: 20:33
 */
public class TableData {

    @JsonProperty("sTable")
    private String tableName;

    @JsonProperty("aColumn")
    private String[] columnNames;

    @JsonProperty("aRow")
    private List<String[]> rows;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public List<String[]> getRows() {
        return rows;
    }

    public void setRows(List<String[]> rows) {
        this.rows = rows;
    }
}

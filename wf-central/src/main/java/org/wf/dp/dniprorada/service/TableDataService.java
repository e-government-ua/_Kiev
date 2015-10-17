package org.wf.dp.dniprorada.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;
import org.wf.dp.dniprorada.model.*;
import org.wf.dp.dniprorada.viewobject.TableData;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Provide methods to export and import TableData for predefined table sets.
 * <p/>
 * User: goodg_000
 * Date: 30.05.2015
 * Time: 20:44
 */
public class TableDataService {

    private final static String DELIMITER = ",";
    private static Map<Class, EntityMetadata> entityMetadataMap = new HashMap<>();
    private SessionFactory sessionFactory;
    private JdbcTemplate jdbcTemplate;

    private static void removeLastDelimiter(StringBuilder sb) {
        sb.delete(sb.length() - DELIMITER.length(), sb.length());
    }

    @Required
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Required
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<TableData> exportData(TablesSet tablesSet) {

        List<TableData> res = new ArrayList<>();

        for (Class entityClass : tablesSet.getEntityClasses()) {
            res.add(exportDataInternal(entityClass));
        }

        return res;
    }

    private EntityMetadata getEntityMetadata(Class entityClass) {
        EntityMetadata metadata = entityMetadataMap.get(entityClass);
        if (metadata == null) {
            metadata = createEntityMetadata(entityClass);
            entityMetadataMap.put(entityClass, metadata);
        }

        return metadata;
    }

    private EntityMetadata createEntityMetadata(Class entityClass) {
        EntityMetadata res = new EntityMetadata();

        AbstractEntityPersister entityPersister = ((AbstractEntityPersister) sessionFactory.getClassMetadata(
                entityClass));

        res.setTableName(entityPersister.getTableName());

        LinkedHashSet<String> propertyNames = new LinkedHashSet<>();
        propertyNames.add(entityPersister.getIdentifierPropertyName());
        propertyNames.addAll(Arrays.asList(entityPersister.getPropertyNames()));

        List<PropertyMetadata> properties = new ArrayList<>();
        for (String propertyName : propertyNames) {

            Class propertyType = entityPersister.getPropertyType(propertyName).getReturnedClass();
            if (Collection.class.isAssignableFrom(propertyType) || propertyType.isArray()) {
                continue;
            }

            if (sessionFactory.getClassMetadata(propertyType) != null) {
                propertyType = Integer.class; // foreign key of associated entity
            }

            PropertyMetadata propertyMetadata = new PropertyMetadata();
            propertyMetadata.setPropertyName(propertyName);
            propertyMetadata.setPropertyType(propertyType);
            propertyMetadata.setColumnName(entityPersister.getPropertyColumnNames(propertyName)[0]);
            properties.add(propertyMetadata);
        }

        res.setProperties(properties);

        return res;
    }

    private TableData exportDataInternal(Class entityClass) {

        final EntityMetadata entityMetadata = getEntityMetadata(entityClass);

        final String[] columnNames = new String[entityMetadata.getProperties().size()];

        StringBuilder selectQuery = new StringBuilder("SELECT ");
        final List<PropertyMetadata> properties = entityMetadata.getProperties();
        for (int i = 0; i < properties.size(); i++) {
            PropertyMetadata propertyMetadata = properties.get(i);

            String columnName = propertyMetadata.getColumnName();
            columnNames[i] = removeQuotes(columnName);
            selectQuery.append(columnName).append(DELIMITER);
        }
        removeLastDelimiter(selectQuery);
        selectQuery.append(" FROM ").append(entityMetadata.getTableName());

        List<String[]> rows = jdbcTemplate.query(
                selectQuery.toString(), new RowMapper<String[]>() {
                    @Override
                    public String[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                        String[] row = new String[columnNames.length];
                        for (int i = 0; i < properties.size(); i++) {
                            PropertyMetadata propertyMetadata = properties.get(i);
                            Object value = readValue(propertyMetadata, rs);
                            row[i] = convertValueToString(value);
                        }

                        return row;
                    }
                });

        TableData res = new TableData();
        res.setTableName(removeQuotes(entityMetadata.getTableName()));
        res.setColumnNames(columnNames);
        res.setRows(rows);

        return res;
    }

    private Object readValue(PropertyMetadata propertyMetadata, ResultSet rs) {

        Class propertyType = propertyMetadata.getPropertyType();

        String columnName = removeQuotes(propertyMetadata.getColumnName());

        Object value;
        try {
            value = rs.getObject(columnName);

            if (value == null) {
                return null;
            }

            if (Integer.class.isAssignableFrom(propertyType)) {
                value = rs.getInt(columnName);
            } else if (String.class.isAssignableFrom(propertyType)) {
                value = rs.getString(columnName);
            } else if (Boolean.class.isAssignableFrom(propertyType)) {
                value = rs.getBoolean(columnName);
            } else if (DateTime.class.isAssignableFrom(propertyType)) {
                value = new DateTime(rs.getDate(columnName));
            } else if (Date.class.isAssignableFrom(propertyType)) {
                value = rs.getDate(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return value;
    }

    private String removeQuotes(String dbObjectName) {
        return dbObjectName.replace("\"", "");
    }

    private String convertValueToString(Object value) {
        if (value == null) {
            return null;
        }

        return "" + value;
    }

    public void importData(TablesSet tablesSet, List<TableData> tableDataList) {
        deleteAllData(tablesSet);

        Map<String, TableData> tableDataMap = new HashMap<>();
        for (TableData tableData : tableDataList) {
            tableDataMap.put(tableData.getTableName(), tableData);
        }

        for (Class entityClass : tablesSet.getEntityClasses()) {
            final EntityMetadata entityMetadata = getEntityMetadata(entityClass);

            String tableNameWithoutQuotes = removeQuotes(entityMetadata.getTableName());
            TableData tableData = tableDataMap.get(tableNameWithoutQuotes);
            Assert.notNull(tableData, "TableData for table " + tableNameWithoutQuotes + " is not specified!");

            importDataInternal(entityMetadata, tableData);
        }
    }

    private void importDataInternal(EntityMetadata entityMetadata, TableData tableData) {

        Map<String, PropertyMetadata> columnNameToPropertyMetadataMap = new HashMap<>();
        for (PropertyMetadata propertyMetadata : entityMetadata.getProperties()) {
            columnNameToPropertyMetadataMap.put(removeQuotes(propertyMetadata.getColumnName()), propertyMetadata);
        }

        StringBuilder insertQuery = new StringBuilder();
        insertQuery.append("INSERT INTO ").append(entityMetadata.getTableName()).append(" (");
        String[] columnNames = tableData.getColumnNames();

        for (String columnName : columnNames) {
            PropertyMetadata propertyMetadata = columnNameToPropertyMetadataMap.get(columnName);
            insertQuery.append(propertyMetadata.getColumnName()).append(DELIMITER);
        }
        removeLastDelimiter(insertQuery);

        insertQuery.append(") VALUES (");
        for (int i = 0; i < columnNames.length; ++i) {
            insertQuery.append("?").append(DELIMITER);
        }
        removeLastDelimiter(insertQuery);
        insertQuery.append(")");

        String query = insertQuery.toString();
        for (String[] values : tableData.getRows()) {

            Object[] parsedValues = new Object[values.length];

            for (int i = 0; i < values.length; ++i) {
                String columnNameWithoutQuotes = columnNames[i];
                PropertyMetadata propertyMetadata = columnNameToPropertyMetadataMap.get(columnNameWithoutQuotes);

                parsedValues[i] = parseValue(values[i], propertyMetadata.getPropertyType());
            }

            jdbcTemplate.update(query, parsedValues);
        }
    }

    private Object parseValue(String value, Class requiredType) {
        if (value == null) {
            return null;
        }

        Object res = null;

        if (requiredType.equals(String.class)) {
            res = value;
        } else if (requiredType.equals(Integer.class)) {
            res = Integer.parseInt(value);
        } else if (requiredType.equals(Long.class)) {
            res = Long.parseLong(value);
        } else if (requiredType.equals(Boolean.class)) {
            res = Boolean.parseBoolean(value);
        } else {
            throw new IllegalArgumentException("Type " + requiredType + " is not supported!");
        }

        return res;
    }

    private void deleteAllData(TablesSet tablesSet) {

        // Reverse order because entities are sorted from more to less independent
        // (independent - no references to other entities).
        // Less independent should be removed first to prevent failing foreign key constraints.
        Class[] entityClasses = tablesSet.getEntityClasses();
        for (int i = entityClasses.length - 1; i >= 0; i--) {
            Class entityClass = entityClasses[i];
            final EntityMetadata entityMetadata = getEntityMetadata(entityClass);

            jdbcTemplate.update("DELETE FROM " + entityMetadata.getTableName());
        }
    }

    /**
     * Set of predefined tables which are imported/exported all together. <br/>
     * Entity classes listed in data structure should be sorted in such way that first entities should not contain ManyToOne
     * or OneToOne associations to subsequent entities.
     */
    public enum TablesSet {
        ServicesAndPlaces(Region.class, City.class, Category.class, Subcategory.class, ServiceType.class,
                Service.class, ServiceData.class);

        private Class[] entityClasses;

        TablesSet(Class... entityClasses) {
            this.entityClasses = entityClasses;
        }

        public Class[] getEntityClasses() {
            return entityClasses;
        }
    }

    private static class PropertyMetadata {
        private String propertyName;
        private String columnName;
        private Class propertyType;

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public Class getPropertyType() {
            return propertyType;
        }

        public void setPropertyType(Class propertyType) {
            this.propertyType = propertyType;
        }
    }

    private static class EntityMetadata {
        private String tableName;
        private List<PropertyMetadata> properties;

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public List<PropertyMetadata> getProperties() {
            return properties;
        }

        public void setProperties(List<PropertyMetadata> properties) {
            this.properties = properties;
        }
    }
}

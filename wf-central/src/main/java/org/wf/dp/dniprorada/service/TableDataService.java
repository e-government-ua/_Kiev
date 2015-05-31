package org.wf.dp.dniprorada.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.wf.dp.dniprorada.model.*;
import org.wf.dp.dniprorada.viewobject.TableData;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Provide methods to export and import TableData for predefined table sets.
 *
 * User: goodg_000
 * Date: 30.05.2015
 * Time: 20:44
 */
public class TableDataService {

   private static Map<Class, EntityMetadata> entityMetadataMap = new HashMap<>();

   private SessionFactory sessionFactory;
   private JdbcTemplate jdbcTemplate;

   @Required
   public void setDataSource(DataSource dataSource){
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

      SortedSet<String> propertyNames = new TreeSet<>();
      propertyNames.add(entityPersister.getIdentifierPropertyName());
      propertyNames.addAll(Arrays.asList(entityPersister.getPropertyNames()));

      List<PropertyMetadata> properties = new ArrayList<>();
      for (String propertyName : propertyNames) {

         Class propertyType = entityPersister.getPropertyType(propertyName).getReturnedClass();
         if (Collection.class.isAssignableFrom(propertyType) || propertyType.isArray()) {
            continue;
         }

         if (Entity.class.isAssignableFrom(propertyType)) {
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

      final String DELIMITER = ", ";
      StringBuilder selectQuery = new StringBuilder("SELECT ");
      final List<PropertyMetadata> properties = entityMetadata.getProperties();
      for (int i = 0; i < properties.size(); i++) {
         PropertyMetadata propertyMetadata = properties.get(i);

         String columnName = propertyMetadata.getColumnName();
         columnNames[i] = removeQuotes(columnName);
         selectQuery.append(columnName).append(DELIMITER);
      }
      selectQuery.delete(selectQuery.length() - DELIMITER.length(), selectQuery.length());
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
      Object value = null;

      Class propertyType = propertyMetadata.getPropertyType();

      String columnName = removeQuotes(propertyMetadata.getColumnName());

      try {
         if (Integer.class.isAssignableFrom(propertyType)) {
            value = rs.getInt(columnName);
         }
         else if (String.class.isAssignableFrom(propertyType)) {
            value = rs.getString(columnName);
         }
         else if (DateTime.class.isAssignableFrom(propertyType)) {
            value = new DateTime(rs.getDate(columnName));
         }
         else if (Date.class.isAssignableFrom(propertyType)) {
            value = rs.getDate(columnName);
         }
      }
      catch (SQLException e) {
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

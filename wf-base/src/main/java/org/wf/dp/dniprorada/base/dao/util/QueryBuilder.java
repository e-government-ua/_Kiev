package org.wf.dp.dniprorada.base.dao.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.Type;
import org.springframework.util.Assert;

/**
 * Builds Query.
 */
public class QueryBuilder {

   private final int MAX_IN_VALUES_COUNT = 1000;

   private final Session session;
   private final StringBuilder hqlQuery;
   private final Map<String, Couple> parameters = new HashMap<String, Couple>();

   /**
    * Instantiates a new Query builder.
    *
    * @param session the session
    */
   public QueryBuilder(Session session) {
      this(session, "");
   }

   /**
    * Instantiates a new Query builder.
    *
    * @param session the session
    * @param hql     the hql
    */
   public QueryBuilder(Session session, String hql) {
      Assert.notNull(session, "Session is undefined");

      this.session = session;
      this.hqlQuery = new StringBuilder(hql);
   }

   /**
    * Append hql to query builder.
    *
    * @param hql the hql
    * @return the query builder
    */
   public QueryBuilder append(String hql) {
      this.hqlQuery.append(hql);

      return this;
   }

   /**
    * Append hql and parameter value to query builder.
    *
    * @param hql   the hql
    * @param value the value
    * @return the query builder
    */
   public QueryBuilder append(String hql, Object value) {
      this.hqlQuery.append(hql);

      final String key = extractParameter(hql).toUpperCase();
      final Couple old = parameters.put(key, new Couple(null, value));

      if (old != null) {
         assertValue(value, key, old);
      }

      return this;
   }

   private void assertValue(Object value, String key, Couple old) {
      Assert.isTrue(old.value.equals(value),
              String.format("More than one value for the same value: %s old: %s; new: %s", key, old.value, value));
   }

   /**
    * Append different hqls to query builder depends on value(null or not null).
    *
    * @param hqlNotNull the hql not null
    * @param hqlNull    the hql null
    * @param value      the value
    * @return the query builder
    */
   public QueryBuilder append(String hqlNotNull, String hqlNull, Object value) {
      if (value == null) {
         append(hqlNull);
      } else {
         append(hqlNotNull, value);
      }

      return this;
   }

   /**
    * Append different hqls to query builder depends on value(null or not null) nad it's type.
    *
    * @param hqlNotNull the hql not null
    * @param hqlNull    the hql null
    * @param value      the value
    * @param type       the type
    * @return the query builder
    */
   public QueryBuilder append(String hqlNotNull, String hqlNull, Object value, Type type) {
      if (value == null) {
         append(hqlNull);
      } else {
         append(hqlNotNull, value, type);
      }

      return this;
   }

   /**
    * Append hql to query builder, parameter value and it's type.
    *
    * @param hql   the hql
    * @param value the value
    * @param type  the type
    * @return the query builder
    */
   public QueryBuilder append(String hql, Object value, Type type) {
      Assert.notNull(type, "Parameter type is undefined");

      this.hqlQuery.append(hql);

      final String key = extractParameter(hql).toUpperCase();
      final Couple old = parameters.put(key, new Couple(type, value));

      if (old != null) {
         Assert.isTrue(old.type.equals(type), "More than one type for the same value: " + key + " old: " +
                 old.type + "; new: " + type);
         assertValue(value, key, old);
      }

      return this;
   }

   /**
    * Append not null value and corresponding hql.
    *
    * @param hql   the hql
    * @param value the value
    * @return the query builder
    */
   public QueryBuilder appendNotNull(String hql, Object value) {
      if (value != null) {
         append(hql, value);
      }

      return this;
   }

   /**
    * Appends in criteria with protection that max values count is 1000 records. In case of more it splits in criteria
    * on parts connected via OR.
    */
   public QueryBuilder appendInSafe(String alias, String fieldName, List values) {

      if (values == null || values.isEmpty()) {
         return this;
      }

      if (values.size() <= MAX_IN_VALUES_COUNT) {
         append(String.format("%s in (:%s)", alias, fieldName), values);
         return this;
      }

      int currRecord = 0;
      int i = 1;
      append("(");
      while (currRecord < values.size()) {
         int nextCurrRecord = Math.min(currRecord + MAX_IN_VALUES_COUNT, values.size());
         List subList = values.subList(currRecord, nextCurrRecord);
         append(String.format("(%s in (:%s))", alias, fieldName + i), subList);

         if (currRecord < values.size()) {
            append(" or ");
         }
      }
      append(")");


      return this;
   }

   /**
    * Append like hql in case of value is not null.
    *
    * @param hql   the hql
    * @param value the value
    * @return the query builder
    */
   public QueryBuilder appendLikeNotNull(String hql, Object value) {
      if (value != null) {
         this.hqlQuery.append(hql);

         final String delimiter = "%";
         final String key = extractParameter(hql).toUpperCase();
         final Couple old = parameters.put(key, new Couple(null, delimiter + value + delimiter));

         if (old != null) {
            assertValue(value, key, old);
         }
      }

      return this;
   }

   /**
    * Builds query object.
    *
    * @return the query
    */
   public Query toQuery() {
      final Query query = session.createQuery(hqlQuery.toString());

      for (Map.Entry<String, Couple> entry : parameters.entrySet()) {
         if (entry.getValue().type != null) {
            if (entry.getValue().value instanceof Collection) {
               final Collection<?> values = (Collection<?>) entry.getValue().value;
               query.setParameterList(entry.getKey(), values, entry.getValue().type);
            } else {
               query.setParameter(entry.getKey(), entry.getValue().value, entry.getValue().type);
            }
         } else {
            if (entry.getValue().value instanceof Collection) {
               final Collection<?> values = (Collection<?>) entry.getValue().value;
               query.setParameterList(entry.getKey(), values);
            } else {
               query.setParameter(entry.getKey(), entry.getValue().value);
            }
         }
      }

      return query;
   }

   /**
    * Extracts parameter.
    *
    * @param hql the hql
    * @return the string
    */
   protected static String extractParameter(String hql) {
      final int i = hql.indexOf(':');

      Assert.isTrue(i != -1, "There is no value in the given HQL query part: " + hql);

      char ch;
      int j = i + 1;

      while (j < hql.length() && ((ch = hql.charAt(j)) == '_' || Character.isLetter(ch) || Character.isDigit(ch))) {
         j++;
      }

      Assert.isTrue(j != i + 1, "Empty parameter name is not allowed: " + hql);

      return hql.substring(i + 1, j);
   }

   /**
    * Class for pair of hibernate type and value of parameter.
    */
   private static final class Couple {
      private final Type type;
      private final Object value;

      private Couple(Type type, Object value) {
         this.type = type;
         this.value = value;
      }
   }
}
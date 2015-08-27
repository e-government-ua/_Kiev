### What?
The sizes and lines of our sql/hql queries will grow, therefore we have to think about storage. If we try to store in java directly - it's a lot of unused lines, dirty code.

**Current solution is very simple:**
* we have a default directory(`resources/queryloader/`) wherein you can put/store your `*.sql` file.
 You can override the home directory if needed;
* **QueryLoader** allows you to load your `*.sql` file and transform to **java.lang.String**, which you can use as you wish;
* A lot of IDE supports `*.sql` so you will receive some additional pros (comparing with hard coded in java queries)

**Example:**

 Some `SQL_FILE` located in `SQL_HOME_DIRECTORY` and contains next query:
```sql
   select 42 from dual
```
hence, we can do next things
```java
    String sql = new QueryLoader(SQL_HOME_DIRECTORY).get(SQL_FILE);
    int meaningOfTheLife = hibernateSession.createQuery(sql).uniqueResult();
```
If you put your file into default directory(`resources/queryloader/`) you can skip/omit `SQL_HOME_DIRECTORY` argument in constructor.

Have a good day
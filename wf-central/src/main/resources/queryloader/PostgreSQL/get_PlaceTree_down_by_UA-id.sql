SELECT *
FROM (
       WITH RECURSIVE all_places(
           "nID_Place",
           "nID_Place_Parent",
           "nID_Place_Area",
           "nID_Place_Root",
           level) AS (
         SELECT
           t1."nID_Place",
           t1."nID_Place_Parent",
           t1."nID_Place_Area",
           t1."nID_Place_Root",
           0
         FROM
           "PlaceTree" t1,
           "Place" p
         WHERE
           t1."nID_Place" = p."nID"
           AND p."sID_UA" = :UA_ID
         UNION
         SELECT
           t2."nID_Place",
           t2."nID_Place_Parent",
           t2."nID_Place_Area",
           t2."nID_Place_Root",
           level + 1
         FROM
           "PlaceTree" t2
           , all_places ap
         WHERE
           ap."nID_Place" = t2."nID_Place_Parent"
       )
       SELECT
         p."nID"               AS id,
         p."nID_PlaceType"     AS type_id,
         p."sID_UA"            AS ua_id,
         p."sName"             AS name,
         p."sNameOriginal"     AS original_name,
         ap."nID_Place_Parent" AS parent_id,
         ap."nID_Place_Area"   AS area_id,
         ap."nID_Place_Root"   AS root_id,
         t."bArea"             AS area,
         t."bRoot"             AS root,
         ap.level
       FROM
         all_places ap
         , "Place" p
         , "PlaceType" t
       WHERE
         ap."nID_Place" = p."nID"
         and t."nID" = p."nID_PlaceType"
       ORDER BY ap.level
     ) the_places
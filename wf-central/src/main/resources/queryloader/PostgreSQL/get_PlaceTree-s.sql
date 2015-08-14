SELECT *
FROM (
       WITH RECURSIVE all_places(
           "nID_Place",
           "nID_Place_Parent",
           "nID_Place_Area",
           "nID_Place_Root") AS (
         SELECT
           t1."nID_Place",
           t1."nID_Place_Parent",
           t1."nID_Place_Area",
           t1."nID_Place_Root",
           0 AS depth
         FROM
           "PlaceTree" t1
         UNION
         SELECT
           t2."nID_Place",
           t2."nID_Place_Parent",
           t2."nID_Place_Area",
           t2."nID_Place_Root",
           depth + 1
         FROM
           "PlaceTree" t2
           , all_places ap
         WHERE
           t2."nID_Place_Parent" = ap."nID_Place"
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
         ap.depth
       FROM
         all_places ap
         , "Place" p
       WHERE
         ap."nID_Place" = p."nID"
     ) the_places


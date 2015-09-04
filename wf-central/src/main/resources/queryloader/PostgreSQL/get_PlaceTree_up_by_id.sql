select *
from (
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
      0 AS level
    FROM
      "PlaceTree" t1
    WHERE
      t1."nID_Place" = :PLACE_ID
    UNION
    SELECT
      t2."nID_Place",
      t2."nID_Place_Parent",
      t2."nID_Place_Area",
      t2."nID_Place_Root",
      level + 1 AS level
    FROM
      "PlaceTree" t2
      , all_places ap
    WHERE
      ap."nID_Place_Parent" = t2."nID_Place"
  )
  SELECT
    p."nID"               AS id,
    p."nID_PlaceType"     AS type_id,
    p."sID_UA"            AS ua_id,
    p."sName"             AS name,
    p."sNameOriginal"     AS original_name,
    ap."nID_Place_Parent" AS parent_id,
    ap.level
  FROM
    all_places ap
    , "Place" p
  WHERE
    ap."nID_Place" = p."nID"
  order by ap.level DESC
) place_tree_up
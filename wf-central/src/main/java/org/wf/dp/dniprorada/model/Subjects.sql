create table subjects (
 nID SERIAL PRIMARY KEY,
 sID varchar(100) not null UNIQUE,
 sINN varchar(80) null,
 sPassport varchar(80) null,
 sSB varchar(80) null,
 sOKPO varchar(80) null,
 sName varchar(250) null
);

GRANT SELECT, UPDATE, INSERT, DELETE ON subjects TO central;
GRANT SELECT, UPDATE, INSERT, DELETE ON subjects_nid_seq TO central;

/*
[18:32:33] Belyavtsev Vladimir Vladimirovich (dn310780bvv): 2015-06-15_18:34:33.645 | ERROR | org.hibernate.engine.jdbc.spi.SqlExceptionHelper- ERROR: duplicate key value violates unique constraint "pk_subject"
  Detail: Key ("nID")=(2) already exists.
*/
/*
CREATE SEQUENCE "public"."hibernate_sequence"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 8
 CACHE 1;

GRANT SELECT, UPDATE ON hibernate_sequence TO central;

SELECT setval('"public"."hibernate_sequence"', 8, true);
*/
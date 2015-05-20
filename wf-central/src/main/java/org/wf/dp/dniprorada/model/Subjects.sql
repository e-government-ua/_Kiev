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

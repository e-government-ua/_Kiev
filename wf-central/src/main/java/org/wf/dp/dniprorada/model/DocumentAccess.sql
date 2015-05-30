drop table DocumentAccess

create table DocumentAccess(
 nID SERIAL PRIMARY KEY,
 nID_Document int,
 sDateCreate date,
 nMS int,
 sFIO varchar (70) null,
 sTarget varchar (200) null,
 sTelephone varchar (13) null,
 sMail varchar (50) null,
 sSecret varchar (40)
 sAnswer varchar (40)
);

/*test*/
GRANT SELECT, UPDATE, INSERT, DELETE ON DocumentAccess TO activiti;
GRANT SELECT, UPDATE, INSERT, DELETE ON DocumentAccess_nid_seq TO activiti;

/*prod*/
GRANT SELECT, UPDATE, INSERT, DELETE ON DocumentAccess TO central;
GRANT SELECT, UPDATE, INSERT, DELETE ON DocumentAccess_nid_seq TO central;

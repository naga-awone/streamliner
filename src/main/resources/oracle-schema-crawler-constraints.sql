SELECT
    P.TABLE_NAME AS TABLE_NAME,
    PC.COLUMN_NAME AS COLUMN_NAME,
    p.CONSTRAINT_TYPE AS CONSTRAINT_TYPE
FROM
    ALL_CONS_COLUMNS PC,
    ALL_CONSTRAINTS P
WHERE P.OWNER NOT IN
      ('ANONYMOUS', 'APEX_PUBLIC_USER', 'APPQOSSYS', 'BI', 'CTXSYS', 'DBSNMP', 'DIP',
       'EXFSYS', 'FLOWS_30000', 'FLOWS_FILES', 'GSMADMIN_INTERNAL', 'IX', 'LBACSYS',
       'MDDATA', 'MDSYS', 'MGMT_VIEW', 'OE', 'OLAPSYS', 'ORACLE_OCM',
       'ORDPLUGINS', 'ORDSYS', 'OUTLN', 'OWBSYS', 'PM', 'SCOTT', 'SH',
       'SI_INFORMTN_SCHEMA', 'SPATIAL_CSW_ADMIN_USR', 'SPATIAL_WFS_ADMIN_USR',
       'SYS', 'SYSMAN', 'SYSTEM', 'TSMSYS', 'WKPROXY', 'WKSYS', 'WK_TEST',
       'WMSYS', 'XDB', 'XS$NULL', 'RDSADMIN')
  AND NOT REGEXP_LIKE(P.OWNER, '^APEX_[0-9]{6}$')
  AND NOT REGEXP_LIKE(P.OWNER, '^FLOWS_[0-9]{5,6}$')
  AND P.OWNER = '{{SCHEMA_NAME}}'
  AND P.CONSTRAINT_TYPE IN ('P', 'U')
  AND PC.OWNER = P.OWNER
  AND PC.CONSTRAINT_NAME = P.CONSTRAINT_NAME
  AND PC.TABLE_NAME = P.TABLE_NAME
  AND ABS(MOD(ORA_HASH(PC.COLUMN_NAME), 4)) = {{THREAD_INDEX}}
ORDER BY
    TABLE_NAME
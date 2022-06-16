USE DATABASE SANDBOX_POC1;

USE SCHEMA EMPLOYEES;

COPY INTO Employee (ID,Age,NAME)
FROM ( SELECT

	$1:ID::NUMBER(38, 0),
$1:Age::NUMBER(38, 0),
$1:NAME::VARCHAR(38)
FROM @STREAMLINER_QUICKSTART_1_stage/Employee)

	FILE_FORMAT = ( TYPE = PARQUET);
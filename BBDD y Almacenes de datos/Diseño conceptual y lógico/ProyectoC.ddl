-- Generado por Oracle SQL Developer Data Modeler 23.1.0.087.0806
--   en:        2024-10-22 10:16:36 CEST
--   sitio:      Oracle Database 11g
--   tipo:      Oracle Database 11g



-- predefined type, no DDL - MDSYS.SDO_GEOMETRY

-- predefined type, no DDL - XMLTYPE

CREATE TABLE admissiondx (
    admissiondxid                  INTEGER NOT NULL,
    admitdxtext                    VARCHAR(255),
    admitdxname                    VARCHAR(255),
    apachepatientresult_apacheprid INTEGER NOT NULL
);

ALTER TABLE admissiondx ADD CONSTRAINT admissiondx_pk PRIMARY KEY ( admissiondxid );

CREATE TABLE apachepatientresult (
    apacheversion             VARCHAR(64),
    apacheprid                INTEGER NOT NULL,
    admissiondx_admissiondxid INTEGER NOT NULL,
    admissiondxid             INTEGER NOT NULL
);

ALTER TABLE apachepatientresult ADD CONSTRAINT apachepatientresult_pk PRIMARY KEY ( apacheprid );

CREATE TABLE city (
    cityname            VARCHAR(64) NOT NULL,
    hospital_hospitalid INTEGER NOT NULL
);

ALTER TABLE city ADD CONSTRAINT city_pk PRIMARY KEY ( cityname );

CREATE TABLE hospital (
    hospitalid    INTEGER NOT NULL,
    hospitalname  VARCHAR(64),
    region        VARCHAR(64),
    city          VARCHAR(64),
    city_cityname VARCHAR(64) NOT NULL
);

ALTER TABLE hospital ADD CONSTRAINT hospital_pk PRIMARY KEY ( hospitalid );

CREATE TABLE icuadmission (
    admissionid                   INTEGER NOT NULL,
    apachescore                   INTEGER,
    hospitaldischargetime         INTEGER,
    dateintake                    INTEGER,
    heartrate                     INTEGER,
    lengthofstay                  INTEGER,
    nummedications                INTEGER,
    hospitalloc                   VARCHAR(255),
    time_dateid2                  INTEGER NOT NULL,
    time_dateid                   INTEGER NOT NULL,
    admissiondx_admissiondxid     INTEGER NOT NULL,
    hospital_hospitalid           INTEGER NOT NULL,
    patient_patiendid             INTEGER NOT NULL,
    vitalaperiodic_vitalaperiodic INTEGER NOT NULL,
    medication_medicationid       INTEGER NOT NULL,
    admissiondxid                 INTEGER NOT NULL
);

ALTER TABLE icuadmission ADD CONSTRAINT icuadmission_pk PRIMARY KEY ( admissionid );

CREATE TABLE medication (
    medicationid    INTEGER NOT NULL,
    drugorderoffset INTEGER,
    drugstopoffset  INTEGER,
    drugstartoffset INTEGER,
    drugname        VARCHAR(255),
    dosage          VARCHAR(60),
    routeadmin      VARCHAR(120),
    frequency       VARCHAR(255),
    gtc             INTEGER
);

ALTER TABLE medication ADD CONSTRAINT medication_pk PRIMARY KEY ( medicationid );

CREATE TABLE pasthistory (
    pasthistory         INTEGER NOT NULL,
    pasthistorynotetype VARCHAR(255),
    pasthistoryvalue    VARCHAR(255),
    patient_patiendid   INTEGER NOT NULL
);

ALTER TABLE pasthistory ADD CONSTRAINT pasthistory_pk PRIMARY KEY ( pasthistory );

CREATE TABLE patient (
    age            VARCHAR(50),
    patiendid      INTEGER NOT NULL,
    gender         VARCHAR(50),
    ethnicity      VARCHAR(50),
    medicalhistory VARCHAR(255)
);

ALTER TABLE patient ADD CONSTRAINT patient_pk PRIMARY KEY ( patiendid );

CREATE TABLE time (
    dateid     INTEGER NOT NULL,
    "Date"     INTEGER,
    month      INTEGER,
    dayofweek  INTEGER,
    dayofmonth INTEGER,
    monthname  VARCHAR(50),
    year       INTEGER
);

ALTER TABLE time ADD CONSTRAINT time_pk PRIMARY KEY ( dateid );

CREATE TABLE vitalaperiodic (
    vitalaperiodic INTEGER NOT NULL,
    cardiacinput   FLOAT,
    cardiacoutput  FLOAT
);

ALTER TABLE vitalaperiodic ADD CONSTRAINT vitalaperiodic_pk PRIMARY KEY ( vitalaperiodic );

ALTER TABLE admissiondx
    ADD CONSTRAINT admissiondx_apr_fk FOREIGN KEY ( apachepatientresult_apacheprid )
        REFERENCES apachepatientresult ( apacheprid );

ALTER TABLE apachepatientresult
    ADD CONSTRAINT apachepr_admissiondx_fk FOREIGN KEY ( admissiondx_admissiondxid )
        REFERENCES admissiondx ( admissiondxid );

ALTER TABLE city
    ADD CONSTRAINT city_hospital_fk FOREIGN KEY ( hospital_hospitalid )
        REFERENCES hospital ( hospitalid );

ALTER TABLE hospital
    ADD CONSTRAINT hospital_city_fk FOREIGN KEY ( city_cityname )
        REFERENCES city ( cityname );

ALTER TABLE icuadmission
    ADD CONSTRAINT icuadmission_admissiondx_fk FOREIGN KEY ( admissiondx_admissiondxid )
        REFERENCES admissiondx ( admissiondxid );

ALTER TABLE icuadmission
    ADD CONSTRAINT icuadmission_hospital_fk FOREIGN KEY ( hospital_hospitalid )
        REFERENCES hospital ( hospitalid );

ALTER TABLE icuadmission
    ADD CONSTRAINT icuadmission_medication_fk FOREIGN KEY ( medication_medicationid )
        REFERENCES medication ( medicationid );

ALTER TABLE icuadmission
    ADD CONSTRAINT icuadmission_patient_fk FOREIGN KEY ( patient_patiendid )
        REFERENCES patient ( patiendid );

ALTER TABLE icuadmission
    ADD CONSTRAINT icuadmission_time_fk FOREIGN KEY ( time_dateid )
        REFERENCES time ( dateid );

ALTER TABLE icuadmission
    ADD CONSTRAINT icuadmission_time_fkv2 FOREIGN KEY ( time_dateid2 )
        REFERENCES time ( dateid );

ALTER TABLE icuadmission
    ADD CONSTRAINT icuadmission_vitalaperiodic_fk FOREIGN KEY ( vitalaperiodic_vitalaperiodic )
        REFERENCES vitalaperiodic ( vitalaperiodic );

ALTER TABLE pasthistory
    ADD CONSTRAINT pasthistory_patient_fk FOREIGN KEY ( patient_patiendid )
        REFERENCES patient ( patiendid );



-- Informe de Resumen de Oracle SQL Developer Data Modeler: 
-- 
-- CREATE TABLE                            10
-- CREATE INDEX                             0
-- ALTER TABLE                             22
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          0
-- CREATE MATERIALIZED VIEW                 0
-- CREATE MATERIALIZED VIEW LOG             0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0

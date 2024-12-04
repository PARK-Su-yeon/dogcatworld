

ALTER TABLE batch_job_execution_context DROP FOREIGN KEY JOB_EXEC_CTX_FK;
ALTER TABLE batch_step_execution_context DROP FOREIGN KEY STEP_EXEC_CTX_FK;
ALTER TABLE batch_step_execution DROP FOREIGN KEY JOB_EXEC_STEP_FK;
ALTER TABLE batch_job_execution_params DROP FOREIGN KEY JOB_EXEC_PARAMS_FK;
ALTER TABLE batch_job_execution DROP FOREIGN KEY JOB_INST_EXEC_FK;

DROP TABLE IF EXISTS batch_step_execution_context;
DROP TABLE IF EXISTS batch_job_execution_context;
DROP TABLE IF EXISTS batch_step_execution;
DROP TABLE IF EXISTS batch_job_execution_params;
DROP TABLE IF EXISTS batch_job_execution;
DROP TABLE IF EXISTS batch_job_instance;
DROP TABLE IF EXISTS batch_step_execution_seq;
DROP TABLE IF EXISTS batch_job_execution_seq;
DROP TABLE IF EXISTS batch_job_seq;

CREATE TABLE batch_job_instance (
                                    JOB_INSTANCE_ID BIGINT NOT NULL PRIMARY KEY,
                                    VERSION BIGINT,
                                    JOB_NAME VARCHAR(100) NOT NULL,
                                    JOB_KEY VARCHAR(32) NOT NULL,
                                    CONSTRAINT JOB_INST_UN UNIQUE (JOB_NAME, JOB_KEY)
) ENGINE=InnoDB;

CREATE TABLE batch_job_execution (
                                     JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
                                     VERSION BIGINT,
                                     JOB_INSTANCE_ID BIGINT NOT NULL,
                                     CREATE_TIME DATETIME(6) NOT NULL,
                                     START_TIME DATETIME(6) DEFAULT NULL,
                                     END_TIME DATETIME(6) DEFAULT NULL,
                                     STATUS VARCHAR(10),
                                     EXIT_CODE VARCHAR(2500),
                                     EXIT_MESSAGE VARCHAR(2500),
                                     LAST_UPDATED DATETIME(6),
                                     CONSTRAINT JOB_INST_EXEC_FK FOREIGN KEY (JOB_INSTANCE_ID) REFERENCES batch_job_instance(JOB_INSTANCE_ID)
) ENGINE=InnoDB;

CREATE TABLE batch_job_execution_params (
                                            JOB_EXECUTION_ID BIGINT NOT NULL,
                                            PARAMETER_NAME VARCHAR(100) NOT NULL,
                                            PARAMETER_TYPE VARCHAR(100) NOT NULL,
                                            PARAMETER_VALUE VARCHAR(2500),
                                            IDENTIFYING CHAR(1) NOT NULL,
                                            CONSTRAINT JOB_EXEC_PARAMS_FK FOREIGN KEY (JOB_EXECUTION_ID) REFERENCES batch_job_execution(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE batch_step_execution (
                                      STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
                                      VERSION BIGINT NOT NULL,
                                      STEP_NAME VARCHAR(100) NOT NULL,
                                      JOB_EXECUTION_ID BIGINT NOT NULL,
                                      CREATE_TIME DATETIME(6) NOT NULL,
                                      START_TIME DATETIME(6) DEFAULT NULL,
                                      END_TIME DATETIME(6) DEFAULT NULL,
                                      STATUS VARCHAR(10),
                                      COMMIT_COUNT BIGINT,
                                      READ_COUNT BIGINT,
                                      FILTER_COUNT BIGINT,
                                      WRITE_COUNT BIGINT,
                                      READ_SKIP_COUNT BIGINT,
                                      WRITE_SKIP_COUNT BIGINT,
                                      PROCESS_SKIP_COUNT BIGINT,
                                      ROLLBACK_COUNT BIGINT,
                                      EXIT_CODE VARCHAR(2500),
                                      EXIT_MESSAGE VARCHAR(2500),
                                      LAST_UPDATED DATETIME(6),
                                      CONSTRAINT JOB_EXEC_STEP_FK FOREIGN KEY (JOB_EXECUTION_ID) REFERENCES batch_job_execution(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE batch_step_execution_context (
                                              STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
                                              SHORT_CONTEXT VARCHAR(2500) NOT NULL,
                                              SERIALIZED_CONTEXT TEXT,
                                              CONSTRAINT STEP_EXEC_CTX_FK FOREIGN KEY (STEP_EXECUTION_ID) REFERENCES batch_step_execution(STEP_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE batch_job_execution_context (
                                             JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
                                             SHORT_CONTEXT VARCHAR(2500) NOT NULL,
                                             SERIALIZED_CONTEXT TEXT,
                                             CONSTRAINT JOB_EXEC_CTX_FK FOREIGN KEY (JOB_EXECUTION_ID) REFERENCES batch_job_execution(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE batch_step_execution_seq (
                                          ID BIGINT NOT NULL,
                                          UNIQUE_KEY CHAR(1) NOT NULL,
                                          CONSTRAINT UNIQUE_KEY_UN UNIQUE (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO batch_step_execution_seq (ID, UNIQUE_KEY)
SELECT * FROM (SELECT 0 AS ID, '0' AS UNIQUE_KEY) AS tmp
WHERE NOT EXISTS (SELECT * FROM batch_step_execution_seq);

CREATE TABLE batch_job_execution_seq (
                                         ID BIGINT NOT NULL,
                                         UNIQUE_KEY CHAR(1) NOT NULL,
                                         CONSTRAINT UNIQUE_KEY_UN UNIQUE (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO batch_job_execution_seq (ID, UNIQUE_KEY)
SELECT * FROM (SELECT 0 AS ID, '0' AS UNIQUE_KEY) AS tmp
WHERE NOT EXISTS (SELECT * FROM batch_job_execution_seq);

CREATE TABLE batch_job_seq (
                               ID BIGINT NOT NULL,
                               UNIQUE_KEY CHAR(1) NOT NULL,
                               CONSTRAINT UNIQUE_KEY_UN UNIQUE (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO batch_job_seq (ID, UNIQUE_KEY)
SELECT * FROM (SELECT 0 AS ID, '0' AS UNIQUE_KEY) AS tmp
WHERE NOT EXISTS (SELECT * FROM batch_job_seq);

DROP TABLE IF EXISTS rem.sample_survey_unit;
DROP TABLE IF EXISTS rem.sample;
DROP TABLE IF EXISTS rem.survey_unit;
DROP SEQUENCE IF EXISTS rem.seq_sample;
DROP SEQUENCE IF EXISTS rem.seq_sample_survey_unit;
DROP SEQUENCE IF EXISTS rem.seq_survey_unit;

DROP SCHEMA IF EXISTS rem;

CREATE SCHEMA rem;

CREATE TABLE rem.sample (
	id int8 NOT NULL,
	"label" varchar(255) NULL,
	CONSTRAINT sample_pkey PRIMARY KEY (id),
	CONSTRAINT uk_bt4g7for18rpq14kwfu32ui3v UNIQUE (label)
);

CREATE TABLE rem.survey_unit (
	id int8 NOT NULL,
	"data" jsonb NULL,
	CONSTRAINT survey_unit_pkey PRIMARY KEY (id)
);

CREATE TABLE rem.sample_survey_unit (
	registered_date date NULL,
	sample_id int8 NOT NULL,
	survey_unit_id int8 NOT NULL,
	CONSTRAINT sample_survey_unit_pkey PRIMARY KEY (sample_id, survey_unit_id)
);

ALTER TABLE rem.sample_survey_unit ADD CONSTRAINT fk7gd1330i4bykx212u6kwux2oa FOREIGN KEY (survey_unit_id) REFERENCES rem.survey_unit(id);
ALTER TABLE rem.sample_survey_unit ADD CONSTRAINT fkg7qvf84fm2grlurf342s5mmqi FOREIGN KEY (sample_id) REFERENCES rem.sample(id);


CREATE SEQUENCE rem.seq_sample
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
	
CREATE SEQUENCE rem.seq_sample_survey_unit
	INCREMENT BY 100
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
	
CREATE SEQUENCE rem.seq_survey_unit
	INCREMENT BY 100
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
CREATE TABLE eg_budgetregister (
    id bigint NOT NULL,
    budgetregisternumber character varying(50) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    financial_year_id bigint NOT NULL,
    budgettype character varying(50),
    statusid bigint,
    state_type character varying(100),
    state_id bigint,
    createdby bigint NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    isactive boolean DEFAULT true,
    version bigint
);


CREATE SEQUENCE seq_eg_budgetregister
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ONLY eg_budgetregister
    ADD CONSTRAINT pk_eg_budgetregister PRIMARY KEY (id);

ALTER TABLE ONLY eg_budgetregister
    ADD CONSTRAINT uk_eg_budgetregister_number UNIQUE (budgetregisternumber);

ALTER TABLE ONLY eg_budgetregister
    ADD CONSTRAINT fk_budgetregister_status FOREIGN KEY (statusid)
        REFERENCES egw_status(id);

ALTER TABLE ONLY eg_budgetregister
    ADD CONSTRAINT fk_budgetregister_finyear FOREIGN KEY (financial_year_id)
        REFERENCES financialyear(id);

ALTER TABLE ONLY eg_budgetregister
    ADD CONSTRAINT fk_budgetregister_state FOREIGN KEY (state_id)
        REFERENCES eg_wf_states(id);

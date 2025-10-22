CREATE SEQUENCE seq_egf_budgetinput
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE TABLE egf_budgetinput
(
  id BIGINT NOT NULL DEFAULT nextval('seq_egf_budgetinput'),
  functionid BIGINT,
  budgetheadid BIGINT,
  financialyearid BIGINT,
  currentfinancialyearid BIGINT,
  budgetcode VARCHAR(50),
  budgetgroup VARCHAR(100),
  currentestimate NUMERIC(13,2),
  currentactual NUMERIC(13,2),
  currentrevisedestimate NUMERIC(13,2),
  nextestimate NUMERIC(13,2),
  createddate TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),

  CONSTRAINT pk_budgetinput PRIMARY KEY (id),

  CONSTRAINT fk_budgethead FOREIGN KEY (budgetheadid)
        REFERENCES egf_budgethead(id) ON DELETE CASCADE,

  CONSTRAINT fk_function FOREIGN KEY (functionid)
        REFERENCES function(id) ON DELETE CASCADE,

  CONSTRAINT fk_financialyear FOREIGN KEY (financialyearid)
        REFERENCES financialyear(id) ON DELETE CASCADE,

  CONSTRAINT fk_currentfinancialyear FOREIGN KEY (currentfinancialyearid)
        REFERENCES financialyear(id) ON DELETE CASCADE
);

ALTER SEQUENCE seq_egf_budgetinput OWNED BY egf_budgetinput.id;

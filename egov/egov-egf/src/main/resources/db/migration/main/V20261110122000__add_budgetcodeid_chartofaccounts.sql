ALTER TABLE IF EXISTS chartofaccounts
    ADD COLUMN IF NOT EXISTS budgetcodeid INT;


ALTER TABLE IF EXISTS chartofaccounts_aud
    ADD COLUMN IF NOT EXISTS budgetcodeid INT;
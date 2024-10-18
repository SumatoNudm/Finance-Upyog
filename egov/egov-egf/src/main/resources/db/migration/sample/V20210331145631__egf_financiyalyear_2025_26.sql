-- SQL to create financial year
INSERT INTO financialyear (id, financialyear, startingdate, endingdate, isactive, createddate, lastmodifieddate, lastmodifiedby, createdby, version, isactiveforposting, isclosed, transferclosingbalance) 
SELECT nextval('seq_financialyear'), '2022-23', '01-Apr-2022', '31-Mar-2023', true, current_date, current_date, 1, 1, 0, true, false, false 
WHERE NOT EXISTS (SELECT 1 FROM financialyear WHERE financialyear='2022-23');

INSERT INTO financialyear (id, financialyear, startingdate, endingdate, isactive, createddate, lastmodifieddate, lastmodifiedby, createdby, version, isactiveforposting, isclosed, transferclosingbalance) 
SELECT nextval('seq_financialyear'), '2023-24', '01-Apr-2023', '31-Mar-2024', true, current_date, current_date, 1, 1, 0, true, false, false 
WHERE NOT EXISTS (SELECT 1 FROM financialyear WHERE financialyear='2023-24');

INSERT INTO financialyear (id, financialyear, startingdate, endingdate, isactive, createddate, lastmodifieddate, lastmodifiedby, createdby, version, isactiveforposting, isclosed, transferclosingbalance) 
SELECT nextval('seq_financialyear'), '2024-25', '01-Apr-2024', '31-Mar-2025', true, current_date, current_date, 1, 1, 0, true, false, false 
WHERE NOT EXISTS (SELECT 1 FROM financialyear WHERE financialyear='2024-25');

INSERT INTO financialyear (id, financialyear, startingdate, endingdate, isactive, createddate, lastmodifieddate, lastmodifiedby, createdby, version, isactiveforposting, isclosed, transferclosingbalance) 
SELECT nextval('seq_financialyear'), '2025-26', '01-Apr-2025', '31-Mar-2026', true, current_date, current_date, 1, 1, 0, true, false, false 
WHERE NOT EXISTS (SELECT 1 FROM financialyear WHERE financialyear='2025-26');

-- SQL to create fiscal periods
INSERT INTO fiscalperiod (id, name, startingdate, endingdate, isactiveforposting, isactive, createddate, lastmodifieddate, lastmodifiedby, createdby, version, financialyearid) 
SELECT nextval('seq_fiscalperiod'), '202223', '01-Apr-2022', '31-Mar-2023', false, true, current_date, current_date, 1, 1, 0, (SELECT id FROM financialyear WHERE financialyear='2022-23') 
WHERE NOT EXISTS (SELECT 1 FROM fiscalperiod WHERE name='202223');

INSERT INTO fiscalperiod (id, name, startingdate, endingdate, isactiveforposting, isactive, createddate, lastmodifieddate, lastmodifiedby, createdby, version, financialyearid) 
SELECT nextval('seq_fiscalperiod'), '202324', '01-Apr-2023', '31-Mar-2024', false, true, current_date, current_date, 1, 1, 0, (SELECT id FROM financialyear WHERE financialyear='2023-24') 
WHERE NOT EXISTS (SELECT 1 FROM fiscalperiod WHERE name='202324');

INSERT INTO fiscalperiod (id, name, startingdate, endingdate, isactiveforposting, isactive, createddate, lastmodifieddate, lastmodifiedby, createdby, version, financialyearid) 
SELECT nextval('seq_fiscalperiod'), '202425', '01-Apr-2024', '31-Mar-2025', false, true, current_date, current_date, 1, 1, 0, (SELECT id FROM financialyear WHERE financialyear='2024-25') 
WHERE NOT EXISTS (SELECT 1 FROM fiscalperiod WHERE name='202425');

INSERT INTO fiscalperiod (id, name, startingdate, endingdate, isactiveforposting, isactive, createddate, lastmodifieddate, lastmodifiedby, createdby, version, financialyearid) 
SELECT nextval('seq_fiscalperiod'), '202526', '01-Apr-2025', '31-Mar-2026', false, true, current_date, current_date, 1, 1, 0, (SELECT id FROM financialyear WHERE financialyear='2025-26') 
WHERE NOT EXISTS (SELECT 1 FROM fiscalperiod WHERE name='202526');
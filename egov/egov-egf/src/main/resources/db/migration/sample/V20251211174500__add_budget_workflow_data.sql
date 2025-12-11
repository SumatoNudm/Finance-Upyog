
INSERT INTO eg_wf_matrix
(id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, "version", enablefields, forwardenabled, smsemailenabled, nextref, rejectenabled)
VALUES(NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'BudgetRegister', 'REVERTED', NULL, NULL, 'Executive Officer', NULL, 'PENDING_EO_APPROVAL', 'Forward, Reject or Revert', NULL, NULL, 'Forward', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);

INSERT INTO eg_wf_matrix
(id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate, "version", enablefields, forwardenabled, smsemailenabled, nextref, rejectenabled)
VALUES(NEXTVAL('SEQ_EG_WF_MATRIX'), 'ANY', 'BudgetRegister', 'FORWARDED_TO_DMA', NULL, NULL, NULL, NULL, 'DMA_APPROVED', 'END', NULL, NULL, 'Approve,Reject', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL);

UPDATE eg_wf_matrix SET
    currentdesignation = 'DMA', nextaction = 'DMA Approval Pending', validactions = 'Forward to DMA,Revert,Reject'
    WHERE id = (SELECT id FROM eg_wf_matrix WHERE currentstate = 'PENDING_EO_APPROVAL');

DELETE FROM eg_wf_matrix WHERE currentstate = 'SUBMITTED_TO_EO';

--SELECT setval('seq_eg_wf_types', (SELECT COALESCE(MAX(id), 0) FROM eg_wf_types));

UPDATE egw_status SET description = 'NEW', code = 'NEW' WHERE id = (SELECT id FROM egw_status WHERE code = 'New');
UPDATE egw_status SET description = 'Cancelled', code = 'CANCELLED' WHERE id = (SELECT id FROM egw_status WHERE code = 'Cancelled');
UPDATE egw_status SET description = 'Approved', code = 'APPROVED' WHERE id = (SELECT id FROM egw_status WHERE code = 'Approved');
UPDATE egw_status SET description = 'Rejected', code = 'REJECTED' WHERE id = (SELECT id FROM egw_status WHERE code = 'Rejected');
UPDATE egw_status SET description = 'FMO Forwarded', code = 'FMO_FORWARDED' WHERE id = (SELECT id FROM egw_status WHERE code = 'FMO_FORWARDED');

INSERT INTO egw_status (id, moduletype, description, lastmodifieddate, code, order_id)
    VALUES (nextval('SEQ_EGW_STATUS'), 'BudgetRegister', 'Reverted', '2025-12-11 18:10:06.324', 'REVERTED', NULL);

INSERT INTO egw_status (id, moduletype, description, lastmodifieddate, code, order_id)
        VALUES (nextval('SEQ_EGW_STATUS'), 'BudgetRegister', 'EO Forwarded', '2025-12-11 18:11:06.324', 'EO_FORWARDED', NULL);

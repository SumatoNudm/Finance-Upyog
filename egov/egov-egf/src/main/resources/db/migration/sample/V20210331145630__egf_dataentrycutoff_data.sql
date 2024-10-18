-- Setting DATA ENTRY CUT OFF date value to 1st April 2025 
-- during software sample data migration.

update eg_appconfig_values set value='1-Apr-2025' where key_id='94';


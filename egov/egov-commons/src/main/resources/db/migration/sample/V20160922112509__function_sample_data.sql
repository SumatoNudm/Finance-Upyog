INSERT INTO function (id,code,"name","type",llevel,parentid,isactive,isnotleaf,parentcode,createddate,createdby,lastmodifieddate,lastmodifiedby,"version") VALUES
	 (1,'00','General Administration',NULL,0,NULL,true,false,NULL,'2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (17,'10','Planning & Regulation',NULL,0,NULL,true,false,NULL,'2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (23,'20','Public Works',NULL,0,NULL,true,false,NULL,'2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (32,'30','Health',NULL,0,NULL,true,false,NULL,'2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (42,'40','Sanitation & Solid Waste Management',NULL,0,NULL,true,false,NULL,'2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (48,'50','Civic Amenities',NULL,0,NULL,true,false,NULL,'2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (57,'60','Urban Forestry',NULL,0,NULL,true,false,NULL,'2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (64,'70','Urban Poverty Alleviation & Social Welfare',NULL,0,NULL,true,false,NULL,'2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (74,'80','Other Services',NULL,0,NULL,true,false,NULL,'2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (79,'90','Revenue',NULL,0,NULL,true,false,NULL,'2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL);
INSERT INTO function (id,code,"name","type",llevel,parentid,isactive,isnotleaf,parentcode,createddate,createdby,lastmodifieddate,lastmodifiedby,"version") VALUES
	 (2,'0001','General Administration-Municipal Body',NULL,1,1,true,false,'00','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (3,'000101','General Body',NULL,2,2,true,false,'0001','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (4,'000102','Standing Committee',NULL,2,2,true,false,'0001','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (5,'000103','Executive Members Office',NULL,2,2,true,false,'0001','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (6,'0002','General Administration-Administration',NULL,1,1,true,false,'00','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (7,'000201','General Administration',NULL,2,6,true,false,'0002','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (8,'000202','Public Relation and IT',NULL,2,6,true,false,'0002','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (9,'000203','Legal',NULL,2,6,true,false,'0002','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (10,'0003','General Administration-Finance, Accounts, Audit',NULL,1,1,true,false,'00','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (11,'0004','General Administration-Election',NULL,1,1,true,false,'00','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL);
INSERT INTO function (id,code,"name","type",llevel,parentid,isactive,isnotleaf,parentcode,createddate,createdby,lastmodifieddate,lastmodifiedby,"version") VALUES
	 (12,'0005','General Administration-Record Room',NULL,1,1,true,false,'00','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (13,'0006','General Administration-Estate',NULL,1,1,true,false,'00','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (14,'0007','General Administration-Stores & Purchase',NULL,1,1,true,false,'00','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (15,'0008','General Administration-Workshop',NULL,1,1,true,false,'00','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (16,'0009','General Administration-Census',NULL,1,1,true,false,'00','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (18,'1011','Planning & Regulation-City & Town Planning',NULL,1,17,true,false,'10','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (19,'1012','Planning & Regulation-Building Regulation',NULL,1,17,true,false,'10','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (20,'1013','Planning & Regulation-Economic Planning',NULL,1,17,true,false,'10','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (21,'1014','Planning & Regulation-Encroachment Removal',NULL,1,17,true,false,'10','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (22,'1015','Planning & Regulation-Trade License / Regulation',NULL,1,17,true,false,'10','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL);
INSERT INTO function (id,code,"name","type",llevel,parentid,isactive,isnotleaf,parentcode,createddate,createdby,lastmodifieddate,lastmodifiedby,"version") VALUES
	 (24,'2021','Public Works-Roads and Pavement',NULL,1,23,true,false,'20','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (25,'202101','Engineering Branch',NULL,2,24,true,false,'2021','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (26,'2022','Public Works-Bridges and Flyovers',NULL,1,23,true,false,'20','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (27,'2023','Public Works-Subways & Causeways',NULL,1,23,true,false,'20','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (28,'2024','Public Works-Street Lighting',NULL,1,23,true,false,'20','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (29,'2025','Public Works-Storm water Drains',NULL,1,23,true,false,'20','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (30,'2026','Public Works-Traffic Signals',NULL,1,23,true,false,'20','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (31,'2027','Public Works-Guest Houses',NULL,1,23,true,false,'20','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (33,'3031','Health-Public Health',NULL,1,32,true,false,'30','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (34,'3032','Health-Epidemic Prevention/ Control',NULL,1,32,true,false,'30','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL);
INSERT INTO function (id,code,"name","type",llevel,parentid,isactive,isnotleaf,parentcode,createddate,createdby,lastmodifieddate,lastmodifiedby,"version") VALUES
	 (35,'3033','Health-Family Planning',NULL,1,32,true,false,'30','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (36,'3034','Health-Primary Health Care',NULL,1,32,true,false,'30','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (37,'3035','Health-Hospital Services',NULL,1,32,true,false,'30','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (38,'3036','Health-Burial and Cremations',NULL,1,32,true,false,'30','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (39,'3037','Health-Vital Statistics',NULL,1,32,true,false,'30','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (40,'3038','Health-Prevention of Food Adulteration',NULL,1,32,true,false,'30','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (41,'3039','Health-Ambulance / Hearse Services',NULL,1,32,true,false,'30','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (43,'4041','Sanitation & Solid Waste Management-Solid Waste Management',NULL,1,42,true,false,'40','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (44,'4042','Sanitation & Solid Waste Management-Public Convenience',NULL,1,42,true,false,'40','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (45,'4043','Sanitation & Solid Waste Management-Veterinary Services',NULL,1,42,true,false,'40','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL);
INSERT INTO function (id,code,"name","type",llevel,parentid,isactive,isnotleaf,parentcode,createddate,createdby,lastmodifieddate,lastmodifiedby,"version") VALUES
	 (46,'4044','Sanitation & Solid Waste Management-Cattle Pounding',NULL,1,42,true,false,'40','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (47,'4045','Sanitation & Solid Waste Management-Slaughter Houses',NULL,1,42,true,false,'40','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (49,'5051','Civic Amenities-Water Supply',NULL,1,48,true,false,'50','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (50,'5052','Civic Amenities-Sewerage',NULL,1,48,true,false,'50','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (51,'5053','Civic Amenities-Fire Services',NULL,1,48,true,false,'50','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (52,'5054','Civic Amenities-Arts & Culture',NULL,1,48,true,false,'50','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (53,'5055','Civic Amenities-Community / Marriage Centers',NULL,1,48,true,false,'50','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (54,'5056','Civic Amenities-Amusement',NULL,1,48,true,false,'50','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (55,'5057','Civic Amenities-Museums',NULL,1,48,true,false,'50','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (56,'5058','Civic Amenities-Municipal Markets',NULL,1,48,true,false,'50','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL);
INSERT INTO function (id,code,"name","type",llevel,parentid,isactive,isnotleaf,parentcode,createddate,createdby,lastmodifieddate,lastmodifiedby,"version") VALUES
	 (58,'6061','Urban Forestry-Parks, Gardens',NULL,1,57,true,false,'60','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (59,'6062','Urban Forestry-Play Grounds',NULL,1,57,true,false,'60','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (60,'6063','Urban Forestry-Lakes and Ponds',NULL,1,57,true,false,'60','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (61,'6064','Urban Forestry-Urban Forestry',NULL,1,57,true,false,'60','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (62,'6065','Urban Forestry-Environment Conservation',NULL,1,57,true,false,'60','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (63,'6066','Urban Forestry-Zoos',NULL,1,57,true,false,'60','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (65,'7071','Urban Poverty Alleviation & Social Welfare-Welfare of Women',NULL,1,64,true,false,'70','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (66,'7072','Urban Poverty Alleviation & Social Welfare-Welfare of Children',NULL,1,64,true,false,'70','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (67,'7073','Urban Poverty Alleviation & Social Welfare-Welfare of Aged',NULL,1,64,true,false,'70','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (68,'7074','Urban Poverty Alleviation & Social Welfare-Welfare of Handicapped',NULL,1,64,true,false,'70','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL);
INSERT INTO function (id,code,"name","type",llevel,parentid,isactive,isnotleaf,parentcode,createddate,createdby,lastmodifieddate,lastmodifiedby,"version") VALUES
	 (69,'7075','Urban Poverty Alleviation & Social Welfare-Welfare of SC/ST/OBC',NULL,1,64,true,false,'70','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (70,'7076','Urban Poverty Alleviation & Social Welfare-Slum Improvements',NULL,1,64,true,false,'70','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (71,'7077','Urban Poverty Alleviation & Social Welfare-Housing',NULL,1,64,true,false,'70','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (72,'7078','Urban Poverty Alleviation & Social Welfare-Urban Poverty Alleviation',NULL,1,64,true,false,'70','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (73,'7079','Urban Poverty Alleviation & Social Welfare-Others',NULL,1,64,true,false,'70','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (75,'8081','Other Services-Electricity',NULL,1,74,true,false,'80','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (76,'8082','Other Services-Education',NULL,1,74,true,false,'80','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (77,'8083','Other Services-Transportation',NULL,1,74,true,false,'80','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (78,'8084','Other Services-Facility for pilgrims',NULL,1,74,true,false,'80','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (80,'9091','Revenue-Property Taxes',NULL,1,79,true,false,'90','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL);
INSERT INTO function (id,code,"name","type",llevel,parentid,isactive,isnotleaf,parentcode,createddate,createdby,lastmodifieddate,lastmodifiedby,"version") VALUES
	 (81,'909101','Tax Branch',NULL,2,80,true,false,'9091','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (82,'9092','Revenue-Octroi / Entry Cess',NULL,1,79,true,false,'90','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (83,'9093','Revenue-Advertisement Tax',NULL,1,79,true,false,'90','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (84,'9094','Revenue-Professional Tax',NULL,1,79,true,false,'90','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (85,'9095','Revenue-Tax on Animals',NULL,1,79,true,false,'90','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (86,'9096','Revenue-Tax on Vehicles',NULL,1,79,true,false,'90','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (87,'9097','Revenue-Toll',NULL,1,79,true,false,'90','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL),
	 (88,'9098','Revenue-Other Taxes',NULL,1,79,true,false,'90','2024-10-15 15:13:15.205382',NULL,'2024-10-15 15:13:15.205382',NULL,NULL);


UPDATE function SET isnotleaf=true WHERE code='00';
UPDATE function SET isnotleaf=true WHERE code='0001';
UPDATE function SET isnotleaf=true WHERE code='0002';
UPDATE function SET isnotleaf=true WHERE code='10';
UPDATE function SET isnotleaf=true WHERE code='20';
UPDATE function SET isnotleaf=true WHERE code='2021';
UPDATE function SET isnotleaf=true WHERE code='30';
UPDATE function SET isnotleaf=true WHERE code='40';
UPDATE function SET isnotleaf=true WHERE code='50';
UPDATE function SET isnotleaf=true WHERE code='60';
UPDATE function SET isnotleaf=true WHERE code='70';
UPDATE function SET isnotleaf=true WHERE code='80';
UPDATE function SET isnotleaf=true WHERE code='90';
UPDATE function SET isnotleaf=true WHERE code='9091';




drop sequence seq_function;

CREATE SEQUENCE seq_function
    START WITH 89
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
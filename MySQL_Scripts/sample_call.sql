/*
About: a file to test the functionality of the procedures and see if the data has changed inside the target table.

Direction: Run this file last.
*/
use grocery_drone_delivery;
select * from `ADMIN`;
select * from `CHAIN`;

-- 2.a
CALL register_customer('testcustomers2', 'password42', 'test', 'customer', '420 Austerlitz Rd', 'Nashville', 'TN', '30319', '2254 7887 8863 3801', '863', '2021-04-01');
-- testcustomers	password42	test	customer	420 Austerlitz Rd	Nashville	TN	30319	2254 7887 8863 3800	863	2022-04-01
-- testcustomers	password42	test	customer	420 Austerlitz Rd	Nashville	TN	30319	2254 7887 8863 3800	863	2022-04-01
-- testcustomers	password42	test	customer	420 Austerlitz Rd	Nashville	TN	30319	2254 7887 8863 3800	863	2022-04-01
-- testcustomers	password42	test	customer	420 Austerlitz Rd	Nashville	TN	30319	2254 7887 8863 3800	863	2022-04-01
-- testcustomers	password104	test	customer	420 Austerlitz Rd	Nashville	TN	30319	2254 7887 8863 3800	863	2022-04-01
-- testcustomers	password42	test	customer	420 Austerlitz Rd	Nashville	TN	30319	2254 7887 8863 3800	863	2022-04-01
-- testcustomers2	password42	test	customer	420 Austerlitz Rd	Nashville	TN	30319	2254 7887 8863 3800	863	2022-04-01
-- testcustomers3	password42	test	customer	420 Austerlitz Rd	Nashville	TN	30319	2254 7887 8863 3811	863	2020-04-01
-- testcustomers	password42	test	customer	420 Austerlitz Rd	Nashville	TN	30319	2254 7887 8863 3800	863	2022-04-01
-- DrVeryStrange134	password129	Strange	Stephen	221 Baker St	New York	NY	30439	2364 7107 8773 3812	863	2022-05-07
-- DrVeryStrange1345	password129	Strange	Stephen	221 Baker St	New York	NY	30439	2364 7107 8773 3813	863	2022-05-07
-- DrVeryStrange1345	password129	Strange	Stephen	221 Baker St	New York	NY		2364 7107 8773 3813	863	2022-05-07
CALL register_customer("testcustomers","password42","test","customer","420 Austerlitz Rd","Nashville","TN","30319","2254 7887 8863 3800","863","2022-04-01");
CALL register_customer("testcustomers","password42","test","customer","420 Austerlitz Rd","Nashville","TN","30319","2254 7887 8863 3800","863","2022-04-01");
CALL register_customer("testcustomers","password42","test","customer","420 Austerlitz Rd","Nashville","TN","30319","2254 7887 8863 3800","863","2022-04-01");
CALL register_customer("testcustomers","password42","test","customer","420 Austerlitz Rd","Nashville","TN","30319","2254 7887 8863 3800","863","2022-04-01");
CALL register_customer("testcustomers","password104","test","customer","420 Austerlitz Rd","Nashville","TN","30319","2254 7887 8863 3800","863","2022-04-01");
CALL register_customer("testcustomers","password42","test","customer","420 Austerlitz Rd","Nashville","TN","30319","2254 7887 8863 3800","863","2022-04-01");
CALL register_customer("testcustomers2","password42","test","customer","420 Austerlitz Rd","Nashville","TN","30319","2254 7887 8863 3800","863","2022-04-01");
CALL register_customer("testcustomers3","password42","test","customer","420 Austerlitz Rd","Nashville","TN","30319","2254 7887 8863 3811","863","2020-04-01");
CALL register_customer("testcustomers","password42","test","customer","420 Austerlitz Rd","Nashville","TN","30319","2254 7887 8863 3800","863","2022-04-01");
CALL register_customer("DrVeryStrange134","password129","Strange","Stephen","221 Baker St","New York","NY","30439","2364 7107 8773 3812","863","2022-05-07");
CALL register_customer("DrVeryStrange1345","password129","Strange","Stephen","221 Baker St","New York","NY","30439","2364 7107 8773 3813","863","2022-05-07");
CALL register_customer("DrVeryStrange1345","password129","Strange","Stephen","221 Baker St","New York","NY","","2364 7107 8773 3813","863","2022-05-07");

select count(*) from users where username = "testcustomers" and pass = md5("password42");
-- 2.b
CALL register_employee('kszalkowski3', 'password21', 'Kristen', 'Szalkowski', '13 Bobby Dodd Dr', 'Kennesaw', 'GA', '30144'); 
-- sspiegal57	password5	Spike	Spiegal	50 Walker Rd	Atlanta	GA	30145
-- sspiegal57	password5	Spike	Spiegal	50 Walker Rd	Atlanta	GA	30145
-- sspiegal57	password5	Spike	Spiegal	50 Walker Rd	Atlanta	GA	30145
-- sspiegal57	password5	Spike	Spiegal	50 Walker Rd	Atlanta	GA	30145
-- DrVeryStrange134	password5	Strange	Stephen	50 Walker Rd	Atlanta	GA	30145
-- RadicalEdward	password5	Franscoise	Edward	50 Walker Rd	Atlanta	Ge	30145
-- RadicalEdward2	password5	Franscoise	Edward	50 Walker Rd	Atlanta	GA	
-- RadicalEdward2	password5	Franscoise	Edward	50 Walker Rd	Atlanta	GA	00000
CALL register_employee("sspiegal57","password5","Spike","Spiegal","50 Walker Rd","Atlanta","GA","30145");
CALL register_employee("sspiegal57","password5","Spike","Spiegal","50 Walker Rd","Atlanta","GA","30145");
CALL register_employee("sspiegal57","password5","Spike","Spiegal","50 Walker Rd","Atlanta","GA","30145");
CALL register_employee("sspiegal57","password5","Spike","Spiegal","50 Walker Rd","Atlanta","GA","30145");
CALL register_employee("DrVeryStrange134","password5","Strange","Stephen","50 Walker Rd","Atlanta","GA","30145");
CALL register_employee("RadicalEdward","password5","Franscoise","Edward","50 Walker Rd","Atlanta","Ge","30145");
CALL register_employee("RadicalEdward2","password5","Franscoise","Edward","50 Walker Rd","Atlanta","GA","");
CALL register_employee("RadicalEdward2","password5","Franscoise","Edward","50 Walker Rd","Atlanta","GA","0");

select * from drone_tech;
select * from users;
select * from customer;
select * from manager;
-- insert into manager values("RadicalEdward", "Wegmans");
select * from store;

select count(*) from manager where username ="RadicalEdward" or ChainName = "Wegmans";
select count(*) from `CHAIN` where ChainName = "Wegmans";

-- 4.a.
CALL admin_create_grocery_chain('Wegmans');
CALL admin_create_grocery_chain('TestChain');
CALL admin_create_grocery_chain('TestChain');
CALL admin_create_grocery_chain('Publix');

select * from manager;
-- 5.a.
CALL admin_create_new_store('Cumberland','MossMarket','2860 Cumberland Mall SE', 'Atlanta', 'GA', '30338');
-- Howell Mill	Wal Mart	Howell Mill Rd NW	Atlanta	GA	30318
-- Howell Mill	Wal Mart	Howell Mill Rd NW	Atlanta	GA	30318
-- Howell Mill	Kroger	Howell Mill Rd NW	Atlanta	GA	30318
-- Midtown	Kroger	Howell Mill Rd NW	Atlanta	GA	30332
-- Howell Mill	Food Lion	Howell Mill Rd NW	Atlanta	GA	30318
-- Howell Mill	Wal Mart	Howell Mill Rd NW	Atlanta	Ge	30318
-- Howell Mill	Food Lion	Howell Mill Rd NW	Atlanta	GA	30318
CALL admin_create_new_store("Howell Mill","Wal Mart","Howell Mill Rd NW","Atlanta","GA","30318");
CALL admin_create_new_store("Howell Mill","Wal Mart","Howell Mill Rd NW","Atlanta","GA","30318");
CALL admin_create_new_store("Howell Mill","Kroger","Howell Mill Rd NW","Atlanta","GA","30318");
CALL admin_create_new_store("Midtown","Kroger","Howell Mill Rd NW","Atlanta","GA","30332");
CALL admin_create_new_store("Howell Mill","Food Lion","Howell Mill Rd NW","Atlanta","GA","30318");
CALL admin_create_new_store("Howell Mill","Wal Mart","Howell Mill Rd NW","Atlanta","Ge","30318");
CALL admin_create_new_store("Howell Mill","Food Lion","Howell Mill Rd NW","Atlanta","GA","30318");

-- 6.a.
CALL admin_create_drone(121, 30363, 10, "hliu88");
-- 121	30363	10	hliu88
-- 122	30363	10	drthompson
-- 123	30318	10	hliu88
-- 124	30318	10	hliu88
-- 125	30363	0	hliu88
CALL admin_create_drone("121","30363","10","hliu88"); -- 21
CALL admin_create_drone("122","30363","10","drthompson"); -- 21
CALL admin_create_drone("123","30318","10","hliu88"); -- 22 WRONG RETURN 21
CALL admin_create_drone("124","30318","10","hliu88"); -- 23 WRONG RETURN 21
CALL admin_create_drone("125","30363","0","hliu88"); -- 24

select max(ID) from drone;
select * from store;
select * from drone_tech natural join store where Zipcode = '30022';
select * from drone;

-- 7.a.
CALL admin_create_item('Dog Shampoo', 'Personal Care', 'No', 'New Jersey');
-- Dog Shampoo	Personal Care	No	New Jersey
-- Bandaids	Personal Care	No	New Jersey
-- Hairbrush	Personal Care	IDK	New Jersey
CALL admin_create_item("Dog Shampoo","Personal Care","No","New Jersey");
CALL admin_create_item("hello","Personal Care","No","New Jersey");
CALL admin_create_item("Hairbrush","Personal Care","IDK","New Jersey");

select * from item;

-- 8.a.
CALL admin_view_customers('Aiysha', 'Allman');
CALL admin_view_customers(null, null);
CALL admin_view_customers('Leslie', null);
CALL admin_view_customers(null, 'Smith');
CALL admin_view_customers('Aiysha', 'Allman');
CALL admin_view_customers('Kristen', null);
CALL admin_view_customers('Chuck', 'Norris');
CALL admin_view_customers('Jake', 'Park');
CALL admin_view_customers(null, 'McDonald');

select * from admin_view_customers_result;

-- 9.a.
CALL manager_create_chain_item('MossMarket','NavelOrange', '500', '20', '10098', '0.88');
-- Moss Market	Navel Orange	500	20	10098	0.88
-- NULL	NULL	NULL	NULL	NULL	NULL
-- Moss Market	Navel Orange	500	20	10098	0.88
-- Costco	Navel Orange	500	20	10098	0.88
-- Moss Market	Navel Orange	500	20	10098	0.88
-- Moss Market	Garlic	500	20	10098	0.88
-- Moss Market	Navel Orange	500	20	10098	0.88
-- Moss Market	Navel Orange	0	20	10098	0.88
-- Moss Market	Navel Orange	500	20	10098	0.88
-- Moss Market	Navel Orange	500	0	10098	0.88
-- Moss Market	Navel Orange	500	20	10098	0.88
-- Moss Market	Navel Orange	500	20	10002	0.88
-- Moss Market	Navel Orange	500	20	10098	0.88
-- Moss Market	Navel Orange	500	20	10098	0.00
-- Moss Market	Navel Orange	500	20	10098	0.88
-- Moss Market	Fuji Apple	500	20	10098	0.88
-- Moss Market	Navel Orange	500	20	10098	0.88
CALL manager_create_chain_item("Moss Market","Navel Orange","500","20","10098","0.88");
CALL manager_create_chain_item(NULL, NULL, NULL, NULL, NULL, NULL);
CALL manager_create_chain_item("Moss Market","Navel Orange","500","20","10098","0.88");
CALL manager_create_chain_item("Costco","Navel Orange","500","20","10098","0.88");
CALL manager_create_chain_item("Moss Market","Navel Orange","500","20","10098","0.88");
CALL manager_create_chain_item("Moss Market","Garlic","500","20","10098","0.88");
CALL manager_create_chain_item("Moss Market","Navel Orange","500","20","10098","0.88");
CALL manager_create_chain_item("Moss Market","Navel Orange","0","20","10098","0.88");
CALL manager_create_chain_item("Moss Market","Navel Orange","500","20","10098","0.88");
CALL manager_create_chain_item("Moss Market","Navel Orange","500","0","10098","0.88");
CALL manager_create_chain_item("Moss Market","Navel Orange","500","20","10098","0.88");
CALL manager_create_chain_item("Moss Market","Navel Orange","500","20","10002","0.88");
CALL manager_create_chain_item("Moss Market","Navel Orange","500","20","10098","0.88");
CALL manager_create_chain_item("Moss Market","Navel Orange","500","20","10098","0");
CALL manager_create_chain_item("Moss Market","Navel Orange","500","20","10098","0.88");
CALL manager_create_chain_item("Moss Market","Fuji Apple","500","20","10098","0.88");
CALL manager_create_chain_item("Moss Market","Navel Orange","500","20","10098","0.88");

select distinct ChainItemName from chain_item;
select max(PLUNumber) from chain_item where ChainName = 'Publix';
select * from chain_item;
select * from item;

-- 10.a.
CALL manager_view_drone_technicians('Moss Market','fdavenport49','College Park');
-- Moss Market	NULL	NULL
-- Moss Market	NULL	College Park
-- Moss Market	fdavenport49	NULL
-- Moss Market	fdavenport49	College Park
-- NULL	fdavenport49	College Park
-- Moss Market	jdoe381	NULL
-- Moss Market	NULL	Atlanta Station
-- Costco	NULL	NULL
CALL manager_view_drone_technicians("Moss Market",NULL,NULL);
CALL manager_view_drone_technicians("Moss Market",NULL,"College Park");
CALL manager_view_drone_technicians("Moss Market","fdavenport49",NULL);
CALL manager_view_drone_technicians("Moss Market","fdavenport49","College Park");
CALL manager_view_drone_technicians(NULL,"fdavenport49","College Park");
CALL manager_view_drone_technicians("Moss Market","jdoe381",NULL);
CALL manager_view_drone_technicians("Moss Market",NULL,"Atlanta Station");
CALL manager_view_drone_technicians("Costco",NULL,NULL);

select * from chain_item;
-- 11.a.
CALL manager_view_drones('cbing101', NULL, NULL);
-- amartin365	101	3
-- cbing101	NULL	NULL
-- cbing101	NULL	7
-- jhalpert	101	3
-- cbing101	131	3
-- rgreen97	113	3
-- cbing101	105	5
CALL manager_view_drones("amartin365","101","3");
CALL manager_view_drones("cbing101",NULL,NULL);
CALL manager_view_drones("cbing101",NULL,"7");
CALL manager_view_drones("jhalpert","101","3");
CALL manager_view_drones("cbing101","131","3");
CALL manager_view_drones("rgreen97","113","3");
CALL manager_view_drones("cbing101","105","5");

-- 12.a.
CALL manager_manage_stores('rgreen97', NULL, 10, 120);
-- cbing101	NULL	NULL	NULL
-- dschrute18	NULL	NULL	NULL
-- dschrute18	North Point	10	100
-- rgreen97	NULL	10	120
-- rgreen97	Mall of GA	100	300
-- pbeesly61	NULL	300	400
CALL manager_manage_stores("cbing101",NULL,NULL,NULL);
CALL manager_manage_stores("dschrute18",NULL,NULL,NULL);
CALL manager_manage_stores("dschrute18","North Point","10","100");
CALL manager_manage_stores("rgreen97",NULL,"10","120");
CALL manager_manage_stores("rgreen97","Mall of GA","100","300");
CALL manager_manage_stores("pbeesly61",NULL,"300","400");

select * from manager_manage_stores_result;

-- 13.a.
CALL customer_change_credit_card_information('dsmith102','1247 0598 9213 1562', 173, '2021-05-02');
CALL customer_change_credit_card_information('dbrown85','4862 6875 8321 6983', 967, '2021-08-25');
CALL customer_change_credit_card_information('tlee984','3284 5432 4681 3218', 208, '2032-04-01');

select FirstName, LastName from users where Username = 'dkim99';

-- 14.a.
CALL customer_view_order_history('hpeterson55', 10001);
-- hpeterson55	10001
-- hpeterson5	10001
-- dbrown85	10002
CALL customer_view_order_history("hpeterson55","10001");
CALL customer_view_order_history("hpeterson5","10001"); -- returns nan (expected?!)
CALL customer_view_order_history("dbrown85","10002"); -- returns nan

select * from orders;
select * from customer_view_order_history_result;

select concat(FirstName, ' ', LastName) from users where Username = 'jdoe381';
-- 15.a.
CALL customer_view_store_items('dkim99', 'Moss Market', 'Bobby Dodd', 'ALL');
-- dkim99	Moss Market	Bobby Dodd	ALL
-- dsmith102	Query Mart	GT Center	Diary
-- mscott845	Moss Market	Bobby Dodd	Other
-- dkim99	Moss Market	Norcross	ALL
CALL customer_view_store_items("dkim99","Moss Market","Bobby Dodd","ALL");
CALL customer_view_store_items("dsmith102","Query Mart","GT Center","Diary");
CALL customer_view_store_items("mscott845","Moss Market","Bobby Dodd","Other");
CALL customer_view_store_items("dkim99","Moss Market","Norcross","ALL");

select * from customer_view_store_items_result;
select * from store;
select * from orders;
select * from chain_item;
select * from store where Zipcode = '30332';
select distinct ItemType from item;
select * from contains;

select * from manager;
select * from users;

-- 15.b. 
call customer_select_items('dkim99', 'Moss Market', 'Bobby Dodd', 'Fuji Apple', '0');
-- dkim99	Moss Market	Bobby Dodd	Fuji Apple	2
-- dkim99	Moss Market	Bobby Dodd	Gala Apple	3
-- dkim99	Moss Market	Bobby Dodd	Bagels	2
-- mscott845	Moss Market	Bobby Dodd	Bagels	2
-- dkim99	Moss Market	Norcross	Fuji Apple	2
CALL customer_select_items("dkim99","Moss Market","Bobby Dodd","Fuji Apple","2"); -- 16
CALL customer_select_items("dkim99","Moss Market","Bobby Dodd","Gala Apple","3"); -- 16
CALL customer_select_items("dkim99","Moss Market","Bobby Dodd","Bagels","2"); -- 16
CALL customer_select_items("mscott845","Moss Market","Bobby Dodd","Bagels","2"); -- 17
CALL customer_select_items("dkim99","Moss Market","Norcross","Fuji Apple","2"); -- 17

select * from orders;
select * from `contains`;
select * from store;
select * from drone_tech;
-- 30144 (Store: KSU Center, Moss Market)

-- 16.a.
CALL customer_review_order('dkim99');
CALL customer_review_order('lpiper20');
CALL customer_review_order('lpiper20');

select * from customer_review_order_result;
select * from users;
select * from drone_tech;

-- 16.b.
CALL customer_update_order('dkim99', 'Fuji Apple','1');
CALL customer_update_order('lpiper20', 'Earl Grey Tea','3');
CALL customer_update_order('lpiper20', 'Earl Grey Tea','0');

-- 17.a.
CALL drone_technician_view_order_history('jrosario34', '2021-01-01', '2021-12-31');
CALL drone_technician_view_order_history('ExtraDroneOperator', null, null);
-- jrosario34	2021-01-01	2021-12-31
-- sstrange11	2021-01-01	2021-12-31
-- sstrange11	2021-01-01	2021-01-02
CALL drone_technician_view_order_history("jrosario34","2021-01-01","2021-12-31");
CALL drone_technician_view_order_history("sstrange11","2021-01-01","2021-12-31");
CALL drone_technician_view_order_history("sstrange11","2021-01-01","2021-01-02");

select * from drone_technician_view_order_history_result;
select CONCAT(FirstName, ' ', LastName) from users where username = 'ExtraDroneOperator';

-- call manager_assign_drone_technicians('Whole Foods', 'ygao10', 'North Point', 'dschrute18');
select * from drone where DroneTech = 'ygao10';
select * from drone_tech where Username = 'ygao10';
select * from store where ChainName = 'Whole Foods';

select * from drone_tech 
		left outer join drone on drone_tech.username = drone.dronetech 
		join manager on manager.chainname = drone_tech.chainname
        where drone_tech.username = 'ygao10';
			-- and manager.username = i_chain_manager
-- 			and drone_tech.storename = i_new_store_name;

-- 17.b.
CALL dronetech_assign_order('pbuffay56', 112, 'In Transit', '10014');
-- pbuffay56	112	In Transit	10014
-- pbuffay56	112	In Transit	10014
-- jdoe381	102	In Transit	10014
CALL dronetech_assign_order("pbuffay56","112","In Transit","10014");
CALL dronetech_assign_order("pbuffay56","112","In Transit","10014");
CALL dronetech_assign_order("jdoe381","102","In Transit","10014");
CALL dronetech_assign_order("sstrange11","113","Pending","10015");

-- 18.a.
CALL dronetech_order_details('akarev16', '10002');
-- CALL dronetech_order_details('akarev16', '10002');
CALL dronetech_order_details('akarev16', '10002');
CALL dronetech_order_details('akarev16', '10002');
CALL dronetech_order_details('jdoe381', '10001');
CALl dronetech_order_details('kfrog03', '10003');
CALl dronetech_order_details('nshea230', '10004');

select * from dronetech_order_details_result;

-- 18.b.
CALL dronetech_order_items('akarev16', '10002');
CALL dronetech_order_items('akarev16', '10002');
CALL dronetech_order_items('jdoe381', '10001');
CALl dronetech_order_items('kfrog03', '10003');
CALl dronetech_order_items('nshea230', '10004');

select * from dronetech_order_items_result;

-- 19.a.
CALL dronetech_assigned_drones('dmcstuffins7', '104', 'All');
-- akarev16	NULL	ALL
-- akarev16	111	ALL
-- akarev16	NULL	Busy
-- akarev16	NULL	Available
CALL dronetech_assigned_drones("akarev16",NULL,"ALL");
CALL dronetech_assigned_drones("akarev16","111","ALL");
CALL dronetech_assigned_drones("akarev16",NULL,"Busy");
CALL dronetech_assigned_drones("akarev16",NULL,"Available");

CALL dronetech_assigned_drones("sstrange11",NULL,"Available");

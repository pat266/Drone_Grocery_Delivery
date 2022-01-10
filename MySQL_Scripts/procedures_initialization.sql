/*
CS4400: Introduction to Database Systems
Spring 2021
Team 06
Aidan Perras (aperras3)
Ana Belen Coronel (arodriguez328)
Minjik Kim (mkim616)
Phat Tran (ptran74)

Directions:
Run this file after the database_initalization.sql file.

About: Create different procedures in order to access/retreive/change the data
inside the tables. The procedures will have different conditions to ensure that
the change of data is approrpriate.
*/


-- ID: 2a
-- Author: asmith457
-- Name: register_customer
DROP PROCEDURE IF EXISTS register_customer;
DELIMITER //
CREATE PROCEDURE register_customer(
	   IN i_username VARCHAR(40),
       IN i_password VARCHAR(40),
	   IN i_fname VARCHAR(40),
       IN i_lname VARCHAR(40),
       IN i_street VARCHAR(40),
       IN i_city VARCHAR(40),
       IN i_state VARCHAR(2),
	   IN i_zipcode CHAR(5),
       IN i_ccnumber VARCHAR(40),
	   IN i_cvv CHAR(3),
       IN i_exp_date DATE
)
sp_main: BEGIN
-- Type solution below

	if i_username is null or  i_password is null or i_fname is null or i_lname is null
		or i_street is null or i_city is null or i_state is null or i_zipcode is null
        or i_ccnumber is null or i_cvv is null or i_exp_date is null
	then leave sp_main; end if;

	if i_username in (select Username from users)
	then leave sp_main; end if;

    if i_username in (select Username from Employee)
	then leave sp_main; end if;
    
	if length(i_zipcode) != 5
	then leave sp_main; end if;
    
    if (i_username in (select Username from Users) 
		and i_ccnumber in (select ccnumber from Customer where Username = i_username)) 
	then leave sp_main; end if;

    
	insert into users values 
    (i_username, md5(i_password), i_fname, i_lname, i_street, i_city, i_state, i_zipcode);
    
    insert into customer values 
    (i_username, i_ccnumber, i_cvv, i_exp_date);

-- End of solution
END //
DELIMITER ;


-- ID: 2b
-- Author: asmith457
-- Name: register_employee
DROP PROCEDURE IF EXISTS register_employee;
DELIMITER //
CREATE PROCEDURE register_employee(
	   IN i_username VARCHAR(40),
       IN i_password VARCHAR(40),
	   IN i_fname VARCHAR(40),
       IN i_lname VARCHAR(40),
       IN i_street VARCHAR(40),
       IN i_city VARCHAR(40),
       IN i_state VARCHAR(2),
       IN i_zipcode CHAR(5)
)
sp_main: BEGIN
-- Type solution below
	if i_username is null or  i_password is null or i_fname is null or i_lname is null
		or i_street is null or i_city is null or i_state is null or i_zipcode is null
	then leave sp_main; end if;

-- This was added after the autograder was fixed and still results in a 100% - Aidan
    if (i_username in (select Username from Users)
		or i_username in (select username from employee)) 
	then leave sp_main; end if;
    
	if length(i_zipcode) != 5
	then leave sp_main; end if;
	
	insert into users values 
    (i_username, md5(i_password), i_fname, i_lname, i_street, i_city, i_state, i_zipcode);

    insert into employee values (i_username);

-- End of solution
END //
DELIMITER ;


-- ID: 4a
-- Author: asmith457
-- Name: admin_create_grocery_chain
DROP PROCEDURE IF EXISTS admin_create_grocery_chain;
DELIMITER //
CREATE PROCEDURE admin_create_grocery_chain(
        IN i_grocery_chain_name VARCHAR(40)
)
sp_main: BEGIN
-- Type solution below
	if (i_grocery_chain_name in (select chainname from `chain`) or i_grocery_chain_name is null) 
    then leave sp_main; end if;

	insert into CHAIN values
    (i_grocery_chain_name);
-- End of solution
END //
DELIMITER ;


-- ID: 5a
-- Author: ahatcher8
-- Name: admin_create_new_store
DROP PROCEDURE IF EXISTS admin_create_new_store;
DELIMITER //
CREATE PROCEDURE admin_create_new_store(
    	IN i_store_name VARCHAR(40),
        IN i_chain_name VARCHAR(40),
    	IN i_street VARCHAR(40),
    	IN i_city VARCHAR(40),
    	IN i_state VARCHAR(2),
    	IN i_zipcode CHAR(5)
)
sp_main: BEGIN
-- Type solution below
	if  i_store_name is null or i_chain_name is null or i_street is null or i_city is null
		or i_state is null or i_zipcode is null
    then leave sp_main; end if;	

	if ((i_store_name, i_chain_name) in (select storename, chainname from store)) 
    then leave sp_main; end if;

	if (i_chain_name not in (select chainname from chain)) 
    then leave sp_main; end if;

	if (i_zipcode in (select zipcode from store
	where chainname = i_chain_name)) then leave sp_main; end if;

	insert into store values
    (i_store_name, i_chain_name, i_street, i_city, i_state, i_zipcode);
-- End of solution
END //
DELIMITER ;


-- ID: 6a
-- Author: ahatcher8
-- Name: admin_create_drone
DROP PROCEDURE IF EXISTS admin_create_drone;
DELIMITER //
CREATE PROCEDURE admin_create_drone(
	   IN i_drone_id INT,
       IN i_zip CHAR(5),
       IN i_radius INT,
       IN i_drone_tech VARCHAR(40)
)
sp_main: BEGIN
-- Type solution below 
	if i_drone_id is null or i_zip is null or i_radius is null or i_drone_tech is null
    then leave sp_main; end if;

	if (i_drone_id in (select ID from drone)) 
    then leave sp_main; end if;
    
    if (i_zip not in (select zipcode from store)) 
    then leave sp_main; end if;
    
	if (i_zip not in (select zipcode from users where username = i_drone_tech)) 
    then leave sp_main; end if;
    
    
    if (i_drone_tech not in (select Username from drone_tech)) 
    then leave sp_main; end if;

	insert into drone values
    (i_drone_id, 'Available', i_zip, ifnull(i_radius, 0),i_drone_tech);
-- End of solution
END //
DELIMITER ;

-- ID: 7a
-- Author: ahatcher8
-- Name: admin_create_item
DROP PROCEDURE IF EXISTS admin_create_item;
DELIMITER //
CREATE PROCEDURE admin_create_item(
        IN i_item_name VARCHAR(40),
        IN i_item_type VARCHAR(40),
        IN i_organic VARCHAR(3),
        IN i_origin VARCHAR(40)
)
sp_main: BEGIN
-- Type solution below
    if i_item_name is null or i_item_type is null or i_organic is null or i_origin is null
    then leave sp_main; end if;		
    
    if (i_organic != 'Yes' and i_organic != 'No') 
    then leave sp_main; end if;	
    
	if (i_item_name is null or i_item_type is null or i_organic is null or i_origin is null) 
    then leave sp_main; end if;	

	if (i_item_name in (select itemname from item)) 
    then leave sp_main; end if;	

	insert into item values
    (i_item_name, i_item_type, i_origin, i_organic);
-- End of solution
END //
DELIMITER ;

-- ID: 8a
-- Author: dvaidyanathan6
-- Name: admin_view_customers
DROP PROCEDURE IF EXISTS admin_view_customers;
DELIMITER //
CREATE PROCEDURE admin_view_customers(
	   IN i_first_name VARCHAR(40),
       IN i_last_name VARCHAR(40)
)
BEGIN
-- Type solution below
	drop table if exists admin_view_customers_result;

	if (i_first_name is null and i_last_name is null) then
		create table admin_view_customers_result
		select username, CONCAT(FirstName, ' ', LastName) as "Name", CONCAT(Street, ", ", City, ", ", State, " ", Zipcode) as "Address"
		from `customer` natural join users;
	elseif (i_first_name is null) then
		create table admin_view_customers_result
		select username, CONCAT(FirstName, ' ', LastName) as "Name", CONCAT(Street, ", ", City, ", ", State, " ", Zipcode) as "Address"
		from `customer` natural join users
		where LastName = i_last_name;
	elseif (i_last_name is null) then
		create table admin_view_customers_result
		select username, CONCAT(FirstName, ' ', LastName) as "Name", CONCAT(Street, ", ", City, ", ", State, " ", Zipcode) as "Address"
		from `customer` natural join users
		where FirstName = i_first_name;
	else
		create table admin_view_customers_result
		select username, CONCAT(FirstName, ' ', LastName) as "Name", CONCAT(Street, ", ", City, ", ", State, " ", Zipcode) as "Address"
		from `customer` natural join users
		where FirstName = i_first_name and LastName = i_last_name;
	end if;
	-- End of solution
END //
DELIMITER ;



-- ID: 9a
-- Author: dvaidyanathan6
-- Name: manager_create_chain_item
DROP PROCEDURE IF EXISTS manager_create_chain_item;
DELIMITER //
CREATE PROCEDURE manager_create_chain_item(
        IN i_chain_name VARCHAR(40),
    	IN i_item_name VARCHAR(40),
    	IN i_quantity INT, 
    	IN i_order_limit INT,
    	IN i_PLU_number INT,
    	IN i_price DECIMAL(4, 2)
)
sp_main: BEGIN
-- Type solution below
	if (i_item_name is null or i_chain_name is null or i_quantity is null or i_order_limit is null
		or i_PLU_number is null or i_price is null) 
    then leave sp_main; end if;
    
	if (i_item_name not in (select itemname from item)) 
    then leave sp_main; end if;
    
    -- foreign key integrity
    if (i_chain_name not in (select chainname from `chain`)) 
    then leave sp_main; end if;
	
    -- itemname, chainname, and plu number make up the primary key
    if ((i_item_name, i_chain_name, i_PLU_number) in 
    (select ChainItemName, ChainName, PLUNumber from chain_item)) 
    then leave sp_main; end if;
    
    -- chain_name + Plu number must be unique
    if ((i_chain_name, i_PLU_number) in 
    (select ChainName, PLUNumber from chain_item)) 
    then leave sp_main; end if;

	insert into chain_item values
	(i_item_name, i_chain_name, i_PLU_number, i_order_limit, i_quantity,  round(i_price,2));
-- End of solution
END //
DELIMITER ;


-- ID: 10a
-- Author: dvaidyanathan6
-- Name: manager_view_drone_technicians
DROP PROCEDURE IF EXISTS manager_view_drone_technicians;
DELIMITER //
CREATE PROCEDURE manager_view_drone_technicians(
	   IN i_chain_name VARCHAR(40),
       IN i_drone_tech VARCHAR(40),
       IN i_store_name VARCHAR(40)
)
BEGIN
-- Type solution below
	drop table if exists manager_view_drone_technicians_result;
    
if (i_drone_tech is null and i_store_name is null) then
	create table manager_view_drone_technicians_result
	select Username, CONCAT(FirstName, ' ', LastName) as "Name", StoreName as "Location"
	from DRONE_TECH natural join users
	where ChainName = i_chain_name;
elseif (i_drone_tech is null) then
	create table manager_view_drone_technicians_result
	select Username, CONCAT(FirstName, ' ', LastName) as "Name", StoreName as "Location"
	from DRONE_TECH natural join users
	where StoreName = i_store_name and ChainName = i_chain_name;
elseif (i_store_name is null) then
	create table manager_view_drone_technicians_result
	select Username, CONCAT(FirstName, ' ', LastName) as "Name", StoreName as "Location"
	from DRONE_TECH natural join users
	where Username = i_drone_tech and ChainName = i_chain_name;
else
	create table manager_view_drone_technicians_result
	select Username, CONCAT(FirstName, ' ', LastName) as "Name", StoreName as "Location"
	from DRONE_TECH natural join users
	where Username = i_drone_tech and StoreName = i_store_name and ChainName = i_chain_name;
end if;
-- End of solution
END //
DELIMITER ;


-- ID: 11a
-- Author: vtata6
-- Name: manager_view_drones
DROP PROCEDURE IF EXISTS manager_view_drones;
DELIMITER //
CREATE PROCEDURE manager_view_drones(
	   IN i_mgr_username varchar(40), 
	   IN i_drone_id int, 
       drone_radius int
)
BEGIN
-- Type solution below
	drop table if exists manager_view_drones_result;

	if (i_drone_id is null and drone_radius is null) then
		create table manager_view_drones_result
		select Drone.ID, Drone.DroneTech, Drone.radius, Drone.Zip, Drone.DroneStatus
		from drone
			inner join drone_tech
				on drone.DroneTech = drone_tech.Username
			inner join manager
				on drone_tech.ChainName = manager.ChainName
		where manager.Username = i_mgr_username;
	-- only DroneID is null
	elseif (i_drone_id is null) then
		create table manager_view_drones_result
		select Drone.ID, Drone.DroneTech, Drone.radius, Drone.Zip, Drone.DroneStatus
		from drone
			inner join drone_tech
				on drone.DroneTech = drone_tech.Username
			inner join manager
				on drone_tech.ChainName = manager.ChainName
		where Drone.radius >= drone_radius and manager.Username = i_mgr_username;
	-- only radius is null
	elseif (drone_radius is null) then
		create table manager_view_drones_result
		select Drone.ID, Drone.DroneTech, Drone.radius, Drone.Zip, Drone.DroneStatus
		from drone
			inner join drone_tech
				on drone.DroneTech = drone_tech.Username
			inner join manager
				on drone_tech.ChainName = manager.ChainName
		where Drone.ID = i_drone_id and manager.Username = i_mgr_username;
	-- Both DroneID and radius is not null
	else
		create table manager_view_drones_result
		select Drone.ID, Drone.DroneTech, Drone.radius, Drone.Zip, Drone.DroneStatus
		from drone
			inner join drone_tech
				on drone.DroneTech = drone_tech.Username
			inner join manager
				on drone_tech.ChainName = manager.ChainName
		where Drone.ID = i_drone_id and Drone.radius >= drone_radius and manager.Username = i_mgr_username;
	end if;

	-- End of solution
END //
DELIMITER ;

-- ID: 12a
-- Author: vtata6
-- Name: manager_manage_stores
DROP PROCEDURE IF EXISTS manager_manage_stores;
DELIMITER //
CREATE PROCEDURE manager_manage_stores(
	   IN i_mgr_username varchar(50), 
	   IN i_storeName varchar(50), 
	   IN i_minTotal int, 
	   IN i_maxTotal int
)
sp_main: BEGIN
-- Type solution below
	drop table if exists manager_manage_stores_result;
	-- [0, 0, 0]
	if (i_storeName is null and i_minTotal is null and i_maxTotal is null) then
		create table manager_manage_stores_result
		select store.StoreName, CONCAT(Street, " ", City, ", ", State, " ", Zipcode) as "Address",
			count(distinct(orders.ID)) as "# Orders", COUNT(distinct drone_tech.Username) + 1 as "Employees",
			SUM(`contains`.Quantity * chain_item.Price) as "Total"
		from manager
			right outer join drone_tech on drone_tech.ChainName = manager.ChainName
			join store on drone_tech.StoreName = store.StoreName
				and drone_tech.ChainName = store.ChainName
			left outer join drone on drone.DroneTech = drone_tech.Username
			left outer join orders on Drone.ID = Orders.DroneID
			left outer join `contains` on Orders.ID = `Contains`.OrderID
			left outer join chain_item on `contains`.PLUNumber = chain_item.PLUNumber
				and `contains`.ItemName = chain_item.ChainItemName
				and `contains`.ChainName = chain_item.ChainName
		where
			manager.Username = i_mgr_username
		group by Store.StoreName, Store.ChainName
			having SUM(`contains`.Quantity * chain_item.Price) is not null;

	-- [0, 0, 1]
	elseif (i_storeName is null and i_minTotal is null and i_maxTotal is not null) then
		create table manager_manage_stores_result
		select store.StoreName, CONCAT(Street, " ", City, ", ", State, " ", Zipcode) as "Address",
			count(distinct(orders.ID)) as "# Orders", COUNT(distinct drone_tech.Username) + 1 as "Employees",
			SUM(`contains`.Quantity * chain_item.Price) as "Total"
		from manager
			right outer join drone_tech on drone_tech.ChainName = manager.ChainName
			join store on drone_tech.StoreName = store.StoreName
				and drone_tech.ChainName = store.ChainName
			left outer join drone on drone.DroneTech = drone_tech.Username
			left outer join orders on Drone.ID = Orders.DroneID
			left outer join `contains` on Orders.ID = `Contains`.OrderID
			left outer join chain_item on `contains`.PLUNumber = chain_item.PLUNumber
				and `contains`.ItemName = chain_item.ChainItemName
				and `contains`.ChainName = chain_item.ChainName
		where
			manager.Username = i_mgr_username
		group by Store.StoreName, Store.ChainName
			having SUM(`contains`.Quantity * chain_item.Price) <= i_maxTotal
            and SUM(`contains`.Quantity * chain_item.Price) is not null;


	-- [0, 1, 0]
	elseif (i_storeName is null and i_minTotal is not null and i_maxTotal is null) then
		create table manager_manage_stores_result
		select store.StoreName, CONCAT(Street, " ", City, ", ", State, " ", Zipcode) as "Address",
			count(distinct(orders.ID)) as "# Orders", COUNT(distinct drone_tech.Username) + 1 as "Employees",
			SUM(`contains`.Quantity * chain_item.Price) as "Total"
		from manager
			right outer join drone_tech on drone_tech.ChainName = manager.ChainName
			join store on drone_tech.StoreName = store.StoreName
				and drone_tech.ChainName = store.ChainName
			left outer join drone on drone.DroneTech = drone_tech.Username
			left outer join orders on Drone.ID = Orders.DroneID
			left outer join `contains` on Orders.ID = `Contains`.OrderID
			left outer join chain_item on `contains`.PLUNumber = chain_item.PLUNumber
				and `contains`.ItemName = chain_item.ChainItemName
				and `contains`.ChainName = chain_item.ChainName
		where
			manager.Username = i_mgr_username
		group by Store.StoreName, Store.ChainName
			having SUM(`contains`.Quantity * chain_item.Price) >= i_minTotal
            and SUM(`contains`.Quantity * chain_item.Price) is not null;

	-- [0, 1, 1]
	elseif (i_storeName is null and i_minTotal is not null and i_maxTotal is not null) then
		create table manager_manage_stores_result
		select store.StoreName, CONCAT(Street, " ", City, ", ", State, " ", Zipcode) as "Address",
			count(distinct(orders.ID)) as "# Orders", COUNT(distinct drone_tech.Username) + 1 as "Employees",
			SUM(`contains`.Quantity * chain_item.Price) as "Total"
		from manager
			right outer join drone_tech on drone_tech.ChainName = manager.ChainName
			join store on drone_tech.StoreName = store.StoreName
				and drone_tech.ChainName = store.ChainName
			left outer join drone on drone.DroneTech = drone_tech.Username
			left outer join orders on Drone.ID = Orders.DroneID
			left outer join `contains` on Orders.ID = `Contains`.OrderID
			left outer join chain_item on `contains`.PLUNumber = chain_item.PLUNumber
				and `contains`.ItemName = chain_item.ChainItemName
				and `contains`.ChainName = chain_item.ChainName
		where
			manager.Username = i_mgr_username
		group by Store.StoreName, Store.ChainName
			having SUM(`contains`.Quantity * chain_item.Price) <= i_maxTotal
			and SUM(`contains`.Quantity * chain_item.Price) >= i_minTotal
            and SUM(`contains`.Quantity * chain_item.Price) is not null;

	-- [1, 0, 0]
	elseif (i_storeName is not null and i_minTotal is null and i_maxTotal is null) then
		create table manager_manage_stores_result
		select store.StoreName, CONCAT(Street, " ", City, ", ", State, " ", Zipcode) as "Address",
			count(distinct(orders.ID)) as "# Orders", COUNT(distinct drone_tech.Username) + 1 as "Employees",
			SUM(`contains`.Quantity * chain_item.Price) as "Total"
		from manager
			right outer join drone_tech on drone_tech.ChainName = manager.ChainName
			join store on drone_tech.StoreName = store.StoreName
				and drone_tech.ChainName = store.ChainName
			left outer join drone on drone.DroneTech = drone_tech.Username
			left outer join orders on Drone.ID = Orders.DroneID
			left outer join `contains` on Orders.ID = `Contains`.OrderID
			left outer join chain_item on `contains`.PLUNumber = chain_item.PLUNumber
				and `contains`.ItemName = chain_item.ChainItemName
				and `contains`.ChainName = chain_item.ChainName
		where
			manager.Username = i_mgr_username
			and Store.StoreName = i_storeName
		group by Store.StoreName, Store.ChainName
			having SUM(`contains`.Quantity * chain_item.Price) is not null;
        
	-- [1, 0, 1]
	elseif (i_storeName is not null and i_minTotal is null and i_maxTotal is not null) then
		create table manager_manage_stores_result
		select store.StoreName, CONCAT(Street, " ", City, ", ", State, " ", Zipcode) as "Address",
			count(distinct(orders.ID)) as "# Orders", COUNT(distinct drone_tech.Username) + 1 as "Employees",
			SUM(`contains`.Quantity * chain_item.Price) as "Total"
		from manager
			right outer join drone_tech on drone_tech.ChainName = manager.ChainName
			join store on drone_tech.StoreName = store.StoreName
				and drone_tech.ChainName = store.ChainName
			left outer join drone on drone.DroneTech = drone_tech.Username
			left outer join orders on Drone.ID = Orders.DroneID
			left outer join `contains` on Orders.ID = `Contains`.OrderID
			left outer join chain_item on `contains`.PLUNumber = chain_item.PLUNumber
				and `contains`.ItemName = chain_item.ChainItemName
				and `contains`.ChainName = chain_item.ChainName
		where
			manager.Username = i_mgr_username
			and Store.StoreName = i_storeName
		group by Store.StoreName, Store.ChainName
			having SUM(`contains`.Quantity * chain_item.Price) <= i_maxTotal
            and SUM(`contains`.Quantity * chain_item.Price) is not null;
		
	-- [1, 1, 0]
	elseif (i_storeName is not null and i_minTotal is not null and i_maxTotal is null) then
		create table manager_manage_stores_result
				select store.StoreName, CONCAT(Street, " ", City, ", ", State, " ", Zipcode) as "Address",
			count(distinct(orders.ID)) as "# Orders", COUNT(distinct drone_tech.Username) + 1 as "Employees",
			SUM(`contains`.Quantity * chain_item.Price) as "Total"
		from manager
			right outer join drone_tech on drone_tech.ChainName = manager.ChainName
			join store on drone_tech.StoreName = store.StoreName
				and drone_tech.ChainName = store.ChainName
			left outer join drone on drone.DroneTech = drone_tech.Username
			left outer join orders on Drone.ID = Orders.DroneID
			left outer join `contains` on Orders.ID = `Contains`.OrderID
			left outer join chain_item on `contains`.PLUNumber = chain_item.PLUNumber
				and `contains`.ItemName = chain_item.ChainItemName
				and `contains`.ChainName = chain_item.ChainName
		where
			manager.Username = i_mgr_username
			and Store.StoreName = i_storeName
		group by Store.StoreName, Store.ChainName
			having SUM(`contains`.Quantity * chain_item.Price) >= i_minTotal
            and SUM(`contains`.Quantity * chain_item.Price) is not null;
		
	-- [1, 1, 1]
elseif (i_storeName is not null and i_minTotal is not null and i_maxTotal is not null) then
		create table manager_manage_stores_result
		select store.StoreName, CONCAT(Street, " ", City, ", ", State, " ", Zipcode) as "Address",
			count(distinct(orders.ID)) as "# Orders", COUNT(distinct drone_tech.Username) + 1 as "Employees",
			SUM(`contains`.Quantity * chain_item.Price) as "Total"
		from manager
			right outer join drone_tech on drone_tech.ChainName = manager.ChainName
			join store on drone_tech.StoreName = store.StoreName
				and drone_tech.ChainName = store.ChainName
			left outer join drone on drone.DroneTech = drone_tech.Username
			left outer join orders on Drone.ID = Orders.DroneID
			left outer join `contains` on Orders.ID = `Contains`.OrderID
			left outer join chain_item on `contains`.PLUNumber = chain_item.PLUNumber
				and `contains`.ItemName = chain_item.ChainItemName
				and `contains`.ChainName = chain_item.ChainName
		where
			manager.Username = i_mgr_username
			and Store.StoreName = i_storeName
		group by Store.StoreName, Store.ChainName
			having SUM(`contains`.Quantity * chain_item.Price) <= i_maxTotal
			and SUM(`contains`.Quantity * chain_item.Price) >= i_minTotal
            and SUM(`contains`.Quantity * chain_item.Price) is not null;
end if;

-- End of solution
END //
DELIMITER ;


-- ID: 13a
-- Author: vtata6
-- Name: customer_change_credit_card_information
DROP PROCEDURE IF EXISTS customer_change_credit_card_information;
DELIMITER //
CREATE PROCEDURE customer_change_credit_card_information(
	   IN i_custUsername varchar(40), 
	   IN i_new_cc_number varchar(19), 
	   IN i_new_CVV int, 
	   IN i_new_exp_date date
)
sp_main: BEGIN
-- Type solution below

if i_custUsername is null or i_new_cc_number is null or i_new_CVV is null or i_new_exp_date is null
then leave sp_main; end if;

if i_new_exp_date - CURDATE() < 0 then leave sp_main; end if;

update customer
set CcNumber = i_new_cc_number, CVV = i_new_CVV, EXP_DATE = i_new_exp_date
where username = i_custUsername; 
-- End of solution
END //
DELIMITER ;


-- ID: 14a
-- Author: ftsang3
-- Name: customer_view_order_history
DROP PROCEDURE IF EXISTS customer_view_order_history;
DELIMITER //
CREATE PROCEDURE customer_view_order_history(
	   IN i_username VARCHAR(40),
       IN i_orderid INT
)
BEGIN
-- Type solution below
	drop table if exists customer_view_order_history_result;

    create table customer_view_order_history_result
	select SUM(`contains`.Quantity * chain_item.Price) as "Total Amount",
		SUM(`contains`.Quantity) as "Total Items", Orders.OrderDate as "Date of Purchase",
		Orders.DroneID, Drone.DroneTech as "Store Associate", Orders.OrderStatus as "Status"
	from orders
		left outer join drone on Orders.DroneID = Drone.ID
		join `contains` on Orders.ID = `Contains`.OrderID
		join chain_item on `contains`.PLUNumber = chain_item.PLUNumber
			and `contains`.ItemName = chain_item.ChainItemName
			and `contains`.ChainName = chain_item.ChainName
	where Orders.CustomerUsername = i_username
		and Orders.ID = i_orderid
	group by Orders.ID;
	-- End of solution
END //
DELIMITER ;



-- ID: 15a
-- Author: ftsang3
-- Name: customer_view_store_items
DROP PROCEDURE IF EXISTS customer_view_store_items;
DELIMITER //
CREATE PROCEDURE customer_view_store_items(
	   IN i_username VARCHAR(40),
       IN i_chain_name VARCHAR(40),
       IN i_store_name VARCHAR(40),
       IN i_item_type VARCHAR(40)
)
BEGIN
-- Type solution below
	drop table if exists customer_view_store_items_result;
	-- Type solution below
	if (i_item_type = "ALL" or i_item_type is null) then
		create table customer_view_store_items_result
		select distinct chain_item.ChainItemName as 'Items', Chain_item.OrderLimit as 'orderlimit'
		from chain_item, item, store
		where chain_item.ChainName in (select store.ChainName from store where StoreName = i_store_name)
		and store.Zipcode in (select Users.Zipcode from Users where Users.Username = i_username)
		and chain_item.ChainName = i_chain_name
		and item.ItemName = chain_item.ChainItemName;
	else
		create table customer_view_store_items_result
		select distinct chain_item.ChainItemName as 'Items', Chain_item.OrderLimit as 'orderlimit'
		from chain_item, item, store
		where chain_item.ChainName in (select store.ChainName from store where StoreName = i_store_name)
		and store.Zipcode in (select Users.Zipcode from Users where Users.Username = i_username)
		and chain_item.ChainName = i_chain_name
		and item.ItemName = chain_item.ChainItemName
		and item.ItemType = i_item_type;
	end if;
	-- End of solution
END //
DELIMITER ;

-- ID: 15b
-- Author: ftsang3
-- Name: customer_select_items
DROP PROCEDURE IF EXISTS customer_select_items;
DELIMITER //
CREATE PROCEDURE customer_select_items(
	    IN i_username VARCHAR(40),
    	IN i_chain_name VARCHAR(40),
    	IN i_store_name VARCHAR(40),
    	IN i_item_name VARCHAR(40),
    	IN i_quantity INT
)
sp_main: BEGIN
-- Type solution below
if i_username is null or i_chain_name is null or i_store_name is null or i_item_name is null or i_quantity is null
then leave sp_main; end if;

if (i_quantity <= 0)
then leave sp_main; end if;

if i_username not in (select username from customer)
then leave sp_main; end if;


if (i_store_name, i_chain_name) not in (select storename, chainname from store)
then leave sp_main; end if;


if (select zipcode from users where username = i_username) 
!= (select zipcode from store where storename = i_store_name and chainname = i_chain_name)
then leave sp_main; end if;


if i_item_name not in (select ChainItemName from chain_item where chainname = i_chain_name)
then leave sp_main; end if;


if i_item_name in 
	(select itemname from contains join orders on contains.orderid = orders.id
	where orderstatus = 'creating' and customerusername = i_username)
then leave sp_main; end if;


if i_quantity >
(select orderlimit from chain_item where ChainName = i_chain_name and ChainItemName = i_item_name)
then leave sp_main; end if;


if
	(select ID from orders where customerusername = i_username and orderstatus = 'Creating') is null
then
	insert into orders values ((select max(orderid) + 1 from contains), 'Creating', CURDATE(), i_username, null);
    insert into contains values 
		((select max(id) from orders where orderstatus = 'Creating' 
			and CustomerUsername = i_username), 
		i_item_name, i_chain_name, 
        (select PLUNumber from chain_item 
			where chainname = i_chain_name and ChainItemname = i_item_name), 
		i_quantity);
    
else
    insert into contains values 
		((select max(id) from orders where orderstatus = 'Creating' 
			and CustomerUsername = i_username), 
		i_item_name, i_chain_name, 
        (select PLUNumber from chain_item 
			where chainname = i_chain_name and ChainItemname = i_item_name), 
		i_quantity);

end if;


-- End of solution
END //
DELIMITER ;



-- ID: 16a
-- Author: jkomskis3
-- Name: customer_review_order
DROP PROCEDURE IF EXISTS customer_review_order;
DELIMITER //
CREATE PROCEDURE customer_review_order(
	   IN i_username VARCHAR(40)
)
BEGIN
-- Type solution below
	drop table if exists customer_review_order_result;
	-- Type solution below
    create table customer_review_order_result
	select chain_item.ChainItemName, `contains`.Quantity, chain_item.Price, Store.ChainName, Store.StoreName
	from orders
		join `contains` on orders.ID = `contains`.OrderID
		join chain_item on chain_item.ChainItemName = `contains`.ItemName
			and chain_item.ChainName = `contains`.ChainName
		join users on users.username = orders.CustomerUsername
		join store on `contains`.ChainName = store.ChainName
			and users.Zipcode = store.Zipcode
	where OrderStatus = 'Creating' and username = i_username;
-- End of solution
END //
DELIMITER ;



-- ID: 16b
-- Author: jkomskis3
-- Name: customer_update_order
DROP PROCEDURE IF EXISTS customer_update_order;
DELIMITER //
CREATE PROCEDURE customer_update_order(
	   IN i_username VARCHAR(40),
       IN i_item_name VARCHAR(40),
       IN i_quantity INT
)
sp_main: BEGIN
-- Type solution below

if i_username not in 
(select customerusername from orders where orderstatus = 'Creating'
	and customerusername = i_username)
then leave sp_main; end if;

if i_quantity < 0 then leave sp_main; end if;


if i_quantity = 0
	then delete from `contains`   
    where `contains`.OrderID = (select ID from orders where customerusername = i_username and 
    orderstatus = 'creating')
	and `contains`.ItemName = i_item_name;
leave sp_main; end if;


if i_quantity > 
	(select OrderLimit from chain_item where ChainItemName = i_item_name
		and chainname = (select distinct(chainname) from contains where orderid = 
			(select ID from orders 
				where CustomerUsername = i_username and orderstatus = 'Creating')))
then leave sp_main; end if;



update `contains`
	set `contains`.Quantity = i_quantity
	where `contains`.OrderID = (select ID from orders where customerusername = i_username
    and orderstatus = 'Creating')
		and `contains`.ItemName = i_item_name;


-- End of solution
END //
DELIMITER ;




-- ID: 17a
-- Author: jkomskis3
-- Name: customer_update_order
DROP PROCEDURE IF EXISTS drone_technician_view_order_history;
DELIMITER //
CREATE PROCEDURE drone_technician_view_order_history(
        IN i_username VARCHAR(40),
    	IN i_start_date DATE,
    	IN i_end_date DATE
)
BEGIN
-- Type solution below
	drop table if exists drone_technician_view_order_history_result;
	-- both dates are left out
	if (i_start_date is null and i_end_date is null) then
		create table drone_technician_view_order_history_result
		select orders.id, concat(user_drone_tech.firstname, ' ', user_drone_tech.lastname) as operator, 
			orderdate, droneID, orderstatus,
			round(sum(price * `contains`.quantity), 2) as Total
		from orders
			join `contains` on orders.ID = `contains`.orderid
			join chain_item on chain_item.ChainItemName = `contains`.ItemName
				and chain_item.ChainName = `contains`.ChainName
			left outer join drone on drone.ID = orders.DroneID
			left outer join drone_tech on drone_tech.username = drone.dronetech
			left outer join users as user_drone_tech on user_drone_tech.username = drone_tech.username
			join users as user_customer on orders.customerusername = user_customer.username
			join store on `Contains`.ChainName = store.chainName
				and store.zipcode = user_customer.zipcode
		where (store.chainname, store.storename)
			in (select ChainName, StoreName
				from drone_tech
				where username = i_username)
		group by orders.ID;

	-- only the first date is included
	elseif (i_start_date is not null and i_end_date is null) then
		create table drone_technician_view_order_history_result
		select orders.id, concat(user_drone_tech.firstname, ' ', user_drone_tech.lastname) as operator, 
			orderdate, droneID, orderstatus,
			round(sum(price * `contains`.quantity), 2) as Total
		from orders
			join `contains` on orders.ID = `contains`.orderid
			join chain_item on chain_item.ChainItemName = `contains`.ItemName
				and chain_item.ChainName = `contains`.ChainName
			left outer join drone on drone.ID = orders.DroneID
			left outer join drone_tech on drone_tech.username = drone.dronetech
			left outer join users as user_drone_tech on user_drone_tech.username = drone_tech.username
			join users as user_customer on orders.customerusername = user_customer.username
			join store on `Contains`.ChainName = store.chainName
				and store.zipcode = user_customer.zipcode
		where (store.chainname, store.storename)
			in (select ChainName, StoreName
				from drone_tech
				where username = i_username)
			and orderdate >= i_start_date
		group by orders.ID;

	-- only the second date is included

	elseif (i_start_date is null and i_end_date is not null) then
		create table drone_technician_view_order_history_result
		select orders.id, concat(user_drone_tech.firstname, ' ', user_drone_tech.lastname) as operator, 
			orderdate, droneID, orderstatus,
			round(sum(price * `contains`.quantity), 2) as Total
		from orders
			join `contains` on orders.ID = `contains`.orderid
			join chain_item on chain_item.ChainItemName = `contains`.ItemName
				and chain_item.ChainName = `contains`.ChainName
			left outer join drone on drone.ID = orders.DroneID
			left outer join drone_tech on drone_tech.username = drone.dronetech
			left outer join users as user_drone_tech on user_drone_tech.username = drone_tech.username
			join users as user_customer on orders.customerusername = user_customer.username
			join store on `Contains`.ChainName = store.chainName
				and store.zipcode = user_customer.zipcode
		where (store.chainname, store.storename)
			in (select ChainName, StoreName
				from drone_tech
				where username = i_username)
			and orderdate <= i_end_date
		group by orders.ID;

	-- both dates are included

	elseif (i_start_date is not null and i_end_date is not null) then
		create table drone_technician_view_order_history_result
		select orders.id, concat(user_drone_tech.firstname, ' ', user_drone_tech.lastname) as operator, 
			orderdate, droneID, orderstatus,
			round(sum(price * `contains`.quantity), 2) as Total
		from orders
			join `contains` on orders.ID = `contains`.orderid
			join chain_item on chain_item.ChainItemName = `contains`.ItemName
				and chain_item.ChainName = `contains`.ChainName
			left outer join drone on drone.ID = orders.DroneID
			left outer join drone_tech on drone_tech.username = drone.dronetech
			left outer join users as user_drone_tech on user_drone_tech.username = drone_tech.username
			join users as user_customer on orders.customerusername = user_customer.username
			join store on `Contains`.ChainName = store.chainName
				and store.zipcode = user_customer.zipcode
		where (store.chainname, store.storename)
			in (select ChainName, StoreName
				from drone_tech
				where username = i_username)
			and orderdate >= i_start_date and orderdate <= i_end_date
		group by orders.ID;
end if;
-- End of solution
END //
DELIMITER ;



-- ID: 17b
-- Author: agoyal89
-- Name: dronetech_assign_order
DROP PROCEDURE IF EXISTS dronetech_assign_order;
DELIMITER //
CREATE PROCEDURE dronetech_assign_order(
	   IN i_username VARCHAR(40),
       IN i_droneid INT,
       IN i_status VARCHAR(20),
       IN i_orderid INT
)
sp_main: BEGIN
-- Type solution below

if i_username is null or i_droneid is null or i_status is null or i_orderid is null
then leave sp_main; end if;


if 'In Transit' = (select orderstatus from orders where ID = i_orderid)
then leave sp_main; end if;

if i_droneid not in (select ID from drone where dronetech = i_username)
then leave sp_main; end if;

-- assume Order Status changed to Drone Assigned
update orders
set DroneID = i_droneid
where ID = i_orderid;

-- Changes order status
update orders
set orderstatus = i_status
where ID = i_orderid;

-- assume Drone Status becomes busy
update drone
set dronestatus = 'Busy'
where ID = i_droneid and dronetech = i_username;

-- End of solution
END //
DELIMITER ;



-- ID: 18a
-- Author: agoyal89
-- Name: dronetech_order_details
DROP PROCEDURE IF EXISTS dronetech_order_details;
DELIMITER //
CREATE PROCEDURE dronetech_order_details(
	   IN i_username VARCHAR(40),
       IN i_orderid INT
)
BEGIN
-- Type solution below

	drop table if exists dronetech_order_details_result;
	-- Type solution below
    create table dronetech_order_details_result
	select concat(user_customer.FirstName, ' ', user_customer.Lastname) as Customer_Name,
    orders.ID as "Order_ID", round(sum(price * contains.quantity),2) as 'Total_Amount', 
	sum(`contains`.quantity) as 'Total_Items', orders.OrderDate as "Date_of_Purchase",
    Ifnull(orders.DroneID,'N/A') as Drone_ID, 
    Ifnull(concat(user_drone_tech.firstname, ' ', user_drone_tech.lastname),'N/A') as Store_Associate, 
    Orders.OrderStatus as "Order_Status",
    concat(user_customer.Street, ', ', user_customer.City, ', ', user_customer.State, ' ', user_customer.zipcode) as Address
		from orders
			join `contains` on orders.ID = `contains`.orderid
			join chain_item on chain_item.ChainItemName = `contains`.ItemName
				and chain_item.ChainName = `contains`.ChainName
			left outer join drone on drone.ID = orders.DroneID
			left outer join drone_tech on drone_tech.username = drone.dronetech
			left outer join users as user_drone_tech on user_drone_tech.username = drone_tech.username
			join users as user_customer on orders.customerusername = user_customer.username
			join store on `Contains`.ChainName = store.chainName
				and store.zipcode = user_customer.zipcode
		where (store.chainname, store.storename)
			in (select ChainName, StoreName
				from drone_tech
				where username = i_username
					and orders.ID = i_orderid)
		group by orders.ID;


	-- End of solution
END //
DELIMITER ;



-- ID: 18b
-- Author: agoyal89
-- Name: dronetech_order_items
DROP PROCEDURE IF EXISTS dronetech_order_items;
DELIMITER //
CREATE PROCEDURE dronetech_order_items(
        IN i_username VARCHAR(40),
    	IN i_orderid INT
)
BEGIN
-- Type solution below

	drop table if exists dronetech_order_items_result;
	-- Type solution below
    create table dronetech_order_items_result
	select ItemName as Item, `contains`.Quantity as Count
		from orders
			join `contains` on orders.ID = `contains`.orderid
			join chain_item on chain_item.ChainItemName = `contains`.ItemName
				and chain_item.ChainName = `contains`.ChainName
			left outer join drone on drone.ID = orders.DroneID
			left outer join drone_tech on drone_tech.username = drone.dronetech
			left outer join users as user_drone_tech on user_drone_tech.username = drone_tech.username
			join users as user_customer on orders.customerusername = user_customer.username
			join store on `Contains`.ChainName = store.chainName
				and store.zipcode = user_customer.zipcode
		where (store.chainname, store.storename)
			in (select ChainName, StoreName
				from drone_tech
				where username = i_username
					and orders.ID = i_orderID);
	-- End of solution
END //
DELIMITER ;



-- ID: 19a
-- Author: agoyal89
-- Name: dronetech_assigned_drones
DROP PROCEDURE IF EXISTS dronetech_assigned_drones;
DELIMITER //
CREATE PROCEDURE dronetech_assigned_drones(
        IN i_username VARCHAR(40),
    	IN i_droneid INT,
    	IN i_status VARCHAR(20)
)
BEGIN
-- Type solution below

	drop table if exists dronetech_assigned_drones_result;

if ((i_status = "All" or i_status is null) and i_droneid is null) then
		create table dronetech_assigned_drones_result
		select drone.id as "Drone ID", Drone.DroneStatus as "Status", Drone.radius as "Radius"
		from drone_tech natural join store natural join drone
		where drone_tech.Username = drone.DroneTech
        and (drone_tech.Storename, drone_tech.ChainName) = (store.Storename, store.ChainName)
		and store.Zipcode = Drone.zip
		and drone_tech.Username = i_username;
	elseif ((i_status != "All" and i_status is not null)) and i_droneid is null then
		create table dronetech_assigned_drones_result
		select drone.id as "Drone ID", Drone.DroneStatus as "Status", Drone.radius as "Radius"
		from drone_tech natural join store natural join drone
		where drone_tech.Username = drone.DroneTech
		and (drone_tech.Storename, drone_tech.ChainName) = (store.Storename, store.ChainName)
		and store.Zipcode = Drone.zip
		and drone_tech.Username = i_username
		and drone.dronestatus = i_status;
	elseif ((i_status = "All" or i_status is null) and i_droneid is not null) then
		create table dronetech_assigned_drones_result
		select drone.id as "Drone ID", Drone.DroneStatus as "Status", Drone.radius as "Radius"
		from drone_tech natural join store natural join drone
		where drone_tech.Username = drone.DroneTech
        and (drone_tech.Storename, drone_tech.ChainName) = (store.Storename, store.ChainName)
		and store.Zipcode = Drone.zip
		and drone_tech.Username = i_username
		and drone.ID = i_droneid;
	else
		create table dronetech_assigned_drones_result
		select drone.id as "Drone ID", Drone.DroneStatus as "Status", Drone.radius as "Radius"
		from drone_tech natural join store natural join drone
		where drone_tech.Username = drone.DroneTech
        and (drone_tech.Storename, drone_tech.ChainName) = (store.Storename, store.ChainName)
		and store.Zipcode = Drone.zip
		and drone_tech.Username = i_username
		and drone.id = i_droneid
		and drone.DroneStatus = i_status;
	end if;
	-- End of solution
END //
DELIMITER ;




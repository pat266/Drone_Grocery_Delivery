# Drone Grocery Delivery

## CS4400 Group 6 Spring 2021
### Description
This is a project based on the description given in class. The project description states all information necessary for each role on every screen, and we would have to create a relational database to fulfill it.

### Some Login Credentials:
`Admin`: Username: mmoss7, password: password3 <br />
`Chain Manager`: Username: dschrute18, password: password27 <br />
`Customer`: Username: jpark29, password: password40 <br />
`Drone Technician`: Username: ExtraDroneOperator, password: password67 <br />
**Note**: The default login credential is of an Admin

### How it was made
I use JavaFX to design the front end and Java (connected to the MySQL Database by the Connector/J) for the back end. The database connected to this version is the Amazon RDS Free Tier of MySQL Community verison, so all changes and user registration can be seen by everyone else.

Install `mysql-connector-java-8.0.23` and `javafx-sdk-11.0.2` to compile the code.

### Visualization
* When the application cannot establish a connection to the database vs. when it can <br />
<img src="visualizations/database-fail.png" alt="drawing" width="400"/>
<img src="visualizations/database-success.png" alt="drawing" width="400"/><br />

* **Registration**<br />
<img src="visualizations/registration.gif" alt="drawing" height=450 width="600"/>

* **Admin**<br />
<img src="visualizations/admin.gif" alt="drawing" height=450 width="600"/>

* **Chain Manager**<br />
<img src="visualizations/Chain_Manager.gif" alt="drawing" height=450 width="600"/>

* **Entity Relationship Diagram**<br />
<img src="visualizations/diagrams/Entity Relationship Diagram-1.png" alt="drawing"/>

### Contributors
#### Aidan Perras (aperras3)
#### Ana Belen Coronel (arodriguez328)
#### Minjik Kim (mkim616)
#### Pat Tran (ptran74)

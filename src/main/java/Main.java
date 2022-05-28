import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Main extends Application {
    private Stage mainStage;
    // create a boolean value for Register button, it is customer by default
    boolean isCustomer = true;
    // Connection of the Database
    private static Connection connection = null;
    /**
     * This constructor has to be here or else errors would occur
     */
    public Main() {
        this.connection = null;
        this.mainStage = null;
    }

    /**
     * Use to go back to the main class.
     * @param mainStage The Main Stage, used throughout the program.
     * @param connection The database connection.
     */
    public Main(Stage mainStage, Connection connection) {
        this.connection = connection;
        this.mainStage = mainStage;
    }
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.mainStage = primaryStage;
        this.mainStage.setTitle("Group 6 Phase 4");
        this.mainStage.setScene(createLoginScene());
        this.mainStage.setResizable(false);
        this.mainStage.sizeToScene();
        this.mainStage.show();
    }

    public Scene createLoginScene() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(30);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Login");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        grid.add(welcomeTxt, 1, 0);

        // Username
        Label labelUser = new Label("Username: ");
        labelUser.setFont(Font.font("Arial",18));
        grid.add(labelUser, 0, 1);
        TextField userInput = new TextField();
        userInput.setPromptText("Enter Username");
        userInput.setText("mmoss7");
        // userInput.setPrefWidth(200);
        userInput.setPrefSize(150, 35);
        grid.add(userInput, 1, 1);

        // Password
        Label labelPass = new Label("Password: ");
        labelPass.setFont(Font.font("Arial",18));
        grid.add(labelPass, 0, 2);
        PasswordField passInput = new PasswordField();
        passInput.setPromptText("Enter Password");
        passInput.setText("password3");
        // passInput.setPrefWidth(200);
        passInput.setPrefSize(150, 35);
        grid.add(passInput, 1, 2);

        Label connectionStatus = new Label();
        connectionStatus.setFont(Font.font("Arial", 16));
        grid.add(connectionStatus, 0, 3, 2, 1);

        String dbName, endPoint, loginUser, loginPass;
        dbName = "grocery_drone_delivery";
        endPoint = "database-1.cax6knv3dgtq.us-east-2.rds.amazonaws.com";
        loginUser = "admin";
        loginPass = "DroneGroceryDelivery";

        // the port is usually 3306
        String url = "jdbc:mysql://" + endPoint + ":3306/" + dbName;
        boolean isConnected = false;
        // load up the MySQL
        try {
            // Download Connector/j to Connect MySQL to Java, download Zip Archive
            // (https://dev.mysql.com/downloads/connector/j/) with Platform Independent as Operating System
            connection = (Connection) DriverManager.getConnection(url, loginUser, loginPass);
            if (connection != null) {
                isConnected = true;
                connectionStatus.setTextFill(Color.GREEN);
                connectionStatus.setText("Successfully connected to the database!");
            } else {
                isConnected = false;
                connectionStatus.setTextFill(Color.RED);
                connectionStatus.setText("Unsuccessfully connected to the database!");
            }
        } catch (Exception exception) {
            isConnected = false;
            connectionStatus.setTextFill(Color.RED);
            connectionStatus.setText("Unsuccessfully connected to the database!");
            exception.printStackTrace();
        }

        /**
         * Customer: jpark29, password40
         * Drone_tech: ExtraDroneOperator, password67
         * Admin: mmoss7, password3
         * Chain Manager: dschrute18, password27
         */
        // Login
        Button loginButton = new Button("Login");
        loginButton.setPrefSize(100, 50);
        loginButton.setOnAction(e -> {
            if (userInput.getText().isEmpty() || passInput.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please fill in all information.");
                alert.showAndWait();
            } else {
                // a list of table to check
                String[] table = {"admin", "customer", "drone_tech", "manager"};
                // create the java statement
                String checkStm1 = "Select count(*) from users where username = \"" + userInput.getText() +
                        "\" and pass = md5(\"" + passInput.getText() + "\")";
                String checkStm2 = "Select count(*) from users where username = \"" + userInput.getText() +
                        "\" and pass = \"" + passInput.getText() + "\"";
                // the table that we will use
                String useTable = "";
                try {
                    Statement st = connection.createStatement();
                    ResultSet resultSet1 = st.executeQuery(checkStm1);
                    int count1 = 0;
                    if (resultSet1.next()) {
                        count1 = resultSet1.getInt("count(*)");
                    }
                    resultSet1.close();

                    int count2 = 0;
                    ResultSet resultSet2 = st.executeQuery(checkStm2);
                    if (resultSet2.next()) {
                        count2 = resultSet2.getInt("count(*)");
                    }
                    resultSet2.close();

                    // if the username and password match
                    if (count1 >= 1 || count2 >= 1) {
                        // find the table that the username is in
                        for (String temp : table) {
                            String tempStm = "select count(*) from " + temp + " where username = \""
                                    + userInput.getText() + "\"";
                            ResultSet tempRS = st.executeQuery(tempStm);
                            if (tempRS.next()) {
                                int counter = tempRS.getInt("count(*)");
                                if (counter >= 1) {
                                    useTable = temp;
                                    break;
                                }
                            }
                        }
                        System.out.println(useTable);
                        if (!useTable.equalsIgnoreCase("")) {
                            switch (useTable) {
                                case "admin":
                                    Admin admin = new Admin(this.mainStage, this.connection, userInput.getText());
                                    this.mainStage.setScene(admin.createAdminHome());
                                    break;
                                case "customer":
                                    Customer customer = new Customer(this.mainStage, this.connection, userInput.getText());
                                    this.mainStage.setScene(customer.createCustomerHome());
                                    break;
                                case "drone_tech":
                                    DroneTech droneTech = new DroneTech(this.mainStage, this.connection, userInput.getText());
                                    this.mainStage.setScene(droneTech.createDroneTechHome());
                                    break;
                                case "manager":
                                    ChainManager chainManager = new ChainManager(this.mainStage, this.connection, userInput.getText());
                                    this.mainStage.setScene(chainManager.createChainManagerHome());
                                    break;
                            }
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Username or Password is incorrect.");
                        alert.showAndWait();
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Failed to connect to the AWS database. Please contact ptran74@gatech.edu.");
                        alert.show();
                    });
                }
            }
        });
        grid.add(loginButton, 0, 4);

        // Register
        Button registerButton = new Button("Register");
        registerButton.setPrefSize(100, 50);
        boolean finalIsConnected = isConnected;
        registerButton.setOnAction(e -> {
            if (finalIsConnected) {
                this.mainStage.setScene(createRegisterScene());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Failed to connect to the AWS database. Please contact ptran74@gatech.edu.");
                alert.showAndWait();
            }
        });
        grid.add(registerButton, 1, 4);

        Scene scene = new Scene(grid, 800, 600);
        return scene;
    }

    public Scene createRegisterScene() {
        GridPane grid = new GridPane();
        for (int col = 0; col < 4; col++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(25);
            cc.setFillWidth(true);
            // always shrink/grow horizontally when the user changes the stage
            cc.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(cc);
        }
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        // Row 0
        Text welcomeTxt = new Text("Register");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        grid.add(welcomeTxt, 1, 0);

        // Row 1 (First Name and Street)
        Label labelFirst = new Label("First Name:");
        grid.add(labelFirst, 0, 1);
        TextField firstName = new TextField();
        firstName.setPromptText("First Name");
        firstName.setPrefSize(150, 35);
        grid.add(firstName, 1, 1);

        Label labelStreet = new Label("Street:");
        grid.add(labelStreet, 2, 1);
        TextField street = new TextField();
        street.setPromptText("Street:");
        street.setPrefSize(200, 35);
        grid.add(street, 3, 1);

        // Row 2 (Last Name and City)
        Label labelLast = new Label("Last Name:");
        grid.add(labelLast, 0, 2);
        TextField lastName = new TextField();
        lastName.setPromptText("Last Name:");
        lastName.setPrefSize(150, 35);
        grid.add(lastName, 1, 2);

        Label labelCity = new Label("City:");
        grid.add(labelCity, 2, 2);
        TextField city = new TextField();
        city.setPromptText("City");
        city.setPrefSize(200, 35);
        grid.add(city, 3, 2);

        // Row 3 (Username and State)
        Label labelUsername = new Label("Username:");
        grid.add(labelUsername, 0, 3);
        TextField username = new TextField();
        username.setPromptText("Username:");
        username.setPrefSize(150, 35);
        grid.add(username, 1, 3);

        Label labelState = new Label("State:");
        grid.add(labelState, 2, 3);
        ObservableList<String> observableList = FXCollections.observableArrayList("AK","AL","AR","AZ","CA","CO","CT","DC","DE","FL","GA","GU","HI","IA","ID", "IL","IN","KS","KY","LA","MA","MD","ME","MH","MI","MN","MO","MS","MT","NC","ND","NE","NH","NJ","NM","NV","NY", "OH","OK","OR","PA","PR","PW","RI","SC","SD","TN","TX","UT","VA","VI","VT","WA","WI","WV","WY");
        ComboBox<String> state = new ComboBox<>();
        state.setItems(observableList);
        state.getSelectionModel().select(10);
        state.setEditable(true);
        state.setPrefSize(200, 35);
        grid.add(state, 3, 3);

        // Row 4 (Password and Zipcode)
        Label labelPass = new Label("Password:");
        grid.add(labelPass, 0, 4);
        PasswordField pass = new PasswordField();
        pass.setPromptText("Password");
        pass.setPrefSize(150, 35);
        grid.add(pass, 1, 4);

        Label labelZip = new Label("Zip:");
        grid.add(labelZip, 2, 4);
        TextField zip = new TextField();
        zip.setPromptText("Zipcode");
        zip.setPrefSize(200, 35);
        zip.setTextFormatter(new TextFormatter<>((change) -> {
            String text = change.getControlNewText();
            if (text.matches("\\d{0,5}")) {
                return change;
            } else {
                return null;
            }
        }));
        grid.add(zip, 3, 4);

        // Row 5 (Password Confirmation)
        Label labelConfirm = new Label("Confirm:");
        grid.add(labelConfirm, 0, 5);
        PasswordField confirm = new PasswordField();
        confirm.setPromptText("Confirm Password");
        confirm.setPrefSize(150, 35);
        grid.add(confirm, 1, 5);

        // create another GridPane
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(20);
        // gridPane.setAlignment(Pos.CENTER);
        // Add the first row (1)
        Label labelCCNum = new Label("Card Number:");
        TextField ccNum = new TextField();
        ccNum.setPromptText("xxxx xxxx xxxx xxxx");
        ccNum.setPrefSize(200, 35);
        ccNum.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (newValue.length() >= 20) {
                    ccNum.setText(oldValue);
                } else if (!newValue.matches("\\d*")) {
                    ccNum.setText(newValue.replaceAll("[^\\d ]", ""));
                }
            }
        });

        // Add the Employee section
        Label labelChain = new Label("Associated Grocery Chain:");
        TextField chain = new TextField();
        chain.setPromptText("");
        chain.setPrefSize(200, 35);
        Label labelStore = new Label("Associated Store Name:");
        TextField store = new TextField();
        store.setPromptText("");
        store.setPrefSize(200, 35);

        // Add the second row (CVV and Expiration Date)
        Label labelCVV = new Label("CVV:");
        TextField cvv = new TextField();
        cvv.setPromptText("xxx");
        cvv.setPrefSize(200, 35);
        cvv.setTextFormatter(new TextFormatter<>((change) -> {
            String text = change.getControlNewText();
            if (text.matches("\\d{0,3}")) {
                return change;
            } else {
                return null;
            }
        }));
        Label labelExp = new Label("Exp:");
        TextField exp = new TextField();
        exp.setPromptText("mm/yy");
        exp.setPrefSize(200, 35);
        exp.setTextFormatter(new TextFormatter<>((change) -> {
            String text = change.getControlNewText();
            if (text.matches("\\d{0,2}(/\\d{0,2})?")) {
                return change;
            } else {
                return null;
            }
        }));


        gridPane.add(labelCCNum, 0, 1);
        gridPane.add(ccNum, 1, 1);
        gridPane.add(labelCVV, 0, 2);
        gridPane.add(cvv, 1, 2);
        gridPane.add(labelExp, 0, 3);
        gridPane.add(exp, 1, 3);



        // Row 6 (Switch between Customer and Employee)
        ToggleButton selectCustomer = new ToggleButton("Customer");
        ToggleButton selectEmployee = new ToggleButton("Employee");
        ToggleGroup toggleGroup = new ToggleGroup();
        // set toggle group
        selectCustomer.setToggleGroup(toggleGroup);
        selectEmployee.setToggleGroup(toggleGroup);

        // Create a ChangeListener for the ToggleGroup
        toggleGroup.selectedToggleProperty().addListener(
                new ChangeListener<Toggle>() {
                    @Override
                    public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, final Toggle new_toggle) {
                        // prevents NullPointerException
                        if (new_toggle == null) {

                        } else {
                            String toggleBtn = ((ToggleButton) new_toggle).getText();
                            // System.out.println(toggleBtn);
                            if (toggleBtn.equalsIgnoreCase("Customer")) {
                                gridPane.getChildren().clear();
                                gridPane.add(labelCCNum, 0, 1);
                                gridPane.add(ccNum, 1, 1);
                                gridPane.add(labelCVV, 0, 2);
                                gridPane.add(cvv, 1, 2);
                                gridPane.add(labelExp, 0, 3);
                                gridPane.add(exp, 1, 3);
                                isCustomer = true;
                            } else {
                                gridPane.getChildren().clear();
                                gridPane.add(labelChain, 1, 1);
                                gridPane.add(chain, 2, 1);
                                gridPane.add(labelStore, 1, 2);
                                gridPane.add(store, 2, 2);
                                isCustomer = false;
                            }
                        }
                    }
                }
        );
        // set the size
        selectCustomer.setPrefSize(Double.MAX_VALUE, 40);
        selectEmployee.setPrefSize(Double.MAX_VALUE, 40);
        // add to the grid
        grid.add(selectCustomer, 0, 6, 2, 1);
        grid.add(selectEmployee, 2, 6, 2, 1);

        grid.add(gridPane, 0, 7, 4, 3);

        // Back Button
        Button back = new Button("Back");
        back.setWrapText(true);
        back.setMaxWidth(150);
        back.setPrefSize(100, 60);
        back.setTextAlignment(TextAlignment.CENTER);
        back.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        back.setStyle("-fx-background-color: #000;");
        back.setTextFill(Color.WHITE);
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.setScene(createLoginScene());
            }
        });

        // Auto-fill Button
        Button fill = new Button("Auto Fill");
        fill.setWrapText(true);
        fill.setMaxWidth(150);
        fill.setPrefSize(100, 60);
        fill.setTextAlignment(TextAlignment.CENTER);
        fill.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        fill.setStyle("-fx-background-color: #000;");
        fill.setTextFill(Color.WHITE);
        fill.setOnAction(e -> {
            firstName.setText(generateWord(5));
            street.setText(generateWord(5));
            lastName.setText(generateWord(5));
            city.setText(generateWord(5));
            username.setText(generateWord(5));
            zip.setText(generateNumber(5));
            StringBuilder ccNumber = new StringBuilder();
            String num16 = generateNumber(16);
            for (int i = 0; i < 16; i++) {
                if (i % 4 == 0 && i != 0) {
                    ccNumber.append(' ');
                }
                ccNumber.append(num16.charAt(i));
            }
            ccNum.setText(ccNumber.toString());
            cvv.setText(generateNumber(3));

            int min_month = 1;
            int max_month = 12;
            int min_year = 30;
            int max_year = 50;
            Random ran = new Random();
            int month = ran.nextInt(max_month) + min_month;
            String monthStr = null;
            if (month < 10) {
                monthStr = "0" + Integer.toString(month);
            } else {
                monthStr = Integer.toString(month);
            }
            int year = ran.nextInt(max_year) + min_year;
            exp.setText(monthStr + "/" + year);
        });

        // Register Button
        Button register = new Button("Register");
        register.setWrapText(true);
        register.setMaxWidth(150);
        register.setPrefSize(100, 60);
        register.setTextAlignment(TextAlignment.CENTER);
        register.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        register.setStyle("-fx-background-color: #000;");
        register.setTextFill(Color.WHITE);
        register.setOnAction(e -> {
            if (isCustomer) {
                String[] expParts = exp.getText().split("/");
                // process the date
                String expD = exp.getText();
                YearMonth yearMonth = YearMonth.of(1900, 12);
                if (!exp.getText().isEmpty() && expD.length() == 5 && expD.contains("/") && Integer.parseInt(expParts[0]) <= 12) {
                    yearMonth = YearMonth.parse(expD, DateTimeFormatter.ofPattern("MM/yy"));
                }
                // get last day of month (taking care of leap years)
                LocalDate dateParsed = yearMonth.atEndOfMonth();
                // no need to go to the next day now, but will go before inputting into the PreparedStatement
                // dateParsed = dateParsed.plusDays(1);
                // Process LocalDate for today
                LocalDate today = LocalDate.now();
                // check if all values have been filled in
                if (firstName.getText().isEmpty() || street.getText().isEmpty() || lastName.getText().isEmpty() ||
                        city.getText().isEmpty() || username.getText().isEmpty() || state.getValue().isEmpty() ||
                        pass.getText().isEmpty() || zip.getText().isEmpty() || confirm.getText().isEmpty() ||
                        ccNum.getText().isEmpty() || cvv.getText().isEmpty() || exp.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Please fill in all information.");
                    alert.showAndWait();
                } else if (Integer.parseInt(expParts[0]) > 12) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Please enter the month between 01-12");
                    alert.showAndWait();
                } else if (cvv.getText().length() != 3) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("CVV must be 3 digits.");
                    alert.showAndWait();
                } else if (pass.getText().length() < 8) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Password must be at least 8 characters.");
                    alert.showAndWait();
                } else if (!pass.getText().equals(confirm.getText())) {
                    // if the main password does not match the confirmation
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Confirmation password does not match.");
                    alert.showAndWait();
                } else if (expD.length() != 5 || !expD.contains("/")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Please enter expiration date in the format of mm/yy");
                    alert.showAndWait();
                } else if (!today.isBefore(dateParsed)) {
                    // check to see if the expiration date have expired
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("The credit card expiration date has expired.");
                    alert.showAndWait();
                } else if (!zip.getText().chars().allMatch(Character::isDigit)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("The zipcode must be digits.");
                    alert.showAndWait();
                } else if (zip.getText().length() < 5) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("The zipcode must consists of 5 digits.");
                    alert.showAndWait();
                } else if (!(state.getValue().chars().allMatch(Character::isLetter) && state.getValue().length() == 2)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("The state must consists of 2 letters abbreviation.");
                    alert.showAndWait();
                } else if (ccNum.getText().length() != 19) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Please enter credit card in the format of xxxx xxxx xxxx xxxx");
                    alert.showAndWait();
                }
                else {
                    System.out.println(zip.getText().length());
                    try {
                        PreparedStatement registration = (PreparedStatement) connection.prepareStatement("CALL register_customer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        registration.setString(1, username.getText());
                        registration.setString(2, pass.getText());
                        registration.setString(3, firstName.getText());
                        registration.setString(4, lastName.getText());
                        registration.setString(5, street.getText());
                        registration.setString(6, city.getText());
                        registration.setString(7, state.getValue());
                        registration.setString(8, zip.getText());
                        registration.setString(9, ccNum.getText());
                        registration.setString(10, cvv.getText());
                        dateParsed = dateParsed.plusDays(1);
                        registration.setDate(11, Date.valueOf(dateParsed));
                        int status = registration.executeUpdate();
                        System.out.println("Successfully Registering Customer for " + status + " rows");
                        if (status >= 1) {
                            Customer customer = new Customer(this.mainStage, this.connection, username.getText());
                            this.mainStage.setScene(customer.createCustomerHome());
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setContentText("Could not register. Please change the username or the credit card number");
                            alert.showAndWait();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            else {
                if (firstName.getText().isEmpty() || street.getText().isEmpty() || lastName.getText().isEmpty() ||
                        city.getText().isEmpty() || username.getText().isEmpty() || state.getValue().isEmpty() ||
                        pass.getText().isEmpty() || zip.getText().isEmpty() || confirm.getText().isEmpty() ||
                        chain.getText().isEmpty() || chain.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Please fill in all information (and enter at least a chain name).");
                    alert.showAndWait();
                } else if (pass.getText().length() < 8) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Password must be at least 8 characters.");
                    alert.showAndWait();
                } else if (!pass.getText().equals(confirm.getText())) {
                    // if the main password does not match the confirmation
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Confirmation password does not match.");
                    alert.showAndWait();
                } else if (!(zip.getText().chars().allMatch( Character::isDigit) && zip.getText().length() == 5)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("The zipcode must consists of 5 DIGITS.");
                    alert.showAndWait();
                } else if (!(state.getValue().chars().allMatch(Character::isLetter) && state.getValue().length() == 2)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("The state must consists of 2 letters abbreviation.");
                    alert.showAndWait();
                }
                else {
                    // Registering for Employee
                    try {
                        Statement st = connection.createStatement();
                        PreparedStatement registration = (PreparedStatement) connection.prepareStatement("CALL register_employee(?, ?, ?, ?, ?, ?, ?, ?)");
                        registration.setString(1, username.getText());
                        registration.setString(2, pass.getText());
                        registration.setString(3, firstName.getText());
                        registration.setString(4, lastName.getText());
                        registration.setString(5, street.getText());
                        registration.setString(6, city.getText());
                        registration.setString(7, state.getValue());
                        registration.setString(8, zip.getText());

                        String checkUsers = "Select count(*) from users where username = \"" + username.getText() + "\"";
                        int countUser = 0;
                        ResultSet rs = st.executeQuery(checkUsers);
                        if (rs.next()) {
                            countUser = rs.getInt("count(*)");
                        }

                        // if the data is successfully inserted into the Employee table
                        if (countUser == 0) {
                            // if only chain name is entered
                            if (store.getText().isEmpty()) {
                                System.out.println("Register as chain manager");
                                String checkChain1 = "Select count(*) from manager where username = \"" + username.getText() +
                                        "\" or ChainName = \"" + chain.getText() + "\"";
                                int countChain1 = 0;
                                ResultSet resultSet1 = st.executeQuery(checkChain1);
                                if (resultSet1.next()) {
                                    countChain1 = resultSet1.getInt("count(*)");
                                }
                                System.out.println(countChain1);
                                resultSet1.close();

                                String checkChain2 = "Select count(*) from `chain` where ChainName = \"" + chain.getText() + "\"";
                                System.out.println(checkChain2);
                                int countChain2 = 0;
                                ResultSet resultSet2 = st.executeQuery(checkChain2);
                                if (resultSet2.next()) {
                                    countChain2 = resultSet2.getInt("count(*)");
                                }
                                System.out.println(countChain2);
                                resultSet2.close();
                                st.close();

                                if (countChain1 == 0 && countChain2 >= 1) {
                                    int status = registration.executeUpdate();
                                    registration.close();

                                    PreparedStatement registerManager = (PreparedStatement) connection.prepareStatement("insert into manager values (?, ?)");
                                    registerManager.setString(1, username.getText());
                                    registerManager.setString(2, chain.getText());
                                    int t = registerManager.executeUpdate();
                                    System.out.println("Successfully Registering Chain Manager for " + t + " rows");
                                    registerManager.close();

                                    ChainManager chainManager = new ChainManager(this.mainStage, this.connection, username.getText());
                                    this.mainStage.setScene(chainManager.createChainManagerHome());
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setContentText("The Chain does not exists or the Chain already has a Manager.");
                                    alert.showAndWait();
                                }
                            } else {
                                System.out.println("Register as drone technician");
                                // both chain name and store name are entered
                                String checkStore = "Select count(*) from store where StoreName = \"" + store.getText() +
                                        "\" and ChainName = \"" + chain.getText() + "\"";
                                int countStore = 0;
                                ResultSet resultSet1 = st.executeQuery(checkStore);
                                if (resultSet1.next()) {
                                    countStore = resultSet1.getInt("count(*)");
                                }
                                if (countStore >= 1) {
                                    int status = registration.executeUpdate();
                                    registration.close();

                                    PreparedStatement registerDroneTech = (PreparedStatement) connection.prepareStatement("insert into drone_tech values (?, ?, ?)");
                                    registerDroneTech.setString(1, username.getText());
                                    registerDroneTech.setString(2, store.getText());
                                    registerDroneTech.setString(3, chain.getText());
                                    int t = registerDroneTech.executeUpdate();
                                    System.out.println("Successfully Registering Drone Technician for " + t + " rows");
                                    registerDroneTech.close();

                                    DroneTech droneTech = new DroneTech(this.mainStage, this.connection, username.getText());
                                    this.mainStage.setScene(droneTech.createDroneTechHome());
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setContentText("Store and Chain Name combination does not exists.");
                                    alert.showAndWait();
                                }
                                resultSet1.close();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Username already exists in the database. Please sign in.");
                            alert.showAndWait();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));
        HBox hbox = new HBox(20);
        hbox.getChildren().add(back);

        Pane filler = new Pane();
        hbox.getChildren().add(filler);
        HBox.setHgrow(filler, Priority.ALWAYS);

        hbox.getChildren().addAll(fill, register);

        borderPane.setCenter(grid);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 800, 600);
        return scene;
    }

    private static String generateWord(int length) {
        StringBuilder source = new StringBuilder();
        // add A-Z
        for (int i = 65; i <= 90; i++) {
            source.append((char) i);
        }
        // add a-z
        for (int i = 97; i <= 122; i++) {
            source.append((char) i);
        }
        String alphabet = source.toString();

        // create an object of Random class
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i++) {
            // generate random index number
            int index = random.nextInt(alphabet.length());

            // get character specified by index
            // from the string
            char randomChar = alphabet.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }

        String returnStr = sb.toString();
        return returnStr;
    }

    private static String generateNumber(int length) {
        StringBuilder source = new StringBuilder();
        // add 0-9
        for (int i = 0; i <= 9; i++) {
            source.append(i);
        }
        String numeric = source.toString();

        // create an object of Random class
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i++) {
            // generate random index number
            int index = random.nextInt(numeric.length());

            // get character specified by index
            // from the string
            char randomChar = numeric.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }

        String returnStr = sb.toString();
        return returnStr;
    }
}

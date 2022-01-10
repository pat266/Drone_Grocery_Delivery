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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;


public class Customer {
    private String username = null;
    private String customerZip = null;
    private Connection connection = null;
    private Stage mainStage = null;
    public Customer(Stage mainStage, Connection connection, String username) {
        this.connection = connection;
        this.mainStage = mainStage;
        this.username = username;
        createZipcode();
    }

    /**
     * Find out the zipcode from Customer's username
     */
    private void createZipcode() {
        try {
            Statement st = this.connection.createStatement();
            String checkChainName = "Select Zipcode from users where Username = \"" + this.username + "\"";
            ResultSet rs = st.executeQuery(checkChainName);
            while (rs.next()) {
                this.customerZip = rs.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Scene createCustomerHome() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(40);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Customer Home");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 0, 0, 2, 1);

        // View Store Orders
        Button creditCardInfo = new Button("Change Credit Card Info");
        creditCardInfo.setWrapText(true);
        creditCardInfo.setMaxWidth(150);
        creditCardInfo.setPrefHeight(60);
        creditCardInfo.setTextAlignment(TextAlignment.CENTER);
        creditCardInfo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        creditCardInfo.setStyle("-fx-background-color: #000;");
        creditCardInfo.setTextFill(Color.WHITE);
        creditCardInfo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.setScene(changeCreditCard());
            }
        });
        grid.add(creditCardInfo, 0, 1);

        // Review Order
        Button reviewOrder = new Button("Review Order");
        reviewOrder.setWrapText(true);
        reviewOrder.setMaxWidth(150);
        reviewOrder.setPrefHeight(60);
        reviewOrder.setTextAlignment(TextAlignment.CENTER);
        reviewOrder.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        reviewOrder.setStyle("-fx-background-color: #000;");
        reviewOrder.setTextFill(Color.WHITE);
        reviewOrder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.setScene(reviewOrder());
            }
        });
        grid.add(reviewOrder, 1, 1);

        // View Order History
        Button orderHistory = new Button("View Order History");
        orderHistory.setWrapText(true);
        orderHistory.setMaxWidth(150);
        orderHistory.setPrefHeight(60);
        orderHistory.setTextAlignment(TextAlignment.CENTER);
        orderHistory.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        orderHistory.setStyle("-fx-background-color: #000;");
        orderHistory.setTextFill(Color.WHITE);
        orderHistory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.setScene(viewOrderHistory(null));
            }
        });
        grid.add(orderHistory, 0, 2);

        // View Store Items
        Button storeItems = new Button("View Store Items");
        storeItems.setWrapText(true);
        storeItems.setMaxWidth(150);
        storeItems.setPrefHeight(60);
        storeItems.setTextAlignment(TextAlignment.CENTER);
        storeItems.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        storeItems.setStyle("-fx-background-color: #000;");
        storeItems.setTextFill(Color.WHITE);
        storeItems.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.setScene(viewStoreItems(null, null, null));
            }
        });
        grid.add(storeItems, 1, 2);

        // Logout Button
        Button back = new Button("Logout");
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
                Main main = new Main(mainStage, connection);
                mainStage.setScene(main.createLoginScene());
            }
        });

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));
        HBox hbox = new HBox();
        hbox.getChildren().add(back);

        Pane filler = new Pane();
        hbox.getChildren().add(filler);
        HBox.setHgrow(filler, Priority.ALWAYS);

        borderPane.setCenter(grid);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 800, 600);
        return scene;
    }

    /**
     * Screen 13
     */
    public Scene changeCreditCard() {
        String fName = "", lName = "";
        try {
            Statement st = this.connection.createStatement();
            // check the maximum PLU Number
            String checkPLU = "select FirstName, LastName from users where Username = '" + this.username + "'";
            ResultSet rs1 = st.executeQuery(checkPLU);
            while (rs1.next()) {
                fName = rs1.getString(1);
                lName = rs1.getString(2);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // System.out.println(fName + " " + lName);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(30);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Change Credit Card Information");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 0, 0, 4, 1);

        // Username Label
        Label username = new Label("Username:");
        username.setFont(Font.font("Arial",18));
        grid.add(username, 0, 1);
        // Username (non editable)
        Text user = new Text();
        user.setText(this.username);
        user.setFont(Font.font("Arial", 16));
        user.resize(150, 35);
        grid.add(user, 1, 1);

        // First Name
        Label firstName = new Label("First Name:");
        firstName.setFont(Font.font("Arial",18));
        grid.add(firstName, 0, 2);
        // FirstName
        Text first = new Text();
        first.setText(fName);
        first.setFont(Font.font("Arial", 16));
        first.resize(150, 35);
        grid.add(first, 1, 2);

        // Last Name
        Label lastName = new Label("Last Name:");
        lastName.setFont(Font.font("Arial",18));
        grid.add(lastName, 0, 3);
        // LastName
        Text last = new Text();
        last.setText(lName);
        last.setFont(Font.font("Arial", 16));
        last.resize(150, 35);
        grid.add(last, 1, 3);

        // Credit Card Number
        Label labelCCNum = new Label("Card Number:");
        labelCCNum.setFont(Font.font("Arial",18));
        grid.add(labelCCNum, 0, 4);
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
        grid.add(ccNum, 1, 4);
        // Add the second row (CVV and Expiration Date)
        Label labelCVV = new Label("CVV:");
        labelCVV.setFont(Font.font("Arial",18));
        grid.add(labelCVV, 0, 5);
        TextField cvv = new TextField();
        cvv.setPromptText("xxx");
        cvv.setPrefSize(150, 35);
        cvv.setTextFormatter(new TextFormatter<>((change) -> {
            String text = change.getControlNewText();
            if (text.matches("\\d{0,3}")) {
                return change;
            } else {
                return null;
            }
        }));
        grid.add(cvv, 1, 5);

        Label labelExp = new Label("Exp:");
        labelExp.setFont(Font.font("Arial",18));
        grid.add(labelExp, 0, 6);
        TextField exp = new TextField();
        exp.setPromptText("mm/yy");
        exp.setPrefSize(150, 35);
        exp.setTextFormatter(new TextFormatter<>((change) -> {
            String text = change.getControlNewText();
            if (text.matches("\\d{0,2}(/\\d{0,2})?")) {
                return change;
            } else {
                return null;
            }
        }));
        grid.add(exp, 1, 6);

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
                mainStage.setScene(createCustomerHome());
            }
        });

        // Approve Button
        Button approve = new Button("Approve");
        approve.setWrapText(true);
        approve.setMaxWidth(150);
        approve.setPrefSize(100, 60);
        approve.setTextAlignment(TextAlignment.CENTER);
        approve.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        approve.setStyle("-fx-background-color: #000;");
        approve.setTextFill(Color.WHITE);
        approve.setOnAction(e -> {
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

            if (ccNum.getText().isEmpty() || cvv.getText().isEmpty() || exp.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please fill in all information.");
                alert.showAndWait();
            } else if (Integer.parseInt(expParts[0]) > 12) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please enter the month between 01-12");
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
            } else if (ccNum.getText().length() != 19) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please enter credit card in the format of xxxx xxxx xxxx xxxx");
                alert.showAndWait();
            } else if (cvv.getText().length() != 3) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please enter 3 digits of CVV number");
                alert.showAndWait();
            }
            else {
                try {
                    PreparedStatement createChainItem = (PreparedStatement) connection.prepareStatement("CALL customer_change_credit_card_information(?, ?, ?, ?)");
                    createChainItem.setString(1, this.username);
                    createChainItem.setString(2, ccNum.getText());
                    createChainItem.setString(3, cvv.getText());
                    dateParsed = dateParsed.plusDays(1);
                    createChainItem.setDate(4, Date.valueOf(dateParsed));

                    int status = createChainItem.executeUpdate();
                    if (status >= 1) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Successfully changed the credit card information!");
                        alert.showAndWait();
                        mainStage.setScene(createCustomerHome());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Failed to change credit card information.");
                        alert.showAndWait();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
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

        hbox.getChildren().addAll(approve);

        borderPane.setCenter(grid);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 800, 600);
        mainStage.setScene(scene);
        return scene;
    }

    /**
     * Screen 14
     */
    public Scene viewOrderHistory(String inputOrderID) {
        ObservableList<String> orderList = FXCollections.observableArrayList();
        try {
            Statement st = this.connection.createStatement();
            // get all of the zipcode from stores
            String checkOrder = "select ID from orders where CustomerUsername = '" + this.username + "'";
            // System.out.println(checkLocation);
            ResultSet rs = st.executeQuery(checkOrder);
            while (rs.next()) {
                String current = rs.getString(1);
                orderList.add(current);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if (orderList.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("The customer have not ordered anything.");
            alert.showAndWait();
        }

        // System.out.println(fName + " " + lName);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(30);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Customer View Order History");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 0, 0, 4, 1);

        // Username Label
        Label username = new Label("Username:");
        username.setFont(Font.font("Arial",18));
        grid.add(username, 0, 1);
        // Username (non editable)
        Text user = new Text();
        user.setText(this.username);
        user.setFont(Font.font("Arial", 16));
        user.resize(150, 35);
        grid.add(user, 1, 1);

        // Order ID
        Label firstName = new Label("Order ID:");
        firstName.setFont(Font.font("Arial",18));
        grid.add(firstName, 0, 2);
        // OrderID
        ComboBox<String> orderID = new ComboBox<>();
        orderID.setItems(orderList);
        if (inputOrderID != null) {
            orderID.setValue(inputOrderID);
        } else {
            if (orderList.size() > 0) {
                orderID.setValue(orderList.get(0));
            }
        }
        orderID.resize(150, 35);
        orderID.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                mainStage.setScene(viewOrderHistory(t1));
            }
        });
        grid.add(orderID, 1, 2);

        String totalAmount = "", totalItems = "", datePurchase = "", droneID = "", storeAssociate = "", status = "";
        String storeAssFullName = "";
        try {
            String procedure14a = "";
            if (orderID.getValue() == null) {
                procedure14a = "CALL customer_view_order_history('" + this.username+  "', " + orderID.getValue() + ")";
            } else {
                procedure14a = "CALL customer_view_order_history('" + this.username+  "', '" + orderID.getValue() + "')";
            }
            System.out.println(procedure14a);
            connection.createStatement().executeQuery(procedure14a);

            Statement st = this.connection.createStatement();
            // check the orders
            String checkViewOrder = "select * from customer_view_order_history_result";
            ResultSet rs1 = st.executeQuery(checkViewOrder);
            if (rs1.next()) {
                totalAmount = rs1.getString(1);
                totalItems = rs1.getString(2);
                datePurchase = rs1.getString(3);
                droneID = rs1.getString(4);
                storeAssociate = rs1.getString(5);
                if (rs1.wasNull()) {
                    storeAssociate = null;
                }
                status = rs1.getString(6);
            }
            if (storeAssociate != null) {
                String customProcedure = "select concat(FirstName, ' ', LastName) from users where Username = '" + storeAssociate + "'";
                ResultSet rs7 = st.executeQuery(customProcedure);
                if (rs7.next()) {
                    storeAssFullName = rs7.getString(1);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // Total Amount
        Label total = new Label("Total Amount:");
        total.setFont(Font.font("Arial",18));
        grid.add(total, 0, 3);
        // Username (non editable)
        Text amount = new Text();
        amount.setText("$ " + totalAmount);
        amount.setFont(Font.font("Arial", 16));
        amount.resize(150, 35);
        grid.add(amount, 1, 3);

        // Total Amount
        Label totalItems1 = new Label("Total Items:");
        totalItems1.setFont(Font.font("Arial",18));
        grid.add(totalItems1, 0, 4);
        // Username (non editable)
        Text items = new Text();
        items.setText(totalItems);
        items.setFont(Font.font("Arial", 16));
        items.resize(150, 35);
        grid.add(items, 1, 4);

        // Date of Purchase
        Label date = new Label("Date of Purchase:");
        date.setFont(Font.font("Arial",18));
        grid.add(date, 0, 5);
        // Date of Purchase
        Text purchase = new Text();
        purchase.setText(datePurchase);
        purchase.setFont(Font.font("Arial", 16));
        purchase.resize(150, 35);
        grid.add(purchase, 1, 5);

        // Drone ID
        Label drone = new Label("Drone ID:");
        drone.setFont(Font.font("Arial",18));
        grid.add(drone, 0, 6);
        // Username (non editable)
        Text droneIDs = new Text();
        droneIDs.setText(droneID);
        droneIDs.setFont(Font.font("Arial", 16));
        droneIDs.resize(150, 35);
        grid.add(droneIDs, 1, 6);

        // Store Associate
        Label associate = new Label("Store Associate:");
        associate.setFont(Font.font("Arial",18));
        grid.add(associate, 0, 7);
        // Username (non editable)
        Text storeAss = new Text();
        storeAss.setText(storeAssFullName);
        storeAss.setFont(Font.font("Arial", 16));
        storeAss.resize(150, 35);
        grid.add(storeAss, 1, 7);

        // Status
        Label statuss = new Label("Status:");
        statuss.setFont(Font.font("Arial",18));
        grid.add(statuss, 0, 8);
        // Username (non editable)
        Text statusss = new Text();
        statusss.setText(status);
        statusss.setFont(Font.font("Arial", 16));
        statusss.resize(150, 35);
        grid.add(statusss, 1, 8);


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
                mainStage.setScene(createCustomerHome());
            }
        });

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));
        HBox hbox = new HBox(20);
        hbox.getChildren().add(back);

        Pane filler = new Pane();
        hbox.getChildren().add(filler);
        HBox.setHgrow(filler, Priority.ALWAYS);

        borderPane.setCenter(grid);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 800, 600);
        mainStage.setScene(scene);
        return scene;
    }

    /**
     * Screen 15
     */
    public Scene viewStoreItems(String inputChain, String inputStore, String inputCategory) {
        ObservableList<String> categoryNameList = FXCollections.observableArrayList();
        ObservableList<String> chainNameList = FXCollections.observableArrayList();
        ObservableList<String> storeNameList = FXCollections.observableArrayList();
        try {
            Statement st = this.connection.createStatement();
            String checkChainName = "select distinct ChainName from store where Zipcode = \"" + this.customerZip + "\"";
            ResultSet rs1 = st.executeQuery(checkChainName);
            while (rs1.next()) {
                chainNameList.add(rs1.getString(1));
            }
            if (chainNameList.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Unfortunately, your zipcode does not match any stores.");
                alert.showAndWait();
                mainStage.setScene(createCustomerHome());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(20);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Customer View Stores");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 0, 0, 4, 1);

        // Username Label
        Label username = new Label("Username:");
        username.setFont(Font.font("Arial",18));
        grid.add(username, 0, 1);
        // Username (non editable)
        Text user = new Text(this.username);
        user.setFont(Font.font("Arial", 16));
        user.resize(150, 35);
        grid.add(user, 1, 1);

        // Chain Name Label
        Label chainLabel = new Label("Chain:");
        chainLabel.setFont(Font.font("Arial",18));
        grid.add(chainLabel, 2, 1);
        // Chain Name
        ComboBox<String> chain = new ComboBox<>();
        chain.setItems(chainNameList);
        if (inputChain != null) {
            chain.setValue(inputChain);
        } else {
            chain.setValue(chainNameList.get(0));
        }
        chain.setPrefSize(150, 35);
        chain.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                mainStage.setScene(viewStoreItems(t1, inputStore, inputCategory));
            }
        });
        grid.add(chain, 3, 1);

        try {
            Statement st = this.connection.createStatement();
            String checkCategoryList = "select distinct item.ItemType from chain_item join item where chain_item.ChainItemName = item.ItemName and chain_item.ChainName = '" + chain.getValue() + "'";
            ResultSet rs = st.executeQuery(checkCategoryList);
            categoryNameList.add(null);
            while (rs.next()) {
                categoryNameList.add(rs.getString(1));
            }

            String checkStoreName = "select distinct StoreName from store where Zipcode = \"" + this.customerZip + "\" and ChainName = \"" + chain.getValue() + "\"";
            ResultSet rs2 = st.executeQuery(checkStoreName);
            while (rs2.next()) {
                storeNameList.add(rs2.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // Store Label
        Label storeLabel = new Label("Store:");
        storeLabel.setFont(Font.font("Arial",18));
        grid.add(storeLabel, 2, 2);
        // Store
        ComboBox<String> store = new ComboBox<>();
        store.setItems(storeNameList);
        if (inputStore != null) {
            store.setValue(inputStore);
        } else {
            store.setValue(storeNameList.get(0));
        }
        store.setPrefSize(150, 35);
        store.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                mainStage.setScene(viewStoreItems(inputChain, t1, inputCategory));
            }
        });
        grid.add(store, 3, 2);

        // Category Label
        Label categoryLabel = new Label("Category:");
        categoryLabel.setFont(Font.font("Arial",18));
        grid.add(categoryLabel, 0, 2);
        // Category
        ComboBox<String> category = new ComboBox<>();
        category.setItems(categoryNameList);
        if (inputCategory != null) {
            category.setValue(inputCategory);
        } else {
            category.setValue(categoryNameList.get(0));
        }
        category.setPrefSize(150, 35);
        category.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                mainStage.setScene(viewStoreItems(inputChain, inputStore, t1));
            }
        });
        grid.add(category, 1, 2);

        // Create TableView Node:
        TableView<Screen15> tableView = new TableView();

        // Column of the Table
        TableColumn items = new TableColumn("Items");
        TableColumn orderLimit = new TableColumn("Quantity");
        tableView.getColumns().addAll(items, orderLimit);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        items.setCellValueFactory(new PropertyValueFactory<Screen15, String>("itemName"));
        orderLimit.setCellValueFactory(new PropertyValueFactory<Screen15, String>("quantity"));

        // ObservableList to hold the data, of class screen10
        ObservableList<Screen15> data = FXCollections.observableArrayList();
        // Call the 15a Procedure
        try {
            String procedure15a = "";
            if (category.getValue() == null) {
                procedure15a = "CALL customer_view_store_items('" + this.username + "', '" + chain.getValue() + "', '" + store.getValue() + "', 'All')";
            } else {
                procedure15a = "CALL customer_view_store_items('" + this.username + "', '" + chain.getValue() + "', '" + store.getValue() + "', '" + category.getValue() + "')";
            }
            System.out.println(procedure15a);
            // Execute Procedure 15a
            connection.createStatement().executeQuery(procedure15a);

            // SQL FOR SELECTING THE TABLE
            String SQL = "SELECT * from customer_view_store_items_result";
            //ResultSet
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            while (rs.next()) {
                String tempItems = rs.getString(1);
                String tempOrderLimit = rs.getString(2);
                data.add(new Screen15(tempItems, tempOrderLimit));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
        tableView.setItems(data);
        grid.add(tableView, 0, 3, 4, 1);

        // Back Button
        Button back = new Button("Cancel Order");
        back.setWrapText(true);
        back.setMaxWidth(150);
        back.setPrefSize(130, 60);
        back.setTextAlignment(TextAlignment.CENTER);
        back.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        back.setStyle("-fx-background-color: #000;");
        back.setTextFill(Color.WHITE);
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.setScene(createCustomerHome());
            }
        });

        // Approve Button
        Button approve = new Button("Place Order");
        approve.setWrapText(true);
        approve.setMaxWidth(150);
        approve.setPrefSize(130, 60);
        approve.setTextAlignment(TextAlignment.CENTER);
        approve.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        approve.setStyle("-fx-background-color: #000;");
        approve.setTextFill(Color.WHITE);
        approve.setOnAction(e -> {
            boolean success = false;
            for (int i = 0; i < tableView.getItems().size(); i++) {
                String tempItemsName = (String) (items.getCellObservableValue(i).getValue());
                ComboBox tempOrder = (ComboBox) (orderLimit.getCellObservableValue(i).getValue());
                String tempOrderLimit = (String) (tempOrder.getValue());
                System.out.println(tempItemsName + " " + tempOrderLimit);
                try {
                    // procedure 15b
                    PreparedStatement createDrone = (PreparedStatement) connection.prepareStatement("CALL customer_select_items(?, ?, ?, ?, ?)");
                    createDrone.setString(1, this.username);
                    createDrone.setString(2, chain.getValue());
                    createDrone.setString(3, store.getValue());
                    createDrone.setString(4, tempItemsName);
                    createDrone.setString(5, tempOrderLimit);

                    int status1 = createDrone.executeUpdate();
                    if (status1 >= 1) {
                        success = true;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (success) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Successfully created the order.");
                alert.showAndWait();
                mainStage.setScene(createCustomerHome());
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Could not create place order.");
                alert.showAndWait();
            }
            mainStage.setScene(reviewOrder());
        });

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));
        HBox hbox = new HBox(20);
        hbox.getChildren().add(back);

        Pane filler = new Pane();
        hbox.getChildren().add(filler);
        HBox.setHgrow(filler, Priority.ALWAYS);

        hbox.getChildren().addAll(approve);

        borderPane.setCenter(grid);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 800, 600);
        mainStage.setScene(scene);
        return scene;
    }

    /**
     * Screen 16
     */
    public Scene reviewOrder() {
        ObservableList<Screen16> data = FXCollections.observableArrayList();
        double totalAmount = 0;
        String storeName = "", chainName = "";
        try {
            String procedure16a = "";
            procedure16a = "CALL customer_review_order('" + this.username + "')";
            System.out.println(procedure16a);
            // Execute Procedure 15a
            connection.createStatement().executeQuery(procedure16a);

            // SQL FOR SELECTING THE TABLE
            String SQL = "SELECT * from customer_review_order_result";
            //ResultSet
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            while (rs.next()) {
                // String itemName, String currentQuantity, String maxQuantity, double unitCost
                String tempItems = rs.getString(1);
                String maxQuantity = rs.getString(2);
                String tempQuantity = rs.getString(3);
                String tempPrice = rs.getString(4);
                storeName = rs.getString(5);
                chainName = rs.getString(6);
                data.add(new Screen16(tempItems, tempQuantity, maxQuantity, tempPrice));
                totalAmount += (Double.parseDouble(tempQuantity) * Double.parseDouble(tempPrice));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if (data.size() == 0) {
            // mainStage.setScene(viewStoreItems(null, null, null));
            // mainStage.setScene(createCustomerHome());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please create an order before reviewing.");
            alert.showAndWait();
        }

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(20);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Customer Review Order");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 0, 0, 4, 1);

        // Chain Label
        Label chainLabel = new Label("Chain:");
        chainLabel.setFont(Font.font("Arial",18));
        grid.add(chainLabel, 0, 1);
        // Chain Name (non editable)
        Text chainN = new Text(chainName);
        chainN.setFont(Font.font("Arial", 16));
        chainN.resize(150, 35);
        grid.add(chainN, 1, 1);

        // Store Label
        Label storeLabel = new Label("Chain:");
        storeLabel.setFont(Font.font("Arial",18));
        grid.add(storeLabel, 2, 1);
        // Store Name (non editable)
        Text storeN = new Text(storeName);
        storeN.setFont(Font.font("Arial", 16));
        storeN.resize(150, 35);
        grid.add(storeN, 3, 1);

        // Total Label
        Label total = new Label("Total:");
        total.setFont(Font.font("Arial",18));
        grid.add(total, 2, 2);
        // Total Price (non editable)
        Formatter formatter = new Formatter();
        formatter.format("%.2f", totalAmount);
        String totalPrice1 = (formatter.toString());
        Text totalPrice = new Text("$" + totalPrice1);
        totalPrice.setFont(Font.font("Arial", 16));
        totalPrice.resize(150, 35);
        grid.add(totalPrice, 3, 2);

        // Create TableView Node:
        TableView<Screen16> tableView = new TableView();

        // Column of the Table
        TableColumn items = new TableColumn("Items");
        TableColumn orderLimit = new TableColumn("Quantity");
        TableColumn unitCost = new TableColumn("Unit Cost");
        tableView.getColumns().addAll(items, orderLimit, unitCost);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        items.setCellValueFactory(new PropertyValueFactory<Screen15, String>("itemName"));
        orderLimit.setCellValueFactory(new PropertyValueFactory<Screen15, String>("quantity"));
        unitCost.setCellValueFactory(new PropertyValueFactory<Screen15, String>("unitCost"));


        tableView.setItems(data);
        grid.add(tableView, 0, 3, 4, 1);

        // Cancel Order Button
        Button back = new Button("Cancel Order");
        back.setWrapText(true);
        back.setMaxWidth(150);
        back.setPrefSize(130, 60);
        back.setTextAlignment(TextAlignment.CENTER);
        back.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        back.setStyle("-fx-background-color: #000;");
        back.setTextFill(Color.WHITE);
        back.setOnAction(e -> {
            String OrderID = "";
            try {
                // SQL to get the OrderID
                String getOrderID = "select ID from orders where CustomerUsername = '" + this.username + "' and OrderStatus = 'Creating'";
                //ResultSet
                ResultSet rs4 = connection.createStatement().executeQuery(getOrderID);
                while (rs4.next()) {
                    // String itemName, String currentQuantity, String maxQuantity, double unitCost
                    OrderID = rs4.getString(1);
                }
                String removeOrder = "CALL remove_order('" + OrderID + "')";
                PreparedStatement createDrone = (PreparedStatement) connection.prepareStatement("CALL remove_order(?)");
                createDrone.setString(1, OrderID);

                if (!OrderID.equals("")) {
                    int status6 = createDrone.executeUpdate();
                    if (status6 >= 1) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Successfully cancel the Order with ID of " + OrderID + "!");
                        alert.showAndWait();
                        mainStage.setScene(createCustomerHome());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Something went wrong. Cannot cancel the Order with the ID of " + OrderID);
                        alert.showAndWait();
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            mainStage.setScene(createCustomerHome());
        });

        // Approve Button
        Button approve = new Button("Place Order");
        approve.setWrapText(true);
        approve.setMaxWidth(150);
        approve.setPrefSize(130, 60);
        approve.setTextAlignment(TextAlignment.CENTER);
        approve.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        approve.setStyle("-fx-background-color: #000;");
        approve.setTextFill(Color.WHITE);
        approve.setOnAction(e -> {
            boolean success = false;
            for (int i = 0; i < tableView.getItems().size(); i++) {
                try {
                    PreparedStatement updateOrder = connection.prepareStatement("CALL customer_update_order(?, ?, ?)");
                    String tempItemN = (String) (items.getCellObservableValue(i).getValue());
                    ComboBox tempLimitBox = (ComboBox) (orderLimit.getCellObservableValue(i).getValue());
                    String tempLimit = (String) (tempLimitBox.getValue());
                    updateOrder.setString(1, this.username);
                    updateOrder.setString(2, tempItemN);
                    updateOrder.setString(3, tempLimit);
                    updateOrder.executeUpdate();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            try {
                // custom procedure (after 16b)
                PreparedStatement createDrone = connection.prepareStatement("CALL customer_place_order(?)");
                createDrone.setString(1, this.username);
                int status = createDrone.executeUpdate();
                if (status >= 1) {
                    success = true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (success) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Successfully Place the Order!");
                alert.showAndWait();
                mainStage.setScene(createCustomerHome());
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Could not create place order.");
                alert.showAndWait();
            }
        });

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));
        HBox hbox = new HBox(20);
        hbox.getChildren().add(back);

        Pane filler = new Pane();
        hbox.getChildren().add(filler);
        HBox.setHgrow(filler, Priority.ALWAYS);

        hbox.getChildren().addAll(approve);

        borderPane.setCenter(grid);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 800, 600);
        // mainStage.setScene(scene);
        return scene;
    }

    /**
     * Chain Manager View Drone Technicians
     */
    public class Screen15 {
        private String itemName;
        private ComboBox<String> quantity;
        // private String initialLocation;

        public Screen15(String itemName, String maxQuantity) {
            this.itemName = itemName;
            ObservableList<String> range = FXCollections.observableArrayList();
            for (int i = 0; i <= Integer.parseInt(maxQuantity); i++) {
                range.add(Integer.toString(i));
            }
            this.quantity = new ComboBox<>(range);
            this.quantity.setValue(range.get(0));
            this.quantity.setPrefWidth(50);
            // this.initialLocation = initialLocation;
        }
        public String getItemName() { return itemName; }
        public ComboBox getQuantity() { return quantity; }

        public void setItemName(String itemName) { this.itemName = itemName; }
        public void setQuantity(ComboBox quantity) { this.quantity = quantity; }
    }

    /**
     * Chain Manager View Drone Technicians
     */
    public class Screen16 {
        private String itemName;
        private ComboBox<String> quantity;
        private String unitCost;
        // private String initialLocation;

        public Screen16 (String itemName, String currentQuantity, String maxQuantity, String unitCost) {
            this.itemName = itemName;
            ObservableList<String> range = FXCollections.observableArrayList();
            for (int i = 0; i <= Integer.parseInt(maxQuantity); i++) {
                range.add(Integer.toString(i));
            }
            this.quantity = new ComboBox<>(range);
            this.quantity.setValue(currentQuantity);
            this.quantity.setPrefWidth(50);
            this.unitCost = unitCost;
            // this.initialLocation = initialLocation;
        }
        public String getItemName() { return itemName; }
        public ComboBox getQuantity() { return quantity; }
        public String getUnitCost() { return unitCost; }

        public void setItemName(String itemName) { this.itemName = itemName; }
        public void setQuantity(ComboBox quantity) { this.quantity = quantity; }
    }
}

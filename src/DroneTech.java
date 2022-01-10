import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;
import javafx.util.Callback;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DroneTech {
    private String username = null;
    private Connection connection = null;
    private Stage mainStage = null;
    public DroneTech(Stage mainStage, Connection connection, String username) {
        this.connection = connection;
        this.mainStage = mainStage;
        this.username = username;
    }

    /**
     * A method to check if a string is a valid date
     * @param dateString
     * @return
     */
    public boolean isValidDate(String dateString) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        try {
            df.parse(dateString);
            LocalDate.parse(dateString, df);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Scene createDroneTechHome() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(40);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Drone Technician Home");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 0, 0, 2, 1);

        // View Store Orders
        Button storeOrders = new Button("View Store Orders");
        storeOrders.setWrapText(true);
        storeOrders.setMaxWidth(150);
        storeOrders.setPrefHeight(60);
        storeOrders.setTextAlignment(TextAlignment.CENTER);
        storeOrders.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        storeOrders.setStyle("-fx-background-color: #000;");
        storeOrders.setTextFill(Color.WHITE);
        storeOrders.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.setScene(viewStoreOrder(null, null));
            }
        });
        grid.add(storeOrders, 0, 1);

        // Track Drone Delivery
        Button droneDelivery = new Button("Track Drone Delivery");
        droneDelivery.setWrapText(true);
        droneDelivery.setMaxWidth(150);
        droneDelivery.setPrefHeight(60);
        droneDelivery.setTextAlignment(TextAlignment.CENTER);
        droneDelivery.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        droneDelivery.setStyle("-fx-background-color: #000;");
        droneDelivery.setTextFill(Color.WHITE);
        droneDelivery.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.setScene(trackAssignedDrone(null, null));
            }
        });
        grid.add(droneDelivery, 1, 1);

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
     * Screen 17
     */
    public Scene viewStoreOrder(String inputBeginDate, String inputEndDate) {
        System.out.println(inputBeginDate + " " + inputEndDate);
        String currentFullName = "";
        ObservableList<Screen17> data = FXCollections.observableArrayList();
        try {
            // SQL FOR getting the list of available drone
            String SQL2 = "select CONCAT(FirstName, ' ', LastName) from users where username = '" + this.username + "'";
            //ResultSet
            ResultSet rs2 = connection.createStatement().executeQuery(SQL2);
            if (rs2.next()) {
                currentFullName = rs2.getString(1);
            }

            ObservableList<String> availableDrone = FXCollections.observableArrayList();
            String procedure17a = "";
            if (inputBeginDate == null && inputEndDate == null) {
                procedure17a = "CALL drone_technician_view_order_history('" + this.username + "', " + inputBeginDate + ", " + inputEndDate + ")";
            } else if (inputBeginDate == null) {
                procedure17a = "CALL drone_technician_view_order_history('" + this.username + "', " + inputBeginDate + ", '" + inputEndDate + "')";
            } else if (inputEndDate == null) {
                procedure17a = "CALL drone_technician_view_order_history('" + this.username + "', '" + inputBeginDate + "', " + inputEndDate + ")";
            } else {
                procedure17a = "CALL drone_technician_view_order_history('" + this.username + "', '" + inputBeginDate + "', '" + inputEndDate + "')";
            }
            // procedure17a = "CALL drone_technician_view_order_history('" + this.username + "', '" + inputBeginDate + "', '" + inputEndDate +"')";
            System.out.println(procedure17a);
            // Execute Procedure 17a
            connection.createStatement().executeQuery(procedure17a);

            String procedure19a = "CALL dronetech_assigned_drones('" + this.username+  "', " + null + ", 'Available')";
            connection.createStatement().executeQuery(procedure19a);

            // SQL FOR getting the list of available drone
            String SQL1 = "SELECT * from dronetech_assigned_drones_result";
            //ResultSet
            ResultSet rs1 = connection.createStatement().executeQuery(SQL1);
            while (rs1.next()) {
                availableDrone.add(rs1.getString(1));
            }

            // SQL FOR SELECTING THE TABLE
            String SQL = "SELECT * from drone_technician_view_order_history_result";
            //ResultSet
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            while (rs.next()) {
                // String itemName, String currentQuantity, String maxQuantity, double unitCost
                String tempID = rs.getString(1);
                String tempOperator = rs.getString(2);
                if (rs.wasNull()) {
                    tempOperator = currentFullName;
                }
                String tempDate = rs.getString(3);
                String tempDroneID = rs.getString(4);
                if (rs.wasNull()) {
                    tempDroneID = null;
                }
                String tempStatus = rs.getString(5);
                String tempTotal = rs.getString(6);
                data.add(new Screen17 (tempID, tempOperator, tempDate, tempDroneID, tempStatus, tempTotal, availableDrone, currentFullName,false, connection));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(20);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("View Store Orders");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 1, 0, 3, 1);

        // Dates Label
        Label dateLabel = new Label("Dates:");
        dateLabel.setFont(Font.font("Arial",18));
        grid.add(dateLabel, 0, 1);
        // Begin Date
        TextField beginDate = new TextField();
        beginDate.setPromptText("Begin Date (yyyy/mm/dd)");
        beginDate.setPrefSize(150, 35);
        if (inputBeginDate != null) {
            beginDate.setText(inputBeginDate);
        }
        grid.add(beginDate, 1, 1);
        // Dash
        Text dash = new Text("-");
        dash.setFont(Font.font("Arial", 16));
        grid.add(dash, 2, 1);
        // End Date
        TextField endDate = new TextField();
        endDate.setPromptText("End Date (yyyy/mm/dd)");
        endDate.setPrefSize(150, 35);
        if (inputEndDate != null) {
            endDate.setText(inputEndDate);
        }
        grid.add(endDate, 3, 1);


        // Create TableView Node:
        TableView<Screen17> tableView = new TableView();

        // Column of the Table
        TableColumn ID = new TableColumn("ID");
        TableColumn operator = new TableColumn("Operator");
        TableColumn date = new TableColumn("Date");
        TableColumn droneID = new TableColumn("Drone ID");
        TableColumn status = new TableColumn("Status");
        TableColumn total = new TableColumn("Total");
        TableColumn radioButton = new TableColumn();
        // radioButton.setPrefWidth(15);

        tableView.getColumns().addAll(ID, operator, date, droneID, status, total, radioButton);
        tableView.setItems(data);
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        operator.setCellValueFactory(new PropertyValueFactory<>("operator"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        droneID.setCellValueFactory(new PropertyValueFactory<>("droneID"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));
        radioButton.setCellValueFactory(new PropertyValueFactory<>("clientHasRequestedFax"));
        radioButton.setCellFactory(CheckBoxTableCell.forTableColumn(radioButton));

        ArrayList<Integer> arrayList = new ArrayList<>();
        // add listeners to boolean properties:
        for (Screen17 tempVal : data) {
            tempVal.clientHasRequestedFaxProperty().addListener((obs, faxWasRequested, faxIsNowRequested) ->{
                System.out.println(tempVal.getClientHasRequestedFax());
                if (tempVal.getClientHasRequestedFax() == true) {
                    if (arrayList.size() == 1) {
                        if (arrayList.get(0) == data.indexOf(tempVal)) {
                            // doing nothing
                        }
                        else {
                            data.get(arrayList.get(0)).setClientHasRequestedFax(false);
                            arrayList.remove(0);
                        }
                    }
                    if (arrayList.size() == 0) {
                        arrayList.add(data.indexOf(tempVal));
                    }
                }
            });
        }

        // Back Button
        Button back = new Button("Back");
        back.setWrapText(true);
        back.setMaxWidth(150);
        back.setPrefSize(130, 60);
        back.setTextAlignment(TextAlignment.CENTER);
        back.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        back.setStyle("-fx-background-color: #000;");
        back.setTextFill(Color.WHITE);
        back.setOnAction(e -> {
            mainStage.setScene(createDroneTechHome());
        });

        // Reset Button
        Button reset = new Button("Reset");
        reset.setWrapText(true);
        reset.setMaxWidth(150);
        reset.setPrefSize(130, 60);
        reset.setTextAlignment(TextAlignment.CENTER);
        reset.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        reset.setStyle("-fx-background-color: #000;");
        reset.setTextFill(Color.WHITE);
        reset.setOnAction(e -> {
            beginDate.setText("");
            endDate.setText("");
        });

        // Filter button
        Button filter = new Button("Filter");
        filter.setWrapText(true);
        filter.setMaxWidth(150);
        filter.setPrefSize(130, 60);
        filter.setTextAlignment(TextAlignment.CENTER);
        filter.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        filter.setStyle("-fx-background-color: #000;");
        filter.setTextFill(Color.WHITE);
        filter.setOnAction(e -> {
            if (!beginDate.getText().isEmpty() && !isValidDate(beginDate.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please enter a valid date in the format of yyyy/mm/dd.");
                alert.showAndWait();
            } else if (!endDate.getText().isEmpty() && !isValidDate(endDate.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please enter a valid date in the format of yyyy/mm/dd.");
                alert.showAndWait();
            } else {
                System.out.println(beginDate.getText().isEmpty() + " " + endDate.getText().isEmpty());
                // mainStage.setScene(viewStoreOrder());
                if (beginDate.getText().isEmpty() && endDate.getText().isEmpty()) {
                    mainStage.setScene(viewStoreOrder(null, null));
                } else if (beginDate.getText().isEmpty()) {
                    // System.out.println("hello");
                    mainStage.setScene(viewStoreOrder(null, endDate.getText()));
                } else if (endDate.getText().isEmpty()) {
                    mainStage.setScene(viewStoreOrder(beginDate.getText(), null));
                } else {
                    mainStage.setScene(viewStoreOrder(beginDate.getText(), endDate.getText()));
                }
            }
        });

        // View Order Button
        Button viewOrder = new Button("View Order Details");
        viewOrder.setWrapText(true);
        viewOrder.setMaxWidth(150);
        viewOrder.setPrefSize(350, 60);
        viewOrder.setTextAlignment(TextAlignment.CENTER);
        viewOrder.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        viewOrder.setStyle("-fx-background-color: #000;");
        viewOrder.setTextFill(Color.WHITE);
        viewOrder.setOnAction(e -> {
            if (arrayList.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please select a row.");
                alert.showAndWait();
            } else {
                Screen17 tempVal = data.get(arrayList.get(0));
                this.mainStage.setScene(viewOrderDetails(tempVal.getOrderID()));
            }

        });

        // Save Order Button
        Button save = new Button("Save");
        save.setWrapText(true);
        save.setMaxWidth(150);
        save.setPrefSize(100, 60);
        save.setTextAlignment(TextAlignment.CENTER);
        save.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        save.setStyle("-fx-background-color: #000;");
        save.setTextFill(Color.WHITE);
        save.setOnAction(e -> {
            if (arrayList.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please select a row.");
                alert.showAndWait();
            } else {
                Screen17 tempValue = data.get(arrayList.get(0));
//                if (!String.valueOf(tempValue.getStatus().getValue()).equalsIgnoreCase("Pending")) {
//                    Alert alert = new Alert(Alert.AlertType.WARNING);
//                    alert.setContentText("Can only save the Order with Pending status.");
//                    alert.showAndWait();
//                } else
                if (tempValue.getDroneID().getValue() == null || tempValue.getDroneID().getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Please select an operator and drone.");
                    alert.showAndWait();
                } else {
                    try {
                        // procedure 17b
                        PreparedStatement createDrone = (PreparedStatement) connection.prepareStatement("CALL dronetech_assign_order(?, ?, ?, ?)");
                        createDrone.setString(1, this.username);
                        createDrone.setString(2, (String) tempValue.getDroneID().getValue());
                        createDrone.setString(3, (String) tempValue.getStatus().getValue());
                        createDrone.setString(4, tempValue.getOrderID());

                        int status5 = createDrone.executeUpdate();
                        if (status5 >= 1) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setContentText("Successfully save!");
                            alert.showAndWait();
                            mainStage.setScene(viewStoreOrder(inputBeginDate, inputEndDate));
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Failed to save.");
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

        // Pane filler = new Pane();
        // hbox.getChildren().add(filler);
        // HBox.setHgrow(filler, Priority.ALWAYS);

        hbox.getChildren().addAll(reset, filter, viewOrder, save);

//        borderPane.setCenter(grid);
        borderPane.setTop(grid);
        borderPane.setCenter(tableView);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 800, 600);
        // mainStage.setScene(scene);
        return scene;
    }

    /**
     * Screen 18
     */
    public Scene viewOrderDetails(String inputOrderID) {
        System.out.println(inputOrderID);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(30);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("View Order Details");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 0, 0, 4, 1);

        String customerName = "", orderID = "",
                totalAmount = "", totalItems = "",
                datePurchase = "", droneID = "",
                storeAssociate = "", status = "",
                address = "";
        try {
            String procedure18a = "CALL dronetech_order_details('" + this.username + "', '" + inputOrderID + "')";
            System.out.println(procedure18a);
            // Execute Procedure 18a
            connection.createStatement().executeQuery(procedure18a);

            Statement st = this.connection.createStatement();
            // check the orders (table 18a)
            String checkViewOrder = "select * from dronetech_order_details_result";
            ResultSet rs1 = st.executeQuery(checkViewOrder);
            if (rs1.next()) {
                customerName = rs1.getString(1);
                orderID = rs1.getString(2);
                totalAmount = rs1.getString(3);
                totalItems = rs1.getString(4);
                datePurchase = rs1.getString(5);
                droneID = rs1.getString(6);
                storeAssociate = rs1.getString(7);
                status = rs1.getString(8);
                address = rs1.getString(9);


            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        // Customer Name Label
        Label customerNameLabel = new Label("Customer Name:");
        customerNameLabel.setFont(Font.font("Arial",18));
        grid.add(customerNameLabel, 0, 1);
        // customer (non editable)
        Text customer = new Text();
        customer.setText(customerName);
        customer.setFont(Font.font("Arial", 16));
        customer.resize(150, 35);
        grid.add(customer, 1, 1);

        // Order ID
        Label orderIDLabel = new Label("Order ID:");
        orderIDLabel.setFont(Font.font("Arial",18));
        grid.add(orderIDLabel, 0, 2);
        // Username (non editable)
        Text order = new Text();
        order.setText(orderID);
        order.setFont(Font.font("Arial", 16));
        order.resize(150, 35);
        grid.add(order, 1, 2);

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

        // Total Items
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
        date.setFont(Font.font("Arial",16));
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
        storeAss.setText(storeAssociate);
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


        // Address
        Label addressLabel = new Label("Address:");
        addressLabel.setFont(Font.font("Arial",18));
        grid.add(addressLabel, 0, 9);
        // Username (non editable)
        Text addresss = new Text();
        addresss.setText(address);
        addresss.setFont(Font.font("Arial", 16));
        addresss.resize(150, 35);
        grid.add(addresss, 1, 9);

//        // Items
//        Label itemsLabel = new Label("Items:");
//        itemsLabel.setFont(Font.font("Arial",18));
//        grid.add(itemsLabel, 2, 3, 2, 1);

        // MUST ADD TABLE HERE ******
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        TableView<ObservableList> tableview = new TableView();
        tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        try {
            String procedure18b = "CALL dronetech_order_items('" + this.username + "', '" + inputOrderID + "')";
            System.out.println(procedure18b);
            // Execute Procedure 18a
            connection.createStatement().executeQuery(procedure18b);

            // SQL FOR SELECTING THE TABLE
            String SQL = "SELECT * from dronetech_order_items_result";
            //ResultSet
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            /**********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             **********************************/
            for (int i=0 ; i<rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                tableview.getColumns().add(col);
                // System.out.println("Column ["+i+"] ");
            }

            /********************************
             * Data added to ObservableList *
             ********************************/
            while(rs.next()) {
                //Iterate Row
                // ObservableList<String> row = FXCollections.observableArrayList();
                ObservableList<Object> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
            //FINALLY ADDED TO TableView
            tableview.setItems(data);
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
        // grid.add(tableview, 2, 4, 2, 6);

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
                mainStage.setScene(createDroneTechHome());
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
        borderPane.setRight(tableview);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 800, 600);
        mainStage.setScene(scene);
        return scene;
    }

    /**
     * Screen 19
     */
    public Scene trackAssignedDrone(String inputDroneID, String inputStatus) {
        ObservableList<String> status = FXCollections.observableArrayList();
        status.addAll(null, "Busy", "Available");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(40);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("My Assigned Drones");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 0, 0, 3, 1);

        // Drone ID
        Label droneID = new Label("Drone ID:");
        droneID.setFont(Font.font("Arial",18));
        grid.add(droneID, 0, 1);
        // Chain name (non editable)
        TextField id = new TextField();
        id.setPromptText("Enter Drone ID");
        id.setPrefSize(150, 35);
        if (inputDroneID != null) {
            id.setText(inputDroneID);
        }
        grid.add(id, 1, 1);

        // Status
        Label status1 = new Label("Status:");
        status1.setFont(Font.font("Arial",18));
        grid.add(status1, 2, 1);
        // Chain name (non editable)
        ComboBox<String> status2 = new ComboBox<>();
        status2.setItems(status);
        if (inputStatus != null) {
            status2.setValue(inputStatus);
        }
        status2.setPrefSize(150, 35);
        grid.add(status2, 3, 1);

        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        TableView<ObservableList> tableview = new TableView();
        tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        try {
            // SQL FOR CALLING THE 12a PROCEDURE
            String procedure19a = "";
            if (id.getText().isEmpty() && status2.getValue() == null) {
                procedure19a = "CALL dronetech_assigned_drones('" + this.username+  "', null, null)";
            } else if (id.getText().isEmpty()) {
                procedure19a = "CALL dronetech_assigned_drones('" + this.username+  "', " + null + ", '" + status2.getValue() + "')";
            } else if (status2.getValue() == null) {
                procedure19a = "CALL dronetech_assigned_drones('" + this.username+  "', '" + id.getText() + "', " + null + ")";
            } else {
                procedure19a = "CALL dronetech_assigned_drones('" + this.username+  "', '" + id.getText() + "', '" + status2.getValue() + "')";
            }
            System.out.println(procedure19a);
            connection.createStatement().executeQuery(procedure19a);

            // SQL FOR SELECTING THE TABLE
            String SQL = "SELECT * from dronetech_assigned_drones_result";
            //ResultSet
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            /**********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             **********************************/
            for (int i=0 ; i<rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                tableview.getColumns().add(col);
                // System.out.println("Column ["+i+"] ");
            }

            /********************************
             * Data added to ObservableList *
             ********************************/
            while(rs.next()) {
                //Iterate Row
                // ObservableList<String> row = FXCollections.observableArrayList();
                ObservableList<Object> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
            //FINALLY ADDED TO TableView
            tableview.setItems(data);
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
        grid.add(tableview, 0, 3, 5, 1);


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
                mainStage.setScene(createDroneTechHome());
            }
        });

        // Reset Button
        Button reset = new Button("Reset");
        reset.setWrapText(true);
        reset.setMaxWidth(150);
        reset.setPrefSize(100, 60);
        reset.setTextAlignment(TextAlignment.CENTER);
        reset.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        reset.setStyle("-fx-background-color: #000;");
        reset.setTextFill(Color.WHITE);
        reset.setOnAction(e -> {
            id.setText("");
            status2.setValue(null);
        });

        // Filter Button
        Button filter = new Button("Filter");
        filter.setWrapText(true);
        filter.setMaxWidth(150);
        filter.setPrefSize(100, 60);
        filter.setTextAlignment(TextAlignment.CENTER);
        filter.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        filter.setStyle("-fx-background-color: #000;");
        filter.setTextFill(Color.WHITE);
        filter.setOnAction(e -> {
            if (id.getText().isEmpty() && status2.getValue() == null) {
                System.out.println(1);
                mainStage.setScene(trackAssignedDrone(null, null));
            } else if (id.getText().isEmpty()) {
                System.out.println(2);
                mainStage.setScene(trackAssignedDrone(null, status2.getValue()));
            } else if (status2.getValue() == null) {
                System.out.println(3);
                System.out.println(id.getText());
                mainStage.setScene(trackAssignedDrone(id.getText(), status2.getValue()));
            } else {
                System.out.println(4);
                mainStage.setScene(trackAssignedDrone(id.getText(), status2.getValue()));
            }
        });

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));
        HBox hbox = new HBox(20);
        hbox.getChildren().add(back);

        Pane filler = new Pane();
        hbox.getChildren().add(filler);
        HBox.setHgrow(filler, Priority.ALWAYS);

        hbox.getChildren().addAll(reset, filter);

        borderPane.setCenter(grid);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 800, 600);
        mainStage.setScene(scene);
        return scene;
    }

    public class Screen17 {
        private Connection connection;
        private String orderID;
        private ComboBox<String> operator;
        private String date;
        private ComboBox<String> droneID;
        private ComboBox<String> status;
        private String total;
        private BooleanProperty clientHasRequestedFax;

        public Screen17(String orderID, String fullName, String date, String droneNum,
                        String status, String total, ObservableList availableDrone, String currentFullName, boolean clientHasRequestedFax, Connection connection) {
            // System.out.println(fullName + " " + currentFullName);
            this.connection = connection;

            this.orderID = orderID;

            operator = new ComboBox<>();
            ObservableList<String> droneTech = FXCollections.observableArrayList();
            if (fullName.equalsIgnoreCase(currentFullName)) {
                if (droneNum != null) {
                    droneTech.addAll(fullName);
                    operator.setItems(droneTech);
                    operator.setValue(fullName);
                } else {
                    droneTech.addAll(null, fullName);
                    operator.setItems(droneTech);
                    operator.setValue(null);
                }

            } else {
                droneTech.addAll(fullName);
                operator.setItems(droneTech);
                operator.setValue(fullName);
            }

            this.date = date;

            if (droneNum == null) {
                availableDrone.add(null);
                droneID = new ComboBox<>();
                droneID.setItems(availableDrone);
                droneID.setValue(null);
            } else {
                ObservableList<String> obs = FXCollections.observableArrayList();
                obs.add(droneNum);
                droneID = new ComboBox<>();
                droneID.setItems(obs);
                droneID.setValue(droneNum);
            }

            this.status = new ComboBox<>();
//            if (status.equalsIgnoreCase("Pending")) {
//                ObservableList<String> statuses = FXCollections.observableArrayList();
//                statuses.addAll("Pending", "Drone Assigned", "In Transit", "Delivered");
//                this.status.setItems(statuses);
//                this.status.setValue(status);
//            } else {
//                ObservableList<String> statuses = FXCollections.observableArrayList();
//                statuses.addAll(status);
//                this.status.setItems(statuses);
//                this.status.setValue(status);
//                // this.status.setDisable(true);
//            }

            if (fullName.equalsIgnoreCase(currentFullName)) {
                ObservableList<String> statuses = FXCollections.observableArrayList();
                statuses.addAll("Pending", "Drone Assigned", "In Transit", "Delivered");
                this.status.setItems(statuses);
                this.status.setValue(status);
            } else {
                ObservableList<String> statuses = FXCollections.observableArrayList();
                statuses.addAll(status);
                this.status.setItems(statuses);
                this.status.setValue(status);
                // this.status.setDisable(true);
            }

            this.total = total;
            this.clientHasRequestedFax = new SimpleBooleanProperty(clientHasRequestedFax);
        }

        /**
         * Getter methods
         */
        public String getOrderID() { return orderID; }

        public ComboBox getOperator() { return operator; }

        public String getDate() { return date; }

        public ComboBox getDroneID() { return droneID; }

        public ComboBox getStatus() { return status; }

        public String getTotal() { return total; }

        public boolean getClientHasRequestedFax(){
            return clientHasRequestedFax.get();
        }

        public BooleanProperty clientHasRequestedFaxProperty(){
            return clientHasRequestedFax;
        }

        /**
         * Setter Method
         */
        public void setOrderID(String orderID) { this.orderID = orderID; }

//        public ComboBox getOperator() { return operator; }
//
//        public String getDate() { return date; }
//
//        public ComboBox getDroneID() { return droneID; }
//
//        public ComboBox getStatus() { return status; }
//
//        public String getTotal() { return total; }


        public void setClientHasRequestedFax(boolean clientHasRequestedFax){
            this.clientHasRequestedFax.set(clientHasRequestedFax);
        }


    }
}

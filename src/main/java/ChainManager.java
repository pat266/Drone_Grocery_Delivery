import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
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

public class ChainManager {
    private String username = null;
    private Connection connection = null;
    private Stage mainStage = null;
    private String chainName;
    private ObservableList<String> locats = FXCollections.observableArrayList();
    public ChainManager(Stage mainStage, Connection connection, String username) throws SQLException {
        this.connection = connection;
        this.mainStage = mainStage;
        this.username = username;
        createChainName();
        createLocation();
    }

    /**
     * Find out the chain name from the Manager's username
     * @throws SQLException
     */
    private void createChainName() throws SQLException {
        Statement st = this.connection.createStatement();
        String checkChainName = "Select ChainName from manager where Username = \"" + this.username + "\"";
        ResultSet rs = st.executeQuery(checkChainName);
        while (rs.next()) {
            this.chainName = rs.getString("ChainName");
        }
    }

    /**
     * Find out the Store locations from the Chain Name
     * @throws SQLException
     */
    private void createLocation() throws SQLException {
        Statement st = this.connection.createStatement();
        String checkLocation = "Select StoreName from store where ChainName = \"" + this.chainName + "\"";
        // System.out.println(checkLocation);
        ResultSet rs = st.executeQuery(checkLocation);
        while (rs.next()) {
            String current = rs.getString("StoreName");
            this.locats.add(current);
        }
    }

    public Scene createChainManagerHome() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(40);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Chain Manager Home");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 0, 0, 2, 1);

        // View Drone Technicians
        Button viewDroneTech = new Button("View Drone Technicians");
        viewDroneTech.setWrapText(true);
        viewDroneTech.setMaxWidth(150);
        viewDroneTech.setPrefHeight(60);
        viewDroneTech.setTextAlignment(TextAlignment.CENTER);
        viewDroneTech.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        viewDroneTech.setStyle("-fx-background-color: #000;");
        viewDroneTech.setTextFill(Color.WHITE);
        viewDroneTech.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.setScene(viewDroneTech(null, null));
            }
        });
        grid.add(viewDroneTech, 0, 1);

        // View Drones
        Button viewDrones = new Button("View Drones");
        viewDrones.setWrapText(true);
        viewDrones.setMaxWidth(150);
        viewDrones.setPrefHeight(60);
        viewDrones.setTextAlignment(TextAlignment.CENTER);
        viewDrones.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        viewDrones.setStyle("-fx-background-color: #000;");
        viewDrones.setTextFill(Color.WHITE);
        viewDrones.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.setScene(viewDrones(null, null));
            }
        });
        grid.add(viewDrones, 1, 1);

        // Create Chain Item
        Button createChainItem = new Button("Create Chain Item");
        createChainItem.setWrapText(true);
        createChainItem.setMaxWidth(150);
        createChainItem.setPrefHeight(60);
        createChainItem.setTextAlignment(TextAlignment.CENTER);
        createChainItem.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        createChainItem.setStyle("-fx-background-color: #000;");
        createChainItem.setTextFill(Color.WHITE);
        createChainItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.setScene(createChainItem());
            }
        });
        grid.add(createChainItem, 0, 2);

        // Manage Stores
        Button manageStores = new Button("Manage Stores");
        manageStores.setWrapText(true);
        manageStores.setMaxWidth(150);
        manageStores.setPrefHeight(60);
        manageStores.setTextAlignment(TextAlignment.CENTER);
        manageStores.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        manageStores.setStyle("-fx-background-color: #000;");
        manageStores.setTextFill(Color.WHITE);
        manageStores.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.setScene(manageChainStores(null, null, null));
            }
        });
        grid.add(manageStores, 1, 2);

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
     * Screen 9
     */
    public Scene createChainItem() {
        ObservableList<String> itemList = FXCollections.observableArrayList();
        int maxPLU = 0;
        Statement st = null;
        try {
            st = this.connection.createStatement();
            String checkItem = "Select distinct ChainItemName from chain_item";
            // System.out.println(checkLocation);
            ResultSet rs = st.executeQuery(checkItem);
            while (rs.next()) {
                String current = rs.getString("ChainItemName");
                itemList.add(current);
            }
            // check the maximum PLU Number
            String checkPLU = "select max(PLUNumber) from chain_item where ChainName = '" + this.chainName + "'";
            ResultSet rs1 = st.executeQuery(checkPLU);
            while (rs1.next()) {
                maxPLU = rs1.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(30);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Chain Manager Create Chain Item");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 0, 0, 4, 1);

        // Chain Name Label
        Label chainName = new Label("Chain Name:");
        chainName.setFont(Font.font("Arial",18));
        grid.add(chainName, 0, 1);
        // Chain name (non editable)
        Text chain = new Text();
        chain.setText(this.chainName);
        chain.setFont(Font.font("Arial", 16));
        chain.resize(150, 35);
        grid.add(chain, 1, 1);

        // Item
        Label itemName = new Label("Item:");
        itemName.setFont(Font.font("Arial",18));
        grid.add(itemName, 0, 2);
        // Item name
        ComboBox<String> item = new ComboBox();
        item.setItems(itemList);
        item.setValue(itemList.get(0));
        item.setEditable(true);
        item.resize(150, 35);
        grid.add(item, 1, 2);

        // Quantity
        Label quantityAvai = new Label("Quantity Available:");
        quantityAvai.setFont(Font.font("Arial",18));
        grid.add(quantityAvai, 0, 3);
        // Quantity
        TextField quantity = new TextField();
        quantity.setPromptText("Input the amount available total");
        quantity.resize(150, 35);
        quantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.matches("\\d*")) {
                    quantity.setText(t1.replaceAll("[^\\d]", ""));
                }
            }
        });
        grid.add(quantity, 1, 3);

        // Limit Order
        Label limitOrder = new Label("Limit Order:");
        limitOrder.setFont(Font.font("Arial",18));
        grid.add(limitOrder, 0, 4);
        // Limit
        TextField limit = new TextField();
        limit.setPromptText("Enter Limit per order");
        limit.resize(150, 35);
        limit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.matches("\\d*")) {
                    limit.setText(t1.replaceAll("[^\\d]", ""));
                }
            }
        });
        grid.add(limit, 1, 4);

        // PLU Number
        Label pluNumber = new Label("PLU Number:");
        pluNumber.setFont(Font.font("Arial",18));
        grid.add(pluNumber, 0, 5);
        // PLU (non editable)
        Text plu = new Text();
        // increment the PLU Number by 1
        maxPLU++;
        plu.setText(String.valueOf(maxPLU));
        plu.setFont(Font.font("Arial", 16));
        plu.resize(150, 35);
        grid.add(plu, 1, 5);

        // Price per unit
        Label pricePer = new Label("Price Per Unit $");
        pricePer.setFont(Font.font("Arial",18));
        grid.add(pricePer, 0, 6);
        // price
        TextField price = new TextField();
        price.setPromptText("Enter price");
        price.resize(150, 35);
        price.setTextFormatter(new TextFormatter<>((change) -> {
            String text = change.getControlNewText();
            if (text.matches("\\d{0,2}(\\.\\d{0,2})?")) {
                return change;
            } else {
                return null;
            }
        }));
        grid.add(price, 1, 6);

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
                mainStage.setScene(createChainManagerHome());
            }
        });

        // Create Button
        Button create = new Button("Create");
        create.setWrapText(true);
        create.setMaxWidth(150);
        create.setPrefSize(100, 60);
        create.setTextAlignment(TextAlignment.CENTER);
        create.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        create.setStyle("-fx-background-color: #000;");
        create.setTextFill(Color.WHITE);
        create.setOnAction(e -> {
            if (item.getValue() == null || quantity.getText().isEmpty() ||
                    limit.getText().isEmpty() || price.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please fill in all information.");
                alert.showAndWait();
            } else {
                try {
                    CallableStatement createChainItem = connection.prepareCall("CALL manager_create_chain_item(?, ?, ?, ?, ?, ?)");
                    createChainItem.setString(1, this.chainName);
                    createChainItem.setString(2, item.getValue());
                    createChainItem.setString(3, quantity.getText());
                    createChainItem.setString(4, limit.getText());
                    createChainItem.setString(5, plu.getText());
                    createChainItem.setString(6, price.getText());

                    int status = createChainItem.executeUpdate();
                    if (status >= 1) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Successfully created the chain item " + item.getValue() + ".");
                        alert.showAndWait();
                        mainStage.setScene(createChainManagerHome());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Could not create Chain Item.");
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

        hbox.getChildren().addAll(create);

        borderPane.setCenter(grid);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 800, 600);
        mainStage.setScene(scene);
        return scene;
    }

    /**
     * Screen 10
     */
    public Scene viewDroneTech(String inputUsername, String inputLocation) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(40);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Chain Manager View Drone Technicians");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 0, 0, 4, 1);

        // Chain Name Label
        Label chainName = new Label("Chain:");
        chainName.setFont(Font.font("Arial",18));
        grid.add(chainName, 0, 1);
        // Chain name (non editable)
        Text chain = new Text();
        chain.setText(this.chainName);
        chain.setFont(Font.font("Arial", 16));
        chain.resize(150, 35);
        grid.add(chain, 1, 1);

        // Username Label
        Label usernameL = new Label("Username:");
        usernameL.setFont(Font.font("Arial",18));
        grid.add(usernameL, 2, 1);
        // Username
        TextField username = new TextField();
        username.setPromptText("Username");
        // if the username is not null, then set it to the TextField
        if (inputUsername != null) {
            username.setText(inputUsername);
        }
        username.setPrefSize(150, 35);
        grid.add(username, 3, 1);

        // Location Label
        Label locationL = new Label("Location:");
        locationL.setFont(Font.font("Arial",18));
        grid.add(locationL, 0, 2);
        // Chain name (non editable)
        ComboBox location = new ComboBox();
        // create a temporary ObservableList for a list of locations
        ObservableList<String> tempLocats = this.locats;
        // add a null option to it
        tempLocats.add(null);
        location.setItems(tempLocats);
        if (inputLocation != null) {
            location.setValue(inputLocation);
        } else {
            location.setValue(null);
        }
        location.setPrefSize(150, 35);
        grid.add(location, 1, 2);

        // Filter Button
        Button filter = new Button("Filter");
        filter.setWrapText(true);
        filter.setMaxWidth(150);
        filter.setPrefSize(100, 40);
        filter.setTextAlignment(TextAlignment.CENTER);
        filter.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        filter.setStyle("-fx-background-color: #000;");
        filter.setTextFill(Color.WHITE);
        filter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (username.getText().isEmpty() && location.getValue() == null) {
                    mainStage.setScene(viewDroneTech(null, null));
                } else if (username.getText().isEmpty()) {
                    mainStage.setScene(viewDroneTech(null, (String) location.getValue()));
                } else if (location.getValue() == null) {
                    mainStage.setScene(viewDroneTech(username.getText(), null));
                } else {
                    mainStage.setScene(viewDroneTech(username.getText(), (String) location.getValue()));
                }
            }
        });
        grid.add(filter, 3, 2);

        // Create TableView Node:
        TableView<Screen10> tableView = new TableView();

        // Column of the Table
        TableColumn usernameT = new TableColumn("Username");
        TableColumn nameT = new TableColumn("Name");
        TableColumn locationT = new TableColumn("Location");
        tableView.getColumns().addAll(usernameT, nameT, locationT);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        usernameT.setCellValueFactory(new PropertyValueFactory<Screen10, String>("username"));
        nameT.setCellValueFactory(new PropertyValueFactory<Screen10, String>("name"));
        locationT.setCellValueFactory(new PropertyValueFactory<Screen10, String>("location"));

        // ObservableList to hold the data, of class screen10
        ObservableList<Screen10> data = FXCollections.observableArrayList();
        // Call the 10a Procedure
        try {
            String procedure10a = "";
            if (inputUsername == null && inputLocation == null) {
                procedure10a = "CALL manager_view_drone_technicians('" + this.chainName + "', " + null + ", " + null + ")";
            } else if (inputUsername == null) {
                procedure10a = "CALL manager_view_drone_technicians('" + this.chainName + "', " + null + ", '" + inputLocation + "')";
            } else if (inputLocation == null) {
                procedure10a = "CALL manager_view_drone_technicians('" + this.chainName + "', '" + inputUsername + "', " + null + ")";
            } else {
                procedure10a = "CALL manager_view_drone_technicians('" + this.chainName + "', '" + inputUsername + "', '" + inputLocation + "')";
            }
            System.out.println(procedure10a);
            // Execute Procedure 10a
            connection.createStatement().executeQuery(procedure10a);

            // SQL FOR SELECTING THE TABLE
            String SQL = "SELECT * from manager_view_drone_technicians_result";
            //ResultSet
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            while (rs.next()) {
                String tempUsername = rs.getString("Username");
                String tempName = rs.getString("Name");
                String tempLocation = rs.getString("Location");
                data.add(new Screen10(tempUsername, tempName, this.locats, tempLocation));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
        tableView.setItems(data);
        grid.add(tableView, 0, 3, 4, 1);

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
                mainStage.setScene(createChainManagerHome());
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
            username.setText("");
            location.setValue("");
            mainStage.setScene(viewDroneTech(null, null));
        });

        // Save Button
        Button save = new Button("Save");
        save.setWrapText(true);
        save.setMaxWidth(150);
        save.setPrefSize(100, 60);
        save.setTextAlignment(TextAlignment.CENTER);
        save.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        save.setStyle("-fx-background-color: #000;");
        save.setTextFill(Color.WHITE);
        save.setOnAction(e -> {
            for (int i = 0; i < tableView.getItems().size(); i++) {
                String tempUsername = (String) (usernameT.getCellObservableValue(i).getValue());
                ComboBox tempBox = (ComboBox) (locationT.getCellObservableValue(i).getValue());
                String tempStoreName = (String) (tempBox.getValue());
                String updateDroneTech = "update drone_tech set drone_tech.StoreName = '" +
                        tempStoreName + "' where drone_tech.Username = '" + tempUsername + "'";

                // System.out.println(updateDroneTech);
                try {
                    connection.createStatement().executeUpdate(updateDroneTech);

                    CallableStatement updateZip = connection.prepareCall("CALL manager_assign_drone_technicians(?, ?, ?, ?)");
                    updateZip.setString(1, this.chainName);
                    updateZip.setString(2, tempUsername);
                    updateZip.setString(3, tempStoreName);
                    updateZip.setString(4, this.username);
                    updateZip.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            this.mainStage.setScene(viewDroneTech(inputUsername, inputLocation));
        });

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));
        HBox hbox = new HBox(20);
        hbox.getChildren().add(back);

        Pane filler = new Pane();
        hbox.getChildren().add(filler);
        HBox.setHgrow(filler, Priority.ALWAYS);

        hbox.getChildren().addAll(reset, save);

        borderPane.setCenter(grid);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 800, 600);
        mainStage.setScene(scene);
        return scene;
    }

    /**
     * Screen 11
     */
    public Scene viewDrones(String inputDroneID, String inputRadius) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(40);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Chain Manager View Drone Technicians");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 0, 0, 4, 1);

        // Drone ID
        Label drone = new Label("Drone ID:");
        drone.setFont(Font.font("Arial",18));
        grid.add(drone, 0, 1);

        TextField id = new TextField();
        id.setPromptText("Enter Drone ID");
        if (inputDroneID != null) {
            id.setText(inputDroneID);
        }
        id.resize(150, 35);
        grid.add(id, 1, 1);

        // Radius
        Label radius = new Label("Radius:");
        radius.setFont(Font.font("Arial",18));
        grid.add(radius, 2, 1);

        TextField rad = new TextField();
        rad.setPromptText("Enter Radius");
        if (inputRadius != null) {
            rad.setText(inputRadius);
        }
        rad.resize(150, 35);
        grid.add(rad, 3, 1);

        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        TableView<ObservableList> tableview = new TableView();
        tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        try {
            // SQL FOR CALLING THE 8a PROCEDURE
            String procedure11a = "";
            if (inputDroneID == null && inputRadius == null) {
                procedure11a = "CALL manager_view_drones('" + this.username+  "', " + inputDroneID +
                        ", " + inputRadius + ")";
            } else if (inputDroneID == null) {
                procedure11a = "CALL manager_view_drones('" + this.username+  "', " + inputDroneID +
                        ", '" + inputRadius + "')";
            } else if (inputRadius == null) {
                procedure11a = "CALL manager_view_drones('" + this.username+  "', '" + inputDroneID +
                        "', " + inputRadius + ")";
            } else {
                procedure11a = "CALL manager_view_drones('" + this.username+  "', '" + inputDroneID +
                        "', '" + inputRadius + "')";
            }
            System.out.println(procedure11a);
            connection.createStatement().executeQuery(procedure11a);

            // SQL FOR SELECTING THE TABLE
            String SQL = "SELECT * from manager_view_drones_result";
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
        // grid.add(tableview, 0, 2, 4, 1);

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
                mainStage.setScene(createChainManagerHome());
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
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                id.setText("");
                rad.setText("");
                mainStage.setScene(viewDrones(null, null));
            }
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
            if (id.getText().isEmpty() && rad.getText().isEmpty()) {
                mainStage.setScene(viewDrones(null, null));
            } else if (id.getText().isEmpty()) {
                mainStage.setScene(viewDrones(null, rad.getText()));
            } else if (rad.getText().isEmpty()) {
                mainStage.setScene(viewDrones(id.getText(), null));
            } else {
                mainStage.setScene(viewDrones(id.getText(), rad.getText()));
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

        borderPane.setTop(grid);
        borderPane.setCenter(tableview);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 800, 600);
        mainStage.setScene(scene);
        return scene;
    }

    /**
     * Screen 12
     */
    public Scene manageChainStores(String inputStoreName, String inputMin, String inputMax) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(40);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Manage Chain's Stores");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 1, 0, 3, 1);

        // Chain Name Label
        Label chainName = new Label("Chain:");
        chainName.setFont(Font.font("Arial",18));
        grid.add(chainName, 0, 1);
        // Chain name (non editable)
        Text chain = new Text();
        chain.setText(this.chainName);
        chain.setFont(Font.font("Arial", 16));
        chain.resize(150, 35);
        grid.add(chain, 1, 1);

        // Total Range
        Label totalRange = new Label("Total Range:");
        totalRange.setFont(Font.font("Arial",18));
        grid.add(totalRange, 2, 1);
        // Range
        TextField rangeMin = new TextField();
        rangeMin.setFont(Font.font("Arial",18));
        rangeMin.setPromptText("Min");
        rangeMin.setPrefWidth(120);
        if (inputMin != null) {
            rangeMin.setText(inputMin);
        }
        grid.add(rangeMin, 3, 1);

        Text dash = new Text("-");
        dash.setFont(Font.font("Arial", 16));
        grid.add(dash, 4, 1);

        TextField rangeMax = new TextField();
        rangeMax.setFont(Font.font("Arial",18));
        rangeMax.setPromptText("Max");
        rangeMax.setPrefWidth(120);
        if (inputMax != null) {
            rangeMin.setText(inputMax);
        }
        grid.add(rangeMax, 5, 1);

        // Stores Name
        Label storeName = new Label("Stores Name:");
        storeName.setFont(Font.font("Arial",18));
        grid.add(storeName, 0, 2);

        ComboBox<String> store = new ComboBox();
        // create a temporary ObservableList for a list of locations
        ObservableList<String> tempLocats = this.locats;
        // add a null option to it
        tempLocats.add(null);
        store.setItems(tempLocats);
        if (inputStoreName != null) {
            store.setValue(inputStoreName);
        } else {
            store.setValue(null);
        }
        store.resize(150, 35);
        grid.add(store, 1, 2);

        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        TableView<ObservableList> tableview = new TableView();
        tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        try {
            // SQL FOR CALLING THE 12a PROCEDURE
            String procedure12a = "";
            if (inputStoreName != null && inputMin == null && inputMax == null) {
                // 100
                procedure12a = "CALL manager_manage_stores('" + this.username+  "', '" + inputStoreName + "', " + inputMin + ", " + inputMax + ")";
            } else if (inputStoreName != null && inputMin != null && inputMax == null) {
                // 110
                procedure12a = "CALL manager_manage_stores('" + this.username+  "', '" + inputStoreName + "', '" + inputMin + "', " + inputMax + ")";
            } else if (inputStoreName == null && inputMin != null && inputMax != null) {
                // 011
                procedure12a = "CALL manager_manage_stores('" + this.username+  "', " + inputStoreName + ", '" + inputMin + "', '" + inputMax + "')";
            } else if (inputStoreName == null && inputMin == null && inputMax != null) {
                // 001
                procedure12a = "CALL manager_manage_stores('" + this.username+  "', " + inputStoreName + ", " + inputMin + ", '" + inputMax + "')";
            } else if (inputStoreName == null && inputMin == null && inputMax == null) {
                // 000
                procedure12a = "CALL manager_manage_stores('" + this.username+  "', " + inputStoreName + ", " + inputMin + ", " + inputMax + ")";
            } else if (inputStoreName != null && inputMin == null && inputMax != null) {
                // 101
                procedure12a = "CALL manager_manage_stores('" + this.username+  "', '" + inputStoreName + "', " + inputMin + ", '" + inputMax + "')";
            } else if (inputStoreName == null && inputMin != null && inputMax == null) {
                // 010
                procedure12a = "CALL manager_manage_stores('" + this.username+  "', " + inputStoreName + ", '" + inputMin + "', " + inputMax + ")";
            } else {
                // 111
                procedure12a = "CALL manager_manage_stores('" + this.username+  "', '" + inputStoreName + "', '" + inputMin + "', '" + inputMax + "')";
            }
            System.out.println(procedure12a);
            connection.createStatement().executeQuery(procedure12a);

            // SQL FOR SELECTING THE TABLE
            String SQL = "SELECT * from manager_manage_stores_result";
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
        // grid.add(tableview, 0, 3, 5, 1);


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
                mainStage.setScene(createChainManagerHome());
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
            store.setValue(null);
            rangeMin.setText("");
            rangeMax.setText("");
            mainStage.setScene(viewDrones(null, null));
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
            if (store.getValue() != null && rangeMin.getText().isEmpty() && rangeMax.getText().isEmpty()) {
                // 100
                mainStage.setScene(manageChainStores(store.getValue(), null, null));
            } else if (store.getValue() != null && !rangeMin.getText().isEmpty() && rangeMax.getText().isEmpty()) {
                // 110
                mainStage.setScene(manageChainStores(store.getValue(), rangeMin.getText(), null));
            } else if (store.getValue() == null && !rangeMin.getText().isEmpty() && !rangeMax.getText().isEmpty()) {
                // 011
                mainStage.setScene(manageChainStores(null, rangeMin.getText(), rangeMax.getText()));
            } else if (store.getValue() == null && rangeMin.getText().isEmpty() && !rangeMax.getText().isEmpty()) {
                // 001
                mainStage.setScene(manageChainStores(null, null, rangeMax.getText()));
            } else if (store.getValue() == null && rangeMin.getText().isEmpty() && rangeMax.getText().isEmpty()) {
                // 000
                mainStage.setScene(manageChainStores(null, null, null));
            } else if (store.getValue() != null && rangeMin.getText().isEmpty() && !rangeMax.getText().isEmpty()) {
                // 101
                mainStage.setScene(manageChainStores(store.getValue(), null, rangeMax.getText()));
            } else if (store.getValue() == null && !rangeMin.getText().isEmpty() && rangeMax.getText().isEmpty()) {
                // 010
                mainStage.setScene(manageChainStores(null, rangeMin.getText(), null));
            } else {
                // 111
                mainStage.setScene(manageChainStores(store.getValue(), rangeMin.getText(), rangeMax.getText()));
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

        borderPane.setTop(grid);
        borderPane.setCenter(tableview);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 800, 600);
        mainStage.setScene(scene);
        return scene;
    }

    /**
     * Chain Manager View Drone Technicians
     */
    public class Screen10 {
        private String username;
        private String name;
        private ComboBox<String> location;
        // private String initialLocation;

        public Screen10(String username, String name, ObservableList locations, String initialLocation) {
            this.username = username;
            this.name = name;
            this.location = new ComboBox<>(locations);
            this.location.setValue(initialLocation);
            // this.initialLocation = initialLocation;
        }
        public String getUsername() { return username; }
        public String getName() { return name; }
        // public String getInitialLocation() { return initialLocation; }
        public ComboBox getLocation() { return location; }

        public void setUsername(String username) { this.username = username; }
        public void setName(String name) { this.name = name; }
        // public void setInitialLocation(String initialLocation) { this.initialLocation = initialLocation; }
        public void setInitialLocation(ComboBox location) { this.location = location; }
    }
}

import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;
import javafx.util.Callback;

import java.sql.*;
public class Admin {
    private String username = null;
    private Connection connection = null;
    private Stage mainStage = null;
    public Admin(Stage mainStage, Connection connection, String username) {
        this.connection = connection;
        this.mainStage = mainStage;
        this.username = username;
    }
    public Scene createAdminHome() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(40);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Admin Home");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 1, 0, 2, 1);

        // Create Item
        Button createItem = new Button("Create Item");
        createItem.setWrapText(true);
        createItem.setMaxWidth(150);
        createItem.setPrefHeight(60);
        createItem.setTextAlignment(TextAlignment.CENTER);
        createItem.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        createItem.setStyle("-fx-background-color: #000;");
        createItem.setTextFill(Color.WHITE);
        createItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.setScene(createItem());
            }
        });
        grid.add(createItem, 0, 1);

        // Create Drone
        Button createDrone = new Button("Create Drone");
        createDrone.setWrapText(true);
        createDrone.setMaxWidth(150);
        createDrone.setPrefHeight(60);
        createDrone.setTextAlignment(TextAlignment.CENTER);
        createDrone.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        createDrone.setStyle("-fx-background-color: #000;");
        createDrone.setTextFill(Color.WHITE);
        createDrone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.setScene(createDrone(null));
            }
        });
        grid.add(createDrone, 1, 1);

        // View Customer Info
        Button customerInfo = new Button("View Customer Info");
        customerInfo.setWrapText(true);
        customerInfo.setMaxWidth(150);
        customerInfo.setPrefHeight(60);
        customerInfo.setTextAlignment(TextAlignment.CENTER);
        customerInfo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        customerInfo.setStyle("-fx-background-color: #000;");
        customerInfo.setTextFill(Color.WHITE);
        customerInfo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.setScene(viewCustomer(null, null));
            }
        });
        grid.add(customerInfo, 2, 1);

        // Create Grocery Chain
        Button groceryChain = new Button("Create Grocery Chain");
        groceryChain.setWrapText(true);
        groceryChain.setMaxWidth(150);
        groceryChain.setPrefHeight(60);
        groceryChain.setTextAlignment(TextAlignment.CENTER);
        groceryChain.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        groceryChain.setStyle("-fx-background-color: #000;");
        groceryChain.setTextFill(Color.WHITE);
        groceryChain.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.setScene(createGroceryChain());
            }
        });
        grid.add(groceryChain, 0, 2);

        // Create Store
        Button createStore = new Button("Create Store");
        createStore.setWrapText(true);
        createStore.setMaxWidth(150);
        createStore.setPrefHeight(60);
        createStore.setTextAlignment(TextAlignment.CENTER);
        createStore.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        createStore.setStyle("-fx-background-color: #000;");
        createStore.setTextFill(Color.WHITE);
        createStore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.setScene(createStore());
            }
        });
        grid.add(createStore, 1, 2);

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
     * Screen 4
     */
    public Scene createGroceryChain() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(40);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Admin Create Grocery Chain");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 0, 0, 2, 1);

        // Grocery Chain Name
        Label chainName = new Label("Grocery Chain Name:");
        chainName.setFont(Font.font("Arial",18));
        grid.add(chainName, 0, 1);
        TextField userInput = new TextField(); // the chain name
        userInput.setPromptText("Enter Chain Name");
        // userInput.setPrefWidth(200);
        userInput.setPrefSize(150, 35);
        grid.add(userInput, 1, 1);

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
                mainStage.setScene(createAdminHome());
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
        create.setOnAction(actionEvent -> {
            if (userInput.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please type in the chain name.");
                alert.showAndWait();
            } else {
                try {
                    CallableStatement createGroceryChain = connection.prepareCall("CALL admin_create_grocery_chain(?)");
                    createGroceryChain.setString(1, userInput.getText()); // userInput = the chain name user inputted
                    int status = createGroceryChain.executeUpdate();
                    if (status >= 1) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Successfully created the grocery chain " + userInput.getText() + ".");
                        alert.showAndWait();
                        mainStage.setScene(createAdminHome());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Could not create Grocery Chain.");
                        alert.showAndWait();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        HBox hbox = new HBox();
        hbox.getChildren().add(back);

        Pane filler = new Pane();
        hbox.getChildren().add(filler);
        HBox.setHgrow(filler, Priority.ALWAYS);

        hbox.getChildren().add(create);
        // borderPane.getChildren().addAll(grid, back, create);
        // borderPane.setAlignment(grid, Pos.CENTER);
        borderPane.setCenter(grid);
        borderPane.setBottom(hbox);
        Scene scene = new Scene(borderPane, 800, 600);
        return scene;
    }

    /**
     * Screen 5
     */
    public Scene createStore() {

        ObservableList<String> chainList = FXCollections.observableArrayList();
        int maxPLU = 0;
        Statement st = null;
        try {
            st = this.connection.createStatement();
            String chains = "Select distinct ChainName from chain";
            // System.out.println(checkLocation);
            ResultSet rs = st.executeQuery(chains);
            while (rs.next()) {
                String current = rs.getString("ChainName");
                chainList.add(current);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(40);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Admin Create New Store");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 1, 0, 2, 1);

        // Affiliated Chain Name
        Label chainName = new Label("Affiliated Grocery Chain:");
        chainName.setFont(Font.font("Arial",18));
        grid.add(chainName, 0, 1, 2, 1);

        // Chain name
        ComboBox<String> chain = new ComboBox();
        chain.setItems(chainList);
        chain.setValue(chainList.get(0));
        chain.setEditable(true);
        chain.resize(150, 35);
//        grid.add(chain, 1, 2);
        grid.add(chain, 2, 1, 2, 1);


        // Grocery Store Location Name
        Label storeName = new Label("Grocery Store Location Name:");
        storeName.setFont(Font.font("Arial",18));
        grid.add(storeName, 0, 2, 2, 1);
        TextField location = new TextField();
        location.setPromptText("Enter Store Location Name");
        // userInput.setPrefWidth(200);
        location.setPrefSize(150, 35);
        grid.add(location, 2, 2, 2, 1);

        // Street
        Label streetName = new Label("Street:");
        streetName.setFont(Font.font("Arial",18));
        grid.add(streetName, 0, 3,2 , 1);
        TextField street = new TextField();
        street.setPromptText("Enter Street Name");
        // userInput.setPrefWidth(200);
        street.setPrefSize(150, 35);
        grid.add(street, 2, 3, 2, 1);

        // City
        Label cityName = new Label("City:");
        cityName.setFont(Font.font("Arial",18));
        grid.add(cityName, 0, 4,2 , 1);
        TextField city = new TextField();
        city.setPromptText("Enter City Name");
        // userInput.setPrefWidth(200);
        city.setPrefSize(150, 35);
        grid.add(city, 2, 4, 2, 1);


        Label stateName = new Label("State:");
        stateName.setFont(Font.font("Arial",18));
        grid.add(stateName, 0, 5);
        ObservableList<String> observableList = FXCollections.observableArrayList("AK","AL","AR","AZ","CA","CO","CT","DC","DE","FL","GA","GU","HI","IA","ID", "IL","IN","KS","KY","LA","MA","MD","ME","MH","MI","MN","MO","MS","MT","NC","ND","NE","NH","NJ","NM","NV","NY", "OH","OK","OR","PA","PR","PW","RI","SC","SD","TN","TX","UT","VA","VI","VT","WA","WI","WV","WY");
        ComboBox<String> state = new ComboBox<>();
        state.setItems(observableList);
        state.getSelectionModel().select(10);
        state.setEditable(true);
        state.setPrefSize(150, 35);
        grid.add(state, 1, 5);

        // ZIP
        Label zipcode = new Label("ZIP:");
        zipcode.setFont(Font.font("Arial",18));
        grid.add(zipcode, 2, 5);
        TextField zip = new TextField();
        zip.setPromptText("Enter Zipcode");
        // userInput.setPrefWidth(200);
        zip.setPrefSize(150, 35);
        zip.setTextFormatter(new TextFormatter<>((change) -> {
            String text = change.getControlNewText();
            if (text.matches("\\d{0,5}")) {
                return change;
            } else {
                return null;
            }
        }));
        grid.add(zip, 3, 5);

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
                mainStage.setScene(createAdminHome());
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
        create.setOnAction(actionEvent -> {
            if (location.getText().isEmpty() || chain.getValue() == null ||
                    street.getText().isEmpty() || city.getText().isEmpty()
                    || state.getValue().isEmpty() || zip.getText().isEmpty() ) { // chain.getText().isEmpty()
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please fill in all information.");
                alert.showAndWait();
            } else if (!(zip.getText().chars().allMatch( Character::isDigit) && zip.getText().length() == 5)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("The zipcode must consists of 5 DIGITS.");
                alert.showAndWait();
            } else {
                try {
                    CallableStatement createNewStore = connection.prepareCall("CALL admin_create_new_store(?, ?, ?, ?, ?, ?)");
                    createNewStore.setString(1, location.getText());
                    createNewStore.setString(2, chain.getValue());
                    createNewStore.setString(3, street.getText());
                    createNewStore.setString(4, city.getText());
                    createNewStore.setString(5, state.getValue());
                    createNewStore.setString(6, zip.getText());

                    int status = createNewStore.executeUpdate();
                    if (status >= 1) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Successfully created the new store " + chain.getValue() + ".");
                        alert.showAndWait();
                        mainStage.setScene(createAdminHome());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Could not create new store.");
                        alert.showAndWait();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }}
        });

        HBox hbox = new HBox();
        hbox.getChildren().add(back);

        Pane filler = new Pane();
        hbox.getChildren().add(filler);
        HBox.setHgrow(filler, Priority.ALWAYS);

        hbox.getChildren().add(create);

        borderPane.setBottom(hbox);

        // borderPane.getChildren().addAll(grid, back, create);
        // borderPane.setAlignment(grid, Pos.CENTER);
        borderPane.setCenter(grid);
        GridPane.setHgrow(grid, Priority.ALWAYS);
        borderPane.setBottom(hbox);
        Scene scene = new Scene(borderPane, 800, 600);
        return scene;
    }

    /**
     * Screen 6
     */
    public Scene createDrone(String inputZip) {
        int maxID = 0;
        ObservableList<String> storeZip = FXCollections.observableArrayList();
        try {
            Statement st = this.connection.createStatement();
            // check the maximum PLU Number
            String checkPLU = "select max(ID) from drone";
            ResultSet rs1 = st.executeQuery(checkPLU);
            while (rs1.next()) {
                maxID = rs1.getInt(1);
            }
            // get all of the zipcode from stores
            String checkItem = "select distinct zipcode from store";
            // System.out.println(checkLocation);
            ResultSet rs = st.executeQuery(checkItem);
            while (rs.next()) {
                String current = rs.getString(1);
                storeZip.add(current);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(40);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Admin Create Drone");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 1, 0, 2, 1);

        // Drone ID
        Label droneID = new Label("Drone ID:");
        droneID.setFont(Font.font("Arial",18));
        grid.add(droneID, 0, 1, 2, 1);
        maxID++;
        Text ID = new Text(Integer.toString(maxID));
        // userInput.setPrefWidth(200);
        ID.resize(150, 35);
        ID.setFont(Font.font("Arial", 18));
        grid.add(ID, 2, 1, 2, 1);

        // Associated Zip Code
        Label zipCode = new Label("Associated Zip Code:");
        zipCode.setFont(Font.font("Arial",18));
        grid.add(zipCode, 0, 2, 2, 1);
        ComboBox<String> zip = new ComboBox<>();
        zip.setItems(storeZip);
        if (inputZip != null) {
            zip.setValue(inputZip);
        } else {
            zip.setValue(storeZip.get(0));
        }
        zip.setPrefSize(150, 35);
        zip.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                mainStage.setScene(createDrone(t1));
            }
        });
        grid.add(zip, 2, 2, 2, 1);

        // Travel Radius
        Label travelRadius = new Label("Travel Radius:");
        travelRadius.setFont(Font.font("Arial",18));
        grid.add(travelRadius, 0, 3,2 , 1);
        TextField radius = new TextField();
//        radius.setPromptText("Enter Street Name");
        // userInput.setPrefWidth(200);
        radius.setPrefSize(150, 35);
        radius.setTextFormatter(new TextFormatter<>((change) -> {
            String text = change.getControlNewText();
            if (text.matches("\\d{0,7}")) {
                return change;
            } else {
                return null;
            }
        }));
        grid.add(radius, 2, 3, 2, 1);

        // Status
        Label status = new Label("Status:");
        status.setFont(Font.font("Arial",18));
        grid.add(status, 0, 4,2 , 1);
        TextField stat = new TextField();
        stat.setText("Available");
        stat.setDisable(true);
        stat.setPrefSize(150, 35);
        grid.add(stat, 2, 4, 2, 1);

        ObservableList<String> storeAssociate = FXCollections.observableArrayList();
        try {
            Statement st = this.connection.createStatement();
            // get all of the store associates from zipcode
            String checkAss = "select distinct username from drone_tech natural join store where Zipcode = '" + zip.getValue() + "'";
            // System.out.println(checkLocation);
            ResultSet rs = st.executeQuery(checkAss);
            while (rs.next()) {
                String current = rs.getString(1);
                storeAssociate.add(current);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(storeAssociate.size());
        // Store Associate
        Label storeAssoc = new Label("Store Associate:");
        storeAssoc.setFont(Font.font("Arial",18));
        grid.add(storeAssoc, 0, 5,2 , 1);
        ComboBox<String> storeAss = new ComboBox<>();
        storeAss.setItems(storeAssociate);
        if (storeAssociate.size() > 0) {
            storeAss.setValue(storeAssociate.get(0));
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("This zipcode (" + zip.getValue() + ") does not have any store associate.");
            alert.showAndWait();
        }
        // userInput.setPrefWidth(200);
        storeAss.setPrefSize(150, 35);
        grid.add(storeAss, 2, 5, 2, 1);

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
                mainStage.setScene(createAdminHome());
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
            if (zip.getValue() == null || radius.getText().isEmpty() || storeAss.getValue() == null) {
                if (storeAss.getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Please choose a different Associated Zip Code.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Please fill in all information.");
                    alert.showAndWait();
                }
            } else {
                try {
                    CallableStatement createDrone = connection.prepareCall("CALL admin_create_drone(?, ?, ?, ?)");
                    createDrone.setString(1, ID.getText());
                    createDrone.setString(2, zip.getValue());
                    createDrone.setString(3, radius.getText());
                    createDrone.setString(4, storeAss.getValue());

                    int status1 = createDrone.executeUpdate();
                    if (status1 >= 1) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Successfully created drone.");
                        alert.showAndWait();
                        mainStage.setScene(createAdminHome());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Could not create drone.");
                        alert.showAndWait();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        HBox hbox = new HBox();
        hbox.getChildren().add(back);

        Pane filler = new Pane();
        hbox.getChildren().add(filler);
        HBox.setHgrow(filler, Priority.ALWAYS);

        hbox.getChildren().add(create);

        borderPane.setBottom(hbox);

        // borderPane.getChildren().addAll(grid, back, create);
        // borderPane.setAlignment(grid, Pos.CENTER);
        borderPane.setCenter(grid);
        GridPane.setHgrow(grid, Priority.ALWAYS);
        borderPane.setBottom(hbox);
        Scene scene = new Scene(borderPane, 800, 600);
        return scene;
    }

    /**
     * Screen 7
     */
    public Scene createItem() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(40);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Admin Create Item");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 1, 0, 2, 1);

        // Name
        Label name = new Label("Name:");
        name.setFont(Font.font("Arial",18));
        grid.add(name, 0, 1, 2, 1);
        TextField names = new TextField();
        names.setPromptText("Enter Name");
        // userInput.setPrefWidth(200);
        names.setPrefSize(150, 35);
        grid.add(names, 2, 1, 2, 1);

        // Type
        Label type = new Label("Type:");
        type.setFont(Font.font("Arial",18));
        grid.add(type, 0, 2, 2, 1);
        ObservableList<String> typeList = FXCollections.observableArrayList("Dairy", "Bakery", "Meat", "Produce", "Personal Care", "Paper Goods", "Beverages", "Other");
        ComboBox<String> types = new ComboBox<>();
        types.setItems(typeList);
        types.setEditable(true);
        grid.add(types, 2, 2, 2, 1);

        // Organic
        Label organic = new Label("Organic:");
        organic.setFont(Font.font("Arial",18));
        grid.add(organic, 0, 3,2 , 1);
        ObservableList<String> organicList = FXCollections.observableArrayList("Yes", "No");
        ComboBox<String> org = new ComboBox<>();
        org.setItems(organicList);
        org.getSelectionModel().select(0);
        grid.add(org, 2, 3, 2, 1);

        // Origin
        Label origin = new Label("Origin:");
        origin.setFont(Font.font("Arial",18));
        grid.add(origin, 0, 4,2 , 1);
        TextField ori = new TextField();
        ori.setPromptText("Enter Origin");
        ori.setPrefSize(150, 35);
        grid.add(ori, 2, 4, 2, 1);

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
                mainStage.setScene(createAdminHome());
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
        create.setOnAction(actionEvent -> {
            if (names.getText().isEmpty() || types.getValue() == null ||
                    org.getValue() == null || ori.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please fill in all information.");
                alert.showAndWait();
            }else {
                try {
                    CallableStatement createNewStore = connection.prepareCall("CALL admin_create_item(?, ?, ?, ?)");
                    createNewStore.setString(1, names.getText());
                    createNewStore.setString(2, types.getValue());
                    createNewStore.setString(3, org.getValue());
                    createNewStore.setString(4, ori.getText());


                    int status = createNewStore.executeUpdate();
                    if (status >= 1) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Successfully created the Item " + names.getText() + ".");
                        alert.showAndWait();
                        mainStage.setScene(createAdminHome());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Could not create new item.");
                        alert.showAndWait();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }}
        });


        HBox hbox = new HBox();
        hbox.getChildren().add(back);

        Pane filler = new Pane();
        hbox.getChildren().add(filler);
        HBox.setHgrow(filler, Priority.ALWAYS);

        hbox.getChildren().add(create);

        borderPane.setBottom(hbox);

        // borderPane.getChildren().addAll(grid, back, create);
        // borderPane.setAlignment(grid, Pos.CENTER);
        borderPane.setCenter(grid);
        GridPane.setHgrow(grid, Priority.ALWAYS);
        borderPane.setBottom(hbox);
        Scene scene = new Scene(borderPane, 800, 600);
        return scene;
    }

    /**
     * Screen 8
     */
    public Scene viewCustomer(String firstN, String lastN) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(40);
        grid.setHgap(20);
        grid.setPadding(new Insets(10));

        Text welcomeTxt = new Text("Admin View Customers");
        welcomeTxt.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(welcomeTxt, 1, 0, 2, 1);

        // Name
        Label name = new Label("Customers:");
        name.setFont(Font.font("Arial",18));
        grid.add(name, 0, 1);
        // first name
        TextField first = new TextField();
        first.setPromptText("First");
        first.setPrefSize(150, 35);
        grid.add(first, 1, 1);
        // last name
        TextField last = new TextField();
        last.setPromptText("Last");
        last.setPrefSize(150, 35);
        grid.add(last, 2, 1);

        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        TableView<ObservableList> tableview = new TableView();
        tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        try {
            // SQL FOR CALLING THE 8a PROCEDURE
            String procedure8a = "";
            if (firstN == null && lastN == null) {
                procedure8a = "CALL admin_view_customers(" + firstN +
                        ", " + lastN + ")";
            } else if (firstN == null) {
                procedure8a = "CALL admin_view_customers(" + firstN +
                        ", '" + lastN + "')";
            } else if (lastN == null) {
                procedure8a = "CALL admin_view_customers('" + firstN +
                        "', " + lastN + ")";
            } else {
                procedure8a = "CALL admin_view_customers('" + firstN +
                        "', '" + lastN + "')";
            }
            System.out.println(procedure8a);
            connection.createStatement().executeQuery(procedure8a);

            // SQL FOR SELECTING THE TABLE
            String SQL = "SELECT * from admin_view_customers_result";
            //ResultSet
            ResultSet rs = connection.createStatement().executeQuery(SQL);
            int numCol = 0, numRow = 0;
            /**********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             **********************************/
            for (int i=0 ; i<rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                tableview.getColumns().add(col);
                // System.out.println("Column ["+i+"] ");
                numCol++;
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
                // System.out.println("Row [1] added "+row );
                data.add(row);
            }
            //FINALLY ADDED TO TableView
            tableview.setItems(data);

            // System.out.println("Number of row: " + numRow + " and num of columns: " + numCol);
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
        grid.add(tableview, 0, 2, 3, 1);
        // The first number is column, second is row
        // System.out.println(tableview.getColumns().get(1).getCellObservableValue(0).getValue().toString());


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
                Admin admin = new Admin(mainStage, connection, "mmoss7");
                mainStage.setScene(admin.createAdminHome());
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
                first.setText("");
                last.setText("");
                mainStage.setScene(viewCustomer(null, null));
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
        filter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String firstName = first.getText();
                String lastName = last.getText();
                if (first.getText().isEmpty() && last.getText().isEmpty()) {
                    mainStage.setScene(viewCustomer(null, null));
                } else if (first.getText().isEmpty()) {
                    mainStage.setScene(viewCustomer(null, lastName));
                } else if (last.getText().isEmpty()) {
                    mainStage.setScene(viewCustomer(firstName, null));
                } else {
                    mainStage.setScene(viewCustomer(firstName, lastName));
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

        hbox.getChildren().addAll(reset, filter);

        borderPane.setCenter(grid);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 800, 600);
        mainStage.setScene(scene);
        return scene;
    }

}
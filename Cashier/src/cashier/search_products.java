package cashier;

import static cashier.database.add;
import static cashier.database.connect;
import static cashier.database.passwordDB;
import static cashier.database.user;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class search_products extends Application {

    Stage stage = new Stage();
    get_data get = new get_data();
    TextField search = new TextField();
    TableView<get_data> table = new TableView();
    TableColumn<get_data, String> code = new TableColumn("الكود");
    TableColumn<get_data, String> name_products = new TableColumn("اسم الصنف");
    TableColumn<get_data, String> price = new TableColumn("السعر");
    database base = new database();
    ObservableList data = FXCollections.observableArrayList();
    FilteredList<get_data> filter = new FilteredList<>(data, e -> true);
    money m = new money();
    ObservableList<String> options = FXCollections.observableArrayList("الكود", "اسم الصنف", "السعر");
    ComboBox comboBox = new ComboBox(options);
    Label l_comboBox = new Label("فلتر");
    String check = null;

    @Override
    public void start(Stage stage) throws Exception {
        Pane root = new Pane();
        Scene scene = new Scene(root, 500, 400);
        stage.getIcons().add(new Image("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\Cashier\\src\\cashier\\download.png"));
        File f = new File("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\Cashier\\src\\cashier\\index.css");
        scene.getStylesheets().clear();
        scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
        connect = (Connection) DriverManager.getConnection(add, user, passwordDB);
        base.s = connect.prepareStatement("select * from products");
        base.r = base.s.executeQuery();
        while (base.r.next()) {
                code.setCellValueFactory(new PropertyValueFactory<>("code"));
                name_products.setCellValueFactory(new PropertyValueFactory<>("name_products"));
                price.setCellValueFactory(new PropertyValueFactory<>("price"));
                data.add(new get_data(Integer.toString(base.r.getInt("code")) , base.r.getString("products") , base.r.getString("price")));
                table.setItems(data);
        }
        base.close_database();

        //keyboard
        scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (e.getCode() == KeyCode.NUMPAD0 || e.getCode() == KeyCode.NUMPAD1 || e.getCode() == KeyCode.NUMPAD2
                            || e.getCode() == KeyCode.NUMPAD3 || e.getCode() == KeyCode.NUMPAD4 || e.getCode() == KeyCode.NUMPAD5
                            || e.getCode() == KeyCode.NUMPAD6 || e.getCode() == KeyCode.NUMPAD7 || e.getCode() == KeyCode.NUMPAD8
                            || e.getCode() == KeyCode.NUMPAD9 || e.getCode() == KeyCode.DIGIT0 || e.getCode() == KeyCode.DIGIT1
                            || e.getCode() == KeyCode.DIGIT2 || e.getCode() == KeyCode.DIGIT3 || e.getCode() == KeyCode.DIGIT4
                            || e.getCode() == KeyCode.DIGIT5 || e.getCode() == KeyCode.DIGIT6 || e.getCode() == KeyCode.DIGIT7
                            || e.getCode() == KeyCode.DIGIT8 || e.getCode() == KeyCode.DIGIT9) {
                        search_table();
                    } else if (e.getCode() == KeyCode.BACK_SPACE) {
                        search_table();
                    }
                    search_table();
                }
            });
        });

        //mouse
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                try {
                    check = comboBox.getValue().toString();
                } catch (Exception ex) {
                }
            }
        });

        search.setPromptText("بحث");
        search.setAlignment(Pos.CENTER_RIGHT);
        code.setId("column");
        name_products.setId("column");
        price.setId("column");
        comboBox.setStyle("-fx-font-size:15px; -fx-font-weight:bold; ");
        l_comboBox.setStyle("-fx-font-size:18px; -fx-font-weight:bold; ");
        search.setMaxSize(250, 30);
        search.setMinSize(250, 30);
        comboBox.setMinSize(130, 30);
        comboBox.setMaxSize(130, 30);
        table.autosize();
        table.setMaxHeight(350);
        table.setMinHeight( 350);
        root.getChildren().addAll(search, table, comboBox, l_comboBox);
        table.getColumns().addAll(price, name_products, code);
        search.setLayoutX(240);
        search.setLayoutY(20);
        comboBox.setLayoutX(80);
        comboBox.setLayoutY(20);
        l_comboBox.setLayoutX(40);
        l_comboBox.setLayoutY(20);
        table.setLayoutX(00);
        table.setLayoutY(60);
        code.setMinWidth(120);
        price.setMinWidth(120);
        name_products.setMinWidth(260);
        table.setEditable(
                false);
        code.setEditable(
                false);
        name_products.setEditable(
                false);
        stage.setResizable(
                false);
        code.setResizable(
                false);
        name_products.setResizable(
                false);
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class get_data {

        private String code;
        private String name_products;
        private String price;

        public get_data(String code, String name_products, String price) {
            this.code = code;
            this.name_products = name_products;
            this.price = price;
        }

        public get_data() {

        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName_products() {
            return name_products;
        }

        public void setName_products(String name_products) {
            this.name_products = name_products;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }



    }

    public void search_table() {
        search.textProperty().addListener((observale, oldValue, newValue) -> {
            try {
                filter.setPredicate(get -> {
                    if (newValue.isEmpty() || newValue == null) {
                        return true;
                    }
                    String text = newValue;
                    if (get.getPrice().startsWith(text) && check == "السعر") {
                        return true;
                    } else if (get.getName_products().contains(text) && check == "اسم الصنف") {
                        return true;
                    } else if (get.getCode().startsWith(text) && check == "الكود") {
                        return true;
                    } else {
                        return false;
                    }
                });
                SortedList<get_data> sort = new SortedList<>(filter);
                sort.comparatorProperty().bind(table.comparatorProperty());
                table.setItems(sort);
            } catch (Exception ex) {
            }
        });
    }
}

package cashier;

import static cashier.database.add;
import static cashier.database.connect;
import static cashier.database.passwordDB;
import static cashier.database.user;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

public class search extends Application {

    Stage stage = new Stage();
    get_data get = new get_data();
    TextField search = new TextField();
    TableView<get_data> table = new TableView();
    TableColumn<get_data, String> first = new TableColumn("اسم المستخدم");
    TableColumn<get_data, String> second = new TableColumn("رقم المستخدم");
    database base = new database();
    ObservableList data = FXCollections.observableArrayList();
    FilteredList<get_data> filter = new FilteredList<>(data, e -> true);
    money m = new money();

    @Override
    public void start(Stage stage) throws Exception {
        Pane root = new Pane();
        Scene scene = new Scene(root, 500, 400);
        stage.getIcons().add(new Image("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\Cashier\\src\\cashier\\download.png"));
        File f = new File("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\Cashier\\src\\cashier\\index.css");
        scene.getStylesheets().clear();
        scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
        connect = (Connection) DriverManager.getConnection(add, user, passwordDB);
        base.s = connect.prepareStatement("select * from accounts");
        base.r = base.s.executeQuery();
        while (base.r.next()) {
            first.setCellValueFactory(new PropertyValueFactory<>("user_name"));
            second.setCellValueFactory(new PropertyValueFactory<>("num_user"));
            data.add(new get_data(base.r.getString("user_name"), base.r.getString("num_user")));
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
                    if (event.getClickCount() == 2) {

                        stage.close();
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

        });

        search.setPromptText(
                "بحث");
        search.setAlignment(Pos.CENTER_RIGHT);

        first.setId(
                "column");
        second.setId(
                "column");
        table.autosize();
        search.setMaxSize(
                400, 25);
        search.setMinSize(
                400, 25);
        table.setMaxHeight(390);
        table.setMinHeight(390);
        root.getChildren()
                .addAll(search, table);
        table.getColumns()
                .addAll(first, second);
        search.setLayoutX(
                50);
        search.setLayoutY(
                10);
        table.setLayoutX(
                00);
        table.setLayoutY(
                50);
        first.setMinWidth(
                350);
        second.setMinWidth(
                150);
        table.setEditable(
                false);
        first.setEditable(
                false);
        second.setEditable(
                false);
        stage.setResizable(
                false);
        first.setResizable(
                false);
        second.setResizable(
                false);
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class get_data {

        private String user_name;
        private String num_user;

        public get_data(String user_name, String num_user) {
            this.user_name = user_name;
            this.num_user = num_user;
        }

        public get_data() {

        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getNum_user() {
            return num_user;
        }

        public void setNum_user(String num_user) {
            this.num_user = num_user;
        }
    }

    public void search_table() {
        search.textProperty().addListener((observale, oldValue, newValue) -> {
            filter.setPredicate(get -> {
                if (newValue.isEmpty() || newValue == null) {
                    return true;
                }
                String text = newValue;
                if (get.getUser_name().startsWith(text)) {
                    return true;
                } else if (get.getNum_user().startsWith(text)) {
                    return true;
                } else {
                    return false;
                }
            });
            SortedList<get_data> sort = new SortedList<>(filter);
            sort.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sort);
        });
    }
}

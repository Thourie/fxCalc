module com.fxcalc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.fxcalc to javafx.fxml;
    exports com.fxcalc;
}
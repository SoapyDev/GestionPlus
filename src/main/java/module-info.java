module com.project.tpfinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;



    opens com.project.tpfinal3 to javafx.fxml;
    exports com.project.tpfinal3;
}
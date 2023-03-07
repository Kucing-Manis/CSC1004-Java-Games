module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;
    requires org.kordamp.bootstrapfx.core;
    requires annotations;
//    requires jdk.hotspot.agent;
//    opens org.example to javafx.fxml;
//    exports org.example;
    opens assets.textures;
//    opens assets.sounds;
    opens org.example.userform to javafx.fxml;
    exports org.example.userform;
    opens org.example to org.almasb.fxgl.core;
    exports org.example;
}
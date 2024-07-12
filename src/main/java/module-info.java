
module com.vdq.autogpm {
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;
    requires retrofit2.converter.gson;
    requires com.google.gson;
    requires okhttp3;
    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.chrome_driver;
    requires dev.failsafe.core;
    requires org.seleniumhq.selenium.support;
    requires org.apache.poi.ooxml;
    requires jdk.compiler;
    requires org.slf4j;
    opens com.vdq.autogpm to javafx.fxml;
    opens com.vdq.autogpm.controller;
    opens com.vdq.autogpm.api;
    exports com.vdq.autogpm;
}

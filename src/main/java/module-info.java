module com.vdq.autogpm {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires retrofit2;
    requires retrofit2.converter.gson;
    requires com.google.gson;
    requires okhttp3;
    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.chrome_driver;
    requires dev.failsafe.core;
    requires org.seleniumhq.selenium.support;
    requires org.apache.poi.ooxml;
    opens com.vdq.autogpm.ui;
    opens com.vdq.autogpm;
    opens com.vdq.autogpm.controller;
    opens  com.vdq.autogpm.api;
}

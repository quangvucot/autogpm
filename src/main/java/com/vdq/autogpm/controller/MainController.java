package com.vdq.autogpm.controller;


import com.vdq.autogpm.api.Profile;
import com.vdq.autogpm.automation.ProfileAutomation;
import com.vdq.autogpm.executor.ProfileExecutor;
import com.vdq.autogpm.service.ProfileService;
import com.vdq.autogpm.util.ExcelUtils;
import com.vdq.autogpm.webdriver.WebDriverManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class MainController {
    @FXML
    private TableView<Profile> profileTable;
    @FXML
    private TableColumn<Profile, String> nameColumn;
    @FXML
    private TableColumn<Profile, String> statusColumn;
    @FXML
    private TableColumn<Profile, String> proxyColumn;
    @FXML
    private TableColumn<Profile, String> lastRunColumn;
    @FXML
    private TextField urlField;
    @FXML
    private TextArea logArea;
    @FXML
    private ComboBox groupProfile;
    @FXML
    private TableColumn<Profile, Void> actionColumn;
    @FXML
    private TableColumn<Profile, Boolean> selectColumn;
    @FXML
    private TextField profileOpen;
    @FXML
    private TextField widthField;
    @FXML
    private CheckBox isSendEmail;
    @FXML
    private TextField heightField;

    private ProfileController profileController;
    private ProfileService profileService;
    private ProfileAutomation profileAutomation;
    private ProfileExecutor profileExecutor;

    public void turnOffResizeColumn() {
        nameColumn.setResizable(false);
        statusColumn.setResizable(false);
        proxyColumn.setResizable(false);
        lastRunColumn.setResizable(false);
        actionColumn.setResizable(false);
        selectColumn.setResizable(false);
    }

    public void setSizeColumns() {
        nameColumn.setPrefWidth(150);
        statusColumn.setPrefWidth(70);
        proxyColumn.setPrefWidth(200);
        lastRunColumn.setPrefWidth(200);
        actionColumn.setPrefWidth(200);
        selectColumn.setPrefWidth(30);
    }


    @FXML
    public void initialize() {
        profileService = new ProfileService();
        profileAutomation = new ProfileAutomation();
        profileExecutor = new ProfileExecutor();
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("group_id"));
        proxyColumn.setCellValueFactory(new PropertyValueFactory<>("raw_proxy"));
        lastRunColumn.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        profileController = new ProfileController();
        profileTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        clickAndSelectProfile();
        turnOffResizeColumn();
        setSizeColumns();
        selectDropdown();

        profileTable.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                TableHeaderRow header = (TableHeaderRow) profileTable.lookup("TableHeaderRow");
                header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        header.setReordering(false);
                    }
                });
            }
        });

    }

    public void clickAndSelectProfile() {
        profileTable.setRowFactory(tv -> {
            TableRow<Profile> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) { // Chỉ xử lý khi click một lần
                    Profile rowData = row.getItem();
                    rowData.setSelected(!rowData.isSelected());
                    profileTable.refresh();
                }
            });
            return row;
        });
    }

    @FXML
    private void handleGo() {
        List<Profile> profileList = profileController.fetchProfiles();
        if (profileList != null) {
            profileTable.getItems().setAll(profileList);
            List<String> groupList = profileList.stream().map(Profile::getGroup_id).distinct().collect(Collectors.toList());
            groupProfile.getItems().addAll(groupList);
        }
    }

    public List<Profile> getSelectedProfiles(List<Profile> profiles) {
        return profiles.stream().filter(Profile::isSelected).collect(Collectors.toList());
    }

    @FXML
    private void handleStart() {
        List<Profile> selectedProfiles = getSelectedProfiles(profileTable.getItems());
        for (Profile profile : selectedProfiles) {
            profileController.startProfile(profile);
        }
    }

    @FXML
    private void handleStop() {
        ExcelUtils excelUtils = new ExcelUtils("C:\\Users\\Vu\\Documents\\test_my_code.xlsx");
        Map<String, Map<String, String>> excelData = excelUtils.readExcel();

        // In ra tất cả dữ liệu đã đọc từ tệp Excel
        for (Map.Entry<String, Map<String, String>> entry : excelData.entrySet()) {
            String profileName = entry.getKey();
            Map<String, String> profileData = entry.getValue();
            System.out.println("Profile Name: " + profileName);
            for (Map.Entry<String, String> dataEntry : profileData.entrySet()) {
                System.out.println(dataEntry.getKey() + ": " + dataEntry.getValue());
            }
            System.out.println("--------------------------");


        }
        String targetEmail = "hungnguyen@gmail.com";

        if (excelData.containsKey(targetEmail)) {

        } else {
            System.out.println("Profile with email " + targetEmail + " not found.");
        }
    }

    @FXML
    private void nurtureProfile() {
        List<Profile> selectedProfiles = profileTable.getSelectionModel().getSelectedItems();
        if (selectedProfiles.isEmpty()) {
            System.out.println("No profiles selected.");
            return;
        }

        System.out.println("Số lượng profile chon " + selectedProfiles.size());
        List<CompletableFuture<Profile>> futures = selectedProfiles.stream()
                .map(profile -> CompletableFuture.supplyAsync(() -> profileService.getProfileData(profile)
                        .thenCompose(profileOpened -> CompletableFuture.runAsync(() -> {
                                    System.out.println("Running automation for profile: " + profileOpened.getId());
                                    profileAutomation.runAutomation(profileOpened);
                                    System.out.println("Finished automation for profile: " + profileOpened.getId());
                                }, profileExecutor.getExecutorService())
                                .thenApply(v -> profileOpened)).join(), profileExecutor.getExecutorService()))
                .collect(Collectors.toList());

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.thenRun(() -> {
            System.out.println("All profiles have been processed.");
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });

    }

    private void waitForProfileToBeReady(Profile profile) {
        while (profile.getDriver_path() == null || profile.getRemote_debugging_address() == null) {
            try {
                Thread.sleep(100); // Đợi 100ms trước khi kiểm tra lại
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void selectDropdown() {
        groupProfile.getItems().setAll("Apple", "Orange", "Pear");

        groupProfile.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldFruit, String newFruit) {
                if (oldFruit != null) {
                    switch (oldFruit) {
                        case "Apple":
                            System.out.println("Apple");
                            break;
                        case "Orange":
                            System.out.println("Orange");
                            break;
                        case "Pear":
                            System.out.println("Pear");
                            break;
                    }
                }

            }
        });
    }
}

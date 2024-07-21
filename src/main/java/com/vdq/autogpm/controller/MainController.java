package com.vdq.autogpm.controller;import com.vdq.autogpm.api.Group;import com.vdq.autogpm.api.Profile;import com.vdq.autogpm.automation.ProfileAutomation;import com.vdq.autogpm.automation.ProfileTask;import com.vdq.autogpm.executor.ProfileExecutor;import com.vdq.autogpm.service.ProfileService;import com.vdq.autogpm.util.ExcelUtils;import com.vdq.autogpm.util.Utils;import com.vdq.autogpm.webdriver.WebDriverManager;import javafx.beans.value.ChangeListener;import javafx.beans.value.ObservableValue;import javafx.fxml.FXML;import javafx.scene.control.*;import javafx.scene.control.cell.CheckBoxTableCell;import javafx.scene.control.cell.PropertyValueFactory;import javafx.scene.control.skin.TableHeaderRow;import org.openqa.selenium.WebDriver;import org.openqa.selenium.chrome.ChromeDriver;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import java.util.Map;import java.util.concurrent.CompletableFuture;import java.util.concurrent.CountDownLatch;import java.util.concurrent.ExecutionException;import java.util.logging.Logger;import java.util.stream.Collectors;public class MainController {    @FXML    private TableView<Profile> profileTable;    @FXML    private TableColumn<Profile, String> nameColumn;    @FXML    private TableColumn<Profile, String> statusColumn;    @FXML    private TableColumn<Profile, String> proxyColumn;    @FXML    private TableColumn<Profile, String> lastRunColumn;    @FXML    private TextField urlField;    @FXML    private TextArea logArea;    @FXML    private ComboBox groupProfile;    @FXML    private TableColumn<Profile, Void> actionColumn;    @FXML    private TableColumn<Profile, Boolean> selectColumn;    @FXML    private TextField profileOpen;    @FXML    private TextField passField;    @FXML    private CheckBox isSendEmail;    @FXML    private TextField heightField;    @FXML    private TextField passwordField;    @FXML    private CheckBox beraToWbera, beraToHoney, beraToStgusdc, beraToUsdt, beraToDai, beraToWbtc, beraToWeth;    @FXML    private CheckBox honeyToWbera, honeyToBera, honeyToStgusdc, honeyToUsdt, honeyToDai, honeyToWbtc, honeyToWeth;    private Map<String, List<String>> coinSwaps = new HashMap<>();    private ProfileController profileController;    private ProfileService profileService;    private ProfileAutomation profileAutomation;    private ProfileExecutor profileExecutor;    private Map<String, String> groupProfiles;    private static final Logger logger = Logger.getLogger(ProfileService.class.getName());    public void turnOffResizeColumn() {        nameColumn.setResizable(false);        statusColumn.setResizable(false);        proxyColumn.setResizable(false);        lastRunColumn.setResizable(false);        actionColumn.setResizable(false);        selectColumn.setResizable(false);    }    public void setSizeColumns() {        nameColumn.setPrefWidth(150);        statusColumn.setPrefWidth(70);        proxyColumn.setPrefWidth(200);        lastRunColumn.setPrefWidth(200);        actionColumn.setPrefWidth(200);        selectColumn.setPrefWidth(30);    }    private boolean getValueSelected() {        coinSwaps.clear();        // Clear previous selections        if (beraToWbera.isSelected()) addSwap("BERA", "WBERA");        if (beraToHoney.isSelected()) addSwap("BERA", "HONEY");        if (beraToStgusdc.isSelected()) addSwap("BERA", "STGUSDC");        if (beraToUsdt.isSelected()) addSwap("BERA", "USDT");        if (beraToDai.isSelected()) addSwap("BERA", "DAI");        if (beraToWbtc.isSelected()) addSwap("BERA", "WBTC");        if (beraToWeth.isSelected()) addSwap("BERA", "WETH");        if (honeyToWbera.isSelected()) addSwap("HONEY", "WBERA");        if (honeyToBera.isSelected()) addSwap("HONEY", "BERA");        if (honeyToStgusdc.isSelected()) addSwap("HONEY", "STGUSDC");        if (honeyToUsdt.isSelected()) addSwap("HONEY", "USDT");        if (honeyToDai.isSelected()) addSwap("HONEY", "DAI");        if (honeyToWbtc.isSelected()) addSwap("HONEY", "WBTC");        if (honeyToWeth.isSelected()) addSwap("HONEY", "WETH");        if (coinSwaps.isEmpty()) {            Utils.showWarningAlert("Không có lựa chọn", null, "Vui lòng chọn ít nhất 1 đồng Coin để Swap");            return false;        }        return true;    }    private void addSwap(String fromCoin, String toCoin) {        coinSwaps.computeIfAbsent(fromCoin, k -> new ArrayList<>()).add(toCoin);    }    @FXML    public void initialize() {        profileService = new ProfileService();        profileAutomation = new ProfileAutomation(coinSwaps);        profileExecutor = new ProfileExecutor();        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));        statusColumn.setCellValueFactory(new PropertyValueFactory<>("group_id"));        proxyColumn.setCellValueFactory(new PropertyValueFactory<>("raw_proxy"));        lastRunColumn.setCellValueFactory(new PropertyValueFactory<>("created_at"));        profileController = new ProfileController();        profileTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);        clickAndSelectProfile();        turnOffResizeColumn();        setSizeColumns();        selectDropdown();        profileTable.widthProperty().addListener(new ChangeListener<Number>() {            @Override            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {                TableHeaderRow header = (TableHeaderRow) profileTable.lookup("TableHeaderRow");                header.reorderingProperty().addListener(new ChangeListener<Boolean>() {                    @Override                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {                        header.setReordering(false);                    }                });            }        });    }    public void clickAndSelectProfile() {        profileTable.setRowFactory(tv -> {            TableRow<Profile> row = new TableRow<>();            row.setOnMouseClicked(event -> {                if (!row.isEmpty() && event.getClickCount() == 1) { // Chỉ xử lý khi click một lần                    Profile rowData = row.getItem();                    rowData.setSelected(!rowData.isSelected());                    profileTable.refresh();                }            });            return row;        });    }    @FXML    private void handleGo() {        profileController.fetchProfiles().thenAccept(profiles -> {            profileTable.getItems().setAll(profiles);        }).exceptionally(ex -> {            ex.printStackTrace();            return null;        });        profileController.getGroups().thenAccept(groups -> {            groupProfiles = new HashMap<>();            for (int i = 0; i < groups.size(); i++) {                groupProfile.getItems().add(groups.get(i).getName());                groupProfiles.put(groups.get(i).getId(), groups.get(i).getName());            }        }).exceptionally(ex -> {            ex.printStackTrace();            return null;        });    }    public List<Profile> getSelectedProfiles(List<Profile> profiles) {        return profiles.stream().filter(Profile::isSelected).collect(Collectors.toList());    }    @FXML    private void handleStart() {        List<Profile> selectedProfiles = getSelectedProfiles(profileTable.getItems());        for (Profile profile : selectedProfiles) {            profileController.startProfile(profile);        }    }    @FXML    private void handleStop() {        ExcelUtils excelUtils = new ExcelUtils("C:\\Users\\Vu\\Documents\\test_my_code.xlsx");        Map<String, Map<String, String>> excelData = excelUtils.readExcel();        // In ra tất cả dữ liệu đã đọc từ tệp Excel        for (Map.Entry<String, Map<String, String>> entry : excelData.entrySet()) {            String profileName = entry.getKey();            Map<String, String> profileData = entry.getValue();            logger.info("Profile Name: " + profileName);            for (Map.Entry<String, String> dataEntry : profileData.entrySet()) {                logger.info(dataEntry.getKey() + ": " + dataEntry.getValue());            }            logger.info("--------------------------");        }        String targetEmail = "hungnguyen@gmail.com";        if (excelData.containsKey(targetEmail)) {        } else {            logger.info("Profile with email " + targetEmail + " not found.");        }    }    @FXML    private void nurtureProfile() {        if (!getValueSelected()) {            return;        }        List<Profile> selectedProfiles = getSelectedProfiles(profileTable.getItems());        if (selectedProfiles.isEmpty()) {            Utils.showWarningAlert("Không có lựa chọn", null, "Bạn chưa chọn Profile nào!!!");            return;        }        if (passField.getText() == null || passField.getText().isEmpty()) {            Utils.showWarningAlert("Thông báo", null, "Vui long nhap mat khau !!!");            return;        }        List<CompletableFuture<Profile>> futures = selectedProfiles.stream()                .map(profile -> CompletableFuture.supplyAsync(() -> {                    ProfileTask task = new ProfileTask(profile, profileService, profileAutomation, passField.getText());                    try {                        return task.call();                    } catch (Exception e) {                        throw new RuntimeException(e);                    }                }, profileExecutor.getExecutorService()))                .toList();        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));        allOf.thenRun(() -> {            logger.info("All profiles have been processed.");            profileExecutor.shutdown();        }).exceptionally(ex -> {            ex.printStackTrace();            return null;        });        try {            allOf.get();        } catch (InterruptedException | ExecutionException e) {            e.printStackTrace();        }    }    private void waitForProfileToBeReady(Profile profile) {        while (profile.getDriver_path() == null || profile.getRemote_debugging_address() == null) {            try {                Thread.sleep(100); // Đợi 100ms trước khi kiểm tra lại            } catch (InterruptedException e) {                Thread.currentThread().interrupt();            }        }    }    public void selectDropdown() {        groupProfile.valueProperty().addListener((observable, oldValue, newValue) -> {            for (String profileId : groupProfiles.keySet()) {                if (groupProfiles.get(profileId).equals(newValue)) {                    logger.info("Profile ID " + profileId);                    profileController.fetchProfilesByGroup(profileId).thenAccept(profiles -> {                        profileTable.getItems().clear();                        profileTable.getItems().setAll(profiles);                    }).exceptionally(ex -> {                        ex.printStackTrace();                        return null;                    });                    break;                }            }        });    }}
package com.census.dashboard;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.chart.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.util.Map;
import java.util.HashMap;

public class CensusDashboardApp extends Application {
    private ApiClient apiClient;
    private TabPane tabPane;
    private TextArea statusArea;

    @Override
    public void start(Stage primaryStage) {
        apiClient = new ApiClient();

        primaryStage.setTitle("Census Data Analyzer Dashboard");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top: Title and controls
        VBox topBox = createTopSection();
        root.setTop(topBox);

        // Center: Tabs with different views
        tabPane = new TabPane();
        tabPane.getTabs().addAll(
            createOverviewTab(),
            createAgeAnalysisTab(),
            createEducationAnalysisTab(),
            createIncomeAnalysisTab(),
            createWorkHoursAnalysisTab()
        );
        root.setCenter(tabPane);

        // Bottom: Status area
        statusArea = new TextArea();
        statusArea.setEditable(false);
        statusArea.setPrefHeight(80);
        statusArea.setText("Ready. Click 'Refresh Data' to load statistics.");
        root.setBottom(statusArea);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createTopSection() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label titleLabel = new Label("Census Data Analysis Dashboard");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox controlsBox = new HBox(10);
        controlsBox.setAlignment(Pos.CENTER_LEFT);

        Button refreshBtn = new Button("Refresh Data");
        refreshBtn.setOnAction(e -> refreshAllData());

        Button exportBtn = new Button("Export Data");
        exportBtn.setOnAction(e -> showExportDialog());

        ComboBox<String> viewSelector = new ComboBox<>();
        viewSelector.setPromptText("Quick View");
        viewSelector.getItems().addAll("Overview", "Age", "Education", "Income", "Work Hours");
        viewSelector.setOnAction(e -> {
            String selected = viewSelector.getValue();
            if (selected != null) {
                switchToTab(selected);
            }
        });

        controlsBox.getChildren().addAll(refreshBtn, exportBtn, new Separator(), viewSelector);

        vbox.getChildren().addAll(titleLabel, controlsBox);
        return vbox;
    }

    private Tab createOverviewTab() {
        Tab tab = new Tab("Overview");
        tab.setClosable(false);

        VBox content = new VBox(15);
        content.setPadding(new Insets(15));

        Label titleLabel = new Label("Census Data Overview");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextArea overviewText = new TextArea();
        overviewText.setEditable(false);
        overviewText.setPrefHeight(400);
        overviewText.setWrapText(true);
        overviewText.setText("Click 'Refresh Data' to load census data statistics.\n\n" +
                "This dashboard provides comprehensive analysis of demographic data including:\n" +
                "- Age distribution and statistics\n" +
                "- Education level analysis\n" +
                "- Income distribution\n" +
                "- Work hours patterns\n\n" +
                "Use the tabs above to explore detailed visualizations and statistics.");

        Button loadBtn = new Button("Load Statistics");
        loadBtn.setOnAction(e -> {
            try {
                JsonObject response = apiClient.getAllStatistics();
                if (response.get("success").getAsBoolean()) {
                    JsonObject data = response.getAsJsonObject("data");
                    StringBuilder sb = new StringBuilder();
                    sb.append("=== CENSUS DATA STATISTICS ===\n\n");
                    
                    // Age statistics
                    if (data.has("age")) {
                        JsonObject age = data.getAsJsonObject("age");
                        sb.append("AGE STATISTICS:\n");
                        sb.append("  Mean Age: ").append(age.get("mean").getAsDouble()).append("\n");
                        sb.append("  Median Age: ").append(age.get("median").getAsDouble()).append("\n");
                        sb.append("  Min Age: ").append(age.get("min").getAsInt()).append("\n");
                        sb.append("  Max Age: ").append(age.get("max").getAsInt()).append("\n\n");
                    }
                    
                    // Education statistics
                    if (data.has("education")) {
                        JsonObject education = data.getAsJsonObject("education");
                        sb.append("EDUCATION DISTRIBUTION:\n");
                        JsonObject dist = education.getAsJsonObject("distribution");
                        for (Map.Entry<String, JsonElement> entry : dist.entrySet()) {
                            sb.append("  ").append(entry.getKey()).append(": ")
                              .append(entry.getValue().getAsInt()).append("\n");
                        }
                        sb.append("\n");
                    }
                    
                    // Income statistics
                    if (data.has("income")) {
                        JsonObject income = data.getAsJsonObject("income");
                        sb.append("INCOME DISTRIBUTION:\n");
                        JsonObject dist = income.getAsJsonObject("distribution");
                        for (Map.Entry<String, JsonElement> entry : dist.entrySet()) {
                            sb.append("  ").append(entry.getKey()).append(": ")
                              .append(entry.getValue().getAsInt()).append("\n");
                        }
                        sb.append("\n");
                    }
                    
                    // Work hours statistics
                    if (data.has("work_hours")) {
                        JsonObject workHours = data.getAsJsonObject("work_hours");
                        sb.append("WORK HOURS STATISTICS:\n");
                        sb.append("  Mean: ").append(workHours.get("mean").getAsDouble()).append(" hrs/week\n");
                        sb.append("  Median: ").append(workHours.get("median").getAsDouble()).append(" hrs/week\n");
                        sb.append("  Min: ").append(workHours.get("min").getAsDouble()).append(" hrs/week\n");
                        sb.append("  Max: ").append(workHours.get("max").getAsDouble()).append(" hrs/week\n");
                    }
                    
                    overviewText.setText(sb.toString());
                    updateStatus("Statistics loaded successfully!");
                }
            } catch (Exception ex) {
                updateStatus("Error loading statistics: " + ex.getMessage());
            }
        });

        content.getChildren().addAll(titleLabel, overviewText, loadBtn);
        tab.setContent(content);
        return tab;
    }

    private Tab createAgeAnalysisTab() {
        Tab tab = new Tab("Age Analysis");
        tab.setClosable(false);

        VBox content = new VBox(15);
        content.setPadding(new Insets(15));

        Label titleLabel = new Label("Age Distribution Analysis");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Create bar chart for age distribution
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Age Group Distribution");
        xAxis.setLabel("Age Group");
        yAxis.setLabel("Count");

        Button loadBtn = new Button("Load Age Data");
        loadBtn.setOnAction(e -> {
            try {
                JsonObject response = apiClient.getAgeStatistics();
                if (response.get("success").getAsBoolean()) {
                    JsonObject data = response.getAsJsonObject("data");
                    
                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName("Age Groups");
                    
                    if (data.has("age_groups")) {
                        JsonObject ageGroups = data.getAsJsonObject("age_groups");
                        for (Map.Entry<String, JsonElement> entry : ageGroups.entrySet()) {
                            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue().getAsInt()));
                        }
                    }
                    
                    barChart.getData().clear();
                    barChart.getData().add(series);
                    updateStatus("Age statistics loaded successfully!");
                }
            } catch (Exception ex) {
                updateStatus("Error loading age statistics: " + ex.getMessage());
            }
        });

        content.getChildren().addAll(titleLabel, barChart, loadBtn);
        tab.setContent(content);
        return tab;
    }

    private Tab createEducationAnalysisTab() {
        Tab tab = new Tab("Education Analysis");
        tab.setClosable(false);

        VBox content = new VBox(15);
        content.setPadding(new Insets(15));

        Label titleLabel = new Label("Education Level Analysis");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Create pie chart for education distribution
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Education Distribution");

        Button loadBtn = new Button("Load Education Data");
        loadBtn.setOnAction(e -> {
            try {
                JsonObject response = apiClient.getEducationStatistics();
                if (response.get("success").getAsBoolean()) {
                    JsonObject data = response.getAsJsonObject("data");
                    
                    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
                    
                    if (data.has("distribution")) {
                        JsonObject dist = data.getAsJsonObject("distribution");
                        for (Map.Entry<String, JsonElement> entry : dist.entrySet()) {
                            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue().getAsInt()));
                        }
                    }
                    
                    pieChart.setData(pieChartData);
                    updateStatus("Education statistics loaded successfully!");
                }
            } catch (Exception ex) {
                updateStatus("Error loading education statistics: " + ex.getMessage());
            }
        });

        content.getChildren().addAll(titleLabel, pieChart, loadBtn);
        tab.setContent(content);
        return tab;
    }

    private Tab createIncomeAnalysisTab() {
        Tab tab = new Tab("Income Analysis");
        tab.setClosable(false);

        VBox content = new VBox(15);
        content.setPadding(new Insets(15));

        Label titleLabel = new Label("Income Distribution Analysis");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Create bar chart for income distribution
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Income Distribution");
        xAxis.setLabel("Income Level");
        yAxis.setLabel("Count");

        Button loadBtn = new Button("Load Income Data");
        loadBtn.setOnAction(e -> {
            try {
                JsonObject response = apiClient.getIncomeStatistics();
                if (response.get("success").getAsBoolean()) {
                    JsonObject data = response.getAsJsonObject("data");
                    
                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName("Income Levels");
                    
                    if (data.has("distribution")) {
                        JsonObject dist = data.getAsJsonObject("distribution");
                        for (Map.Entry<String, JsonElement> entry : dist.entrySet()) {
                            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue().getAsInt()));
                        }
                    }
                    
                    barChart.getData().clear();
                    barChart.getData().add(series);
                    updateStatus("Income statistics loaded successfully!");
                }
            } catch (Exception ex) {
                updateStatus("Error loading income statistics: " + ex.getMessage());
            }
        });

        content.getChildren().addAll(titleLabel, barChart, loadBtn);
        tab.setContent(content);
        return tab;
    }

    private Tab createWorkHoursAnalysisTab() {
        Tab tab = new Tab("Work Hours Analysis");
        tab.setClosable(false);

        VBox content = new VBox(15);
        content.setPadding(new Insets(15));

        Label titleLabel = new Label("Work Hours Analysis");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Create bar chart for work hours categories
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Work Hours Distribution");
        xAxis.setLabel("Work Hours Category");
        yAxis.setLabel("Count");

        TextArea statsText = new TextArea();
        statsText.setEditable(false);
        statsText.setPrefHeight(150);

        Button loadBtn = new Button("Load Work Hours Data");
        loadBtn.setOnAction(e -> {
            try {
                JsonObject response = apiClient.getWorkHoursStatistics();
                if (response.get("success").getAsBoolean()) {
                    JsonObject data = response.getAsJsonObject("data");
                    
                    // Update text statistics
                    StringBuilder sb = new StringBuilder();
                    sb.append("Work Hours Statistics:\n");
                    sb.append("Mean: ").append(data.get("mean").getAsDouble()).append(" hrs/week\n");
                    sb.append("Median: ").append(data.get("median").getAsDouble()).append(" hrs/week\n");
                    sb.append("Min: ").append(data.get("min").getAsDouble()).append(" hrs/week\n");
                    sb.append("Max: ").append(data.get("max").getAsDouble()).append(" hrs/week\n");
                    statsText.setText(sb.toString());
                    
                    // Update chart
                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName("Work Hours Categories");
                    
                    if (data.has("categories")) {
                        JsonObject categories = data.getAsJsonObject("categories");
                        for (Map.Entry<String, JsonElement> entry : categories.entrySet()) {
                            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue().getAsInt()));
                        }
                    }
                    
                    barChart.getData().clear();
                    barChart.getData().add(series);
                    updateStatus("Work hours statistics loaded successfully!");
                }
            } catch (Exception ex) {
                updateStatus("Error loading work hours statistics: " + ex.getMessage());
            }
        });

        content.getChildren().addAll(titleLabel, statsText, barChart, loadBtn);
        tab.setContent(content);
        return tab;
    }

    private void refreshAllData() {
        updateStatus("Refreshing all data...");
        // This could be implemented to refresh all tabs
        updateStatus("Data refresh complete. Navigate to each tab to load specific data.");
    }

    private void showExportDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Export Data");
        dialog.setHeaderText("Choose export format");

        ButtonType exportButtonType = new ButtonType("Export", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(exportButtonType, ButtonType.CANCEL);

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        Label label = new Label("Select format:");
        ComboBox<String> formatBox = new ComboBox<>();
        formatBox.getItems().addAll("csv", "excel", "json");
        formatBox.setValue("csv");

        content.getChildren().addAll(label, formatBox);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == exportButtonType) {
                return formatBox.getValue();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(format -> {
            try {
                boolean success = apiClient.exportData(format, new HashMap<>());
                if (success) {
                    updateStatus("Data exported successfully as " + format);
                } else {
                    updateStatus("Export failed");
                }
            } catch (Exception ex) {
                updateStatus("Error exporting data: " + ex.getMessage());
            }
        });
    }

    private void switchToTab(String tabName) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(tabName)) {
                tabPane.getSelectionModel().select(tab);
                break;
            }
        }
    }

    private void updateStatus(String message) {
        statusArea.appendText("\n" + message);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

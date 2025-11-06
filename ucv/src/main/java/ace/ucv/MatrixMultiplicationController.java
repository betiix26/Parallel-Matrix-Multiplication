package ace.ucv;

import ace.ucv.approach.fork_join.ForkJoinStreamMatrixMultiplication;
import ace.ucv.approach.fork_join.RunForkJoinMultiplication;
import ace.ucv.approach.stream.StreamParallelMultiplication;
import ace.ucv.model.Matrix;
import ace.ucv.approach.parallel.ParallelMatrixMultiplication;
import ace.ucv.approach.strassen.StrassenMatrixMultiplication;
import ace.ucv.approach.sequential.SequentialMatrixMultiplication;
import ace.ucv.service.generator.RandomMatrixGenerator;
import ace.ucv.service.output.MatrixPrinter;
import ace.ucv.service.output.PerformanceMetricsRecorder;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class MatrixMultiplicationController {
    @FXML
    public RadioButton parallelStreamRadioButton;
    @FXML
    public RadioButton forkJoinRadioButton;
    @FXML
    private RadioButton classicSequentialButton;
    @FXML
    private RadioButton parallelButton;
    @FXML
    private RadioButton strassenButton;
    @FXML
    private TextField rowsMinField;
    @FXML
    private TextField rowsMaxField;
    @FXML
    private TextArea outputArea;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab outputTab;
    @FXML
    private Tab saveTab;

    private final MatrixPrinter printer = new MatrixPrinter();
    private final PerformanceMetricsRecorder metricsRecorder = new PerformanceMetricsRecorder("performance_metrics.xlsx");

    private Matrix matrixA;
    private Matrix matrixB;

    @FXML
    public void initialize() {
        ToggleGroup methodGroup = new ToggleGroup();
        classicSequentialButton.setToggleGroup(methodGroup);
        parallelButton.setToggleGroup(methodGroup);
        strassenButton.setToggleGroup(methodGroup);
        parallelStreamRadioButton.setToggleGroup(methodGroup);
        forkJoinRadioButton.setToggleGroup(methodGroup);

        classicSequentialButton.setSelected(true);
    }

    private void generateMatrices() {
        try {
            int rows = Integer.parseInt(rowsMinField.getText());
            int cols = Integer.parseInt(rowsMaxField.getText());

            RandomMatrixGenerator generator = new RandomMatrixGenerator();
            matrixA = generator.generateRandomMatrix(rows, cols);
            matrixB = generator.generateRandomMatrix(cols, rows);

            outputArea.appendText("Matrices generated successfully.\n");
        } catch (NumberFormatException e) {
            outputArea.setText("Invalid input for rows/cols");
        }
    }

    @FXML
    private void startMultiplication() {
        String selectedMethod = getSelectedMethod();
        if (selectedMethod == null) {
            outputArea.setText("No method selected. Please choose a method.");
            return;
        }

        if (matrixA == null || matrixB == null) {
            generateMatrices();
        }

        Task<String> multiplicationTask = new Task<>() {
            @Override
            protected String call() {
                long startTime = System.nanoTime();
                Matrix result = performMultiplication(selectedMethod);
                long endTime = System.nanoTime();

                double executionTime = (double) (endTime - startTime) / 1_000_000;
                metricsRecorder.recordMetric("Timings", selectedMethod + " Execution Time", executionTime);

                saveComputationDetails(selectedMethod, result);

                return selectedMethod + " completed.\nExecution Time: " + executionTime + " ms\n(Results saved to file.)";
            }
        };

        multiplicationTask.setOnSucceeded(event -> outputArea.setText(multiplicationTask.getValue()));
        multiplicationTask.setOnFailed(event -> outputArea.setText("An error occurred: " + multiplicationTask.getException().getMessage()));

        new Thread(multiplicationTask).start();
    }

    private String getSelectedMethod() {
        if (parallelStreamRadioButton.isSelected())
            return "ParallelStream";
        if (forkJoinRadioButton.isSelected())
            return "ForkJoin";
        if (classicSequentialButton.isSelected())
            return "Sequential";
        if (parallelButton.isSelected())
            return "Parallel";
        if (strassenButton.isSelected())
            return "Strassen";
        return null;
    }

    private Matrix performMultiplication(String method) {
        switch (method) {
            case "Sequential":
                return new SequentialMatrixMultiplication().multiply(matrixA, matrixB);
            case "Parallel":
                try {
                    return new ParallelMatrixMultiplication().multiply(matrixA, matrixB);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Parallel multiplication interrupted.", e);
                }
            case "Strassen":
                return new StrassenMatrixMultiplication().multiply(matrixA, matrixB);
            case "ParallelStream":
                return new StreamParallelMultiplication().multiply(matrixA, matrixB);
            case "ForkJoin":
                return ForkJoinStreamMatrixMultiplication.multiply(matrixA, matrixB);
            default:
                throw new IllegalArgumentException("Unknown method: " + method);
        }
    }

    private void saveComputationDetails(String method, Matrix result) {
        try {
            File outDir = new File("src/main/resources/out");
            if (!outDir.exists()) {
                outDir.mkdirs();
            }
            Path filePath = new File(outDir, method.toLowerCase() + "_computation.txt").toPath();

            String detailedLog = "";
            switch (method) {
                case "Strassen":
                    StrassenMatrixMultiplication strassen = new StrassenMatrixMultiplication();
                    strassen.multiply(matrixA, matrixB);
                    detailedLog = strassen.getLog();
                    break;
                case "Parallel":
                    ParallelMatrixMultiplication parallel = new ParallelMatrixMultiplication();
                    parallel.multiply(matrixA, matrixB);
                    detailedLog = parallel.getLog();
                    break;
                case "Sequential":
                	SequentialMatrixMultiplication sequential = new SequentialMatrixMultiplication();
                    sequential.multiply(matrixA, matrixB);
                    detailedLog = sequential.getLog();
                    break;
                case "ParallelStream":
                	StreamParallelMultiplication stream = new StreamParallelMultiplication();
                    stream.multiply(matrixA, matrixB);
                    detailedLog = stream.getLog();
                    break;
                case "ForkJoin":
                    RunForkJoinMultiplication fork = new RunForkJoinMultiplication();
                    result = fork.multiply(matrixA, matrixB);  
                    detailedLog = fork.getLog();               
                    break;
                    
            }

            printer.writeComputationLog(matrixA, matrixB, result, method, filePath, detailedLog);

        } catch (Exception e) {
            outputArea.appendText("\nError saving computation details: " + e.getMessage());
        }
    }


    @FXML
    private void saveOutputToFile() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Output");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                Files.write(file.toPath(), outputArea.getText().getBytes());
                outputArea.appendText("\nOutput saved successfully to " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            outputArea.appendText("\nError saving output: " + e.getMessage());
        }
    }

    @FXML
    private void savePerformanceMetrics() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Performance Metrics");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                metricsRecorder.saveToFile(file.getAbsolutePath());
                outputArea.setText("Performance metrics saved successfully to " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            outputArea.setText("Failed to save performance metrics: " + e.getMessage());
        }
    }

    public void onButtonHoverEnter(javafx.scene.input.MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-weight: bold;");
    }

    public void onButtonHoverExit(javafx.scene.input.MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
    }

    @FXML
    private void onExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Application");
        alert.setHeaderText("Confirm exit");
        alert.setContentText("Unsaved data will be lost. Are you sure?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            System.exit(0);
        }
    }
}

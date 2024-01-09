package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TaskManagerFX extends Application 
{
    private ListView<Task> taskListView;
    private TextField taskDescriptionField;
    private DatePicker datePicker;
    private Spinner<Integer> hourSpinner;
    private Spinner<Integer> minuteSpinner;
    private TaskList taskList;

    public static void main(String[] args) 
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) 
    {
        primaryStage.setTitle("Task Manager");

        taskListView = new ListView<>();
        taskDescriptionField = new TextField();
        datePicker = new DatePicker();
        hourSpinner = createSpinner(0, 23, 0); 
        minuteSpinner = createSpinner(0, 59, 0); 
        Button addButton = new Button("Add Task");
        Button markCompletedButton = new Button("Mark as Completed");

        taskList = new TaskList();

        addButton.setOnAction(event -> addTask());
        markCompletedButton.setOnAction(event -> markTaskAsCompleted());

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(
                new Label("Task Description:"),
                taskDescriptionField,
                new Label("Due Date:"),
                datePicker,
                new Label("Due Time:"),
                new HBox(hourSpinner, new Label(":"), minuteSpinner),
                addButton,
                new Separator(),
                new Label("Task List:"),
                taskListView,
                markCompletedButton
        );

        Scene scene = new Scene(vbox, 400, 500);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private Spinner<Integer> createSpinner(int min, int max, int initialValue) 
    {
        Spinner<Integer> spinner = new Spinner<>(min, max, initialValue);
        spinner.setEditable(true);
        return spinner;
    }

    private void addTask() 
    {
        String description = taskDescriptionField.getText();
        LocalDate dueDate = datePicker.getValue();
        int hour = hourSpinner.getValue();
        int minute = minuteSpinner.getValue();
        LocalTime dueTime = LocalTime.of(hour, minute);

        if (!description.isEmpty() && dueDate != null) 
        {
            LocalDateTime dueDateTime = LocalDateTime.of(dueDate, dueTime);
            Task task = new Task(description, dueDateTime);
            taskList.addTask(task);
            taskListView.getItems().add(task);
            taskDescriptionField.clear();
            datePicker.setValue(null);
            hourSpinner.getValueFactory().setValue(0);
            minuteSpinner.getValueFactory().setValue(0);
        }
    }

    private void markTaskAsCompleted() 
    {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            selectedTask.setCompleted(true);
            taskListView.refresh(); 
        }
    }

    private static class Task 
    {
        private String description;
        private LocalDateTime dueDateTime;
        private boolean completed;

        public Task(String description, LocalDateTime dueDateTime) 
        {
            this.description = description;
            this.dueDateTime = dueDateTime;
            this.completed = false;
        }

        public String getDescription() 
        {
            return description;
        }

        public LocalDateTime getDueDateTime() 
        {
            return dueDateTime;
        }

        public boolean isCompleted() 
        {
            return completed;
        }

        public void setCompleted(boolean completed) 
        {
            this.completed = completed;
        }

        @Override
        public String toString() 
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDueDateTime = dueDateTime.format(formatter);
            return description + " - Due: " + formattedDueDateTime + " - " +
                    (completed ? "Completed" : "Pending");
        }
    }

    private static class TaskList 
    {
        private java.util.List<Task> tasks;

        public TaskList() 
        {
            this.tasks = new java.util.ArrayList<>();
        }

        public void addTask(Task task) 
        {
            tasks.add(task);
        }
    }
}

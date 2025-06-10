import java.util.List;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class TaskRepository {

    private List<Task> tasks;
    private static final String dataFilePath = "data/tasks.json";

    public TaskRepository() {
        tasks = new ArrayList<>();
        loadTasks();
    }

    private void loadTasks() {
        File file = new File(dataFilePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Unable to create the task.json fiel");
                e.printStackTrace();
            }
        }

        StringBuilder tasksJSON = new StringBuilder("");
        try (FileReader fileReader = new FileReader(dataFilePath)) {
            int ch;
            while ((ch = fileReader.read()) != -1) {
                tasksJSON.append((char) ch);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (tasksJSON.length() == 0 || tasksJSON.toString().indexOf("},") == -1)
            return;
        for (String taskJSON : tasksJSON.toString().split("},")) {
            Task task = extractTaskFromJSON(taskJSON);
            tasks.add(task);
        }
    }

    private Task extractTaskFromJSON(String taskJSON) {
        int startIndex = -1, endIndex;
        List<String> taskProperties = new ArrayList<>();
        while ((startIndex = taskJSON.indexOf(": \"", startIndex + 1)) != -1) {
            endIndex = taskJSON.indexOf("\"", startIndex + 3);
            taskProperties.add(taskJSON.substring(startIndex + 3, endIndex));
        }

        TaskStatusLookup status = TaskStatusLookup.fromString(taskProperties.get(2));
        Date createdAt = DateTimeUtils.fromStringToDate(taskProperties.get(3));
        Date updatedAt = DateTimeUtils.fromStringToDate(taskProperties.get(4));

        Task task = new Task(Integer.parseInt(taskProperties.get(0)), taskProperties.get(1), status, createdAt,
                updatedAt);

        return task;
    }

    public void saveTasks() {
        try (FileWriter fileWriter = new FileWriter(dataFilePath)) {
            fileWriter.write("[");
            for (int i = 0; i < tasks.size(); i++) {
                fileWriter.write("\r\n");
                fileWriter.write(convertTaskToJSON(tasks.get(i)));
                if (i == tasks.size() - 1)
                    fileWriter.write("\r\n");
                else
                    fileWriter.write(",");
            }
            fileWriter.write("]");
            fileWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertTaskToJSON(Task task) {
        String taskTypeJSON = "  {\r\n" +
                "    \"id\": \"%d\",\r\n" +
                "    \"description\": \"%s\",\r\n" +
                "    \"status\": \"%s\",\r\n" +
                "    \"createdDate\": \"%s\",\r\n" +
                "    \"updatedDate\": \"%s\"\r\n" +
                "  }";

        return String.format(taskTypeJSON, task.getId(), task.getDescription(), task.getStatus(),
                task.getCreatedDate(),
                task.getUpdatedDate());
    }

    public void addTask(String Description) {
        int taskId = generateTaskId();
        Date createdDate = new Date();
        Task task = new Task(taskId, Description, TaskStatusLookup.TODO, createdDate, createdDate);
        tasks.add(task);
        saveTasks();
        System.out.printf("Task added successfully (ID: %d)\r\n", task.getId());
    }

    private int generateTaskId() {
        return tasks.stream().mapToInt(Task::getId).max().orElse(0) + 1;
    }

    public void removeTask(int taskId) {
        Task task = tasks.stream().filter(t -> t.getId() == taskId).findFirst().orElse(null);
        if (task != null) {
            tasks.remove(task);
            saveTasks();
        } else {
            System.out.printf("Task %d not found.\r\n", taskId);
        }
    }

    public void updateTask(int taskId, String description) {
        Task task = tasks.stream().filter(t -> t.getId() == taskId).findFirst().orElse(null);
        task.setDescription(description);
        task.setUpdatedDate(new Date());
        saveTasks();
    }

    public void updateTaskStatus(int taskId, TaskStatusLookup status) {
        Task task = tasks.stream().filter(t -> t.getId() == taskId).findFirst().orElse(null);
        task.setStatus(status);
        task.setUpdatedDate(new Date());
        saveTasks();
    }

    public void getTasks(TaskStatusLookup... args) {
        System.out.printf("%-5s | %-32s | %-15s | %-28s | %-28s%n",
                "ID", "Desc", "Status", "Created At", "Updated At");
        System.out.println(
                "----------------------------------------------------------------------------------------------------------------------");

        if (args.length == 0) {
            tasks.stream().forEach(task -> System.out.println(task.toString()));
        } else {
            TaskStatusLookup statusFilter = args[0];
            tasks.stream().filter(task -> task.getStatus().equals(statusFilter))
                    .forEach(task -> System.out.println(task.toString()));
        }
    }

}

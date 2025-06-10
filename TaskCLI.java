public class TaskCLI {
    public static void main(String[] args) {
        TaskRepository repo = new TaskRepository(); // load from file

        if (args.length < 1) {
            printUsage();
            return;
        }

        String command = args[0];

        switch (command) {
            case "add":
                if (args.length != 2) {
                    System.out.println("Usage: add \"Task title\"");
                    return;
                }
                repo.addTask(args[1]);
                break;

            case "delete":
                if (!validIdArgs(args, "delete <Task Id>"))
                    return;
                repo.removeTask(Integer.parseInt(args[1]));
                break;

            case "list":
                if (args.length == 1) {
                    repo.getTasks();
                } else if (args.length == 2) {
                    if (isNotValidTaskStatus(args[1])) {
                        System.out.println(args[1] + " is not a valid task status");
                        return;
                    }
                    repo.getTasks(TaskStatusLookup.fromString(args[1]));
                } else {
                    System.out.println("Usage: list [status]");
                }
                break;

            case "update":
                if (args.length != 3) {
                    System.out.println("Usage: update <Task Id> \"New Task Title\"");
                    return;
                }
                if (isNotValidTaskId(args[1])) {
                    System.out.println(args[1] + " is not a valid task id.");
                    return;
                }
                repo.updateTask(Integer.parseInt(args[1]), args[2]);
                break;

            case "mark-done":
                if (!validIdArgs(args, "mark-done <Task Id>"))
                    return;
                repo.updateTaskStatus(Integer.parseInt(args[1]), TaskStatusLookup.DONE);
                break;

            case "mark-in-progress":
                if (!validIdArgs(args, "mark-in-progress <Task Id>"))
                    return;
                repo.updateTaskStatus(Integer.parseInt(args[1]), TaskStatusLookup.IN_PROGRESS);
                break;

            default:
                System.out.println("Unknown command: " + command);
                printUsage();
        }
    }

    private static boolean validIdArgs(String[] args, String usage) {
        if (args.length != 2) {
            System.out.println("Usage: " + usage);
            return false;
        }
        if (isNotValidTaskId(args[1])) {
            System.out.println(args[1] + " is not a valid task id.");
            return false;
        }
        return true;
    }

    private static boolean isNotValidTaskId(String taskId) {
        try {
            Integer.parseInt(taskId);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private static boolean isNotValidTaskStatus(String status) {
        try {
            TaskStatusLookup.fromString(status);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    private static void printUsage() {
        System.out.println("""
                Usage: java TaskCLI <command> [args]
                Commands:
                  add "description"
                  update <id> "new description"
                  delete <id>
                  mark-done <id>
                  mark-in-progress <id>
                  list
                  list [todo|in-progress|done]
                """);
    }
}

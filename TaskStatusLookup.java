public enum TaskStatusLookup {
    TODO("Todo"),
    IN_PROGRESS("In-Progress"),
    DONE("Done");

    private final String status;

    TaskStatusLookup(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static TaskStatusLookup fromString(String status) {
        for (TaskStatusLookup s : TaskStatusLookup.values()) {
            if (s.toString().equalsIgnoreCase(status))
                return s;
        }
        throw new IllegalArgumentException("No enum constant for value: " + status);
    }

    @Override
    public String toString() {
        return status;
    }
}
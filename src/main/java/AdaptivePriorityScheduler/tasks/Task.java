package AdaptivePriorityScheduler.tasks;

import java.time.LocalDate;
import java.util.List;

public class Task {
    private int id;
    private String title;
    private LocalDate deadline;
    private float effortHours;
    private String category;
    private TaskStatus status;
    private List<String> dependencyIds;

    public Task(int idGiven, String titleGiven, LocalDate deadlineGiven,  float effortHoursGiven, String categoryGiven, TaskStatus status, List<String> dependencyIds) {

    }

    public String getSlack() {
        return "getSlack";
    }
}

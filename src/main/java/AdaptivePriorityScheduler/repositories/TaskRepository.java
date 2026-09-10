package AdaptivePriorityScheduler.repositories;

import AdaptivePriorityScheduler.tasks.Task;
import AdaptivePriorityScheduler.tasks.TaskStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class TaskRepository {

    private Map<String, Task> taskMap = new HashMap<>();

    public TaskRepository() {
        taskMap.put("11", new Task(11, "Homework", LocalDate.of(2021, 11, 2),  1, 1,"School", TaskStatus.NOT_STARTED, new ArrayList<>()));
    }

    public Task getTask(int id) {
        return taskMap.get(id);
    }

    public Collection<Task> getTaskList() {
        return taskMap.values();
    }

    public void setTask(Task task) {
        taskMap.put(task.getTaskName(), task);
    }

}



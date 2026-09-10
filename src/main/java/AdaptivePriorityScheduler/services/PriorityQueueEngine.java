package AdaptivePriorityScheduler.services;

import AdaptivePriorityScheduler.tasks.Task;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PriorityQueueEngine {
    private List<Task> rankedTasks = new ArrayList<>();

    public PriorityQueueEngine() {

    }


    public Collection<Task> rank(Collection<Task> tasks) {
        List<Task> sorted = new ArrayList<>(tasks);

        sorted.sort(
                Comparator.comparing(Task::getSlack)
                        .thenComparing(Task::getTaskName));

        this.rankedTasks = sorted; // store it
        return sorted;
    }



    public void onTaskChange(Collection<Task> tasks) {
        rank(tasks);
    }


    public List<Task> getRankedTasks() {
        return rankedTasks;
    }
}
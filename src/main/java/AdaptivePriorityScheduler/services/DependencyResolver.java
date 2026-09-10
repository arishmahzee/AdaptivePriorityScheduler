package AdaptivePriorityScheduler.services;

import AdaptivePriorityScheduler.tasks.Task;
import AdaptivePriorityScheduler.tasks.TaskStatus;

import java.util.*;


public class DependencyResolver {

    public DependencyResolver() {
    }

    public boolean isBlocked(Task task) {
        if (task.getDirectDependency() == null) {
            return false;
        }

        for (Task dependency : task.getDirectDependency()) {
            if (dependency.getStatus() != TaskStatus.DONE) {
                return true;
            }
        }
        return false;
    }


    public float getSumDependenciesTime(Task task) {
        float total = 0;

        if (task.getIndirectDependency() != null) {
            for (Task dependency : task.getIndirectDependency()) {
                total += dependency.getHoursNeeded();
            }
        }

        return total;
    }


    public void updateDependencyTime(Task finishedTask, List<Task> allTasks) {
        for (Task task : allTasks) {
            if (task.getIndirectDependency() != null && task.getIndirectDependency().contains(finishedTask)) {
                task.getIndirectDependency().remove(finishedTask);
                task.setDependencyTime();
            }
        }
    }
}
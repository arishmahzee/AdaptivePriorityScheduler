package AdaptivePriorityScheduler.controllers;
import java.util.*;

import AdaptivePriorityScheduler.pyAPI.PythonClient;
import AdaptivePriorityScheduler.repositories.TaskRepository;
import AdaptivePriorityScheduler.services.PriorityQueueEngine;
import AdaptivePriorityScheduler.tasks.Task;
import AdaptivePriorityScheduler.tasks.TaskStatus;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class TaskController {
    private final TaskRepository taskRepository;
    private final PriorityQueueEngine priorityQueueEngine;
    private final PythonClient pythonClient;

    public TaskController(TaskRepository taskRepository, PriorityQueueEngine priorityQueueEngine, PythonClient pythonClient) {
            this.taskRepository = taskRepository;
            this.priorityQueueEngine = priorityQueueEngine;
            this.pythonClient = pythonClient;
        }


    @GetMapping("/seeTask/{id}")
    public Task getTask(@PathVariable int id)
    {
        Task task = this.taskRepository.getTask(id);
        return task;

    }

    @GetMapping("/tasksList")
    public Collection<Task> getTaskList()
    {
        return priorityQueueEngine.getRankedTasks();
    }

    //creates a new task from incoming data - JSON FORM
    @PostMapping("/addTask")
    public Task createTask(@RequestBody Task task)
    {
        double predicted = pythonClient.callPredictDuration(task);
        task.setPredictedHours(predicted);
        this.taskRepository.setTask(task);
        return task;
    }


    @PutMapping("/editTask/{id}")
    public Task editTask(@PathVariable int id, @RequestBody Task updatedTask)
    {
        //updatedTask.setId(id);
        this.taskRepository.setTask(updatedTask);
        priorityQueueEngine.onTaskChange(getTaskList());
        return updatedTask;
    }


}
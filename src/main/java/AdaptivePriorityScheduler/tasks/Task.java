package AdaptivePriorityScheduler.tasks;

import AdaptivePriorityScheduler.services.DependencyResolver;
import AdaptivePriorityScheduler.services.SlackCalculator;

import java.time.LocalDate;
import java.util.*;


public class Task {
    private int id;
    private String taskName;
    private LocalDate deadline;
    private float hoursNeeded; // how many total hoyrs till completion
    private float minDailyHours; //mimimum hours a user can spend on this task in a day
    private String category; //the category of tasks - chosen by the user
    private TaskStatus status; // how urgent it is
    private float maxDependencyTime; // a task dependent on this task will have a max blockage time caused by this task if it is not completed
    private float slack; //how many free days the user has before this becomes urgent

    private double predictedHours; // links to tje python microservice

    private final SlackCalculator slackCalculator;
    private final DependencyResolver dependencyResolver;


    private static final int MAX_DEPENDENCIES_LIMIT = 5; //a limit for direct deppendancies only

    private List<Task> directDependency;

    private Set<Task> indirectDependency; //derived automatically from directDependency.


    public Task(int idGiven, String taskNameGiven, LocalDate deadlineGiven, float hoursNeededGiven,
                float minDailyHoursGiven, String categoryGiven, TaskStatus statusGiven,
                List<Task> directDependencyGiven) {
        this.id = idGiven;
        this.taskName = taskNameGiven;
        this.deadline = deadlineGiven;
        this.hoursNeeded = hoursNeededGiven;
        this.minDailyHours = minDailyHoursGiven;
        this.slackCalculator = new SlackCalculator();
        this.dependencyResolver = new DependencyResolver();
        this.category = categoryGiven;
        this.status = statusGiven;
        setDirectDependency(directDependencyGiven);
        setDependencyTime();
    }

    //Getter methods

    public int getId () {

        return this.id;
    }

    public String getTaskName() {

        return this.taskName;
    }

    public LocalDate getDeadline() {

        return this.deadline;
    }

    public float getHoursNeeded() {

        return this.hoursNeeded;
    }

    public float getMinDailyHours() {

        return this.minDailyHours;
    }

    public String getCategory() {
        return this.category;
    }

    public TaskStatus getStatus() {

        return this.status;
    }

    public List<Task> getDirectDependency() {

        return this.directDependency;
    }

    public Set<Task> getIndirectDependency() {

        return this.indirectDependency;
    }


    // get stuff
    public float getDependencyTime(){

        return this.maxDependencyTime;
    }

    public float getSlack(){

        return slackCalculator.calculate(this);
    }


    public double getPredictedHours() {
        return predictedHours;
    }


    //Setter methods

    public void setPredictedHours(double predictedHours) {

        this.predictedHours = predictedHours;
    }

    public void setDependencyTime(){

        this.maxDependencyTime = dependencyResolver.getSumDependenciesTime(this);
    }


    public void setId (int idGiven) {

        this.id = idGiven;
    }

    public void setTaskName(String taskNameGiven) {

        this.taskName = taskNameGiven;
    }

    public void setDeadline(LocalDate deadlineGiven) {

        this.deadline = deadlineGiven;
    }

    public void setHoursNeeded(float hoursNeededGiven) {

        this.hoursNeeded = hoursNeededGiven;
    }

    public void setMinDailyHours(float minDailyHoursGiven) {

        this.minDailyHours = minDailyHoursGiven;
    }

    public void setCategory(String categoryGiven) {

        this.category =  categoryGiven;
    }

    public void setStatus(TaskStatus taskStatusGiven) {

        this.status =  taskStatusGiven;
    }


    public void setDirectDependency(List<Task> directDependencyGiven) {
        if (directDependencyGiven != null && directDependencyGiven.size() > MAX_DEPENDENCIES_LIMIT) {
            throw new IllegalArgumentException( //will be checked in js frontend too but jst in case theres one here too
                    "Cannot set more than " + MAX_DEPENDENCIES_LIMIT + " direct dependencies for task " + id);
        }

        this.directDependency = directDependencyGiven;
        rebuildIndirectDependency();
    }


    private void rebuildIndirectDependency() {
        Set<Task> combined = new HashSet<>();


        if (this.directDependency != null) {
            for (Task dependency : this.directDependency) {

                combined.add(dependency);

                if(dependency.getDirectDependency() != null) {
                    combined.addAll(dependency.getDirectDependency());
                }

                if(dependency.getIndirectDependency() != null) {
                    combined.addAll(dependency.getIndirectDependency());
                }

            }
        }

        combined.remove(this);
        this.indirectDependency = combined;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        return id == ((Task) o).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
package AdaptivePriorityScheduler.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import AdaptivePriorityScheduler.tasks.Task;

public class SlackCalculator {

    public SlackCalculator() {

    }


    public float calculate(Task task) {
        LocalDate deadlineDate = task.getDeadline();
        LocalDate todayDate = LocalDate.now();

        float daysUntilDeadline = ChronoUnit.DAYS.between(todayDate, deadlineDate);

        float daysNeededForTask = task.getHoursNeeded() / task.getMinDailyHours();
        float daysNeededForDependencies = task.getDependencyTime() / task.getMinDailyHours();

        float totalDaysNeeded = daysNeededForTask + daysNeededForDependencies;

        float slack = daysUntilDeadline - totalDaysNeeded;


        return (float) Math.ceil(slack);
    }
}
package tasks.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.model.TasksOperations;

import java.time.LocalDate;
import java.util.Date;

public class TasksService {
    private final ArrayTaskList tasks;
    private final DateService dateService = new DateService(this);

    public TasksService(ArrayTaskList tasks) {
        this.tasks = tasks;
    }

    public ObservableList<Task> getObservableList() {
        return FXCollections.observableArrayList(tasks.getAll());
    }

    public String getIntervalInHours(Task task) {
        int seconds = task.getRepeatInterval();
        int minutes = seconds / DateService.SECONDS_IN_MINUTE;
        int hours = minutes / DateService.MINUTES_IN_HOUR;
        minutes = minutes % DateService.MINUTES_IN_HOUR;
        return formTimeUnit(hours) + ":" + formTimeUnit(minutes);//hh:MM
    }

    public String formTimeUnit(int timeUnit) {
        StringBuilder sb = new StringBuilder();
        if (timeUnit < 10) sb.append("0");
        if (timeUnit == 0) sb.append("0");
        else {
            sb.append(timeUnit);
        }
        return sb.toString();
    }

    public int parseFromStringToSeconds(String stringTime) {//hh:MM
        String[] units = stringTime.split(":");
        int hours = Integer.parseInt(units[0]);
        int minutes = Integer.parseInt(units[1]);
        return (hours * DateService.MINUTES_IN_HOUR + minutes) * DateService.SECONDS_IN_MINUTE;
    }

    public Iterable<Task> filterTasks(Date start, Date end) {
        TasksOperations tasksOps = new TasksOperations(getObservableList());
        return tasksOps.incoming(start, end);
    }

    public Task addNewTask(String title, String description, Date startDate, Date endDate,
                           Integer interval, boolean isActive) {
        Task result;
        if (endDate != null && interval != null) {
            if (startDate.after(endDate)) throw new IllegalArgumentException("Start date should be before end");
            result = new Task(title, description, startDate, endDate, interval);
        } else {
            result = new Task(title, description, startDate);
        }
        result.setActive(isActive);
        System.out.println(result);
        return result;
    }
}
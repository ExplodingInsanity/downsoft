package tasks.repository;

import tasks.model.ArrayTaskList;

import java.io.IOException;

public interface ITaskRepository {
    ArrayTaskList getAll() throws IOException;
    }
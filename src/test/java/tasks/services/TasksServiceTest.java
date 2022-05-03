package tasks.services;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.repository.ITaskRepository;
import tasks.repository.TaskRepository;
import tasks.view.Main;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.Random.class)
class TasksServiceTest {

    private static final Date END_DATE = new Date("2022/03/19");
    private static final Date START_DATE = new Date("2022/03/17");
    private static final String DESCRIPTION = "De dat cu mopul in baie";
    static ArrayTaskList tasksList;
    static TasksService tasksService;
    static Task task;

    @BeforeAll
    static void initialSetup() {
        tasksList = new ArrayTaskList();
        tasksService = new TasksService(tasksList);
    }

    @BeforeEach
    void setUp() {
        task = null;
    }

    @AfterEach
    void tearDown() {
        if (tasksService.getObservableList().size() > 0) {
            tasksService.getObservableList().clear();
        }
    }

    @Test
    @DisplayName("Test valid => se adauga task")
    void testValid() throws ParseException {
        validTasksProvider().forEach(arg -> {
            Assertions.assertDoesNotThrow(() -> {
                tasksService.createNewTask((String) arg.get()[0], DESCRIPTION, START_DATE, END_DATE, (Integer) arg.get()[1], true);
            });
        });
    }

    private static Stream<Arguments> validTasksProvider() {
        return Stream.of(
                Arguments.of("Spala pe jos", 1440),
                Arguments.of("M", 2),
                Arguments.of("M123123123123123123", 2), //19 caractere
                Arguments.of("M1231231231231231231", 2), //20 caractere
                Arguments.of("M", 1),
                Arguments.of("M", 2),
                Arguments.of("M", Integer.MAX_VALUE - 1),
                Arguments.of("M", Integer.MAX_VALUE)
        );
    }

    @Test
    @DisplayName("Test invalid title => se adauga task")
    void testInvalidTitle() throws ParseException {
        invalidTitleTasksProvider().forEach(arg -> {
            Assertions.assertThrows(IllegalArgumentException.class, (Executable) () -> tasksService.createNewTask((String) arg.get()[0], DESCRIPTION, START_DATE, END_DATE, (Integer) arg.get()[1], true), "Invalid title");
        });
    }

    private static Stream<Arguments> invalidTitleTasksProvider() {
        return Stream.of(
                Arguments.of("Cand incepe sa ploua afara te duci si speli masina cu furtunul de gradina", 24),
                Arguments.of("", 2)
        );
    }

    @Test
    @DisplayName("Test invalid interval => se adauga task")
    void testInvalidInterval() throws ParseException {
        invalidIntervalTasksProvider().forEach(arg -> {
            Assertions.assertThrows(IllegalArgumentException.class, (Executable) () -> tasksService.createNewTask((String) arg.get()[0], DESCRIPTION, START_DATE, END_DATE, (Integer) arg.get()[1], true), "Invalid interval");
        });
    }

    private static Stream<Arguments> invalidIntervalTasksProvider() {
        return Stream.of(
                Arguments.of("Spala pe jos", -6),
                Arguments.of("M", 0),
                Arguments.of("M", Integer.MAX_VALUE+1)
        );
    }

    // lab 4

    // integration testing step 1 - unit tests
    @Test
    void test_addTask() throws IOException {
        ITaskRepository repo = mock(ITaskRepository.class);
        ArrayTaskList arrayTaskList = new ArrayTaskList();
        arrayTaskList.add(mockTask(1990,1995));
        arrayTaskList.add(mockTask(1995,2000));
        arrayTaskList.add(mockTask(2000,2005));
        arrayTaskList.add(mockTask(2005,2010));
        when(repo.getAll()).thenReturn(arrayTaskList);
        TasksService service = new TasksService(repo);
        Date start = Date.from(Instant.parse("2010-12-12T00:00:00Z"));
        Date end = Date.from(Instant.parse("2015-12-12T00:00:00Z"));
        service.addAndCreateNewTask("new","description",start,end,3600,true);
        ObservableList<Task> list = service.getObservableList();
        Assertions.assertEquals(5,list.size());
        Assertions.assertEquals(start,list.get(list.size()-1).getStartTime());
        Assertions.assertEquals(end,list.get(list.size()-1).getEndTime());
        Assertions.assertEquals("new",list.get(list.size()-1).getTitle());
    }

    @Test
    void test_filterTasks() throws IOException {
        ITaskRepository repo = mock(ITaskRepository.class);
        ArrayTaskList arrayTaskList = new ArrayTaskList();
        arrayTaskList.add(mockTask(1990,1995));
        arrayTaskList.add(mockTask(1995,2000));
        arrayTaskList.add(mockTask(2000,2005));
        arrayTaskList.add(mockTask(2005,2010));
        when(repo.getAll()).thenReturn(arrayTaskList);
        TasksService service = new TasksService(repo);
        Date start = Date.from(Instant.parse("1997-12-12T00:00:00Z"));
        Date end = Date.from(Instant.parse("2007-12-12T00:00:00Z"));
        List<Task> filteredTasks = service.filterTasks(start, end);
        Assertions.assertEquals(2,filteredTasks.size());
    }

    // integration testing step 3 - service+repository+entities
    @Test
    void test_addTask_int2() throws IOException {
        TaskRepository repo = new TaskRepository(new File(Main.class.getClassLoader().getResource("data/tasks.txt").getFile()));
        TasksService service = new TasksService(repo);
        Date start = Date.from(Instant.parse("2010-12-12T00:00:00Z"));
        Date end = Date.from(Instant.parse("2015-12-12T00:00:00Z"));
        service.addAndCreateNewTask("new","description",start,end,3600,true);
        ObservableList<Task> list = service.getObservableList();
        Assertions.assertEquals(3,list.size());
        Assertions.assertEquals(start,list.get(list.size()-1).getStartTime());
        Assertions.assertEquals(end,list.get(list.size()-1).getEndTime());
        Assertions.assertEquals("new",list.get(list.size()-1).getTitle());
    }

    @Test
    void test_filterTasks_int2() throws IOException {
        TaskRepository repo = new TaskRepository(new File(Main.class.getClassLoader().getResource("data/tasks.txt").getFile()));
        TasksService service = new TasksService(repo);
        Date start = Date.from(Instant.parse("1980-12-12T00:00:00Z"));
        Date end = Date.from(Instant.parse("2007-12-12T00:00:00Z"));
        List<Task> filteredTasks = service.filterTasks(start, end);
        Assertions.assertEquals(2,filteredTasks.size());
    }

    Task mockTask(Integer startYear, Integer endYear){
        Task mock = mock(Task.class);
        when(mock.getTitle()).thenReturn("title");
        when(mock.getDescription()).thenReturn("description");
        when(mock.getStartTime()).thenReturn(Date.from(Instant.parse(""+startYear+"-12-10T00:00:00Z")));
        when(mock.getEndTime()).thenReturn(Date.from(Instant.parse(""+endYear+"-12-10T00:00:00Z")));
        return mock;
    }

    Task makeTask(Integer startYear, Integer endYear){
        return new Task("title","description", Date.from(Instant.parse(""+startYear+"-12-10T00:00:00Z")),Date.from(Instant.parse(""+endYear+"-12-10T00:00:00Z")),3600);
    }
}
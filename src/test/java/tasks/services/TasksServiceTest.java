package tasks.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tasks.model.ArrayTaskList;
import tasks.model.Task;

import java.text.ParseException;
import java.util.Date;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @DisplayName("Test valid => se adauga task")
    @MethodSource(value = "validTasksProvider")
    void testValid(String title, int interval) throws ParseException {
        Assertions.assertDoesNotThrow(() -> {
            tasksService.addNewTask(title, DESCRIPTION, START_DATE, END_DATE, interval, true);
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

    @ParameterizedTest
    @DisplayName("Test invalid title => se adauga task")
    @MethodSource(value = "invalidTitleTasksProvider")
    void testInvalidTitle(String title, int interval) throws ParseException {
        Assertions.assertThrows(IllegalArgumentException.class, (Executable) () -> tasksService.addNewTask(title, DESCRIPTION, START_DATE, END_DATE, interval, true), "Invalid title");
    }

    private static Stream<Arguments> invalidTitleTasksProvider() {
        return Stream.of(
                Arguments.of("Cand incepe sa ploua afara te duci si speli masina cu furtunul de gradina", 24),
                Arguments.of("", 2)
        );
    }

    @ParameterizedTest
    @DisplayName("Test invalid interval => se adauga task")
    @MethodSource(value = "invalidIntervalTasksProvider")
    void testInvalidInterval(String title, int interval) throws ParseException {
        Assertions.assertThrows(IllegalArgumentException.class, (Executable) () -> tasksService.addNewTask(title, DESCRIPTION, START_DATE, END_DATE, interval, true), "Invalid interval");
    }

    private static Stream<Arguments> invalidIntervalTasksProvider() {
        return Stream.of(
                Arguments.of("Spala pe jos", -6),
                Arguments.of("M", 0),
                Arguments.of("M", Integer.MAX_VALUE+1)
        );
    }
}
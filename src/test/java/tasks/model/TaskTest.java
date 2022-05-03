package tasks.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        try {
            task = new Task("new task", " ", Task.getDateFormat().parse("2021-02-12 10:10"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testTaskCreation() throws ParseException {
        assert task.getTitle() == "new task";
        System.out.println(task.getFormattedDateStart());
        System.out.println(task.getDateFormat().format(Task.getDateFormat().parse("2021-02-12 10:10")));
        assert task.getFormattedDateStart().equals(task.getDateFormat().format(Task.getDateFormat().parse("2021-02-12 10:10")));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void computeNextOccurrence1() {
        Task t = new Task("null","null", new Date(),Date.from(Instant.parse("2030-12-12T00:00:00Z")),1);
        assertEquals(null, t.computeNextOccurrence(Date.from(Instant.parse("1990-12-12T00:00:00Z")),null,null, Date.from(Instant.parse("2020-12-01T00:00:00Z")),null));
    }
    @Test
    void computeNextOccurrence2() {
        Task t = new Task("null","null", new Date(),Date.from(Instant.parse("2030-12-12T00:00:00Z")),1);
        assertEquals(null, t.computeNextOccurrence(Date.from(Instant.parse("2020-12-12T00:00:00Z")),null,null, Date.from(Instant.parse("2020-12-12T00:00:00Z")),Date.from(Instant.parse("2020-12-12T00:00:00Z"))));
    }
    @Test
    void computeNextOccurrence3() {
        Task t = new Task("null","null", new Date(),Date.from(Instant.parse("2030-12-12T00:00:00Z")),1);
        assertEquals(Date.from(Instant.parse("2020-12-12T00:00:01Z")), t.computeNextOccurrence(Date.from(Instant.parse("2020-12-12T00:00:00Z")),null,Date.from(Instant.parse("2020-12-12T00:00:00Z")), Date.from(Instant.parse("2020-12-12T00:00:00Z")),Date.from(Instant.parse("2020-12-14T00:00:00Z"))));
    }
    @Test
    void computeNextOccurrence4() {
        Task t = new Task("null","null", new Date(),Date.from(Instant.parse("2030-12-12T00:00:00Z")),1);
        assertEquals(Date.from(Instant.parse("2020-12-10T00:00:00Z")), t.computeNextOccurrence(Date.from(Instant.parse("2020-12-12T00:00:00Z")),Date.from(Instant.parse("2020-12-10T00:00:00Z")),Date.from(Instant.parse("2020-12-14T00:00:00Z")), Date.from(Instant.parse("2020-12-12T00:00:00Z")),Date.from(Instant.parse("2020-12-14T00:00:00Z"))));
    }
    @Test
    void computeNextOccurrence5() {
        Task t = new Task("null","null", new Date(),Date.from(Instant.parse("2030-12-12T00:00:00Z")),1);
        assertEquals(null, t.computeNextOccurrence(Date.from(Instant.parse("2020-12-12T00:00:00Z")),Date.from(Instant.parse("2020-12-10T00:00:00Z")),Date.from(Instant.parse("2020-12-10T00:00:01Z")), Date.from(Instant.parse("2020-12-12T00:00:00Z")),Date.from(Instant.parse("2020-12-12T00:00:01Z"))));
    }

    // lab 4
    @Test
    void test_getTitle(){
        Task t = new Task("abc","def",Date.from(Instant.parse("2030-12-12T00:00:00Z")));
        assertEquals(t.getTitle(),"abc");
    }
    @Test
    void test_setTitle(){
        Task t = new Task("abc","def",Date.from(Instant.parse("2030-12-12T00:00:00Z")));
        assertNotEquals(t.getTitle(),"aaaa");
        t.setTitle("aaaa");
        assertEquals(t.getTitle(),"aaaa");
    }
}

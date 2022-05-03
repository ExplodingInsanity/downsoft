package tasks.repository;

import org.junit.jupiter.api.Test;
import tasks.model.Task;

import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TaskRepositoryTest {
    @Test
    void test_getTaskFromString_noEndTime() throws ParseException {
        String line = "\"dsadwe\" \"ewqeds\" at [2022-03-29 08:00:00.000] inactive.\n";
        Task t = TaskRepository.getTaskFromString(line);
        assertEquals("dsadwe",t.getTitle());
        assertEquals("ewqeds",t.getDescription());
        assertEquals(TaskRepository.simpleDateFormat.parse("[2022-03-29 08:00:00.000]"), t.getStartTime());
        assertEquals(TaskRepository.simpleDateFormat.parse("[2022-03-29 08:00:00.000]"), t.getEndTime());
    }

    @Test
    void test_getTaskFromString_withEndTime() throws ParseException {
        String line = "\"wdqdsaf\" \"cxvcxvdsv\" from [2022-03-29 08:00:00.000] to [2022-03-29 10:00:00.000] every [1 day 6 hours 1 minute ] inactive.";
        Task t = TaskRepository.getTaskFromString(line);
        assertEquals("wdqdsaf",t.getTitle());
        assertEquals("cxvcxvdsv",t.getDescription());
        assertEquals(TaskRepository.simpleDateFormat.parse("[2022-03-29 08:00:00.000]"), t.getStartTime());
        assertEquals(TaskRepository.simpleDateFormat.parse("[2022-03-29 10:00:00.000]"), t.getEndTime());
        assertEquals(t.getRepeatInterval(),30 * 3600 + 60);
    }

    @Test
    void test_getIntervalFromString(){
        String line = "\"wdqdsaf\" \"cxvcxvdsv\" from [2022-03-29 08:00:00.000] to [2022-03-29 10:00:00.000] every [1 day 6 hours 1 minute ] inactive.";
        assertEquals(30 * 3600 + 60, TaskRepository.getIntervalFromText(line));
    }


}
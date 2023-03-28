package bg.sofia.uni.fmi.mjt.bookmarks.repository.logs;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ErrorLogsTest {
    static ErrorLogs errorLogs;

    @BeforeAll
    static void setUp() {
        errorLogs = new ErrorLogs("asdf");
    }

    @Test
    void testCreateErrorLogsWithNullPath() {
        assertThrows(IllegalArgumentException.class, () -> new ErrorLogs(null),
            "The system should correctly handle null path!");
    }

    @Test
    void testAppendNullLog() {
        assertThrows(IllegalArgumentException.class, () -> errorLogs.appendLogs(null),
            "The system should correctly handle null exception passed to append logs!");
    }
}
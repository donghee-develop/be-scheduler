package test.schedule.exception;

public class ScheduleNotFoundException extends RuntimeException{
    public ScheduleNotFoundException(Long id) {
        super(id + "not found");
    }
}

package test.schedule.dto.schedule;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class GetScheduleDTO extends ScheduleDTO{
    private int rn;
    private String name;
    public GetScheduleDTO(Long id, String content, Long userId, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, content, userId, createdAt, updatedAt);
        this.name = name;
    }

}

package test.schedule.dto.schedule;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetScheduleDTO{
    private String name;
    private Long id;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

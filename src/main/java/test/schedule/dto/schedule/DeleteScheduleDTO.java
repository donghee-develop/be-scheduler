package test.schedule.dto.schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DeleteScheduleDTO {
    private Long id;
    @NotBlank(message = "비밀번호는 필수 값입니다.")
    @Size(min = 6, max = 100, message = "비밀번호는 6자리 ~ 100자리 입니다.")
    private String password;
}

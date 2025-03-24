package test.schedule.dto.schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DeleteScheduleDTO {
    private Long id;
    @NotBlank(message = "비밀번호는 필수 값입니다.")
    @Size(min = 6, max = 100, message = "비밀번호는 6자리 ~ 100자리 입니다.")
    private String password;
}

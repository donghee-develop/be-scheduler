package test.schedule.dto.schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@ToString
public class UpdateScheduleDTO {
    private Long id;
    @Size(max=200, message = "일정은 최대 200자리 까지 작성할 수 있습니다.")
    @NotBlank(message = "일정 내용은 필수 값입니다.")
    private String content;
    @NotBlank(message = "비밀번호는 필수 값입니다.")
    private String password;
}

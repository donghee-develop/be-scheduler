package test.schedule.dto.schedule;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostScheduleDTO{
    @NotBlank(message = "작성자명은 필수 값입니다.")
    private String name;
    @NotBlank(message = "비밀번호는 필수 값입니다.")
    @Size(min = 6, max = 100, message = "비밀번호는 6자리 ~ 100자리 입니다.")
    private String password;
    @NotBlank(message = "이메일은 필수 값입니다.")
    @Email(message = "올바른 이메일 형식을 입력해주세요")
    private String email;
    @NotBlank(message = "일정 내용은 필수 값입니다.")
    @Size(max=200, message = "일정은 최대 200자리 까지 작성할 수 있습니다.")
    private String content;
}

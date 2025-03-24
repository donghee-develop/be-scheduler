package test.schedule.service.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.schedule.dto.schedule.*;
import test.schedule.dto.util.PagingDTO;
import test.schedule.exception.PasswordMisMatchException;
import test.schedule.repository.schedule.ScheduleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
// 인터페이스가 없으면 서비스만 다른 개발자가 헷갈림
// 나중에 버전에 따른 인터페이스 스위칭 가능 구현체가 달라질 때
// 백업 + 배포용 클라우드를 두개 쓸 경우가 있다.
// 분기 처리용
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public List<GetScheduleDTO> getSchedules(String keyword, LocalDate startDate, LocalDate endDate, PagingDTO pagingDTO) {
        return scheduleRepository.getSchedules(keyword,startDate,endDate,pagingDTO);
    }

    @Transactional
    public void postSchedule(PostScheduleDTO postScheduleDTO) {
        Long userId = scheduleRepository.findUserByEmail(postScheduleDTO.getEmail());

        if (userId == null) {
            userId = scheduleRepository.insertUser(postScheduleDTO.getName(), postScheduleDTO.getEmail());
        }

        scheduleRepository.insertSchedule(postScheduleDTO, userId);

    }



    public int getTotalSchedules(String keyword, LocalDate startDate, LocalDate endDate) {
        return  scheduleRepository.getTotalSchedules(keyword, startDate, endDate);
    }

    public void updateSchedule(UpdateScheduleDTO updateScheduleDTO) {
        String dbPassword = scheduleRepository.getPasswordById(updateScheduleDTO.getId());

        if (!Objects.equals(updateScheduleDTO.getPassword(), dbPassword)) {
            throw new PasswordMisMatchException();
        }

        scheduleRepository.updateSchedule(updateScheduleDTO);
    }

    public Optional<GetScheduleDTO> getScheduleById(Long id) {
        return scheduleRepository.getScheduleById(id);
    }

    public void deleteSchedule(DeleteScheduleDTO deleteScheduleDTO) {
        String dbPassword = scheduleRepository.getPasswordById(deleteScheduleDTO.getId());
        if (!Objects.equals(deleteScheduleDTO.getPassword(), dbPassword)) {
            throw new PasswordMisMatchException();
        }
        scheduleRepository.deleteSchedule(deleteScheduleDTO);
    }
}

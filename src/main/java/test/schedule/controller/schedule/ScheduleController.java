package test.schedule.controller.schedule;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.schedule.dto.schedule.*;
import test.schedule.dto.util.PagingDTO;
import test.schedule.exception.ScheduleNotFoundException;
import test.schedule.service.schedule.ScheduleService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/schedules")
    public Map<String,Object> getSchedules(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "1") int nowPage,
            @RequestParam(defaultValue = "5") int cntPerPage
    ) {
        Map<String,Object> response = new HashMap<>();
        int total = scheduleService.getTotalSchedules(keyword, startDate, endDate);
        PagingDTO pagingDTO = new PagingDTO(total, nowPage, cntPerPage);
        List<GetScheduleDTO> schedules = scheduleService.getSchedules(keyword, startDate, endDate, pagingDTO);

        response.put("schedules",schedules);
        response.put("total",total);
        return response;
    }
    @PostMapping("/schedules")
    public ResponseEntity<Void> postSchedule(
           @Valid @RequestBody PostScheduleDTO postScheduleDTO
    ){
        scheduleService.postSchedule(postScheduleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/schedules/{id}")
    public ResponseEntity<GetScheduleDTO> updateSchedule(
            @PathVariable Long id
    ){
        GetScheduleDTO getScheduleDTO = scheduleService.getScheduleById(id)
                .orElseThrow(() -> new ScheduleNotFoundException(id));

        return ResponseEntity.ok(getScheduleDTO);
    }
    @PutMapping("/schedules")
    public ResponseEntity<Void> updateSchedule(
            @Valid @RequestBody UpdateScheduleDTO updateScheduleDTO
    ){
        scheduleService.updateSchedule(updateScheduleDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    // 필요 시 use_yn 변수를 통해 update
    @DeleteMapping("/schedules")
    public ResponseEntity<Void> deleteSchedule(
            @Valid @RequestBody DeleteScheduleDTO deleteScheduleDTO
    ){
        scheduleService.deleteSchedule(deleteScheduleDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

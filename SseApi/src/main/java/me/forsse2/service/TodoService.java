package me.forsse2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.forsse2.common.exception.customs.CustomException;
import me.forsse2.common.exception.customs.ExceptionCode;
import me.forsse2.dto.request.TodoReqDto;
import me.forsse2.dto.response.TodoResDto;
import me.forsse2.entity.Todo;
import me.forsse2.entity.User;
import me.forsse2.repository.TodoRepository;
import me.forsse2.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TodoService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000;


    //private final EmitterRepository emitterRepository;
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private Map<Long, SseEmitter> session = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String loginId){
        User user = userRepository.findByLoginId(loginId).orElseThrow(()->new CustomException(ExceptionCode.USER_NOT_FOUND));
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        if(session.containsKey(user.getId())){
            session.remove(user.getId());
        }

        session.put(user.getId(), emitter);
        sendToClient(emitter, user.getId(), "EventStream Created, [loginId =" + loginId +"]");

        return emitter;
    }

    private void sendToClient(SseEmitter emitter, Long id,Object data){
        try{
            emitter.send(SseEmitter.event()
                    .id(id.toString())
                    .name("sse")
                    .data(data));
        }catch (IOException e){
            throw new RuntimeException("SSE 연결을 실패하였습니다.");
        }
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void send() throws ParseException {
        List<Todo> result = todoRepository.findAll();
        String todayFm = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date(dateFormat.parse(todayFm).getTime());

        for (Todo todo: result){
            Long id = todo.getUser().getId();
            String deadLine = todo.getDeadLine();
            Date todoDay = new Date(dateFormat.parse(deadLine).getTime());
            if(today.compareTo(todoDay) == 0){
                SseEmitter sseEmitter = session.get(id);
                log.info("제대로 호출되었습니다!");
                sendToClient(sseEmitter, id, todo);
            }
        }
    }

    public TodoResDto saveTodo(String loginId, TodoReqDto todoReqDto){
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
        return  TodoResDto.toDto(todoRepository.save(TodoReqDto.toEntity(todoReqDto, user)));
    }

}

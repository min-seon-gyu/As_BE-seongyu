package Auction_shop.auction.sse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@AllArgsConstructor
@RestController
public class SSEConnection {
    @Qualifier("longRedisTemplate")
    private final RedisTemplate<String, Long> messageQueue;
    private static final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final long TIMEOUT = 5 * 60 * 1000L;
    private final long RECONNECTION_TIMEOUT = 5000L;

    @GetMapping(value = "/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connectSSEConnect(@RequestParam Long userId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        // 여러 유저의 emitter 인스턴스를 저장. 이후 특정 userId에 맞는 emitter 인스턴스를 찾아 해당 유저에게 통신
        emitters.put(userId, emitter);

        // Emitter 생성 후 만료 시간(TIMEOUT)까지 아무 데이터도 보내지 않는 경우 503에러가 발생한다.
        // "name: connect, data: connect success"와 같은 더미데이터를 전달하여 에러 방지
        // 오프라인 상태일 때 구매자에 의해 생성된 채팅방 번호를 전달
        // Redis의 key로 문자열이 들어가기에 Long타입의 userId를 String으로 변환하여 저장
        String idTypeChange = String.valueOf(userId);
        List<Long> chatRoomIds = messageQueue.opsForList().range(idTypeChange, 0, -1);
        sendEvent(emitter, "connect", chatRoomIds);
        messageQueue.delete(idTypeChange);

        // 타임아웃이 발생하면 브라우저에서 재연결 요청을 보내고 새로운 Emitter 객체를 생성하기에 기존의 Emitter를 삭제
        emitter.onTimeout(() -> {
            emitter.complete(); // onCompletion() 콜백
        });
        emitter.onCompletion(() -> {
            log.info("onCompletion callback run");
            emitters.remove(emitter);
        });

        return ResponseEntity.ok(emitter);
    }

    public void sendEvent(SseEmitter emitter, String name, Object roomId) {
        try {
            emitter.send(SseEmitter.event()
                    .name(name)
                    .data(roomId)
                    .reconnectTime(RECONNECTION_TIMEOUT)
            );
        } catch (IOException e) {
            log.info("SSE Connection Error = {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public SseEmitter getEmitter(Long yourId) {
        return emitters.get(yourId);
    }

}

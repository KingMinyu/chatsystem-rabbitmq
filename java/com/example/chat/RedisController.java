package com.example.chat;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.chat.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
public class RedisController {

    private final RedisService redisService;

    @Autowired
    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }


    @GetMapping("/getRoomsData")
    public ResponseEntity<Object> showData(Model model) {
        // Redis에서 데이터 가져오기
        Object data = redisService.getDataFromRedis("example_key");

     // 데이터를 JSON 형태로 응답
        return ResponseEntity.ok(data);
    }
    @PostMapping("/setRoomsData")
    public ResponseEntity<String> setData(@RequestBody Map<String, String> requestBody) throws JsonProcessingException {
        String roomId = requestBody.get("roomId");
        // 키 "rooms"에 추가 데이터를 JSON 형식으로 저장
        redisService.appendJsonDataToKey("rooms", roomId);
        return ResponseEntity.status(HttpStatus.OK).body("JSON Data successfully appended to Redis!");
    }
    @DeleteMapping("/closeConsultation/{roomId}")
    public ResponseEntity<String> closeConsultation(@PathVariable(name = "roomId") String roomId) {
        try {
            // Redis 데이터 및 WebSocket topic 삭제
        	redisService.removeDataFromRedis(roomId);
            return ResponseEntity.status(HttpStatus.OK).body("Consultation closed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
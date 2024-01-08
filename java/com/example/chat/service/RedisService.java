package com.example.chat.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

//RedisService.java

@Service
public class RedisService {

 private final RedisTemplate<String, Object> redisTemplate;
 private final ObjectMapper objectMapper;
 private static final String DATE_FORMAT_PATTERN = "yyyyMMddHHmmss";

 @Autowired
 public RedisService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
     this.redisTemplate = redisTemplate;
     this.objectMapper = objectMapper;
 }
 public void removeDataFromRedis(String roomId) {
	 Object dataFromRedis = redisTemplate.opsForValue().get("rooms");

	 ArrayNode existingJsonArray;
		if (dataFromRedis == null) {
		    existingJsonArray = objectMapper.createArrayNode();
		} else if (dataFromRedis instanceof String) {
		    // 기존 데이터를 JSON 배열로 변환
		    String existingData = (String) dataFromRedis;
		    try {
		        existingJsonArray = (ArrayNode) objectMapper.readTree(existingData);
		    } catch (JsonProcessingException e) {
		        e.printStackTrace();
		        // 예외 처리
		        return; // 예외 발생 시 중단하거나 적절한 대응을 추가하세요.
		    }
		} else if (dataFromRedis instanceof ArrayList) {
		    // 기존 데이터가 ArrayList인 경우에 대한 처리
		    existingJsonArray = objectMapper.valueToTree(dataFromRedis);
		} else {
		    // 기존 데이터가 다른 형태로 저장되어 있는 경우에 대한 예외 처리
		    return; // 적절한 대응을 추가하세요.
		}
		// roomId에 해당하는 데이터를 찾아 삭제
		for (Iterator<JsonNode> iterator = existingJsonArray.iterator(); iterator.hasNext();) {
		    JsonNode node = iterator.next();
		    if (node.isObject() && node.has("roomId") && node.get("roomId").asText().equals(roomId)) {
		        iterator.remove(); // 해당 노드를 삭제
		        break; // roomId가 중복될 경우 여러 개를 삭제하지 않도록 하기 위해 break
		    }
		}
        // 업데이트된 데이터로 기존 키 업데이트
        redisTemplate.opsForValue().set("rooms", existingJsonArray);
 }
 public Object getDataFromRedis(String key) {
	 Object dataFromRedis = redisTemplate.opsForValue().get("rooms");
	 if (dataFromRedis != null) {
         ObjectMapper objectMapper = new ObjectMapper();
         try {
             JsonNode jsonNode;
             if (dataFromRedis instanceof String) {
                 jsonNode = objectMapper.readTree((String) dataFromRedis);
             } else {
                 // Handle JsonArray case
                 jsonNode = objectMapper.valueToTree(dataFromRedis);
             }

             return findRoomIdWithMinDateTime(jsonNode);
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
     return null;
 }
 private String findRoomIdWithMinDateTime(JsonNode jsonNode) {
     String roomIdWithMinDateTime = null;
     LocalDateTime minDateTime = null;

     Iterator<JsonNode> iterator = jsonNode.iterator();
     while (iterator.hasNext()) {
         JsonNode node = iterator.next();
         String dateString = node.get("date").asText();

         LocalDateTime dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));

         if (minDateTime == null || dateTime.isBefore(minDateTime)) {
             minDateTime = dateTime;
             roomIdWithMinDateTime = node.get("roomId").asText();
         }
     }

     return roomIdWithMinDateTime;
 }
 public void appendJsonDataToKey(String key, String additionalData) throws JsonProcessingException {
     // 현재 날짜와 시간 가져오기
     LocalDateTime now = LocalDateTime.now();

     // 원하는 형식으로 포맷팅
     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
     String date = now.format(formatter);
	// 기존 데이터 가져오기
	Object existingDataObject = redisTemplate.opsForValue().get(key);

	// 기존 데이터가 null이거나 비어있다면 빈 배열 생성
	ArrayNode existingJsonArray;
	if (existingDataObject == null) {
	    existingJsonArray = objectMapper.createArrayNode();
	} else if (existingDataObject instanceof String) {
	    // 기존 데이터를 JSON 배열로 변환
	    String existingData = (String) existingDataObject;
	    try {
	        existingJsonArray = (ArrayNode) objectMapper.readTree(existingData);
	    } catch (JsonProcessingException e) {
	        e.printStackTrace();
	        // 예외 처리
	        return; // 예외 발생 시 중단하거나 적절한 대응을 추가하세요.
	    }
	} else if (existingDataObject instanceof ArrayList) {
	    // 기존 데이터가 ArrayList인 경우에 대한 처리
	    existingJsonArray = objectMapper.valueToTree(existingDataObject);
	} else {
	    // 기존 데이터가 다른 형태로 저장되어 있는 경우에 대한 예외 처리
	    return; // 적절한 대응을 추가하세요.
	}

	// 추가 데이터를 JSON 형식으로 생성
	ObjectNode additionalJson = objectMapper.createObjectNode()
	        .put("roomId", additionalData)
	        .put("date", date);

	// 새로운 데이터 추가
	existingJsonArray.add(additionalJson);

	// 업데이트된 데이터로 기존 키 업데이트
	redisTemplate.opsForValue().set(key, existingJsonArray);
 }
}
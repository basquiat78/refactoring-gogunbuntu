package io.basquiat.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * commmon utils
 * created by basquiat
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {

    private static ObjectMapper objectMapper = null;

    private static ObjectMapper mapper() {
        if(objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        }
        return objectMapper;
    }

    /**
     * 객체를 json string으로 변환한다.
     * @param object
     * @return String
     * @throws JsonProcessingException
     */
    public static String toJson(Object object) {
        String result;
        try {
            result = mapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

}

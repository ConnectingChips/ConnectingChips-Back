package connectingchips.samchips.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtils {

    private final RedisTemplate<String, Object> redisTemplate;

    // key-value 데이터 추가
    public void setData(String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }

    // key-value 데이터 추가, 만료시간 적용
    public void setData(String key, Object value, Long expiredTime){
        redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.MILLISECONDS);
    }

    public Optional<Object> getData(String key){
        Object value = redisTemplate.opsForValue().get(key);

        if(value == null){
            return Optional.empty();
        }else{
            return Optional.of(value);
        }
    }

    public void deleteData(String key){
        redisTemplate.delete(key);
    }
}

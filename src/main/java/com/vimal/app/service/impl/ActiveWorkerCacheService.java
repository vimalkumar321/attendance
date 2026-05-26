package com.vimal.app.service.impl;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.vimal.app.request.ActiveWorkerCache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActiveWorkerCacheService {
	
	private static final String PREFIX = "active-workers:";
	private static final String ACTIVE_WORKERS_SET = "active-workers:set";
	private static final Duration TTL = Duration.ofHours(16);
	
    private final RedisTemplate<String,Object> redisTemplate;
    
    public void save(ActiveWorkerCache cache) {
        try {
        	redisTemplate.opsForValue().set(
                    PREFIX + cache.workerId(),
                    cache,
                    TTL
            );
        	
        	redisTemplate.opsForSet().add(
                    ACTIVE_WORKERS_SET,
                    cache.workerId().toString()
            );
		} catch (Exception e) {
			log.error("Failed to save active worker in redis. workerId={}", cache.workerId(), e);
		}
    }

    public ActiveWorkerCache get(Long workerId) {
        try {
        	Object value = redisTemplate.opsForValue().get(PREFIX + workerId);

            if (value == null) {
                return null;
            }
            
            return (ActiveWorkerCache) value;
		} catch (Exception e) {
			log.error("Failed to fetch active worker from redis. workerId={}", workerId, e);
			return null;
		}
    }

    public void remove(Long workerId) {
    	try {
    		redisTemplate.delete(PREFIX + workerId);
    		
    		redisTemplate.opsForSet().remove(
                    ACTIVE_WORKERS_SET,
                    workerId.toString()
            );
    	} catch (Exception ex) {
    		log.error("Failed to remove active worker from redis. workerId={}", workerId, ex);
        }
    }

	public List<ActiveWorkerCache> getAllActiveWorkers() {
		try {
			Set<Object> workerIds = redisTemplate.opsForSet().members(ACTIVE_WORKERS_SET);
			
			if (workerIds == null || workerIds.isEmpty()) {
                return Collections.emptyList();
            }
			
			return workerIds.stream()
                    .map(Object::toString)
                    .map(Long::valueOf)
                    .map(this::get)
                    .filter(worker -> worker != null)
                    .toList();
        } catch (Exception ex) {
        	log.error("Failed to fetch active workers from redis", ex);
        	return Collections.emptyList();
        }

    }

    public void invalidateWorkerCache(Long workerId) {
    	remove(workerId);
    }
    
}

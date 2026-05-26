package com.vimal.app.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisCacheErrorHandler implements CacheErrorHandler{
	@Override
	public void handleCacheGetError(RuntimeException ex, Cache cache, Object key) {
		log.error("Redis GET failed: {}", ex.getMessage());
	}

	@Override
	public void handleCachePutError(RuntimeException ex, Cache cache, Object key, @Nullable Object value) {
		log.error("Redis PUT failed: {}", ex.getMessage());
	}

	@Override
	public void handleCacheEvictError(RuntimeException ex, Cache cache, Object key) {
		log.error("Redis EVICT failed: {}", ex.getMessage());
	}

	@Override
	public void handleCacheClearError(RuntimeException ex, Cache cache) {
		log.error("Redis CLEAR failed: {}", ex.getMessage());
	}

}

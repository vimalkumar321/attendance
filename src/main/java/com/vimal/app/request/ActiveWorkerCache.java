package com.vimal.app.request;

import java.time.LocalDateTime;

public record ActiveWorkerCache(
		Long workerId,
		String workerName,
		String designation,
		Long siteId,
		String siteName,
		LocalDateTime clockIn) {

}

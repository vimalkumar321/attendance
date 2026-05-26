package com.vimal.app.request;

import jakarta.validation.constraints.NotNull;

public record ClockInRequest(
		@NotNull Long workerId,
        @NotNull Long siteId) {
}

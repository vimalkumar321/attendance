package com.vimal.app.request;

import jakarta.validation.constraints.NotNull;

public record ClockOutRequest(@NotNull Long workerId) {

}

package com.dylej.taskapi.dto;

import java.time.Instant;
import java.util.List;

public record ApiError(
        int status,
        String message,
        List<String> errors,
        Instant timestamp
) {}
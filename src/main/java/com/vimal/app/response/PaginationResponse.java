package com.vimal.app.response;

import java.util.List;

public record PaginationResponse(
		List<?> content,

        long totalElements,

        int totalPages,

        int currentPage,

        int size) {

}

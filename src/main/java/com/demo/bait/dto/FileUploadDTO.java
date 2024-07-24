package com.demo.bait.dto;

import lombok.Builder;

@Builder
public record FileUploadDTO(Integer id, String fileName, String filePath, Long fileSize, String fileType,
                            String thumbnailUrl) {
}

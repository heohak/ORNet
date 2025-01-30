package com.demo.bait.controller.FileUploadController;

import com.demo.bait.components.RequestParamParser;
import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.service.FileUploadServices.FileUploadService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/file")
public class FileUploadGetController {

    public final FileUploadService fileUploadService;
    private final RequestParamParser requestParamParser;


    @GetMapping("/thumbnail/{fileId}")
    public ResponseEntity<byte[]> getThumbnail(@PathVariable String fileId) {
        Integer parsedFileId = requestParamParser.parseId(fileId, "File ID");
        return fileUploadService.getThumbnail(parsedFileId);
    }

    @GetMapping("/all")
    public List<FileUploadDTO> getAllFiles() {
        return fileUploadService.getAllFiles();
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        Integer parsedFileId = requestParamParser.parseId(fileId, "File ID");
        return fileUploadService.downloadFile(parsedFileId);
    }

    @GetMapping("/open/{fileId}")
    public ResponseEntity<Resource> openFile(@PathVariable String fileId) {
        Integer parsedFileId = requestParamParser.parseId(fileId, "File ID");
        return fileUploadService.openFileInBrowser(parsedFileId);
    }

    @GetMapping("/{fileId}")
    public FileUploadDTO getFileById(@PathVariable String fileId) {
        Integer parsedFileId = requestParamParser.parseId(fileId, "File ID");
        return fileUploadService.getFileById(parsedFileId);
    }
}

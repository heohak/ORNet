package com.demo.bait.controller.FileUploadController;

import com.demo.bait.service.FileUploadServices.FileUploadService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/file")
public class FileUploadController {

    public final FileUploadService fileUploadService;
}

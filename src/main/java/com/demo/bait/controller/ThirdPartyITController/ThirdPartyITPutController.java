package com.demo.bait.controller.ThirdPartyITController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.ThirdPartyITDTO;
import com.demo.bait.service.ThirdPartyITServices.ThirdPartyITFileUploadService;
import com.demo.bait.service.ThirdPartyITServices.ThirdPartyITService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/third-party")
public class ThirdPartyITPutController {

    public final ThirdPartyITService thirdPartyITService;
    public final ThirdPartyITFileUploadService thirdPartyITFileUploadService;

    @PutMapping("/update/{thirdPartyId}")
    public ResponseDTO updateThirdPartyIT(@PathVariable Integer thirdPartyId,
                                          @RequestBody ThirdPartyITDTO thirdPartyITDTO) {
        return thirdPartyITService.updateThirdPartyIT(thirdPartyId, thirdPartyITDTO);
    }

    @PutMapping("/upload/{thirdPartyId}")
    public ResponseDTO uploadFiles(@PathVariable Integer thirdPartyId,
                                   @RequestParam("files") List<MultipartFile> files) throws IOException {
        return thirdPartyITFileUploadService.uploadFilesToThirdPartyIT(thirdPartyId, files);
    }
}

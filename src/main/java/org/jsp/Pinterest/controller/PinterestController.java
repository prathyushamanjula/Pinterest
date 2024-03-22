package org.jsp.Pinterest.controller;

import java.util.Map;

import org.jsp.Pinterest.dto.BoardDTO;
import org.jsp.Pinterest.dto.MediaPinCreateRequestDTO;
import org.jsp.Pinterest.dto.PinDTO;
import org.jsp.Pinterest.service.PinterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class PinterestController {

	@Autowired
	PinterestService pinterestService;

	@PostMapping("/boards")
	public String createBoard(@RequestBody BoardDTO boardDTO) {
	    return pinterestService.createBoard(boardDTO);
	}

	@PostMapping("/pins")
	public String createPin(@RequestBody PinDTO pinDTO) {
		return pinterestService.createPin(pinDTO);
	}

	// Video Uploading Code
	// Step - 1
	@PostMapping("/upload-media")
    public String uploadMedia() {
        return pinterestService.uploadMedia();
    }

	// Step - 2
    @PostMapping("/upload-media-to-s3")
    public String uploadMediaToS3(@RequestParam("upload_url") String upload_url,
                                              @RequestPart("upload_parameters") Map<String, String> upload_parameters,
                                              @RequestPart("file") MultipartFile file) {
    	return pinterestService.uploadMediaToS3(upload_url, upload_parameters, file);
    }

    // Step - 3
    @GetMapping("/get-media-status")
    public String getMediaStatus(@RequestParam String media_id) {
    	return pinterestService.getMediaStatus(media_id);
    }

    // Step - 4
    @PostMapping("/create-pin")
    public String createPin(@RequestBody MediaPinCreateRequestDTO pinCreateRequestDTO) {
    	return pinterestService.createPin(pinCreateRequestDTO);  
    }
}
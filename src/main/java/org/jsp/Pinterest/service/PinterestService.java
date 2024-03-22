package org.jsp.Pinterest.service;

import java.io.IOException;
import java.util.Map;

import org.json.JSONObject;
import org.jsp.Pinterest.dto.BoardDTO;
import org.jsp.Pinterest.dto.MediaPinCreateRequestDTO;
import org.jsp.Pinterest.dto.PinDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PinterestService {

	@Value("${pinterest.api.base-url}")
	private String baseUrl;

	@Value("${pinterest.api.access-token}")
	private String accessToken;

	private final RestTemplate restTemplate;

	public PinterestService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String createBoard(BoardDTO boardDTO) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

		String creatBoardsurl = baseUrl + "/boards";

		JSONObject requestBodyJson = new JSONObject();
		requestBodyJson.put("name", boardDTO.getName());
		requestBodyJson.put("description", boardDTO.getDescription()); 
		requestBodyJson.put("privacy", boardDTO.getPrivacy()); 

		String requestBody = requestBodyJson.toString();

		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<String> response = restTemplate.exchange(creatBoardsurl, HttpMethod.POST, requestEntity,
				String.class);
		System.out.println(response.getBody());
		if (response.getStatusCode() == HttpStatus.CREATED) {
			System.out.println("Board created successfully!");
			return response.getBody();
		} else {
			System.err.println("Failed to create board: " + response.getBody());
			return null;
		}
	}

	public String createPin(PinDTO pinDTO) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

		String createPinurl = baseUrl + "/pins";

		JSONObject requestBodyJson = new JSONObject();
		requestBodyJson.put("title", pinDTO.getTitle());
		requestBodyJson.put("description", pinDTO.getDescription());
		requestBodyJson.put("board_id", pinDTO.getBoard_id());

		JSONObject mediaSourceJson = new JSONObject();
		mediaSourceJson.put("source_type", pinDTO.getMedia_source().getSource_type());
		mediaSourceJson.put("url", pinDTO.getMedia_source().getUrl());
		requestBodyJson.put("media_source", mediaSourceJson);

		HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson.toString(), headers);

		ResponseEntity<String> response = restTemplate.exchange(createPinurl, HttpMethod.POST, requestEntity,
				String.class);
		System.out.println(response.getBody());
		if (response.getStatusCode() == HttpStatus.CREATED) {
			System.out.println("Pin created successfully!");
			return response.getBody();
		} else {
			System.err.println("Failed to create pin: " + response.getBody());
			return null;
		}
	}

	// Video Uploading Code
	// Step - 1
	public String uploadMedia() {
		String uploadMediaUrl = "https://api-sandbox.pinterest.com/v5/media";
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		System.out.println("***************1***************");

		String requestBody = "{\"media_type\": \"video\"}";
		System.out.println("***************2***************");

		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
		System.out.println("***************3***************");

		ResponseEntity<String> response = restTemplate.exchange(uploadMediaUrl, HttpMethod.POST, requestEntity, String.class);
		System.out.println("***************4***************");
		System.out.println(response.getBody());

		if (response.getStatusCode() == HttpStatus.CREATED) {
			System.out.println("Video Upload Success");
			return response.getBody();
		} else {
			System.err.println("Failed to upload media. Status code: " + response.getStatusCode());
			return null;
		}
	}

	// Step - 2
	public String uploadMediaToS3(String upload_url, Map<String, String> upload_parameters, MultipartFile file) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	    MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
		upload_parameters.forEach(formData::add);

		try {
			ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
				@Override
				public String getFilename() {
					return file.getOriginalFilename();
				}
			};
			formData.add("file", fileResource);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error uploading media to S3: " + e.getMessage());
		}

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);

		ResponseEntity<String> response = restTemplate.exchange(upload_url, HttpMethod.POST, requestEntity,
				String.class);

		if (response.getStatusCode() != HttpStatus.NO_CONTENT) {
			throw new RuntimeException("Failed to upload media to S3. Status code: " + response.getStatusCode());
		}
		return upload_url;
	}

	// Step - 3
	public String getMediaStatus(String media_id) {
		String getMediaStatusUrl = "https://api-sandbox.pinterest.com/v5/media/" + media_id;
		HttpHeaders headers = new HttpHeaders();
		System.out.println("***************1***************");

		headers.setBearerAuth(accessToken);
		System.out.println("***************2***************");
		ResponseEntity<String> response = restTemplate.getForEntity(getMediaStatusUrl, String.class);

		System.out.println("***************3***************");
		if (response.getStatusCode() == HttpStatus.ACCEPTED) {
			System.out.println("***************4***************");
			return response.getBody();
		} else {
			throw new RuntimeException("Failed to get media status. Status code: " + response.getStatusCode());
		}
	}

	// Step - 4
	public String createPin(MediaPinCreateRequestDTO pinCreateRequestDTO) {
		String createPinUrl = "https://api-sandbox.pinterest.com/v5/pins";
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		System.out.println("***************1***************");
		String requestBody = pinCreateRequestDTO.toJsonString();

		System.out.println("***************2***************");
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		System.out.println("***************3***************");
		ResponseEntity<String> response = restTemplate.postForEntity(createPinUrl, requestEntity, String.class);

		System.out.println("***************4***************");
		System.out.println(response.getBody());

		if (response.getStatusCode() == HttpStatus.CREATED) {
			System.out.println("Video With Cover Image Uploaded Successfully");
			return response.getBody();
		} else {
			System.err.println("Failed to create pin. Status code: " + response.getStatusCode());
			return null;
		}
	}
}
package org.jsp.Pinterest.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class MediaPinCreateRequestDTO {
	private String title;
	private String description;
	private String board_id;
	private MediaSourceDTO media_source;

	public MediaPinCreateRequestDTO(String title, String description, String board_id, MediaSourceDTO media_source) {
		super();
		this.title = title;
		this.description = description;
		this.board_id = board_id;
		this.media_source = media_source;
	}

	public String toJsonString() {
		try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            // Handle JSON processing exception
            e.printStackTrace();
            return null;
        }
	}
}

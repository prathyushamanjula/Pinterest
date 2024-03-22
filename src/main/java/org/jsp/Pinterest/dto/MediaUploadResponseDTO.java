package org.jsp.Pinterest.dto;

import java.util.Map;
import lombok.Data;

@Data
public class MediaUploadResponseDTO {
	private String media_id;
	private String media_type;
	private String upload_url;
	private Map<String, String> upload_parameters;

	public MediaUploadResponseDTO(String media_id, String media_type, String upload_url,
			Map<String, String> upload_parameters) {
		super();
		this.media_id = media_id;
		this.media_type = media_type;
		this.upload_url = upload_url;
		this.upload_parameters = upload_parameters;
	}
}

package org.jsp.Pinterest.dto;

import lombok.Data;

@Data
public class MediaSourceDTO {
	private String source_type;
	private String cover_image_url;
	private String media_id;

	public MediaSourceDTO(String source_type, String cover_image_url, String media_id) {
		super();
		this.source_type = source_type;
		this.cover_image_url = cover_image_url;
		this.media_id = media_id;
	}
}
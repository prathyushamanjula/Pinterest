package org.jsp.Pinterest.dto;

import lombok.Data;

@Data
public class PinDTO {
    private String title; 
    private String description;
    private String board_id; 
    private PinMediaSource media_source; 
}

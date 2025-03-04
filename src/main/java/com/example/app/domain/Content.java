package com.example.app.domain;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Content {
	
	private Integer id;
	private Integer userId;
	private Integer categoryId;
	
	@NotBlank
	@Size(max = 50)
	private String productName;
	private LocalDate workDate;
	private String place;
	private String totalPrice;
	private String note;
	private String imageFlower1;
	private String imageFlower2;
	private String imageFlower3;

}

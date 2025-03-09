package com.example.app.domain;

import java.lang.reflect.Member;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Content {
	
	private Integer id;
	private Member member;
	private Category category;
	
	@NotBlank
	@Size(max = 50)
	private String productName;
	private LocalDate workDate;
	private String place;
	private String totalPrice;
	private String note;
	private String imagePlant1;
	private String imagePlant2;
	private String imagePlant3;
	private String status;

}

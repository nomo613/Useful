package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Category;

@Mapper
public interface CategoryMapper {
	
	List<Category>selectAll();

}

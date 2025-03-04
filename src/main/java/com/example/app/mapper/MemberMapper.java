package com.example.app.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.app.domain.Member;

@Mapper
@Repository
public interface MemberMapper {
	
	Member selectByLoginId(String loginId);

}

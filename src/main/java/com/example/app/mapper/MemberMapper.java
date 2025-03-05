package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Member;

@Mapper
public interface MemberMapper {
	
	List<Member>selectAll();
	Member selectById(Integer id);
	Member selectByLoginId(String loginId);
	void setDeleteById(Integer id);
	void insert(Member member);
	void update(Member member);
	List<Member> selectLimited(@Param("offset") int offset, @Param("num") int num);
    long countActive();
	

}

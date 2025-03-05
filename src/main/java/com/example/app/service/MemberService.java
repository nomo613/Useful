package com.example.app.service;

import java.util.List;

import com.example.app.domain.Member;

public interface MemberService {
	
	List<Member> getMemberList();
	Member getMemberById(Integer id);
	Member getMemberByLoginId(String logingId);
	void deleteMemberById(Integer id);
	void addMember(Member member);
	void editMember(Member member);
	boolean isExsitingMember(String loginId);
	List<Member> getMemberListPerPage(int page, int numPerPage);
    int getTotalPages(int numPerPage);
}

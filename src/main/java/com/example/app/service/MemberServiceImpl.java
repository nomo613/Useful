package com.example.app.service;

import org.springframework.stereotype.Service;

import com.example.app.domain.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService{

	@Override
	public Member getAuthenticatedMember(String loginId, String loginPass) {
		Member authenticatedMember = null;
		
		Member member = mapper.selectByLoginId(loginId);
		if(member != null && BCrypt.checkpw(loginPass, member.getLoginPass())) {
			authenticatedMember = member;
		}
		
		return authenticatedMember;
	}

}

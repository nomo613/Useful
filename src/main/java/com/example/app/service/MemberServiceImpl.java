package com.example.app.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.app.domain.Member;
import com.example.app.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService{
	
	private final MemberMapper mapper;

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

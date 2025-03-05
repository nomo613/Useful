package com.example.app.service;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Member;
import com.example.app.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService{
	
	private final MemberMapper memberMapper;

	@Override
	public List<Member> getMemberList() {
		return memberMapper.selectAll();
	}

	@Override
	public Member getMemberById(Integer id) {
		return memberMapper.selectById(id);
	}

	@Override
	public Member getMemberByLoginId(String logingId) {
		return memberMapper.selectByLoginId(logingId);
	}

	@Override
	public void deleteMemberById(Integer id) {
		memberMapper.setDeleteById(id);
		
	}

	@Override
	public void addMember(Member member) {
		String password = member.getLoginPass();
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		member.setLoginPass(hashedPassword);
		memberMapper.insert(member);
	}

	@Override
	public void editMember(Member member) {
		String password = member.getLoginPass();
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		member.setLoginPass(hashedPassword);
		memberMapper.update(member);
		
	}

	@Override
	public boolean isExsitingMember(String loginId) {
		Member member = memberMapper.selectByLoginId(loginId);
		if(member != null) {
			return true;
		}
		return false;
	}

	@Override
	public List<Member> getMemberListPerPage(int page, int numPerPage) {
		int offset = numPerPage * (page - 1);
		return memberMapper.selectLimited(offset, numPerPage);
	}

	@Override
	public int getTotalPages(int numPerPage) {
		long count = memberMapper.countActive();
		return(int)Math.ceil((double) count / numPerPage);
	}
	}

	
	



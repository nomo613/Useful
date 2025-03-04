package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Member;
import com.example.app.service.MemberService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AuthController {

	private final MemberService service;
	private final HttpSession session;
	
	@GetMapping
	public String showLoginForm(Model model) {
		model.addAttribute("member", new Member());
		return "login";
	}
	
	@PostMapping
	public String loginCheck(@Valid Member member,
			Errors errors,
			RedirectAttributes ra) {
		if(errors.hasErrors()) {
			return "login";
		}
		
		Member authMember = service.getAuthenticatedMember(member.getLoginId(), member.getLoginPass());
		if(authMember == null) {
			errors.rejectValue("loginId", "wrong_id_or_pass");
			return "login";
		}
		
		session.setAttribute("authMember", authMember);				
		ra.addFlashAttribute("status", "ログインしました");
		return "redirect:/items";
	}
	
	@GetMapping("/logout")
	public String logout(RedirectAttributes ra) {
		session.invalidate();
		ra.addFlashAttribute("status", "ログアウトしました");
		return "redirect:/";
	}
	
}

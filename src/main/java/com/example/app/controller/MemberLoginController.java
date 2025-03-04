package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Login;
import com.example.app.domain.Member;
import com.example.app.login.LoginAuthority;
import com.example.app.login.LoginStatus;
import com.example.app.service.AdminService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberLoginController {
	
	private final AdminService memberService;
	private final HttpSession session;
	
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute(new Login());
		return "login-member";
	}

	@PostMapping("/login")
	public String login(
			@Valid Login login,
			Errors errors) {
		if(errors.hasErrors()) {
			return "login-member";
		}
        
		// 正しいIDとパスワードの組み合わせか確認
		Member member = memberService.getMemberByLoginId(login.getLoginId());
		if(member == null || !login.isCorrectPassword(member.getLoginPass())) {
			errors.rejectValue("loginId", "error.incorrect_id_or_password");
			return "login-member";
		}

		// セッションに認証情報を格納
		LoginStatus loginStatus = LoginStatus.builder()
				.id(member.getId())
				.name(member.getName())
				.loginId(member.getLoginId())
				.authority(LoginAuthority.MEMBER)
				.build();
		session.setAttribute("loginStatus", loginStatus);
		return "redirect:/search";
	}

	@GetMapping("/logout")
	public String logout(
			RedirectAttributes redirectAttributes) {
		session.removeAttribute("loginStatus");
		redirectAttributes.addFlashAttribute("message", "ログアウトしました。");
		return "redirect:/login";
	}
}


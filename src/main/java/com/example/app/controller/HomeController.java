package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.domain.Admin;
import com.example.app.service.AdminService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

	private final AdminService service;
	private final HttpSession session;

	@GetMapping
	public String showHome(Model model) {
		model.addAttribute("admin", new Admin());
		return "home";
	}

	@PostMapping
	public String login(
			@Valid Admin admin,
			Errors errors) {
		// 入力に不備がある
		if (errors.hasErrors()) {
			return "home";
		}
		String loginId = admin.getLoginId();
		String loginPass = admin.getLoginPass();
		// ログインID・パスワードが正しくない
		if (!service.isCorrectIdAndPassword(loginId, loginPass)) {
			errors.rejectValue("loginId", "error.incorrect_id_password");
			return "home";
		}
		// 正しいログインID・パスワード
		// ⇒ セッションにログインID を格納し、リダイレクト
		session.setAttribute("loginId", loginId);
		return "redirect:/";
	}

	@GetMapping("/logout")
	public String logout() {
		// セッションを破棄し、トップページへ遷移
		session.invalidate();
		return "redirect:/";
	}
}

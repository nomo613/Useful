package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Content;
import com.example.app.service.ContentService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/content")
@RequiredArgsConstructor
public class MemberController {

	private static final int NUM_PER_PAGE = 5;

	private final ContentService service;
	private final HttpSession session;
	
	@GetMapping("/list")
	public String list(
			@RequestParam(name="page", defaultValue="1") Integer page,
			Model model) {
		// 詳細・追加・編集ページから戻る際に利用
		session.setAttribute("page", page);
				
		int totalPages = service.getTotalPages(NUM_PER_PAGE);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("currentPage", page);
		model.addAttribute("contentList", service.getContentListPerPage(page, NUM_PER_PAGE));
		return "list-content";
	}
	
	@GetMapping("/add")
	public String add(Model model) {
		Content content = new Content();
		model.addAttribute("content", content);
		model.addAttribute("heading", "社員の追加");
		return "save-content";
	}
	
	@PostMapping("/add")
	public String add(
			@Valid Content content,
			Errors errors,
			Model model,
			RedirectAttributes redirectAttributes) {
		
		if(!content.getLoginId().isBlank()) {
			if(service.isExsitingContent(content.getLoginId())) {
				errors.rejectValue("loginId", "error.existing_student_loginId");
			}
		}

		if(errors.hasErrors()) {
			model.addAttribute("heading", "社員の追加");
			return "save-content";
		}
		service.addContent(content);
		redirectAttributes.addFlashAttribute("message", "生徒を追加しました。");
		
		// 追加後に戻るページ(⇒最終ページ)
		int totalPages = service.getTotalPages(NUM_PER_PAGE);
		return "redirect:/content/list?page=" + totalPages;
	}

	@GetMapping("/edit/{id}")
	public String edit(
			@PathVariable Integer id,
			Model model) {
		model.addAttribute("content", service.getContentById(id));
		model.addAttribute("heading", "生徒の追加");
		return "save-content";
	}

	@PostMapping("/edit/{id}")
	public String edit(
			@PathVariable Integer id,
			@Valid Content content,
			Errors errors,
			Model model,
			RedirectAttributes redirectAttributes) {

		String originalLoginId = service.getContentById(id).getLoginId();

		if(!content.getLoginId().isBlank()) {
			if(!originalLoginId.equals(content.getLoginId()) && service.isExsitingContent(content.getLoginId())) {
				errors.rejectValue("loginId", "error.existing_content_loginId");
			}
		}

		if(errors.hasErrors()) {
			model.addAttribute("heading", "案件の編集");
			return "admin/save-student";
		}

		service.editContent(content);
		redirectAttributes.addFlashAttribute("message", "案件を編集しました。");
		
		// 編集後に戻るページ(元のページ)
		int previousPage = (int) session.getAttribute("page");
		return "redirect:/content/list?page=" + previousPage;
	}

	@GetMapping("/delete/{id}")
	public String delete(
			@PathVariable Integer id,
			RedirectAttributes redirectAttributes) {
		service.deleteContentById(id);
		redirectAttributes.addFlashAttribute("message", "案件を削除しました。");
		
		// 削除後に戻るページ(⇒ページ数が減って、元のページが無くなった場合は最終ページ)
		int previousPage = (int) session.getAttribute("page");
		int totalPages = service.getTotalPages(NUM_PER_PAGE);
		int page = previousPage <= service.getTotalPages(NUM_PER_PAGE) ? previousPage : totalPages;
		return "redirect:/content/list?page=" + page;
	}

}


































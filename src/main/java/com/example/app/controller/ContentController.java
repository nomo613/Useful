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

@RequiredArgsConstructor
@Controller
@RequestMapping("/content")
public class ContentController {
	
	private static final int NUM_PER_PAGE = 10;
	
	private final ContentService service;
    private final HttpSession session;

    @GetMapping("/list")
    public String showContentList(
			@RequestParam(name="page", defaultValue="1") Integer page,
			Model model) {
    	
    	// 詳細・追加・編集ページから戻る際に利用
    	session.setAttribute("page", page);
    	
		model.addAttribute("contentList", service.getContentByPage(page, NUM_PER_PAGE));
		model.addAttribute("page", page);
		model.addAttribute("totalPages", service.getTotalPages(NUM_PER_PAGE));
		return "list-cose";
	}
	
	@GetMapping("/detail/{id}")
	public String showContentDetail(@PathVariable Integer id,
			Model model) {
		model.addAttribute("content", service.getContentById(id));
		return "detail-cose";
	}
	
	@GetMapping("/add")
	public String showAddForm(Model model) {
		model.addAttribute("content", new Content());
		model.addAttribute("categoyrList", service.getContentCategories());
		return "save-cose";
	}
	
	@PostMapping("/add")
	public String addContent(@Valid Content content,
			Errors errors,
			Model model,
			RedirectAttributes ra) {
		if(errors.hasErrors()) {
			model.addAttribute("categoyrList", service.getContentCategories());
			return "save-cose";
		}
		
		service.addContent(content);
		ra.addFlashAttribute("status", "案件を追加しました");
		return "redirect:/contents";
	}
	
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Integer id,
			Model model) {
		model.addAttribute("content", service.getContentById(id));
		model.addAttribute("categoyrList", service.getContentCategories());
		return "edit-cose";
	}
	
	@PostMapping("/edit/{id}")
	public String editContent(@PathVariable Integer id,
			@Valid Content content,
			Errors errors,
			Model model,
			RedirectAttributes ra) {
		if(errors.hasErrors()) {
			model.addAttribute("categoyrList", service.getContentCategories());
			return "edit-cose";
		}
		
		service.editContent(content);
		ra.addFlashAttribute("status", "案件を更新しました");
		return "redirect:/contents/detail/" + id;
	}
	
	@GetMapping("/delete/{id}")
	public String deleteContent(@PathVariable Integer id,
			RedirectAttributes ra) {
		service.deleteContent(id);		
		ra.addFlashAttribute("status", "案件を削除しました");
		return "redirect:/contents";
	}

}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	



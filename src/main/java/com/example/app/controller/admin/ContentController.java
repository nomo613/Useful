package com.example.app.controller.admin;

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
@RequestMapping("/admin/content")
public class ContentController {
	
// 2. フィールドの定義 	
	private static final int NUM_PER_PAGE = 10;
	
	private final ContentService service;
    private final HttpSession session;

// 3. 一覧表示   
    @GetMapping("/list")
    public String list(
			@RequestParam(name="page", defaultValue="1") Integer page,
			Model model) throws Exception {
    	
    	// 詳細・追加・編集ページから戻る際に利用 pageをセッションに保存
    	session.setAttribute("page", page);
    	
    	int totalPages = service.getTotalPages(NUM_PER_PAGE);           // 総ページ数を取得
	    model.addAttribute("totalPages", totalPages);                    // 総ページ数をテンプレートへ渡します
	    model.addAttribute("currentPage", page);                         // 現在のページ番号をテンプレートへ渡します
		model.addAttribute("contentList", service.getContentListPerPage(page, NUM_PER_PAGE));  // 指定されたページのデータを取得し、テンプレートへ渡します
		return "admin/list-content";
	}
    
 // 4. 詳細表示	
	@GetMapping("/show/{id}")
	public String show(
			@PathVariable Integer id,
			Model model) throws Exception {
		model.addAttribute("content", service.getContentById(id));
		return "admin/show-content";
	}
	
// 5. 追加	
	// (1) 追加フォームの表示  新規のcontentオブジェクトをモデルに追加
	@GetMapping("/add")
	public String add(Model model) throws Exception {
		model.addAttribute("content", new Content());
		model.addAttribute("categoryList", service.getCategoryList());    // カテゴリリストの取得
		model.addAttribute("heading", "案件の追加");                      // 画面タイトルの設定     
		return "admin/save-content";                                      // 表示するHTMLの指定
	}
	
	// (2) 登録処理
	@PostMapping("/add")
	public String add(
			@Valid Content content,
			Errors errors,
			Model model,
			RedirectAttributes redirectAttributes) throws Exception {

		if(!content.getProductName().isBlank()) {
			if(service.isExsitingContent(content.getProductName())) {
				errors.rejectValue("name", "error.existing_content_name");    // 既存の教材名があればエラー
			}
		}
		if(errors.hasErrors()) {                                              // エラーがあれば入力画面に戻る
			model.addAttribute("categoryList", service.getCategoryList());
			model.addAttribute("heading", "案件の追加");
			return "admin/save-content";
		}
		
		service.addContent(content);                                          // 新しい教材をデータベースに保存
		redirectAttributes.addFlashAttribute("message", "案件を追加しました");;
	
	   // 追加後に戻るページ(⇒最終ページ) 追加後、最終ページへリダイレクト
			int totalPages = service.getTotalPages(NUM_PER_PAGE);
			return "redirect:/admin/content/list?page=" + totalPages;
		}
	
// 6. 編集	
	// (1) 編集フォームの表示  *指定IDの案件を取得し、フォームに表示
	@GetMapping("/edit/{id}")
	public String edit(
			@PathVariable Integer id,
			Model model)  throws Exception {
		model.addAttribute("content", service.getContentById(id));
		model.addAttribute("categoryList", service.getCategoryList());
		model.addAttribute("heading", "案件の編集");
		return "admin/save-content";
	}
	
	// (2) 更新処理	
	@PostMapping("/edit/{id}")
	public String edit(
			@PathVariable Integer id,
			@Valid Content content,
			Errors errors,
			Model model,
			RedirectAttributes redirectAttributes) throws Exception {
		
		String originalContentProductName = service.getContentById(id).getProductName();
		
		if(!content.getProductName().isBlank()) {                           // 既存の教材名があればエラー
			if(!originalContentProductName.equals(content.getProductName()) && service.isExsitingContent(content.getProductName())) {
				errors.rejectValue("name", "error.existing_content_name");
			}
		}
		
		if(errors.hasErrors()) {                                            // エラーがあれば入力画面に戻る
			model.addAttribute("categryList", service.getCategoryList());
			model.addAttribute("heading", "案件の編集");
			return "admin/save-content";
		}
		
		service.editContent(content);                                       // 更新した案件をデータベースに保存
		redirectAttributes.addFlashAttribute("message", "案件を編集しました");
		
		// 編集後に戻るページ(編集後、元のページへリダイレクト)
				int previousPage = (int) session.getAttribute("page");
				return "redirect:/admin/content/list?page=" + previousPage;
	 }
	
// 7. 削除	
	@GetMapping("/delete/{id}")
	public String delete(
			@PathVariable Integer id,
			RedirectAttributes redirectAttributes) throws Exception {
		service.deleteContentById(id);		
		redirectAttributes.addFlashAttribute("massage", "案件を削除しました");
		
		// 削除後に戻るページ(⇒ページ数が減って、元のページが無くなった場合は最終ページ)
				int previousPage = (int) session.getAttribute("page");
				int totalPages = service.getTotalPages(NUM_PER_PAGE);
				int page = previousPage <= totalPages ? previousPage : totalPages;
				return "redirect:/admin/content/list?page=" + page;
	}

}
	









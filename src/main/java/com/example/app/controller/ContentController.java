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
	
// 2. フィールドの定義 
	private static final int NUM_PER_PAGE = 10;
	
	private final ContentService service;
    private final HttpSession session;

 // 3. 一覧表示 (/list) 
    @GetMapping("/list")
    public String list(
			@RequestParam(name="page", defaultValue="1") Integer page,
			Model model) {
    	
    	// 詳細・追加・編集ページから戻る際に利用
    	session.setAttribute("page", page);
    	
    	int totalPages = service.getTotalPages(NUM_PER_PAGE);                                   // 全データの総ページ数を取得
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("currentPage", page);
		model.addAttribute("contentList", service.getContentListPerPage(page, NUM_PER_PAGE));   // 指定したページの教材リストを取得。
		return "list-content";                                                                   // list-contentにデータを渡してページを表示
	}
	
 // 4. 詳細表示 (/show/{id})
	@GetMapping("/show/{id}")                                      // URL の {id} を取得
	public String show(@PathVariable Integer id,
			Model model) {
		model.addAttribute("content", service.getContentById(id)); // 指定 ID の教材を取得
		return "show-content";                                     // 詳細ページへデータを渡す
	}

// 5. 追加 (/add)
	// (1) 追加フォームの表示   // 新規のcontentオブジェクトをモデルに追加
	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("content", new Content());
		model.addAttribute("categoyrList", service.getContentCategories());
		model.addAttribute("heading", "案件の追加");
		return "save-content";
	}

	// (2) 登録処理
	@PostMapping("/add")
	public String add(@Valid Content content,  // オブジェクトのバリデーションを実施
			Errors errors,
			Model model,
			RedirectAttributes ra) {
		
		if(!content.getProductName().isBlank()) {
			if(service.isExistingContent(content.getProductName())) {
				errors.rejectValue("name", "error.existing_content_name"); // 既存の教材名があればエラー
			}
		}
		
		if(errors.hasErrors()) {                                           // エラーがあれば入力画面に戻る
			model.addAttribute("categoyrList", service.getContentCategories());
			model.addAttribute("heading", "案件の追加");
			return "save-content";
		}
		
		service.addContent(content);                             // 新しい教材をデータベースに保存
		ra.addFlashAttribute("message", "案件を追加しました");
		
		// 追加後に戻るページ(⇒最終ページ) 追加後、最終ページへリダイレクト
	    int totalPages = service.getTotalPages(NUM_PER_PAGE);
		return "redirect:/content/list?page=" + totalPages;
	}

// 6. 編集 (/edit/{id})
 // (1) 編集フォームの表示  *指定IDの案件を取得し、フォームに表示
	@GetMapping("/edit/{id}")
	public String edit(
			@PathVariable Integer id,Model model) {
		model.addAttribute("content", service.getContentById(id));
		model.addAttribute("categoyrList", service.getContentCategories());
		model.addAttribute("heading", "案件の編集");
		return "save-content";
	}

// (2) 更新処理	
	@PostMapping("/edit/{id}")
	public String editContent(
			@PathVariable Integer id,
			@Valid Content content,
			Errors errors,
			Model model,
			RedirectAttributes ra) {
		
		String originalContentProductName = service.getContentById(id).getProductName();
		
		if(!content.getProductName().isBlank()) {       // 既存の教材名があればエラー
			if(!originalContentProductName.equals(content.getProductName()) && service.isExistingContent(content.getProductName())) {
				errors.rejectValue("name", "error.existing_material_name");
			}
		}

		if(errors.hasErrors()) {  // エラーがあれば入力画面に戻る
			model.addAttribute("categoryList", service.getCategoryList());
			model.addAttribute("heading", "案件の編集");
			return "admin/save-content";
		}

		service.editContent(content);  // 更新した案件をデータベースに保存
		redirectAttributes.addFlashAttribute("message", "案件を編集しました。");
		
		// 編集後に戻るページ(元のページ)   編集後、元のページへリダイレクト
		int previousPage = (int) session.getAttribute("page");
		return "redirect:/content/list?page=" + previousPage;
	}
	
// 7. 教材の削除 (/delete/{id})
	@GetMapping("/delete/{id}")
	public String delete(
			@PathVariable Integer id,
			RedirectAttributes redirectAttributes) {
		service.deleteContentById(id);                                            // 指定 ID の案件を削除
		redirectAttributes.addFlashAttribute("message", "案件を削除しました。");
		
		// 削除後、元のページがなくなった場合は最終ページへリダイレクト 
		// 削除後に戻るページ(⇒ページ数が減って、元のページが無くなった場合は最終ページ)
		int previousPage = (int) session.getAttribute("page");
		int totalPages = service.getTotalPages(NUM_PER_PAGE);
		int page = previousPage <= totalPages ? previousPage : totalPages;
		return "redirect:/content/list?page=" + page;
	}

}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	



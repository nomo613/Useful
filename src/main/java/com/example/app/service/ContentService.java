package com.example.app.service;

import java.util.List;

import com.example.app.domain.Category;
import com.example.app.domain.Content;


public interface ContentService {

    List <Content>getAllContents() ; // テーブルの全データを取得

	Content getContentById(Integer id); // 指定した id 取得
	
	List<Content> getContentByPage(int page, int numPerPage) ; // ページ分割機能用  

	void addContent(Content content) ; // 追加

	void editContent(Content content); // 編集

	void deleteContent(Integer id); // 削除

	int getTotalPages(int numPerPage); // ページネーション用のメソッドで、総ページ数を取得

	List<Category> getCategoryList();

	List<Category> getContentCategories();

	boolean isExistingContent(String productName);

	void deleteContentById(Integer id);



}

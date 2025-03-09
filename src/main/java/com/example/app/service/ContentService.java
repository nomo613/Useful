package com.example.app.service;

import java.util.List;

import com.example.app.domain.Category;
import com.example.app.domain.Content;


public interface ContentService {

    List <Content>getContentList() throws Exception;          // テーブルの全データを取得

	Content getContentById(Integer id) throws Exception;;     // 指定した id 取得
	
	void deleteContentById(Integer id) throws Exception;      // 指定した id 削除	

	void addContent(Content content) throws Exception;        // 追加

	void editContent(Content content) throws Exception;       // 編集

	int getTotalPages(int numPerPage) throws Exception;       // ページネーション用のメソッドで、総ページ数を取得

	List<Content> getContentListPerPage(int page, int numPerPage) throws Exception; // ページ分割機能用  

	List<Category> getCategoryList() throws Exception;

	boolean isExsitingContent(String productName) throws Exception;

}

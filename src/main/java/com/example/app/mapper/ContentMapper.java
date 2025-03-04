package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Content;

@Mapper
public interface ContentMapper {
	
	List <Content> selectAll();   // 全データを取得
	
	Content selectById(int id); // idに基づいてデータを1件習得
	
	// ページ分割機能  offset → 取得開始位置 limit → 取得する件数
	List<Content> selectLimited(@Param("offset") int offset, @Param("limit") int limit);
	Long count();
	
	void insert(Content content);  // 追加する
	
	void update(Content content);  // 更新する
	
	void delete(int id);        // 削除する
	



}

package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Content;

@Mapper
public interface ContentMapper {
	
	List <Content> selectAll() throws Exception;       // 全データを取得
	
	Content selectById(Integer id) throws Exception;    // idに基づいてデータを1件習得
	Content selectByProdactName(String prodactName) throws Exception;
	
	void setDeleteById(Integer id) throws Exception;   // 削除する
	
	void insert(Content content) throws Exception;     // 追加する
	
	void update(Content content) throws Exception;     // 更新する
	
                                                       // ページ分割機能  offset → 取得開始位置 limit → 取得する件数
    List<Content> selectLimited(@Param("offset") int offset, @Param("limit") int limit) throws Exception;

    Long count() throws Exception;

	int getcountActive()throws Exception;

	Content selectByProductName(String productName)throws Exception;

}

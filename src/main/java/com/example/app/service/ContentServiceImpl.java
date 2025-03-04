package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.app.domain.Category;
import com.example.app.domain.Content;
import com.example.app.mapper.CategoryMapper;
import com.example.app.mapper.ContentMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ContentServiceImpl implements ContentService{
	
	private final ContentMapper contentMapper;
	private final CategoryMapper categoryMapper;
	

	@Override
	public List<Content> getAllContents(){
		return contentMapper.selectAll();
		}

	@Override
	public Content getContentById(Integer id){
		if(id == null) {
			return null;
		}
		
		return contentMapper.selectById(id);
	}
	
	@Override
	public List<Content> getContentByPage(int page, int numPerPage){
		int offset = numPerPage * (page -1); 
		return contentMapper.selectLimited(offset, numPerPage);
	}

	@Override
	public int getTotalPages(int numPerPage){
		double totalNum = (double) contentMapper.count();
        return (int) Math.ceil(totalNum / numPerPage);
	}
	
	@Override
	public void addContent(Content content){
		contentMapper.insert(content);
		
	}

	@Override
	public void editContent(Content content){
		contentMapper.update(content);	
	}

	@Override
	public void deleteContent(Integer id){
		  if (id == null) {
		        return;
		    }
		  
		  contentMapper.delete(id); // 削除処理を追加
		
	}

	@Override
	public List<Category> getContentCategories(){
		return categoryMapper.selectAll();
	}

	@Override
	public List<Category> getCategoryList() {
		return categoryMapper.selectAll();
	}


}



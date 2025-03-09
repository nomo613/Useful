package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Category;
import com.example.app.domain.Content;
import com.example.app.mapper.CategoryMapper;
import com.example.app.mapper.ContentMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService{
	
	private final ContentMapper contentMapper;
	private final CategoryMapper categoryMapper;
	

	@Override
	public List<Content> getContentList()throws Exception {
		return contentMapper.selectAll();
		}


	@Override
	public Content getContentById(Integer id) throws Exception {
		return contentMapper.selectById(id);
	}


	@Override
	public void deleteContentById(Integer id) throws Exception {
		contentMapper.setDeleteById(id);
	}


	@Override
	public void addContent(Content content) throws Exception {
		contentMapper.insert(content);
	}


	@Override
	public void editContent(Content content) throws Exception {
		contentMapper.update(content);
	}


	@Override
	public int getTotalPages(int numPerPage) throws Exception {
		long count = contentMapper.getcountActive();
		return (int) Math.ceil((double) count / numPerPage);
	}


	@Override
	public List<Content> getContentListPerPage(int page, int numPerPage) throws Exception {
		int offset = numPerPage * (page - 1);
		return contentMapper.selectLimited(offset, numPerPage);
	}


	@Override
	public List<Category> getCategoryList() throws Exception {
		return categoryMapper.selectAll();
}


	@Override
	public boolean isExsitingContent(String productName)  throws Exception {
		Content content = contentMapper.selectByProductName(productName);
		if(content != null) {
			return true;
		}

		return false;
	}




	



}

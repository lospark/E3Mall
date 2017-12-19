package cn.lospark.search.service;

import cn.lospark.common.pojo.SearchResult;

public interface SearchService {
	
	SearchResult  search(String keyword,int page,int rows) throws Exception;

}

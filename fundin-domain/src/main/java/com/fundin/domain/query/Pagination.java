package com.fundin.domain.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pagination<E> {

	private int count;
	private int pageCount;
	private int pageNum;
	private int startIndex;
	private int pageSize;
	
	private Map<String, Object> queryParams = new HashMap<String, Object>();
	private List<E> elements;
	
	public static <E> Pagination<E> getInstance(int count, int pageNum, int pageSize) {
		Pagination<E> query = new Pagination<E>();
		query.setCount(count);
		query.setPageNum(pageNum);
		query.setPageSize(pageSize);
		
		query.setPageCount(calcPageCount(count, pageSize));
		query.setStartIndex(pageSize * (pageNum - 1));
		return query;
	}
	
	private static int calcPageCount(int count, int pageSize) {
		if (count % pageSize == 0) {
			return count / pageSize;
		}
		
		return count / pageSize + 1;
	}

	public void addQueryParams(String key, Object val) {
		queryParams.put(key, val);
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public Map<String, Object> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(Map<String, Object> queryParams) {
		this.queryParams = queryParams;
	}

	public List<E> getElements() {
		return elements;
	}

	public void setElements(List<E> elements) {
		this.elements = elements;
	}
	
}

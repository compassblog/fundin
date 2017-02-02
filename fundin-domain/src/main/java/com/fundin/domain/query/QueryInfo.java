package com.fundin.domain.query;

public class QueryInfo {

	private int count;
	private int pageCount;
	private int pageNum;
	private int startIndex;
	private int pageSize;
	
	public static QueryInfo getInstance(int pageNum, int pageSize) {
		QueryInfo query = new QueryInfo();
		query.setPageNum(pageNum);
		query.setPageSize(pageSize);
		
		query.setStartIndex(pageSize * (pageNum - 1));
		return query;
	}
	
	public static QueryInfo getInstance(int count, int pageNum, int pageSize) {
		QueryInfo query = new QueryInfo();
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
	
}

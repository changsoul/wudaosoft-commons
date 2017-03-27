/* 
 * Copyright(c)2010-2014 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */
package com.wudaosoft.commons.persistence;

import java.util.ArrayList;
import java.util.List;

import com.wudaosoft.commons.persistence.SearchFilter.Operator;

/**
 * @author Changsoul Wu
 *
 */
public class OrFilter {

	private List<SearchFilter> searchFilters;

	public OrFilter() {
		searchFilters = new ArrayList<SearchFilter>(3);
	}

	public OrFilter or(SearchFilter filter) {
		searchFilters.add(filter);
		return this;
	}

	public OrFilter or(String fieldName, Operator operator, Object value) {
		return or(new SearchFilter(fieldName, operator, value));
	}

	public List<SearchFilter> getSearchFilters() {
		return searchFilters;
	}

	public void setSearchFilters(List<SearchFilter> searchFilters) {
		this.searchFilters = searchFilters;
	}
}

/**
 *    Copyright 2009-2018 Wudao Software Studio(wudaosoft.com)
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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

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
package com.wudaosoft.commons.mvc.i18n;

import org.springframework.util.Assert;

/** 
 * @author Changsoul Wu
 * 
 */
public class Lang {

	private String langKey = "lang";
	
	private LangType langType = LangType.Param;

	public Lang() {
		super();
	}

	public Lang(String langKey, LangType langType) {
		super();
		this.setLangKey(langKey);
		this.setLangType(langType);
	}

	public String getLangKey() {
		return langKey;
	}

	public void setLangKey(String langKey) {
		Assert.hasText(langKey, "langKey must not be empty");
		this.langKey = langKey;
	}

	public LangType getLangType() {
		return langType;
	}

	public void setLangType(LangType langType) {
		Assert.notNull(langType, "langType must not be null");
		this.langType = langType;
	}
}

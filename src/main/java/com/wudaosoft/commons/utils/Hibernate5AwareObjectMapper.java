/* 
 * Copyright(c)2010-2017 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */ 
 
package com.wudaosoft.commons.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module.Feature;

/** 
 * @author Changsoul Wu
 * 
 */
@SuppressWarnings("serial")
public class Hibernate5AwareObjectMapper extends ObjectMapper {

	public Hibernate5AwareObjectMapper() {
		Hibernate5Module hm = new Hibernate5Module();
		hm.disable(Feature.USE_TRANSIENT_ANNOTATION);
        registerModule(hm);
    }
}

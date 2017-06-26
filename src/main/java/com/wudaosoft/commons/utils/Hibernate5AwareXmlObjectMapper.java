/* 
 * Copyright(c)2010-2017 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */ 
 
package com.wudaosoft.commons.utils;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module.Feature;

/** 
 * @author Changsoul Wu
 * 
 */
@SuppressWarnings("serial")
public class Hibernate5AwareXmlObjectMapper extends XmlMapper {

	public Hibernate5AwareXmlObjectMapper() {
		Hibernate5Module hm = new Hibernate5Module();
		hm.disable(Feature.USE_TRANSIENT_ANNOTATION);
        registerModule(hm);
    }
}

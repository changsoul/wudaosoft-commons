/* 
 * Copyright(c)2010-2016 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */ 
 
package com.wudaosoft.commons.persistence;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.wudaosoft.commons.utils.UUIDUtils;

/** 
 * @author Changsoul Wu
 * 
 */
public class Base64UUIDGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        return UUIDUtils.base64Uuid();
    }

}

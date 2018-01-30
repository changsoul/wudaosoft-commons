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
package com.wudaosoft.commons.listener;

import java.util.TimeZone;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/** 
 * 确保在spring加载之前加载。请把这个listener放在web.xml所有listner之前。
 * 
 * @author Changsoul Wu
 * 
 */
public class AsiaShanghaiTimeZoneListener implements ServletContextListener {
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");//获取中国所在时区
		TimeZone.setDefault(timeZone);//设置全局默认时区为东八区。(注：影响范围为当前本线程以及在本线程之后创建的线程,所以请确保在WEB容器启动的时候第一个调用)
	}

}

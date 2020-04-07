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

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

/** 
 * @author Changsoul Wu
 * 
 */
public class AbandonedConnectionCleanupThreadShutdownListener implements ServletContextListener {
	
	private static final Logger logger = LoggerFactory.getLogger(AbandonedConnectionCleanupThreadShutdownListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver d = null;
        while(drivers.hasMoreElements()) {
            try {
                d = drivers.nextElement();
                DriverManager.deregisterDriver(d);
                logger.info(String.format("Driver %s deregistered", d));
            } catch (SQLException ex) {
            	logger.warn(String.format("Error deregistering driver %s", d), ex);
            }
        }
//        try {
            AbandonedConnectionCleanupThread.uncheckedShutdown();
            logger.info("Shutdown AbandonedConnectionCleanupThread.");
//        } catch (InterruptedException e) {
//            logger.warn("SEVERE problem cleaning up: " + e.getMessage());
//            e.printStackTrace();
//        }
	}

}

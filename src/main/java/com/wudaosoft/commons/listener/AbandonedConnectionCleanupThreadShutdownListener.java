/* 
 * Copyright(c)2010-2016 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
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

import com.mysql.jdbc.AbandonedConnectionCleanupThread;

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
        try {
            AbandonedConnectionCleanupThread.shutdown();
            logger.info("Shutdown AbandonedConnectionCleanupThread.");
        } catch (InterruptedException e) {
            logger.warn("SEVERE problem cleaning up: " + e.getMessage());
            e.printStackTrace();
        }
	}

}

package com.insprit.vmg.checker.obstacle.core;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 * Config ÆÄÀÏ (Config)
 *
 * @author ½Éº´Ã¶
 * @author <a href="mailto:simhero@in-sprit.com">½Éº´Ã¶</a>
 * @author <a href="mailto:simhero@gmail.com">½Éº´Ã¶</a>
 * @version $Id: Config.java,v 1.0 2009/05/09 00:00:00 ½Éº´Ã¶ Express $
 */
public class Config extends XMLConfiguration {
	private static final long serialVersionUID = 1L;
	private static Config instance_ = null;
	private FileChangedReloadingStrategy strategy = null;

	private Config(String fileName) {
		try {
			init(fileName);
			strategy = new FileChangedReloadingStrategy();
			strategy.setRefreshDelay(5000);// Refresh Scope
			setReloadingStrategy(strategy);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final static Config instance(String fileName) {
		if (instance_ == null) {
			synchronized (Config.class) {
				if (instance_ == null)
					instance_ = new Config(fileName);
			}
		}
		return instance_;
	}

	/**
	 * Initialize the class.
	 *
	 * @param fileName
	 *            Configuration file name.
	 * @throws ConfigurationException
	 */
	private void init(String fileName) throws Exception {
		setFileName(fileName);
		try {
			load();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}


package utils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import systemengine.systemengineImpl;
import systemengine.systemengine;
import user.UserManager;


import static constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {
	private static final String SYSTEM_ENGINE_ATTRIBUTE_NAME = "systemengine";

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	//private static final Object userManagerLock = new Object();
	private static final Object systemEngineManagerLock = new Object();
	public static systemengine getSystemEngine(ServletContext servletContext) {
		synchronized (systemEngineManagerLock) {
			if (servletContext.getAttribute(SYSTEM_ENGINE_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(SYSTEM_ENGINE_ATTRIBUTE_NAME, new systemengineImpl());
			}
		}
		return (systemengine) servletContext.getAttribute(SYSTEM_ENGINE_ATTRIBUTE_NAME);
	}
}

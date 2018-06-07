package com.optimal.web.optimal_web.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
public class BizLogFilter extends AbstractRequestLoggingFilter {
	private static Set<String> LOG_NO_URI_SET = new HashSet();
	public static String GLOBAL_MSG_ID = "_global_msg_id";
	public static String URI = "URI";

	static {
		LOG_NO_URI_SET.add("/");
		LOG_NO_URI_SET.add("/error");
		LOG_NO_URI_SET.add("/restart");
		LOG_NO_URI_SET.add("/health");
		LOG_NO_URI_SET.add("/resume");
		LOG_NO_URI_SET.add("/info");
		LOG_NO_URI_SET.add("/metrics");
		LOG_NO_URI_SET.add("/pause");
		LOG_NO_URI_SET.add("/env");
		LOG_NO_URI_SET.add("/env/reset");
		LOG_NO_URI_SET.add("/refresh");
		LOG_NO_URI_SET.add("/swagger");
		LOG_NO_URI_SET.add("/webjars");
	}

	public BizLogFilter() {
		// setIncludeHeaders(true);
		setIncludePayload(true);
		setIncludeQueryString(true);
		setMaxPayloadLength(4096);
	}

	/**
	 * Subclasses may override this to perform custom filter shutdown.
	 * <p>
	 * Note: This method will be called from standard filter destruction as well as
	 * filter bean destruction in a Spring application context.
	 * <p>
	 * This default implementation is empty.
	 */
	@Override
	public void destroy() {
		MDC.remove(GLOBAL_MSG_ID);
		MDC.remove(URI);
		super.destroy();
	}

	protected void setHeaderKeyKryGlobalMsgId() {
		String ids = UUID.randomUUID().toString();
		ids = ids.replaceAll("-", "");
		MDC.put(GLOBAL_MSG_ID, ids);
	}

	@Override
	protected void beforeRequest(HttpServletRequest httpServletRequest, String s) {
		setHeaderKeyKryGlobalMsgId();
		MDC.put(URI, httpServletRequest.getRequestURI());
		String msg = StringUtils.isBlank(s) ? "" : StringUtils.remove(s, "\n");
		log.info(msg);
	}

	@Override
	protected void afterRequest(HttpServletRequest httpServletRequest, String s) {
		String msg = StringUtils.isBlank(s) ? "" : StringUtils.remove(s, "\n");
		log.info(msg);
	}

	protected boolean shouldLog(HttpServletRequest request) {
		for (String uri : LOG_NO_URI_SET) {
			return !StringUtils.startsWith(request.getRequestURI(), uri);
		}
		return true;
	}

}

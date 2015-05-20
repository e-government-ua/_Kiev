package org.activiti.redis.constant;

import java.util.regex.Pattern;

public final class RedisPatterns {
	private RedisPatterns(){};
	public static Pattern fileNamePattern =Pattern.compile("^[-a-zA-Z0-9+&#/%?=~:.;\"_*]+$");


}

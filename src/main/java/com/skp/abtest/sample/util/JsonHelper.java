package com.skp.abtest.sample.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonHelper {
	private static final Logger logger = LoggerFactory.getLogger(JsonHelper.class);

	static String REGEX = "\\{\\{ (.*?) \\}\\}";
	public static String getExpValue(Map map, String exp) {
		Pattern p = Pattern.compile(REGEX);
		Matcher m = p.matcher(exp);
		String result = exp;
		while (m.find()) {
			String path = exp.substring(m.start(), m.end());
			logger.debug("start={} end={} token={}", m.start(), m.end(), path);
			result = result.replace(path, getPathValue(map, path));
		}
		return result;
	}

	static String PRE = "{{";
	static String POST = "}}";
	public static String getPathValue(Map map, String path) {
		path = path.trim();
		if (path.startsWith(PRE) && path.endsWith(POST)) {
			String rpath = strip(path, PRE, POST);

			String tokens[] = rpath.split("\\.");
			List<String> tokensList = Arrays.asList(tokens);
			if ("".equals(tokensList.get(0)))
				return getTokenValue(map, tokensList.subList(1, tokensList.size()));
			return getTokenValue(map, tokensList);
		}
		return path;
	}

	private static String getTokenValue(Map map, List<String> tokensList) {
		if (tokensList.size() == 1)
			return (String) map.get(tokensList.get(0));
		String token = tokensList.get(0);;
		Map nmap = (Map) map.get(token);
		return getTokenValue(nmap, tokensList.subList(1, tokensList.size()));
	}

	public static String strip(String path, String pre, String post) {
		int start = path.indexOf(pre) + pre.length();
		int end = path.indexOf(post);
		return path.substring(start, end).trim();
    }

}

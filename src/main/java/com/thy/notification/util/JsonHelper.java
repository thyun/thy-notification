package com.thy.notification.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thy.notification.entity.NotifyRequest;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonHelper {
	private static final Logger logger = LoggerFactory.getLogger(JsonHelper.class);
	static ObjectMapper objectMapper = new ObjectMapper();

	public static String getExpressionValue(NotifyRequest request, String exp, String format) {
		String value = writeValue(request);
		Map map = readValue(value);
		return getExpressionValue(map, exp, format);
	}

	static String REGEX = "\\{\\{ (.*?) \\}\\}";
	public static String getExpressionValue(Map map, String exp, String format) {
		Pattern p = Pattern.compile(REGEX);
		Matcher m = p.matcher(exp);
		String result = exp;
		while (m.find()) {
			String path = exp.substring(m.start(), m.end());
//			logger.debug("start={} end={} token={}", m.start(), m.end(), path);
			result = result.replace(path, getPathValue(map, path, format));
		}
		return result;
	}

	static String PRE = "{{";
	static String POST = "}}";
	public static String getPathValue(Map map, String path, String format) {
		path = path.trim();
		if (path.startsWith(PRE) && path.endsWith(POST)) {
			String ipath = strip(path, PRE, POST);		// (ex) .commonLabels.alertname
			String rpath = strip(ipath, ".");		// (ex) commonLabels.alertname

			String tokens[] = rpath.split("\\.");
			List<String> tokensList = Arrays.asList(tokens);
			return getPathValue(map, tokensList, format);
		}
		return path;
	}

	private static String getPathValue(Map map, List<String> tokensList, String format) {
		if (tokensList.size() == 1) {
			Object obj = map.get(tokensList.get(0));
			if (obj instanceof String)
				return (String) obj;
			else if (obj instanceof ArrayList)
				return buildValueList((ArrayList) obj, format);
			else
				return writeValue(map.get(tokensList.get(0)));
		}
		String token = tokensList.get(0);
		Map nmap = (Map) map.get(token);
		return getPathValue(nmap, tokensList.subList(1, tokensList.size()), format);
	}

	private static String buildValueList(List<String> valueList, String format) {
		if ("json".equals(format))
			return JsonHelper.writeValue(valueList);
		return String.join(",", valueList);
	}

	public static String strip(String value, String pre, String post) {
		int start = value.indexOf(pre) + pre.length();
		int end = value.indexOf(post);
		return value.substring(start, end).trim();
    }

	public static String strip(String value, String pre) {
		int start = value.indexOf(pre) + pre.length();
		int end = value.length();
		return value.substring(start, end).trim();
	}

    public static String writeValue(Object object) {
		// Output example: {alertname=High Memory Usage of Container, container_name=broker, namespace=default, pod_name=kafka-0, team=devops}
		// return object.toString();

		// Output example: {"alertname":"High Memory Usage of Container","container_name":"broker","namespace":"default","pod_name":"kafka-0","team":"devops"}
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			logger.error(e.toString());
		}
		return "{ }";
	}

	public static Map readValue(String value) {
		try {
			return objectMapper.readValue(value, Map.class);
		} catch (IOException e) {
			logger.error(e.toString());
		}
		return new HashMap<>();
	}

	public static String getJsonValue(String value) {
		JSONObject jo = new JSONObject();
		jo.put("key", value);
		return (String) jo.get("key");
	}

}

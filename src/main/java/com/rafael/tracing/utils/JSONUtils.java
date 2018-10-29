package com.rafael.tracing.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.rafael.tracing.classes.Span;

public class JSONUtils {
	
	// retrieves each property from the JSON Object, make the cast, creates a Span Object
	public static Span convertJsonToSpanObject(JSONObject spanJSON) {

		ZonedDateTime time = ZonedDateTime.parse((String) spanJSON.get("time"));
		String msg = (String) spanJSON.get("msg");
		String component = (String) spanJSON.get("component");
		String app = (String) spanJSON.get("app");
		String spanId = (String) spanJSON.get("span_id");
		String traceId = (String) spanJSON.get("trace_id");

		Span span = new Span();

		span.setTime(time);
		span.setMsg(msg);
		span.setComponent(component);
		span.setApp(app);
		span.setSpanId(spanId);
		span.setTraceId(traceId);

		return span;
	}
	
	
	public static List<JSONObject> sortJsonByTraceIdAndTime() throws FileNotFoundException, IOException, ParseException {

		JSONParser parser = new JSONParser();

		// reads the JSON file and parses it to JSONArray Object
		JSONArray jsonLogs = (JSONArray) parser.parse(new FileReader("resources/log-data.json"));
		
		// sorts the JSON Object by "trace_id" following by "time"
		@SuppressWarnings("unchecked")
		List<JSONObject> jSON = (List<JSONObject>) jsonLogs.parallelStream()
				.sorted((Comparator.comparing((t) -> ((JSONObject) t).get("trace_id").toString())).thenComparing(
						Comparator.comparing((t) -> ZonedDateTime.parse(((JSONObject) t).get("time").toString()))))
				.collect(Collectors.toList());
		return jSON;
	}

}

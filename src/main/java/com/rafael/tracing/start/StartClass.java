package com.rafael.tracing.start;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.simple.JSONObject;
import com.rafael.tracing.classes.Span;
import com.rafael.tracing.utils.JSONUtils;
import com.rafael.tracing.utils.MsgUtils;

public class StartClass {

	private static Map<String, List<Span>> mapSpans;
	private static Set<String> listErrorTraceId;
	private static int numberOfFailTransactions = 0;

	public static void main(String args[]) {
		
		//initiate collections
		mapSpans = new HashMap<String, List<Span>>();
		listErrorTraceId = new HashSet<String>();

		//starts the program
		startProgram();
	}
	
	private static void startProgram(){
		
		try {
			//reads JSON file and sort spans by trace_id followed by time
			List<JSONObject> jsonLogs = JSONUtils.sortJsonByTraceIdAndTime();
			
			//interacts over the sorted collection
			for (Object transaction : jsonLogs) {
				
				// cast current object to JSONObject
				JSONObject spanJSON = (JSONObject) transaction;
				
				// check if the current object contains property "error"
				boolean isAnError = spanJSON.containsKey("error");
				
				//converts current JSON Object to a Java Object (Span)
				Span span = JSONUtils.convertJsonToSpanObject(spanJSON);
				
				// at every interaction update the mapSpans with current span Object
				updateMapSpans(span);
				
				// if the current span object failed, then keep record of the traceId and update by 1 the variable "numberOfFailTransactions"
				if (isAnError) {
					listErrorTraceId.add(span.getTraceId());
					numberOfFailTransactions++;
				}
			}
			
			// if variable "numberOfFailTransactions" is greater than zero then prints the number of fail transaction along with fail log msg from all failed logs
			if(numberOfFailTransactions > 0){
				MsgUtils.printNumberOfFailTransactions(numberOfFailTransactions);
				MsgUtils.printErrorLog(mapSpans, listErrorTraceId);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
	}
	
	private static void updateMapSpans(Span span){
	
		// if mapSpans contains already a key with current traceId then retrieves the list of Spans and update the new list with the current span
		if(mapSpans.containsKey(span.getTraceId())){
			List<Span> spans = mapSpans.get(span.getTraceId());
			spans.add(span);
			mapSpans.put(span.getTraceId(), spans);
		}else{
			//otherwise creates a new list of spans and associate it with the current traceId 
			List<Span> spans = new ArrayList<>();
			spans.add(span);
			mapSpans.put(span.getTraceId(), spans);
		}
	}

	

	

}

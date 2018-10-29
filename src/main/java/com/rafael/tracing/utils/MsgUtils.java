package com.rafael.tracing.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.rafael.tracing.classes.Span;

public class MsgUtils {

	public static void printErrorLog(Map<String, List<Span>> mapSpans, Set<String>  listErrorTraceId) {

		//from the list of errorTraceId retrieves each trace_id
		for (String errorTraceId : listErrorTraceId) {

			// from the mapSpans, use the trace_id as key to retrieve the failed error logs
			List<Span> spans = mapSpans.get(errorTraceId);

			// interacts over spans and print the message error for every span Object
			for (Span span : spans) {

				StringBuilder logFailMsg = new StringBuilder(" - ");

				logFailMsg.append(span.getTime()).append(" ");
				logFailMsg.append(span.getApp()).append(" ");
				logFailMsg.append(span.getComponent()).append(" ");
				logFailMsg.append(span.getMsg());

				System.out.println(logFailMsg);
			}

			System.out.println("");
		}

	}
	
	// prints the total number of fail transactions
	public static void printNumberOfFailTransactions(int numberOfFailTransactions){
		System.out.println("The number of fail transactions are: " + numberOfFailTransactions);
		System.out.println("--------------------------------------------------------------");
	}

}

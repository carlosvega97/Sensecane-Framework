package com.magc.sensecane.framework.spark;

import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.magc.sensecane.framework.container.Container;

import spark.Request;
import spark.Response;
import spark.Route;

public abstract class AbstractRoute<T> implements Route {

	protected final Container container;
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	public AbstractRoute(Container container) {
		this.container = container;
	}
	
	public abstract Boolean isValidRequest(Request request, Response response) throws Exception;
	
	public <T> String toJson(T param) {
		String str = null;
		
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			str = ow.writeValueAsString(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return str;
	}
}

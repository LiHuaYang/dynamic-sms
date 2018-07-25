package com.xxx.rh.sms.bean;

import java.io.Serializable;
import java.util.*;


public class OpenResult implements Serializable {
	private static final long serialVersionUID = 5065447801734585945L;
	
	private String code = "";
	
	private String message = "";
    
    private String successful = Boolean.TRUE.toString();
    
    private Object result = new HashMap<String, Object>();
    
    private String signature = "";
    
	public OpenResult() {
	}
	
	public OpenResult(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getSuccessful() {
		return successful;
	}

	public void setSuccessful(String successful) {
		this.successful = successful;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
	
	public void setResult(Object result, boolean filterNULL) {
		if(filterNULL){
			if(result instanceof Map){
				this.result = filterNULL(result);
				return;
			}
		}
		this.result = result;
	}
	
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	private Object filterNULL(Object result){
		if(result instanceof Map){
			Map tmp = (Map)result;
			for(Object key : tmp.keySet()){
				if(tmp.get(key)==null){
					tmp.put(key, "");
				}else{
					filterNULL(tmp.get(key));
				}
			}
		}else if(result instanceof Collection){
			Collection list = (Collection)result;
			for(Object obj : list){
				filterNULL(obj);
			}
		}
		return result;
	}
}

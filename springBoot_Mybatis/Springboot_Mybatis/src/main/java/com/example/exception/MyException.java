package com.example.exception;

public class MyException extends RuntimeException {
	
	
	
	
	
	
	private static final long serialVersionUID = -4415629889922148689L;
	private String code;
	private String msg;

	public MyException() {
		super();
	}

	public MyException(String message) {
		super(message);
	}

	public MyException(Throwable throwable) {
		super(throwable);
	}

	public MyException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public MyException(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}



	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
}
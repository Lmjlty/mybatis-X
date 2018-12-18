package com.lmj.base.exception;

/**
 * 启动异常
 * @author lmj
 *
 */
public class StartException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5359525622016515295L;

	public StartException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StartException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public StartException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public StartException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public StartException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}

package org.spat.dao;

//TODO : check me
/*
 * 自定义的异常类，包括无参数对象的构造函数和有指定参数对象的构造函数；
 */
	public class ErrorInputException extends Exception {
	    private static final long serialVersionUID = 1L;
	    /**
	     * Constructs a new exception with <code>null</code> as its detail
	     * message. The cause is not initialized, and may subsequently be
	     * initialized by a call to {@link #initCause}.
	     */
	    public ErrorInputException() {
	       super();
	    }
	    /**
	     * @param message
	     *            the detail message. The detail message is saved for later
	     *            retrieval by the {@link #getMessage()} method.
	     */
	    public ErrorInputException(String message) {
	       super(message);
	    }
}

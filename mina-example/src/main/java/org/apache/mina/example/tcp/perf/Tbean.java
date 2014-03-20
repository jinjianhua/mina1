package org.apache.mina.example.tcp.perf;

import java.io.Serializable;

class Tbean implements Serializable{
	
	private static final long serialVersionUID = 1039088580989241301L;

	private String name;
	
	private String code;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
}
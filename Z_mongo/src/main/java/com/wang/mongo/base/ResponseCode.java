package com.wang.mongo.base;

import lombok.Data;


/**
 * 响应码
 * @author wxe
 * @since 1.0.0
 */
@Data
public class ResponseCode {
	
	public static final ResponseCode SUCCESS = new ResponseCode("success", "成功");
	public static final ResponseCode FAILURE = new ResponseCode("false", "失败");
	public static final ResponseCode EXAM_OUT_COUNT = new ResponseCode("false","答题次数超限，无法答题");
	public static final ResponseCode UNKNOWN_FAILURE = new ResponseCode("error", "未知错误");

	private String code;
	private String desc;
	
	public ResponseCode() {
	}

	public ResponseCode(String code, String desc) {
		super();
		this.code = code;
		this.desc = desc;
	}

}

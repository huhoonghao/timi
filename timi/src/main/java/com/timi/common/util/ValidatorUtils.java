package com.timi.common.util;

import java.util.regex.Pattern;

/** 
 * @ClassName: ValidatorUtil 
 * @Description: 
 */
public class ValidatorUtils {

	/**
	 * 正则表达式：验证手机号
	 */
	public static final String REGEX_MOBILE = "^1[0-9]{10}$";

	/**
	 * 正则表达式：验证邮箱
	 */
	public static final String REGEX_EMAIL = "^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,5}$";

    /**
     * 正则表达式：验证密码
     */
	public static final String REGEX_PASSWORD = "^([A-Za-z0-9]|[-_.&@#$]){6,18}$";

	/**
	 * 校验手机号
	 * 
	 * @param mobile
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isMobile(String mobile) {
		return Pattern.matches(REGEX_MOBILE, mobile);
	}

	/**
	 * 校验邮箱
	 * 
	 * @param email
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isEmail(String email) {
		return Pattern.matches(REGEX_EMAIL, email);
	}

	public static boolean isPassword(String password) {return Pattern.matches(REGEX_PASSWORD, password);}

}
package com.baidu.BaiduMap.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.spec.EncodedKeySpec;
import java.util.Random;

public class YCode {
	//key 建议100-200 避免字符串处理出现特殊符�?\
	private static int[] k = {112, 182, 134, 121, 199, 183, 121, 191, 140, 190};
	
	public static String a(String str) {
		if (str == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		char[] result = str.toCharArray();
		try {
			for (int i = 0; i < result.length; i++) {
				char c = result[i];
				char cc = (char) (c + k[(i % k.length)]);
				System.out.print(Integer.toHexString((int)c)+"-"+Integer.toHexString((int)cc)+":");
				sb.append(cc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static String b(String code) {
		if (code == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder(code);

		char[] result = sb.toString().toCharArray();
		sb = new StringBuilder();
		try {
			for (int i = 0; i < result.length; i++) {
				char c = result[i];
				char cc = (char) (c - k[(i % k.length)]);
				sb.append(cc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static byte[] a(byte[] bytes) {
		if (bytes == null) {
			return bytes;
		}
		byte[] retBytes = new byte[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			retBytes[i] = (byte) (bytes[i]^k[(i % k.length)]);
		}

		return retBytes;
	}
	
	public static String getString(byte[] bytes) {
		String retString = new String();
		byte[] tmpBytes = a(bytes);
		try {
			retString = new String(tmpBytes,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return retString;
	}
	
	/**
	 * 设置key
	 * @param k
	 */
	public static void setK(int[] k) {
		YCode.k = k;
	}
	
	/**
	 * set key
	 * @param kString
	 */
	public static void setK(String kString) {
		String [] strArrayStrings = kString.split(",");
		int[] k = new int[strArrayStrings.length];
		for (int i = 0; i < k.length; i++) {
			k[i] = Integer.valueOf(strArrayStrings[i].trim());
		}
		YCode.k = k;
	}

	public static void main(String[] args) {
		Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < 10; i++) {
			System.out.print((random.nextInt() & 0xFF)%100+100 + ", ");
		}
		System.out.println();
		System.out.println(a("123"));
		System.out.println(b(a("\n")));
	}
}
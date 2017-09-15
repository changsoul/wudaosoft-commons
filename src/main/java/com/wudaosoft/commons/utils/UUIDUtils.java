/* 
 * Copyright(c)2010-2016 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */

package com.wudaosoft.commons.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

/**
 * @author Changsoul Wu
 * 
 */
public abstract class UUIDUtils {

	private static final char[] chars = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
			'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z' };

	public static String uuid() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	public static String uuid32() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}

	public static String base64Uuid() {
		UUID uuid = UUID.randomUUID();
		return base64Uuid(uuid);
	}

	protected static String base64Uuid(UUID uuid) {

		ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
		bb.putLong(uuid.getMostSignificantBits());
		bb.putLong(uuid.getLeastSignificantBits());

		return Base64.encodeBase64URLSafeString(bb.array());
	}

	public static String encodeBase64Uuid(String uuidString) {
		UUID uuid = UUID.fromString(uuidString);
		return base64Uuid(uuid);
	}

	public static String decodeBase64Uuid(String compressedUuid) {

		byte[] byUuid = Base64.decodeBase64(compressedUuid);

		ByteBuffer bb = ByteBuffer.wrap(byUuid);
		UUID uuid = new UUID(bb.getLong(), bb.getLong());
		return uuid.toString();
	}

	public static String base58Uuid() {
		UUID uuid = UUID.randomUUID();
		return base58Uuid(uuid);
	}

	protected static String base58Uuid(UUID uuid) {

		ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
		bb.putLong(uuid.getMostSignificantBits());
		bb.putLong(uuid.getLeastSignificantBits());

		return Base58.encode(bb.array());
	}

	public static String encodeBase58Uuid(String uuidString) {
		UUID uuid = UUID.fromString(uuidString);
		return base58Uuid(uuid);
	}

	public static String decodeBase58Uuid(String base58uuid) {
		byte[] byUuid = Base58.decode(base58uuid);
		ByteBuffer bb = ByteBuffer.wrap(byUuid);
		UUID uuid = new UUID(bb.getLong(), bb.getLong());
		return uuid.toString();
	}

	public static String uuid8() {

		StringBuilder sb = new StringBuilder();
		String uuid = uuid32();

		for (int i = 0; i < 8; i++) {

			String str = uuid.substring(i * 4, i * 4 + 4);
			int strInteger = Integer.parseInt(str, 16);
			sb.append(chars[strInteger % 0x3E]);
		}

		return sb.toString();
	}

	public static String uuid19() {
		UUID uuid = UUID.randomUUID();
		StringBuilder sb = new StringBuilder();
		sb.append(digits(uuid.getMostSignificantBits() >> 32, 8));
		sb.append(digits(uuid.getMostSignificantBits() >> 16, 4));
		sb.append(digits(uuid.getMostSignificantBits(), 4));
		sb.append(digits(uuid.getLeastSignificantBits() >> 48, 4));
		sb.append(digits(uuid.getLeastSignificantBits(), 12));
		return sb.toString();
	}

	protected static String longToString(long i, int radix) {
		if (radix < 2 || radix > 62)
			radix = 10;
		if (radix == 10)
			return Long.toString(i);

		final int size = 65;
		int charPos = 64;

		char[] buf = new char[size];
		boolean negative = (i < 0);

		if (!negative) {
			i = -i;
		}

		while (i <= -radix) {
			buf[charPos--] = chars[(int) (-(i % radix))];
			i = i / radix;
		}
		buf[charPos] = chars[(int) (-i)];

		if (negative) {
			buf[--charPos] = '-';
		}

		return new String(buf, charPos, (size - charPos));
	}

	protected static String digits(long val, int digits) {
		long hi = 1L << (digits * 4);
		return longToString(hi | (val & (hi - 1)), 62).substring(1);
	}
}

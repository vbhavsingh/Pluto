package com.log.analyzer.commons;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.ArrayUtils;

public class CipherService {
	/**
	 * 
	 * @param key
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static void setCipherKey(String key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Constants.CIPHER_KEY = generateKey(key);
	}

	/**
	 * 
	 * @param key
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String generateKey(String key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] keyBytes = new byte[32];
		md.update(key.getBytes("iso-8859-1"), 0, key.length());
		keyBytes = md.digest();
		for (int i = 0; i < keyBytes.length; i++) {
			if (keyBytes[i] < 0) {
				keyBytes[i] = (byte) (keyBytes[i] * -1);
			}
			if (keyBytes[i] <= -128) {
				keyBytes[i] = 125;
			}
			if (keyBytes[i] <= 32) {
				keyBytes[i] = (byte) (keyBytes[i] + 32);
			}
			if (keyBytes[i] > 127) {
				keyBytes[i] = 35;
			}
		}
		String secret = new String(keyBytes);
		return secret + secret + new StringBuilder(secret + secret).reverse();
	}

	/**
	 * 
	 * @param plainText
	 * @return
	 * @throws Exception
	 */
	public static Object encrypt(Object object) throws Exception {
		return encrypt(object, Constants.CIPHER_KEY);
	}

	/**
	 * 
	 * @param object
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static Object encrypt(Object object, String key) throws Exception {
		if (key == null) {
			throw new InvalidKeyException("key is null");
		}
		return encryptDecryptObject(object, key, false);
	}
    /**
     * 
     * @param object
     * @param key
     * @return
     * @throws Exception
     */
	public static Object encryptWithOpenkey(Object object, String key) throws Exception {
		String jumbledKey = generateKey(key);
		return encrypt(object, jumbledKey);
	}

	/**
	 * 
	 * @param plainText
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws UnsupportedEncodingException
	 */
	public static String encrypt(String plainText) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		return encrypt(plainText, Constants.CIPHER_KEY);
	}

	/**
	 * 
	 * @param plainText
	 * @param key
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String encryptWithOpenkey(String plainText, String key) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException,
			InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		String jumbledKey = generateKey(key);
		return encrypt(plainText, jumbledKey);
	}

	/**
	 * 
	 * @param plainText
	 * @param key
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws UnsupportedEncodingException
	 */
	private static String encrypt(String plainText, String key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		if (key == null) {
			throw new InvalidKeyException("key is null");
		}
		DESedeKeySpec pbeKeySpec = new DESedeKeySpec(key.getBytes());
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
		SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);

		byte[] salt = new byte[8];
		Random random = new Random();
		random.nextBytes(salt);

		IvParameterSpec ivParameterSpec = new IvParameterSpec(salt);
		Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
		byte[] cipherByte = cipher.doFinal(plainText.getBytes());
		byte[] finalSet = ArrayUtils.addAll(salt, cipherByte);
		return DatatypeConverter.printBase64Binary(finalSet);

	}

	/**
	 * 
	 * @param plainText
	 * @return
	 * @throws Exception
	 */
	public static Object decrypt(Object object) throws Exception {
		return decrypt(object, Constants.CIPHER_KEY);
	}

	/**
	 * 
	 * @param object
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static Object decrypt(Object object, String key) throws Exception {
		if (key == null) {
			throw new InvalidKeyException("key is null");
		}
		return encryptDecryptObject(object, key, true);
	}

	/**
	 * 
	 * @param plainText
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws UnsupportedEncodingException
	 */
	public static String decrypt(String plainText) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		return decrypt(plainText, Constants.CIPHER_KEY);
	}

	/**
	 * 
	 * @param cipherText
	 * @param key
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String decrypt(String cipherText, String key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		if (key == null) {
			throw new InvalidKeyException("key is null");
		}
		DESedeKeySpec pbeKeySpec = new DESedeKeySpec(key.getBytes());
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
		SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);

		byte[] cipherTextBytes = DatatypeConverter.parseBase64Binary(cipherText);

		byte[] salt = new byte[8];
		salt = Arrays.copyOfRange(cipherTextBytes, 0, 8);

		byte[] dataBytes = Arrays.copyOfRange(cipherTextBytes, 8, cipherTextBytes.length);

		IvParameterSpec ivParameterSpec = new IvParameterSpec(salt);
		Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
		return new String(cipher.doFinal(dataBytes));
	}

	private static Object encryptDecryptObject(Object object, String enKey, boolean decrypt) throws Exception {
		if (object == null || isPrimitive(object)) {
			return object;
		}
		if (object instanceof String) {
			if (decrypt) {
				return decrypt((String) object, enKey);
			}
			return encrypt((String) object, enKey);
		}
		if (isMapTyped(object)) {
			return encryptDecryptMap(object, enKey, decrypt);
		}
		if (isCollection(object)) {
			return encryptDecryptCollection(object, enKey, decrypt);
		}
		if (isArrary(object)) {
			return encryptDecryptArray(object, enKey, decrypt);
		}
		Field fields[] = object.getClass().getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			Object fieldVal = f.get(object);
			Object encryptedVal = encryptDecryptObject(fieldVal, enKey, decrypt);
			f.set(object, encryptedVal);
		}
		return object;
	}

	private static Object encryptDecryptMap(Object obj, String enKey, boolean decrypt) throws Exception {
		if (obj == null)
			return obj;
		Map map = (Map) obj;
		Set s = map.keySet();
		Iterator i = s.iterator();
		Map tempMap = (Map) Class.forName(obj.getClass().getName()).newInstance();
		while (i.hasNext()) {
			Object originalKey = i.next();
			Object val = map.get(originalKey);

			Object newKey = encryptDecryptObject(originalKey, enKey, decrypt);
			Object newVal = encryptDecryptObject(val, enKey, decrypt);

			tempMap.put(newKey, newVal);
		}
		return tempMap;
	}

	private static Object encryptDecryptCollection(Object obj, String enKey, boolean decrypt) throws Exception {
		if (obj == null) {
			return obj;
		}
		Collection c = (Collection) obj;
		Iterator i = c.iterator();
		Collection newCollection = (Collection) Class.forName(obj.getClass().getName()).newInstance();
		while (i.hasNext()) {
			Object original = i.next();
			Object encypted = encryptDecryptObject(original, enKey, decrypt);
			newCollection.add(encypted);
		}
		return newCollection;
	}

	private static Object encryptDecryptArray(Object obj, String enKey, boolean decrypt) throws Exception {
		if (obj == null) {
			return obj;
		}
		Object[] newObject = (Object[]) obj;
		for (int i = 0; i < newObject.length; i++) {
			Object transformedObject = encryptDecryptObject(newObject[i], enKey, decrypt);
			newObject[i] = transformedObject;
		}
		return newObject;
	}

	private static boolean isPrimitive(Object object) {
		if (object instanceof Integer)
			return true;
		if (object instanceof Double)
			return true;
		if (object instanceof Boolean)
			return true;
		if (object instanceof Short)
			return true;
		if (object instanceof Float)
			return true;
		if (object instanceof Character)
			return true;
		if (object instanceof Long)
			return true;
		return false;
	}

	private static boolean isCollection(Object obj) {
		if (Collection.class.isAssignableFrom(obj.getClass())) {
			return true;
		}
		if (obj instanceof java.util.Collection) {
			return true;
		}
		return false;
	}

	private static boolean isArrary(Object obj) {
		if (obj instanceof Object[]) {
			return true;
		}
		return false;
	}

	private static boolean isMapTyped(Object obj) {
		if (obj instanceof Map) {
			return true;
		}
		return false;
	}
}

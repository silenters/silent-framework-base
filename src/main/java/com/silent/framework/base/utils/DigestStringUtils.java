package com.silent.framework.base.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DigestStringUtils {

	/**
	 * Hashes a String using the Md5 algorithm and returns the result as a
	 * String of hexadecimal numbers. This method is synchronized to avoid
	 * excessive MessageDigest object creation. If calling this method becomes
	 * a bottleneck in your code, you may wish to maintain a pool of
	 * MessageDigest objects instead of using this method.
	 * <p/>
	 * A hash is a one-way function -- that is, given an
	 * input, an output is easily computed. However, given the output, the
	 * input is almost impossible to compute. This is useful for passwords
	 * since we can store the hash and a hacker will then have a very hard time
	 * determining the original password.
	 * <p/>
	 * In Jive, every time a user logs in, we simply
	 * take their plain text password, compute the hash, and compare the
	 * generated hash to the stored hash. Since it is almost impossible that
	 * two passwords will generate the same hash, we know if the user gave us
	 * the correct password or not. The only negative to this system is that
	 * password recovery is basically impossible. Therefore, a reset password
	 * method is used instead.
	 *
	 * @param data the String to compute the hash of.
	 * @return a hashed version of the passed-in String
	 */
	public static String hash(String data) {
	    return DigestStringUtils.hash(data, "MD5");
	}
	public static String[] getCryptoImpls(String serviceType) {
		Set<String> result = new HashSet<String>();

		// All all providers
		Provider[] providers = Security.getProviders();
		for (int i = 0; i < providers.length; i++) {
			// Get services provided by each provider
			Set<Object> keys = providers[i].keySet();
			for (Iterator<Object> it = keys.iterator(); it.hasNext();) {
				String key = (String) it.next();
				if(key.indexOf("MessageDigest")>0){
					System.out.println(key);
				}
				key = key.split("   ")[0];
				
				if (key.startsWith(serviceType + ".")) {
					result.add(key.substring(serviceType.length() + 1));
				} else if (key.startsWith("Alg.Alias. " + serviceType + ".")) {
					// This is an alias
					result.add(key.substring(serviceType.length() + 11));
				}
			}
		}
		return (String[]) result.toArray(new String[result.size()]);
	} 
	public static void main(String[] args){
		String[] names = getCryptoImpls("MessageDigest");
		for(String str:names){
			System.out.println(str);
		}
		System.out.println(System.currentTimeMillis()-20*24*60*60*1000);
		System.out.println("1234567890".substring(0,"1234567890".indexOf("9")));
		System.out.println(new Date(System.currentTimeMillis()-20*24*60*60*1000));
		System.out.println(hash("mob=13972921950&linkid=11956308&status=0753918246"));
//		byte[] bytes, String algorithm
		System.out.println(hash("".getBytes(),"MD5"));
	}
	/**
	 * Hashes a String using the specified algorithm and returns the result as a
	 * String of hexadecimal numbers. This method is synchronized to avoid
	 * excessive MessageDigest object creation. If calling this method becomes
	 * a bottleneck in your code, you may wish to maintain a pool of
	 * MessageDigest objects instead of using this method.
	 * <p/>
	 * A hash is a one-way function -- that is, given an
	 * input, an output is easily computed. However, given the output, the
	 * input is almost impossible to compute. This is useful for passwords
	 * since we can store the hash and a hacker will then have a very hard time
	 * determining the original password.
	 * <p/>
	 * In Jive, every time a user logs in, we simply
	 * take their plain text password, compute the hash, and compare the
	 * generated hash to the stored hash. Since it is almost impossible that
	 * two passwords will generate the same hash, we know if the user gave us
	 * the correct password or not. The only negative to this system is that
	 * password recovery is basically impossible. Therefore, a reset password
	 * method is used instead.
	 *
	 * @param data the String to compute the hash of.
	 * @param algorithm the name of the algorithm requested.
	 * @return a hashed version of the passed-in String
	 */
	public static String hash(String data, String algorithm) {
	    try {
	        return DigestStringUtils.hash(EncodingUtils.getUTFBytes(data), algorithm);
	    }
	    catch (UnsupportedEncodingException e) {
	    }
	    return data;
	}

	/**
	 * Hashes a byte array using the specified algorithm and returns the result as a
	 * String of hexadecimal numbers. This method is synchronized to avoid
	 * excessive MessageDigest object creation. If calling this method becomes
	 * a bottleneck in your code, you may wish to maintain a pool of
	 * MessageDigest objects instead of using this method.
	 * <p/>
	 * A hash is a one-way function -- that is, given an
	 * input, an output is easily computed. However, given the output, the
	 * input is almost impossible to compute. This is useful for passwords
	 * since we can store the hash and a hacker will then have a very hard time
	 * determining the original password.
	 * <p/>
	 * In Jive, every time a user logs in, we simply
	 * take their plain text password, compute the hash, and compare the
	 * generated hash to the stored hash. Since it is almost impossible that
	 * two passwords will generate the same hash, we know if the user gave us
	 * the correct password or not. The only negative to this system is that
	 * password recovery is basically impossible. Therefore, a reset password
	 * method is used instead.
	 *
	 * @param bytes the byte array to compute the hash of.
	 * @param algorithm the name of the algorithm requested.
	 * @return a hashed version of the passed-in String
	 */
	public static String hash(byte[] bytes, String algorithm) {
	    synchronized (algorithm.intern()) {
	        MessageDigest digest = DigestStringUtils.digests.get(algorithm);
	        if (digest == null) {
	            try {
	                digest = MessageDigest.getInstance(algorithm);
	                DigestStringUtils.digests.put(algorithm, digest);
	            }
	            catch (NoSuchAlgorithmException nsae) {
	                nsae.printStackTrace();
	                return null;
	            }
	        }
	        // Now, compute hash.
	        digest.update(bytes);
	        return EncodingUtils.encodeHex(digest.digest());
	    }
	}

	/**
	 * Used by the hash method.
	 */
	private static Map<String, MessageDigest> digests =new ConcurrentHashMap<String, MessageDigest>();

}

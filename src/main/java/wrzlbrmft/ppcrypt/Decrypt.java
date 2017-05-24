package wrzlbrmft.ppcrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class Decrypt {
	public final static Logger LOGGER = LoggerFactory.getLogger(Decrypt.class);

	public PrivateKey privateKey;

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public boolean setPrivateKey(byte[] privateKeyData) {
		try {
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyData);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			setPrivateKey(keyFactory.generatePrivate(pkcs8EncodedKeySpec));
			return true;
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			LOGGER.error("error setting private-key ({})", e.getMessage());
		}
		return false;
	}

	public byte[] decrypt(byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
			return cipher.doFinal(data);
		}
		catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			LOGGER.error("error on cipher init ()", e.getMessage());
			System.exit(1);
		}
		catch (IllegalBlockSizeException | BadPaddingException e) {
			LOGGER.error("error on decryption ({})", e.getMessage());
			System.exit(1);
		}
		return null;
	}
}

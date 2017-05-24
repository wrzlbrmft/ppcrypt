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
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class Encrypt {
	public final static Logger LOGGER = LoggerFactory.getLogger(Encrypt.class);

	public PublicKey publicKey;

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public boolean setPublicKey(byte[] publicKeyData) {
		try {
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyData);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			setPublicKey(keyFactory.generatePublic(x509EncodedKeySpec));
			return true;
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			LOGGER.error("error setting public key ({})", e.getMessage());
		}
		return false;
	}

	public byte[] encrypt(byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
			return cipher.doFinal(data);
		}
		catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			LOGGER.error("error on cipher init ()", e.getMessage());
			System.exit(1);
		}
		catch (IllegalBlockSizeException | BadPaddingException e) {
			LOGGER.error("error on encryption ({})", e.getMessage());
			System.exit(1);
		}
		return null;
	}
}

package wrzlbrmft.ppcrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}

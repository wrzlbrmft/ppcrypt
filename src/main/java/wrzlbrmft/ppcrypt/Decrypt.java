package wrzlbrmft.ppcrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			LOGGER.error("error setting private key ({})", e.getMessage());
		}
		return false;
	}
}

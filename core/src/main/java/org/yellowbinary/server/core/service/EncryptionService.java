package org.yellowbinary.server.core.service;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.yellowbinary.server.core.dao.ConfigurationDao;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@Service
public class EncryptionService {

    private static final Logger LOG = LoggerFactory.getLogger(EncryptionService.class);

    public static final String ENCODING_CHARSET = "UTF-8";
    public static final String DES_ENCRYPTION_ALGORITHM = "DES";

    private boolean encryptionEnabled;

    @Autowired
    private ConfigurationDao configurationDao;

    @Autowired
    private Environment env;

    private String applicationSecret;

    @PostConstruct
    public void init() {
        encryptionEnabled = configurationDao.readValue(Boolean.class, "application.session.maxAge", true);
        applicationSecret = env.getProperty("application.secret");
        if (encryptionEnabled && StringUtils.isBlank(applicationSecret)) {
            LOG.warn(applicationSecret, "Unable to use encryption without 'application.secret'");
            this.encryptionEnabled = false;
        }

        LOG.debug("Encryption is " + BooleanUtils.toStringOnOff(encryptionEnabled));

    }

    private Cipher getEncryptionCipher(final String value) {
        try {
            final KeySpec keySpec = new DESKeySpec(value.getBytes());
            final SecretKey key = SecretKeyFactory.getInstance(DES_ENCRYPTION_ALGORITHM).generateSecret(keySpec);
            final Cipher encryptionCipher = Cipher.getInstance(key.getAlgorithm());
            encryptionCipher.init(Cipher.ENCRYPT_MODE, key);
            return encryptionCipher;
        } catch (InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            LOG.error("Unable to create encryption cipher", e);
            return null;
        }
    }

    private Cipher getDecryptionCipher(final String value) {
        try
        {
            KeySpec keySpec = new DESKeySpec(value.getBytes());
            SecretKey key = SecretKeyFactory.getInstance(DES_ENCRYPTION_ALGORITHM).generateSecret(keySpec);
            Cipher decryptionCipher = Cipher.getInstance(key.getAlgorithm());
            decryptionCipher.init(Cipher.DECRYPT_MODE, key);
            return decryptionCipher;
        } catch (InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            LOG.error("Unable to create encryption cipher", e);
            return null;
        }
    }

    public String decrypt(final String encryptedValue) {
        try {
            if (this.encryptionEnabled) {
                byte[] decodedValue = new sun.misc.BASE64Decoder().decodeBuffer(encryptedValue);
                Cipher decryptionCipher = getDecryptionCipher(applicationSecret);
                if (decryptionCipher != null) {
                    byte[] unencryptedBytes = decryptionCipher.doFinal(decodedValue);
                    return new String(unencryptedBytes, ENCODING_CHARSET);
                }
                LOG.error("Unable to decrypt value");
                return null;
            } else {
                return encryptedValue;
            }
        } catch (IOException | IllegalBlockSizeException | BadPaddingException e) {
            LOG.error("Unable to decrypt value", e);
            return null;
        }
    }

    public String encrypt(final String plainValue) {
        try {
            if (this.encryptionEnabled) {
                Cipher encryptionCipher = getEncryptionCipher(applicationSecret);
                if (encryptionCipher != null) {
                    final byte[] encryptedBytes = encryptionCipher.doFinal(plainValue.getBytes(ENCODING_CHARSET));
                    final String base64WithNewlines = new sun.misc.BASE64Encoder().encode(encryptedBytes);
                    return base64WithNewlines.replace(System.getProperty("line.separator"), "");
                }
                LOG.error("Unable to encrypt value");
                return null;
            } else {
                return plainValue;
            }
        } catch (IOException | IllegalBlockSizeException | BadPaddingException e) {
            LOG.error("Unable to encrypt value", e);
            return null;
        }
    }

}

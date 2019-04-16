package me.own.learn.commons.base.utils.encode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Christopher.Wang on 2016/8/1.
 */
public class EncodeUtils {

    private static Logger logger = LoggerFactory.getLogger(EncodeUtils.class);

    /**
     * 使用MD5加密+Base64编码
     * @param sourceText
     * @return
     */
    public static String encodeByMD5Base64(String sourceText){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            logger.error("no md5 digest:", e);
        }
        byte[] b = md.digest(sourceText.getBytes());
        BASE64Encoder be = new BASE64Encoder();
        be.encode(b);
        return be.encode(b);
    }
}

package releasehelper.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by guolf on 16/7/16.
 */
public class MessageDigestUtils {

    public static String getMd5FromFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            String md5 = DigestUtils.md5Hex(fis);
            fis.close();
            return md5;
        } catch (Exception ex) {

        }
        return null;
    }

    public static String getSha1FromFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            String md5 = DigestUtils.sha1Hex(fis);
            fis.close();
            return md5;
        } catch (Exception ex) {

        }
        return null;
    }
}

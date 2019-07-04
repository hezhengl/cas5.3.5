package org.suresec.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import cn.com.suresec.jce.provider.SuresecProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class VerifySign {

    public static boolean VerifySignData_RSA(String org,String sign ,String scert)
    {
    	boolean flag = false;
        try {           
            String sFlag = "SHA1WithRSA";
            Security.addProvider(new SuresecProvider());
            BASE64Decoder decoder = new BASE64Decoder();
            //cert
            byte[] byteCert = decoder.decodeBuffer(scert);
            ByteArrayInputStream bain = new ByteArrayInputStream(byteCert);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(bain);

            bain.close();

            Signature signatue = Signature.getInstance(sFlag, "SuresecJCE");
            signatue.initVerify(cert.getPublicKey()); // public
            signatue.update(org.getBytes()); // org

            flag = signatue.verify(decoder.decodeBuffer(sign)); // sign
            System.out.println("验证结果:"+flag);



        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return flag;
    }


    public static boolean VerifySignData_SM2(String org,String sign ,String scert)
    {
    	boolean flag = false;
        try {
            String sFlag = "SM3withSM2";

            Security.addProvider(new SuresecProvider());
            BASE64Decoder decoder = new BASE64Decoder();
            //cert
            byte[] byteCert = decoder.decodeBuffer(scert);
            ByteArrayInputStream bain = new ByteArrayInputStream(byteCert);
            //CertificateFactory cf = CertificateFactory.getInstance("X.509");
            CertificateFactory cf = CertificateFactory.getInstance("X.509","SuresecJCE");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(bain);

            bain.close();

            Signature signatue = Signature.getInstance(sFlag, "SuresecJCE");
            signatue.initVerify(cert.getPublicKey()); // public
            signatue.update(org.getBytes()); // org

            //get sign encode
            byte[] br = new byte[32];
            byte[] bs = new byte[32];
            byte[] bSign = decoder.decodeBuffer(sign);
            System.arraycopy(bSign,32,br,0,32);
            System.arraycopy(bSign,96,bs,0,32);

            SM_signature smsig = new SM_signature(br,bs);
            byte[] bTest = smsig.getEncoded();

            //
            String strB;
            BASE64Encoder encoder = new BASE64Encoder();
            strB = encoder.encode(bTest);
            System.out.println(strB);


            flag = signatue.verify(smsig.getEncoded()); // sign
            System.out.println("验证结果:"+flag);

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
		return flag;
    }

}

package org.suresec.authentication;

import org.apereo.cas.authentication.UsernamePasswordCredential;

/**
 * 
 * @author wcc
 * @time 2019-07-04 02:15
 * @description 凭证（参考老系统）
 */
public class UsernamePasswordKeyPINCredential extends UsernamePasswordCredential {

    public static final String AUTHENTICATION_ATTRIBUTE_PASSWORD = "credential";
   
    private static final long serialVersionUID = -864735145551932618L;
    
    
 // @Size(min=1,message = "required.verificationCode")
    private String verificationCode;  //验证码
    private String submitMode; //登录方式
    private String signvalue; //签名值
    private String CertDN; //DN
    private String sInData; //随机字符串
    private String cert_flag; //证书标识
    
    public String getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	public String getSubmitMode() {
		return submitMode;
	}
	public void setSubmitMode(String submitMode) {
		this.submitMode = submitMode;
	}
	public String getSignvalue() {
		return signvalue;
	}
	public void setSignvalue(String signvalue) {
		this.signvalue = signvalue;
	}
	public String getCertDN() {
		return CertDN;
	}
	public void setCertDN(String certDN) {
		CertDN = certDN;
	}
	public String getsInData() {
		return sInData;
	}
	public void setsInData(String sInData) {
		this.sInData = sInData;
	}
	public String getCert_flag() {
		return cert_flag;
	}
	public void setCert_flag(String cert_flag) {
		this.cert_flag = cert_flag;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((CertDN == null) ? 0 : CertDN.hashCode());
		result = prime * result + ((cert_flag == null) ? 0 : cert_flag.hashCode());
		result = prime * result + ((sInData == null) ? 0 : sInData.hashCode());
		result = prime * result + ((signvalue == null) ? 0 : signvalue.hashCode());
		result = prime * result + ((submitMode == null) ? 0 : submitMode.hashCode());
		result = prime * result + ((verificationCode == null) ? 0 : verificationCode.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsernamePasswordKeyPINCredential other = (UsernamePasswordKeyPINCredential) obj;
		if (CertDN == null) {
			if (other.CertDN != null)
				return false;
		} else if (!CertDN.equals(other.CertDN))
			return false;
		if (cert_flag == null) {
			if (other.cert_flag != null)
				return false;
		} else if (!cert_flag.equals(other.cert_flag))
			return false;
		if (sInData == null) {
			if (other.sInData != null)
				return false;
		} else if (!sInData.equals(other.sInData))
			return false;
		if (signvalue == null) {
			if (other.signvalue != null)
				return false;
		} else if (!signvalue.equals(other.signvalue))
			return false;
		if (submitMode == null) {
			if (other.submitMode != null)
				return false;
		} else if (!submitMode.equals(other.submitMode))
			return false;
		if (verificationCode == null) {
			if (other.verificationCode != null)
				return false;
		} else if (!verificationCode.equals(other.verificationCode))
			return false;
		return true;
	}
}

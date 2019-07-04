package org.suresec.util;

import java.math.BigInteger;
import java.util.Enumeration;

import cn.com.suresec.asn1.ASN1EncodableVector;
import cn.com.suresec.asn1.ASN1Object;
import cn.com.suresec.asn1.ASN1Primitive;
import cn.com.suresec.asn1.ASN1Sequence;
import cn.com.suresec.asn1.DERInteger;
import cn.com.suresec.asn1.DERSequence;

public class SM_signature extends ASN1Object{
	DERInteger      r;
    DERInteger      s;
    
    public static SM_signature getInstance(Object obj) {
		if (obj instanceof SM_signature) {
			return (SM_signature) obj;
		} else if (obj != null) {
			return new SM_signature(ASN1Sequence.getInstance(obj));
		}

		return null;
	}
    
    public SM_signature(
    		byte[] rb,
    		byte[] sb){
    	BigInteger bigR = new BigInteger(1,rb);
    	BigInteger bigS = new BigInteger(1,sb);;
    	
    	this.r = new DERInteger(bigR.toByteArray());
    	this.s = new DERInteger(bigS.toByteArray());
    }
    
    public SM_signature(ASN1Sequence seq){
    	Enumeration e = seq.getObjects();
    	r = DERInteger.getInstance(e.nextElement());
    	s = DERInteger.getInstance(e.nextElement());
    }
    
    
    
	public byte[] getR(){
		return r.getValue().toByteArray();
	}

	public byte[] getS(){
		return s.getValue().toByteArray();
	}

	@Override
	public ASN1Primitive toASN1Primitive() {
		// TODO Auto-generated method stub
		ASN1EncodableVector    v = new ASN1EncodableVector();

        v.add(r);
        v.add(s);
        
        return new DERSequence(v);
	}
}

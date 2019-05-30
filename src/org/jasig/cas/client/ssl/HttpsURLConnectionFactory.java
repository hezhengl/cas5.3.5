/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.cas.client.ssl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import javax.net.ssl.*;
import org.jasig.cas.client.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of the {@link HttpURLConnectionFactory} whose responsible to configure
 * the underlying <i>https</i> connection, if needed, with a given hostname and SSL socket factory based on the
 * configuration provided. 
 * 
 * @author Misagh Moayyed
 * @since 3.3
 * @see #setHostnameVerifier(HostnameVerifier)
 * @see #setSSLConfiguration(Properties)
 */
public final class HttpsURLConnectionFactory implements HttpURLConnectionFactory {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpsURLConnectionFactory.class);

    /**
     * Hostname verifier used when making an SSL request to the CAS server.
     * Defaults to {@link HttpsURLConnection#getDefaultHostnameVerifier()}
     */
    private HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();

    /**
     * Properties file that can contains key/trust info for Client Side Certificates
     */
    private Properties sslConfiguration = new Properties();

    public HttpsURLConnectionFactory() {
    }

    public HttpsURLConnectionFactory(final HostnameVerifier verifier, final Properties config) {
        setHostnameVerifier(verifier);
        setSSLConfiguration(config);
    }

    public final void setSSLConfiguration(final Properties config) {
        this.sslConfiguration = config;
    }

    /**
     * Set the host name verifier for the https connection received.
     * 
     * @see AnyHostnameVerifier
     * @see RegexHostnameVerifier
     * @see WhitelistHostnameVerifier
     */
    public final void setHostnameVerifier(final HostnameVerifier verifier) {
        this.hostnameVerifier = verifier;
    }

    public HttpURLConnection buildHttpURLConnection(final URLConnection url) {
        return this.configureHttpsConnectionIfNeeded(url);
    }

    /**
     * Configures the connection with specific settings for secure http connections
     * If the connection instance is not a {@link HttpsURLConnection},
     * no additional changes will be made and the connection itself is simply returned.
     *
     * @param conn the http connection
     */
    private HttpURLConnection configureHttpsConnectionIfNeeded(final URLConnection conn) {
        if (conn instanceof HttpsURLConnection) {
            final HttpsURLConnection httpsConnection = (HttpsURLConnection) conn;
            final SSLSocketFactory socketFactory = this.createSSLSocketFactory();
            
            if (socketFactory != null) {
                httpsConnection.setSSLSocketFactory(socketFactory);
            }

            //add by wlw.20180530
            if (this.hostnameVerifier != null) {
                httpsConnection.setHostnameVerifier(this.hostnameVerifier);
            }
            
            httpsConnection.setHostnameVerifier(new HostnameVerifier()
            {      
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            });
            //////////////////////////////////////////////////////////////////////
        }
        return (HttpURLConnection) conn;
    }


    
    /**
     * Creates a {@link SSLSocketFactory} based on the configuration specified
     * <p>
     * Sample properties file:
     * <pre>
     * protocol=TLS
     * keyStoreType=JKS
     * keyStorePath=/var/secure/location/.keystore
     * keyStorePass=changeit
     * certificatePassword=aGoodPass
     * </pre>
     * @return the {@link SSLSocketFactory}
     */
    private SSLSocketFactory createSSLSocketFactory() {
        InputStream keyStoreIS = null;
        try {
            final SSLContext sslContext = SSLContext.getInstance(this.sslConfiguration.getProperty("protocol", "SSL"));

            // if keystore exist
            if (this.sslConfiguration.getProperty("keyStoreType") != null) {
                final KeyStore keyStore = KeyStore.getInstance(this.sslConfiguration.getProperty("keyStoreType"));
                if (this.sslConfiguration.getProperty("keyStorePath") != null) {
                    keyStoreIS = new FileInputStream(this.sslConfiguration.getProperty("keyStorePath"));
                    if (this.sslConfiguration.getProperty("keyStorePass") != null) {
                        keyStore.load(keyStoreIS, this.sslConfiguration.getProperty("keyStorePass").toCharArray());
                        LOGGER.debug("Keystore has {} keys", keyStore.size());
                        final KeyManagerFactory keyManager = KeyManagerFactory.getInstance(this.sslConfiguration
                                .getProperty("keyManagerType", "SunX509"));
                        keyManager.init(keyStore, this.sslConfiguration.getProperty("certificatePassword")
                                .toCharArray());
                        
                        TrustManagerFactory tmf =
                                TrustManagerFactory.getInstance("SunX509", "SunJSSE");
                                tmf.init(keyStore);
                                
                        sslContext.init(null, tmf.getTrustManagers(), null);
                        //return sslContext.getSocketFactory();

                    }
                }
            }
            //add by wlw.20180530,no keystrore
            else
            {
            
	            javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];  
	            javax.net.ssl.TrustManager tm = new sureTrustManager();  
	            trustAllCerts[0] = tm;  
	            
	            //mod by wlw
	            sslContext.init(null, trustAllCerts, null);
            }
            
            return sslContext.getSocketFactory();     
            //////////////////////////////////////////////////////////////////////

        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            CommonUtils.closeQuietly(keyStoreIS);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final HttpsURLConnectionFactory that = (HttpsURLConnectionFactory) o;

        if (!hostnameVerifier.equals(that.hostnameVerifier)) return false;
        if (!sslConfiguration.equals(that.sslConfiguration)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = hostnameVerifier.hashCode();
        result = 31 * result + sslConfiguration.hashCode();
        return result;
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        if (this.hostnameVerifier == HttpsURLConnection.getDefaultHostnameVerifier()) {
            out.writeObject(null);
        } else {
            out.writeObject(this.hostnameVerifier);
        }

        out.writeObject(this.sslConfiguration);

    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        final Object internalHostNameVerifier = in.readObject();
        if (internalHostNameVerifier == null) {
            this.hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
        } else {
            this.hostnameVerifier = (HostnameVerifier) internalHostNameVerifier;
        }

        this.sslConfiguration = (Properties) in.readObject();
    }
    
    //add by wlw.20180530
	static class sureTrustManager implements javax.net.ssl.TrustManager,  
	    javax.net.ssl.X509TrustManager {  
		    public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
		    return null;  
	    }  
	 
	    public boolean isServerTrusted(  
		    java.security.cert.X509Certificate[] certs) {  
		    return true;  
	    }  
	 
	    public boolean isClientTrusted(  
		    java.security.cert.X509Certificate[] certs) {  
		    return true;  
	    }  
	 
	    public void checkServerTrusted(  
	    java.security.cert.X509Certificate[] certs, String authType,SSLEngine e)  
	    throws java.security.cert.CertificateException {  
		    return;  
	    }  
	 
	    public void checkClientTrusted(  
	    java.security.cert.X509Certificate[] certs, String authType)  
	    throws java.security.cert.CertificateException {  
	    	return;  
	    }  
	
	
		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			// TODO Auto-generated method stub
			return;
		}  
	}
	//////////////////////////////////////////////////////////////////////
	
}

package com.aliyun.openservices.log;

import com.aliyuncs.auth.AlibabaCloudCredentials;
import com.aliyuncs.auth.AlibabaCloudCredentialsProvider;
import com.aliyuncs.auth.BasicSessionCredentials;

public class Credentials {

    private String accessKeyId;

    private String accessKeySecret;

    private String securityToken;

    private AlibabaCloudCredentialsProvider credentialsProvider;

    public Credentials(AlibabaCloudCredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
        refreshCredentialsIfNeeded();
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public AlibabaCloudCredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public void setCredentialsProvider(AlibabaCloudCredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
    }

    private static boolean isSame(String a, String b) {
        return a == null ? b == null : a.equals(b);
    }

    public boolean refreshCredentialsIfNeeded() {
        if (credentialsProvider == null)
            return false;
        try {
            AlibabaCloudCredentials creds = credentialsProvider.getCredentials();
            String newAkId = creds.getAccessKeyId();
            String newAkSecret = creds.getAccessKeySecret();
            String newToken = "";
            if (creds instanceof BasicSessionCredentials) {
                newToken = ((BasicSessionCredentials) creds).getSessionToken();
            }
            if (isSame(this.accessKeyId, newAkId)
                    && isSame(this.accessKeySecret, newAkSecret)
                    && isSame(this.securityToken, newToken)) {
                return false;
            }
            setAccessKeyId(newAkId);
            setAccessKeySecret(newAkSecret);
            setSecurityToken(newToken);
            return true;
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to refresh credentials", ex);
        }
    }
}


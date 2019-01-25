package com.aliyun.openservices.log;


import com.aliyun.openservices.log.common.Consts;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.http.client.HttpMethod;
import com.aliyun.openservices.log.http.comm.ResponseMessage;

import java.util.Map;


public class AutoRefreshSTSTokenClient extends Client {

    private Credentials credentials;

    public AutoRefreshSTSTokenClient(String endpoint, Credentials credentials) {
        super(endpoint, credentials.getAccessKeyId(), credentials.getAccessKeySecret());
        setSecurityToken(credentials.getSecurityToken());
        this.credentials = credentials;
    }

    @Override
    protected ResponseMessage SendData(String project,
                                       HttpMethod method,
                                       String resourceUri,
                                       Map<String, String> parameters,
                                       Map<String, String> headers,
                                       byte[] body, Map<String, String> output_header,
                                       String serverIp) throws LogException {
        if (credentials.refreshCredentialsIfNeeded()) {
            setAccessId(credentials.getAccessKeyId());
            setAccessKey(credentials.getAccessKeySecret());
            final String securityToken = credentials.getSecurityToken();
            setSecurityToken(securityToken);
            if (securityToken != null && !securityToken.isEmpty()) {
                headers.put(Consts.CONST_X_ACS_SECURITY_TOKEN, credentials.getSecurityToken());
            }
            System.out.println("Credentials has been refreshed successfully");
        }
        return super.SendData(project, method, resourceUri, parameters, headers, body, output_header, serverIp);
    }
}

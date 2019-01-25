package com.aliyun.openservices.log;

import com.aliyun.openservices.log.request.UpdateProjectRequest;
import com.aliyuncs.auth.BasicCredentials;
import com.aliyuncs.auth.STSAssumeRoleSessionCredentialsProvider;
import com.aliyuncs.profile.DefaultProfile;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class AutoRefreshSTSTokenClientTest {


    @Test
    public void testStsTokenWillBeAutoRefreshed() throws Exception {
        String regionId = "cn-hangzhou";
        String accessKeyId = "";
        String accessKeySecret = "";
        String roleArn = "role-arn";

        DefaultProfile profile = DefaultProfile.getProfile(regionId);
        BasicCredentials basicCredentials = new BasicCredentials(accessKeyId, accessKeySecret);
        STSAssumeRoleSessionCredentialsProvider credentialsProvider = new STSAssumeRoleSessionCredentialsProvider(basicCredentials, roleArn, profile);
        credentialsProvider.withRoleSessionDurationSeconds(900);

        String slsEndpoint = "cn-hangzhou.log.aliyuncs.com";

        Credentials credentials = new Credentials(credentialsProvider);
        System.out.println(credentials.getAccessKeyId());
        System.out.println(credentials.getAccessKeySecret());
        System.out.println(credentials.getSecurityToken());

        AutoRefreshSTSTokenClient client = new AutoRefreshSTSTokenClient(
                slsEndpoint, credentials);
        for (int i = 0; i < 1000; i++) {
            // do anything with client
            client.updateProject(new UpdateProjectRequest("xxxx", "xx-new"));
            try {
                System.out.println("Awaiting next call...");
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException ex) {
                System.out.println("InterruptedException occurs");
            }
        }
    }
}

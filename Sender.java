package com.zjw.twitter;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class SendTwitter {
	
	/** 
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果 
     */  
    public void post() {  
        // 创建默认的httpClient实例.    
        
    	CloseableHttpClient httpclient = HttpClients.createDefault();  
        // 创建httppost    
        HttpPost httppost = new HttpPost("http://192.168.146.1:9999/");  
        // 创建参数队列    
        ArrayList<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();  
        formparams.add(new BasicNameValuePair("type", "house"));  
        UrlEncodedFormEntity uefEntity;  
        try {  
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
            httppost.setEntity(uefEntity);  
            System.out.println("executing request " + httppost.getURI());  
            CloseableHttpResponse response = httpclient.execute(httppost);  
            try {  
                HttpEntity entity = response.getEntity();  
                if (entity != null) {  
                    System.out.println("--------------------------------------");  
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));  
                    System.out.println("--------------------------------------");  
                }  
            } finally {  
                response.close();  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
	
    public static void main(String [] args)
    {	while (true) 
    {
    	SendTwitter twitter = new SendTwitter();
    	twitter.post();
	}
    	
    }
    
}

package com.MyLNPU.service.experiment;

import com.MyLNPU.controller.utils.PasswordErrorException;
import com.MyLNPU.model.responseModel.Data;
import com.MyLNPU.model.responseModel.ResEntity;
import com.MyLNPU.model.responseModel.Response;
import com.alibaba.fastjson.JSON;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExperimentLoginService {
    public Response login(String teaId,String teaPwd) throws Exception {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        Data data = new Data();
        HttpPost login = new HttpPost("http://202.118.120.108/login");
        login.setHeader("Content-Type", "application/x-www-form-urlencoded");
        List<NameValuePair> DataForm = new ArrayList<>();
        DataForm.add(new BasicNameValuePair("teaId", teaId));
        DataForm.add(new BasicNameValuePair("teaPwd", teaPwd));
        login.setEntity(new UrlEncodedFormEntity(DataForm, "UTF-8"));
        HttpResponse response = client.execute(login);
        System.out.println(response.getStatusLine());
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode == 200){
            ResEntity parse = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"),ResEntity.class);
            if(parse.getMsgCode().equals("succ")){
                Header header = response.getHeaders("Set-Cookie")[0];
                String cookie = header.getValue().split(";")[0];
                data.setCookie(cookie);
                client.close();
                return new Response(1,"获取实践教学平台Cookie成功",data);
            }
            else {
                client.close();
                throw new PasswordErrorException("账号或密码错误");
            }
        }
        else{
            client.close();
            throw new Exception("登录异常");
        }
    }
}

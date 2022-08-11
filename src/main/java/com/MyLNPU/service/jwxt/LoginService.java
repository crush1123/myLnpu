package com.MyLNPU.service.jwxt;

import com.MyLNPU.controller.utils.PasswordErrorException;
import com.MyLNPU.model.RandomCode;
import com.MyLNPU.model.responseModel.Data;
import com.MyLNPU.model.responseModel.Response;
import com.MyLNPU.utils.Encoded;
import com.MyLNPU.utils.ImgIdentify;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoginService {
    public Response login(String userAccount, String userPassword) throws Exception {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        Data data = new Data();
        String encoded = Encoded.encode(userAccount, userPassword);
        String cookie;
        String code;
        while (true) {
            RandomCode randomCode = ImgIdentify.getVerifyMessage();
            cookie = randomCode.getCookie();
            code = randomCode.getCode();
            HttpPost Login_Post = new HttpPost("https://jwxt.lnpu.edu.cn/jsxsd/xk/LoginToXk");
            Login_Post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            Login_Post.setHeader("cookie", cookie);
            List<NameValuePair> dataForm = new ArrayList<NameValuePair>();
            dataForm.add(new BasicNameValuePair("userAccount", userAccount));
            dataForm.add(new BasicNameValuePair("userPassword", ""));
            dataForm.add(new BasicNameValuePair("RANDOMCODE", code));
            dataForm.add(new BasicNameValuePair("encoded", encoded));
            Login_Post.setEntity(new UrlEncodedFormEntity(dataForm, "UTF-8"));
            HttpResponse PostResponse = client.execute(Login_Post);
            System.out.println(PostResponse.getStatusLine());
            int statusCode = PostResponse.getStatusLine().getStatusCode();
            if (statusCode == 302) {
                HttpGet Login_Get = new HttpGet("http://jwxt.lnpu.edu.cn/jsxsd/framework/xsMain.jsp");
                Login_Get.setHeader("cookie", randomCode.getCookie());
                CloseableHttpResponse GetResponse = client.execute(Login_Get);
                System.out.println(GetResponse.getStatusLine());
                break;
            }
            InputStream inputStream = PostResponse.getEntity().getContent();
            Document document = Jsoup.parse(inputStream, "UTF-8", "");
            Elements elements = document.select("#showMsg");
            if (elements.text().equals("用户名或密码错误")) {
                throw new PasswordErrorException("用户名或密码错误");
            }
            System.out.println("正在重试....");
        }
        client.close();
        data.setCookie(cookie);
        return new Response(1, "登录成功", data);
    }
}

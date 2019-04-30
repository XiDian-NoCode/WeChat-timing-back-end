package org.nocode.timing.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * @Author HanZhao
 * @Description 获取access_token，发送模板消息
 * @Date 2019/4/23
 */
public class SendTemplateMessageUtil {

    private static final String APPID = "wx0885230cd5c5837d";
    private static final String SECRET = "5dabd06fceb7c6cd07bd437fa8c07269";

    /**
     * 发送模板消息sendTemplateMessage
     * 小程序模板消息,发送服务通知
     *
     * @param touser      接收者（用户）的 openid
     * @param template_id 所需下发的模板消息的id
     * @param page        点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
     * @param formid      表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
     * @return
     */
    public static JsonNode sendTemplateMessage(String accessToken, String touser, String template_id, String page, String formid, Map<String, TemplateData> map) throws IOException {
        TemplateMessage templateMessage = new TemplateMessage();
        //拼接数据
        templateMessage.setTouser(touser);
        templateMessage.setTemplate_id(template_id);
        templateMessage.setPage(page);
        templateMessage.setForm_id(formid);
        templateMessage.setData(map);
        templateMessage.setEmphasis_keyword("");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(templateMessage);
        System.out.println("模版发送JSON数据:  " + json);
        String url = "http://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + accessToken;
        String ret = null;
        try {
            ret = sendPost(url, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapper.readTree(ret);
    }

    /**
     * 发送post请求 json格式
     *
     * @param url
     * @param param
     * @return
     */
    public static String sendPost(String url, String param) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();//构建一个Client
        HttpPost post = new HttpPost(url);//构建一个POST请求
        StringEntity s = new StringEntity(param, "UTF-8");
        s.setContentEncoding("UTF-8");
        s.setContentType("application/json; charset=UTF-8");
        post.setEntity(s);//设置编码，不然模板内容会乱码
        HttpResponse response = client.execute(post);//提交POST请求
        HttpEntity result = response.getEntity();//拿到返回的HttpResponse的"实体"
        String content = EntityUtils.toString(result);
        return content;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 从微信服务器获取获取accessToken，时效2小时
     */
    public static String getAccessToken() throws IOException {
        String param = "grant_type=client_credential&appid=" + APPID + "&secret=" + SECRET;
        String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";
        String result = sendGet(ACCESS_TOKEN, param);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(result);
        return mapper.writeValueAsString(rootNode.path("access_token"));
    }

}

package charter9;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class MyCookiesForPost
{
	private ResourceBundle bundle;
	private String url;
	private String result;
	private CookieStore cookiestore;

	@BeforeTest
	public void beforeTest()
	{
		bundle = ResourceBundle.getBundle("application",Locale.CHINA);
		url = bundle.getString("test.url");

	}

	@Test
	public void testGetCookies() throws IOException
	{
		String getcookieurl = bundle.getString("getcookie.url");
		String testurl = url + getcookieurl;

		// 测试代码逻辑书写
		HttpGet get = new HttpGet(testurl);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(get);
		result = EntityUtils.toString(response.getEntity(), "utf-8");
		System.out.println(result);

		// 获取cookies,保存cookiestore
		cookiestore = client.getCookieStore();
		List<Cookie> cookielist = cookiestore.getCookies();
		for (Cookie cookie : cookielist)
		{
			String name = cookie.getName();
			String value = cookie.getValue();
			System.out.println("cookie name: " + name + " cookie value: " + value);

		}

	}

	@Test(dependsOnMethods =
	{ "testGetCookies" })
	public void testGetWithCookies() throws IOException
	{
		String gettestwithcookieurl = bundle.getString("testpostwithcookie");
		String testurl = url + gettestwithcookieurl;

		// 测试代码逻辑书写
		HttpPost post = new HttpPost(testurl);
		JSONObject parm=new JSONObject();
		parm.put("name", "test");
		parm.put("age", "20");
		//设置头信息
		post.setHeader("content-type","application/json");
		//将参数添加到方法中
		StringEntity entity=new StringEntity(parm.toString(),"utf-8");
		post.setEntity(entity);
		
		DefaultHttpClient client = new DefaultHttpClient();
		//添加cookie
		client.setCookieStore(cookiestore);
		HttpResponse response = client.execute(post);

			result = EntityUtils.toString(response.getEntity(), "utf-8");
			System.out.println(result);
			
			JSONObject json=new JSONObject(result);
			
			String name=json.getString("name");
			String age=json.getString("age");
			System.out.println("name: "+name+" age"+age);
		
	}

}

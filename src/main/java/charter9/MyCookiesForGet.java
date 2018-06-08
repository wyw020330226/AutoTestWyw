package charter9;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class MyCookiesForGet
{
	private ResourceBundle bundle;
	private String url;
	private String result;
	private CookieStore cookiestore;

	@BeforeTest
	public void beforeTest()
	{
		bundle = ResourceBundle.getBundle("application");
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
		String gettestwithcookieurl = bundle.getString("testwithcookie.url");
		String testurl = url + gettestwithcookieurl;

		// 测试代码逻辑书写
		HttpGet get = new HttpGet(testurl);
		DefaultHttpClient client = new DefaultHttpClient();
		client.setCookieStore(cookiestore);
		HttpResponse response = client.execute(get);

		// 获取响应状态码
		int statuesCode = response.getStatusLine().getStatusCode();
		if (200 == statuesCode)
		{
			result = EntityUtils.toString(response.getEntity(), "utf-8");
			System.out.println(result);
		}
	}

}

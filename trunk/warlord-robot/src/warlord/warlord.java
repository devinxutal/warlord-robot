package warlord;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.UrlEncodedFormEntity;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

public class warlord {

	private static HttpClient httpclient;
	private static HttpContext localContext;
	private static CookieStore cookieStore;

	public static void main(String[] args) {
		System.getProperties().setProperty("httpclient.useragent",
				"Mozilla/5.0");
		try {
			httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
					CookiePolicy.BROWSER_COMPATIBILITY);
			// Create a local instance of cookie store
			cookieStore = new BasicCookieStore();

			// Obtain default HTTP context
			HttpContext defaultContext = httpclient.getDefaultContext();
			// Create local HTTP context
			localContext = new BasicHttpContext(defaultContext);
			// Bind custom cookie store to the local context
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			
			BasicClientCookie identifyCookie = new BasicClientCookie("warlord2.identify","c95b700b8b0d90fe");
			identifyCookie.setDomain("s2.warlord.cn");
			identifyCookie.setPath("/");
			cookieStore.addCookie(identifyCookie);
			
			HttpGet httpget = new HttpGet("http://s2.warlord.cn/");
			setHeaders(httpget);
			setRefer(httpget, "http://s2.warlord.cn/");

			// Pass local context as a parameter
			HttpResponse response = httpclient.execute(httpget, localContext);
			System.out.println("status code: "
					+ response.getStatusLine().getStatusCode());

			HttpEntity entity = response.getEntity();

			System.out.println("local cookie start");
			Cookie[] cookies = cookieStore.getCookies();
			for (int i = 0; i < cookies.length; i++) {
				System.out.println("Local cookie: " + cookies[i]);
			}
			System.out.println("local cookie end");

			printContent(getAsString(entity.getContent()));
			// Consume response content
			if (response.getEntity() != null)
				response.getEntity().consumeContent();

			httpget = new HttpGet("http://s2.warlord.cn/left.jsp");
			setHeaders(httpget);
			setRefer(httpget, "http://s2.warlord.cn/");
			response = httpclient.execute(httpget, localContext);

			if (response.getEntity() != null)
				response.getEntity().consumeContent();
			
			login();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printContent(String content) {
		System.out.println("----------------------------------------");

		System.out.println(content);

		System.out.println("----------------------------------------");
	}

	private static boolean login() {
		try {
			String _username = "helloworld";
			String _password = "helloworld";
			HttpGet httpget = new HttpGet("http://s2.warlord.cn/main2.jsp?p=0");
			setHeaders(httpget);
			setRefer(httpget, "http://s2.warlord.cn/");
			HttpResponse response = httpclient.execute(httpget, localContext);



			HttpEntity entity = response.getEntity();
			if (entity == null)
				return false;
			String content = getAsString(entity.getContent());
			printContent(content);
			String validateStr = "validateImg.jsp?s=";
			int index = content.indexOf(validateStr);

			validateStr = content.substring(index, index + validateStr.length()
					+ 8);
			System.out.println(validateStr);
			if (response.getEntity() != null)
				response.getEntity().consumeContent();
			
			
			httpget = new HttpGet("http://s2.warlord.cn/" + validateStr);
			setHeaders(httpget);
			setRefer(httpget, "http://s2.warlord.cn/main2.jsp?p=0");
			response = httpclient.execute(httpget, localContext);

			Header[] headers = response.getAllHeaders();
			for(Header header:headers){
				System.out.println(header.getName()+":"+header.getValue());
			}
			Cookie[] cookies = cookieStore.getCookies();
			for (int i = 0; i < cookies.length; i++) {
				System.out.println("Local cookie: " + cookies[i]);
			}
			
			InputStream stream = response.getEntity().getContent();
			byte[] imageData = new byte[1024 * 1024];
			int len = 0;
			int num = 0;
			byte[] buffer = new byte[2048];
			while ((num = stream.read(buffer, 0, 1024)) != -1) {
				for (int i = 0; i < num; i++) {
					imageData[len + i] = buffer[i];
				}
				len += num;
			}
			Image image = Toolkit.getDefaultToolkit().createImage(imageData);
			ValidateFrame frame = new ValidateFrame();
			frame.setImage(image);
			frame.setVisible(true);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));
			String randcode = reader.readLine();
			System.out.println("\"" + randcode + "\"");

			if (response.getEntity() != null)
				response.getEntity().consumeContent();
			
			
			HttpPost httppost = new HttpPost("http://s2.warlord.cn/login.jsp");
			setHeaders(httppost);
			setRefer(httpget, "http://s2.warlord.cn/main2.jsp?p=0");
			httppost.setHeader("Content-Type",
					"application/x-www-form-urlencoded");
			// httppost.setHeader("Content-Length","61");
			httppost.setHeader("Cache-Control", "no-cache");

			System.out.println("hahahaha "
					+ httppost.getAllHeaders()[1].getValue());
			NameValuePair[] nvp = new NameValuePair[] {
					new BasicNameValuePair("alexa", "0"),
					new BasicNameValuePair("accounts", _username),
					new BasicNameValuePair("password", _password),
					new BasicNameValuePair("randcode", randcode) };
			httppost.setEntity(new UrlEncodedFormEntity(nvp,HTTP.UTF_8 ));

			System.out.println(httppost.getURI());

			response = httpclient.execute(httppost, localContext);

			String strSessionid2 = "";
			if (strSessionid2.trim().equals("")) {
				try {
					cookies = cookieStore.getCookies();
					if (cookies != null) {
						System.out.println(cookies.length);
						if (cookies[0] != null) {
							strSessionid2 = cookies[0].getValue();
							System.out.println("####"+strSessionid2);
						}
					}
				} catch (Exception ex) {
					System.out.println(ex.toString());
				}
			}
			String loginResult = getAsString(response.getEntity().getContent());
			printContent(loginResult);
			cookies = cookieStore.getCookies();
			for (int i = 0; i < cookies.length; i++) {
				System.out.println("Local cookie: " + cookies[i]);
			}
			if (response.getEntity() != null)
				response.getEntity().consumeContent();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static String getAsString(InputStream in) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer buffer = new StringBuffer();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				buffer.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	private static void setHeaders(HttpRequest request) {
		request
				.setHeader(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; InfoPath.2; .NET CLR 2.0.50727)");
		request.setHeader("Accept-Encoding", "gzip, deflate");
		request.setHeader("UA-CPU", "x86");
		request.setHeader("Accept-Language", "zh-cn");
		request.setHeader("Accept", "*/*");
		request.setHeader("Host", "s2.warlord.cn");
		request.setHeader("Connection", "Keep-Alive");
		request.setHeader("Keep-Alive", "300");
		request.setHeader("Accept-Charset", "gb2312,utf-8;q=0.7,*;q=0.7");

	}

	private static void setRefer(HttpRequest request, String value) {
		request.setHeader("Refer", value);
	}
}

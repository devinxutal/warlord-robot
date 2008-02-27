package warlord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class warlord {

	private static HttpClient httpclient;
	private static HttpContext localContext;

	public static void main(String[] args) {
		try {
			httpclient = new DefaultHttpClient();
			// Create a local instance of cookie store
			CookieStore cookieStore = new BasicCookieStore();

			// Obtain default HTTP context
			HttpContext defaultContext = httpclient.getDefaultContext();
			// Create local HTTP context
			localContext = new BasicHttpContext(defaultContext);
			// Bind custom cookie store to the local context
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			HttpGet httpget = new HttpGet("http://s2.warlord.cn/");

			// Pass local context as a parameter
			HttpResponse response = httpclient.execute(httpget, localContext);
			System.out.println("status code: "
					+ response.getStatusLine().getStatusCode());

			HttpEntity entity = response.getEntity();
			
			printContent(getAsString(entity.getContent()));

			Cookie[] cookies = cookieStore.getCookies();
			for (int i = 0; i < cookies.length; i++) {
				System.out.println("Local cookie: " + cookies[i]);
			}
			// Consume response content
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

	private static boolean login(){
		try {
			HttpGet httpget = new HttpGet("http://s2.warlord.cn/main2.jsp?p=0");
			HttpResponse response = httpclient.execute(httpget, localContext);
			HttpEntity entity = response.getEntity();
			if(entity == null)
				return false;
			String content = getAsString(entity.getContent());
			printContent(content);
			
			
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
}

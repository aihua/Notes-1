/**
 * 2009-5-20
 */
package org.zlex.chapter11_2;

import static org.junit.Assert.*;

import java.io.DataInputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.junit.Test;

/**
 * HTTPS测试
 * 
 * @author 梁栋
 * @version 1.0
 */
public class HTTPSCoderTest {

	/**
	 * 密钥库/信任库密码
	 */
	private String password = "pekall1234";

	/**
	 * 密钥库文件路径
	 */
	private String keyStorePath =
            "/Users/jiangrui/git/note/computer.science/security/certificate/openssl/certs/server.p12";

	/**
	 * 信任库文件路径
	 */
	private String trustStorePath =
            "/Users/jiangrui/git/note/computer.science/security/certificate/openssl/certs/ca.p12";

	/**
	 * 访问地址
	 */
//	private String httpsUrl = "https://www.zlex.org/ssl/";
	private String httpsUrl = "https://www.zlex.org";

	/**
	 * HTTPS验证
	 * 
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {

		// 建立HTTPS链接
		URL url = new URL(httpsUrl);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

		// conn.setRequestMethod(method);

		// 打开输入输出流
		conn.setDoInput(true);
		// conn.setDoOutput(true);

		// 为HttpsURLConnection配置SSLSocketFactory
		HTTPSCoder.configSSLSocketFactory(conn, password, keyStorePath,
				trustStorePath);

		// 鉴别内容长度
		int length = conn.getContentLength();

		byte[] data = null;

		// 如果内容长度为-1，则放弃解析
		if (length != -1) {

			DataInputStream dis = new DataInputStream(conn.getInputStream());

			data = new byte[length];

			dis.readFully(data);

			dis.close();

			System.err.println(new String(data));
		}

		conn.disconnect();

		// 验证
		assertNotNull(data);

	}

}
package net.justonlyone.http;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class HttpPostUtil {
	public static final String BOUNDARY = "---------7d4a6d158c9"; // 定义数据分隔线

	public static final String BOUNDARY_DATA = "--" + BOUNDARY + "\r\n";

	public static final byte[] END_BOUNDARY_DATA = ("\r\n--" + BOUNDARY + "--\r\n").getBytes(); // 定义结束数据分隔线

	public static final String CONTENT_TYPE = "multipart/form-data; boundary=" + BOUNDARY;

	public static void main(String[] args) {
		String sessionId = login("http://localhost:8080/identity.ajax?cmd=login", "hollycrm", "1");

		String url = "http://localhost:8080/dispatch.ajax?cmd=save";

		List files = new ArrayList();
		files.add("d:/temp/47201554.xls");
		files.add("d:/temp/zsk.dmp");

		List keyValues = new ArrayList();
		keyValues.add(new String[] { "flowId", "47872356" });
		keyValues.add(new String[] { "title", "这是通过程序post过来进行修改的" });
		keyValues.add(new String[] { "sendTo", "47191172" });
		keyValues.add(new String[] { "remark", "营销目标测试，通过post程序发送" });

		// 有几个附件，就写几个infoType类型
		for (int i = 0; i < files.size(); i++) {
			keyValues.add(new String[] { "infoType", "dispatch_" });
		}

		upload(sessionId, url, keyValues, files);
	}

	public static String login(String _url, String user, String password) {
		StringBuffer sb = new StringBuffer();
		sb.append(_url);
		sb.append(_url.indexOf("?") != -1 ? "&" : "?");
		sb.append("userName=");
		sb.append(user);
		sb.append("&password=");
		sb.append(password);

		try {
			URL url = new URL(sb.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			try {
				connection.setRequestMethod("POST");

				Map map = connection.getHeaderFields();
				Iterator iter = map.keySet().iterator();
				while (iter.hasNext()) {
					String key = (String) iter.next();
					List values = (List) map.get(key);
					if (!"Set-Cookie".equals(key)) {
						continue;
					}

					for (int i = 0; i < values.size(); i++) {
						String value = (String) values.get(i);
						if (value.startsWith("JSESSIONID")) {
							return value.substring(0, value.indexOf(";"));
						}
					}
				}
			} finally {
				connection.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String simplePost(String _url, String data, int timeOut) {
		String contentType = "text/html; charset=utf-8";
		return simplePost(_url, contentType, data, timeOut);
	}

	public static String simplePost(String _url, String contentType, String data, int timeOut) {
		try {
			URL url = new URL(_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(timeOut);
			try {
				conn.setRequestMethod("GET");
				conn.setDoOutput(true);
				if (StringUtils.isNotEmpty(contentType)) {
					conn.setRequestProperty("content-type", contentType);
				}

				if (data != null) {
					OutputStream out = conn.getOutputStream();
					out.write(data.getBytes("UTF-8"));
					out.flush();

					out.close();
				}

				InputStream inputStream = conn.getInputStream();
				String encoding = conn.getContentEncoding();
				encoding = encoding == null ? "UTF-8" : encoding;
				return IOUtils.toString(inputStream, encoding);
			} finally {
				conn.disconnect();
			}
		} catch (Exception e) {
			throw new RuntimeException("发送POST请求出现异常！" + e.getMessage(), e);
		}
	}

	public static String upload(String sessionId, String _url, List keyValues, List files) {
		try {
			URL url = new URL(_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			try {
				init(conn, sessionId);

				StringBuffer sb = new StringBuffer();
				OutputStream out = new DataOutputStream(conn.getOutputStream());

				writeKeyValues(keyValues, sb, out);
				if (files != null) {
					writeAttachFiles(files, sb, out);
				}

				out.flush();
				out.close();

				// 定义BufferedReader输入流来读取URL的响应
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = null;
				StringBuilder result = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					result.append(line);
					result.append("\r\n");
				}
				return result.toString();
			} finally {
				conn.disconnect();
			}
		} catch (Exception e) {
			throw new RuntimeException("发送POST请求出现异常！", e);
		}

	}

	/**
	 * 上传前初始化连接
	 * 
	 * @param conn
	 * @throws ProtocolException
	 */
	private static void init(HttpURLConnection conn, String sessionId) throws ProtocolException {
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", CONTENT_TYPE);

		if (sessionId != null) {
			conn.setRequestProperty("Cookie", sessionId);
		}
	}

	/**
	 * 写属性键值对
	 * 
	 * @param keyValues
	 * @param sb
	 * @param out
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private static void writeKeyValues(List keyValues, StringBuffer sb, OutputStream out)
			throws UnsupportedEncodingException, IOException {
		sb.setLength(0);

		for (int i = 0; i < keyValues.size(); i++) {
			String[] keyValue = (String[]) keyValues.get(i);

			sb.append(BOUNDARY_DATA);

			sb.append("Content-Disposition: form-data; name=\"");
			sb.append(keyValue[0]);
			sb.append("\"\r\n\r\n");

			sb = sb.append(keyValue[1]);
			sb = sb.append("\r\n");
		}

		byte[] data = sb.toString().getBytes();
		out.write(data);
	}

	/**
	 * 写附件
	 * 
	 * @param files
	 * @param sb
	 * @param out
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static void writeAttachFiles(List files, StringBuffer sb, OutputStream out) throws IOException,
			FileNotFoundException {
		byte[] data;
		String fname;
		File file;
		byte[] bufferOut;
		int bytes;
		for (int i = 0; i < files.size(); i++) {
			fname = (String) files.get(i);
			file = new File(fname);
			sb.setLength(0);
			sb.append(BOUNDARY_DATA);

			sb.append("Content-Disposition: form-data;name=\"file" + i + "\";filename=\"" + file.getName() + "\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");

			data = sb.toString().getBytes();

			out.write(data);

			DataInputStream in = new DataInputStream(new FileInputStream(file));
			bytes = 0;
			bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
			in.close();
		}

		out.write(END_BOUNDARY_DATA);
	}

}

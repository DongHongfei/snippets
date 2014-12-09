package net.justonlyone.snippets.http.crawler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

/**
 * 将没有访问过的url的内容存储到文件系统中去
 * 
 * @author Xinyuyu
 * 
 */
public class DownloadFile {
	// 构造存储的文件名称（去除url中不能作为文件名的字符）
	public String getFileName(String url, String contentType) {
		String fileName = "";
		if (contentType.contains("html")) {
			fileName = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
		} else {
			// 将类似text/html的字符串截取出文件类型后缀
			fileName = url.replaceAll("[\\?/:*|<>\"]", "_") + "." + contentType.substring(contentType.indexOf("/") + 1);
		}
		return fileName;
	}

	// 一个写文件的方法
	public void saveToLocal(byte[] content, String filePath) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath)));
			for (byte element : content) {
				try {
					out.write(element);
				} catch (IOException e) {
					System.err.println("write error");
					e.printStackTrace();
				}
			}
			try {
				out.close();
			} catch (IOException e) {
				System.err.println("close error");
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.err.println("out error");
			e.printStackTrace();
		}
	}

	// 将page的内容写入文件系统
	public String downloadFile(String url) {
		if (!url.toLowerCase().endsWith(".txt") && !url.toLowerCase().endsWith(".zip")) {
			return "不是txt或zip文件";
		}

		String fileName = "";
		try {
			URL url1 = new URL(url);
			fileName = "/Users/V/work/temp2/" + url.toLowerCase().substring(url.lastIndexOf("/"));
			FileUtils.copyURLToFile(url1, new File(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}

		//
		// HttpClient httpClient = new HttpClient();
		// // 设置HttpClient的连接管理对象，设置 HTTP连接超时5s
		// httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		// if (url.indexOf("http") != -1) {
		// GetMethod getMethod = new GetMethod(url);
		// // 作用是什么？
		// getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		// try {
		//
		// int statut = httpClient.executeMethod(getMethod);
		// if (statut != HttpStatus.SC_OK) {
		// System.err.println("Method failed: " + getMethod.getStatusLine());
		// } else {
		// byte[] content = getMethod.getResponseBody();
		// fileName = "/Users/V/work/temp/"
		// + getFileName(url, getMethod.getResponseHeader("Content-Type").getValue());
		// saveToLocal(content, fileName);
		// System.out.println(url + "::" + fileName);
		// }
		// } catch (HttpException e) {
		// System.err.println("getmethod error");
		// e.printStackTrace();
		// } catch (IOException e) {
		// System.err.println("io error");
		// e.printStackTrace();
		// }
		// }
		return fileName;
	}
}
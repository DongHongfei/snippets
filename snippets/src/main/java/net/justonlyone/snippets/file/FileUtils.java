package net.justonlyone.snippets.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * 文件操作工具 类
 * 
 * @author V
 *
 */
public class FileUtils {

	/**
	 * org.apache.commons.io.FileUtils.readFileToString(file);
	 * 这个方法更牛B
	 * 
	 * @param filePaht
	 * @return
	 */
	@Deprecated
	public static String readFileToString(String filePaht) {

		StringBuffer sb = new StringBuffer();
		try {
			File file = new File(filePaht);
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");// 考虑到编码格式
			BufferedReader bufferedReader = new BufferedReader(read);
			try {
				String lineTxt;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					sb.append(lineTxt);
					sb.append("\r\n");
				}
			} finally {
				read.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}

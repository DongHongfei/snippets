package net.justonlyone.snippets.http.crawler;

import java.util.HashSet;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.EncodingChangeException;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * 完成对要分析的page包含的地址的提取
 * 将提取的地址放入到一个集合中去
 * 
 * @author Xinyuyu
 *
 */
public class HtmlParser {
	private Set<String> links = new HashSet<String>();

	public Set<String> extraLinks(String path) {
		// 需要两个过滤器，一个过滤掉<frame src=>和<a href>标签
		@SuppressWarnings("serial")
		NodeFilter nodeFiler = new NodeFilter() {
			public boolean accept(Node node) {
				if (node.getText().startsWith("fram src=")) {
					return true;
				} else {
					return false;
				}
			}
		};
		NodeFilter tagFilter = new NodeClassFilter(LinkTag.class);
		OrFilter orFilter = new OrFilter(nodeFiler, tagFilter);
		try {
			Parser parser = new Parser(path);
			NodeList nodes;
			try {
				nodes = parser.extractAllNodesThatMatch(orFilter);
			} catch (EncodingChangeException e) {
				// e.printStackTrace();
				parser.setEncoding("gb2312");
				nodes = parser.extractAllNodesThatMatch(orFilter);
			}
			if (nodes != null) {
				for (int i = 0; i < nodes.size(); i++) {
					Node node = nodes.elementAt(i);
					if (node instanceof LinkTag) {
						links.add(((LinkTag) node).getLink());
					} else {
						// 形如<frame src="test.html">
						String frame = node.getText();
						int start = frame.indexOf("src=");
						frame = frame.substring(start);
						int end = frame.indexOf(">");
						if (end == -1) {
							end = frame.indexOf(" ");
						}
						frame = frame.substring(5, end - 1);
					}
				}
			}
		} catch (ParserException e) {
			System.err.println("extra error");
			e.printStackTrace();
		}
		return links;
	}
}

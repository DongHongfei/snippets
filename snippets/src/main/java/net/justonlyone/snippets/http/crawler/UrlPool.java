package net.justonlyone.snippets.http.crawler;

import java.util.HashSet;
import java.util.Set;

/**
 * UrlPool数据结构是用来存放已经访问过的url信息
 * 该结构是一个HashSet
 * 一部分完成对自己的操作，一部分完成对UrlQueue的操作(判断是否已经存在，将没有访问过的入队)
 * 
 * @author Xinyuyu
 *
 */
public class UrlPool {
	private static Set<String> urlPool = new HashSet<String>();
	private static UrlQueue urlQueue = new UrlQueue();

	/*
	 * 完成自身的操作
	 */
	// 增加元素
	public static UrlQueue getQueue() {
		return urlQueue;
	}

	public static void addPool(String url) {
		urlPool.add(url);
	}

	// 删除元素
	public static void removePool(String url) {
		urlPool.remove(url);
	}

	// 判断是否包含
	public static boolean poolContains(String url) {
		return urlPool.contains(url);
	}

	// 返回元素个数
	public static int poolNum() {
		return urlPool.size();
	}

	/*
	 * 完成对UrlQueue的操作
	 */
	// 判断pool中是否包含url,不包含就入队
	public static void inQueue(String url) {
		if ((url != null) && !url.trim().equals("") && !urlPool.contains(url)) {
			urlQueue.inQueue(url);
		}
	}

	// 完成出队
	public static String outQueue() {
		return urlQueue.outQueue();
	}

	// 返回队列元素个数
	public static int queueNum() {
		return urlQueue.queueNum();
	}

	// 判断urlQueue是否为空
	public static boolean queueIsEmpty() {
		return urlQueue.queueIsEmpty();
	}

	// 判断队列中是否包含元素
	public static boolean queueContains(String url) {
		return urlQueue.contains(url);
	}
}
package net.justonlyone.http.crawler;

import java.util.LinkedList;

/**
 * UrlQueue数据结构用来储存需要进行分析的Url
 * 该结构是一个队列
 * 
 * @author Xinyuyu
 *
 */
class UrlQueue {
	private LinkedList<String> urlQueue = new LinkedList<String>();

	// 进队
	void inQueue(String url) {
		urlQueue.addLast(url);
	}

	// 出队
	String outQueue() {
		return urlQueue.removeFirst();
	}

	// 判断是否队列是否为空
	boolean queueIsEmpty() {
		return urlQueue.isEmpty();
	}

	// 返回队列的元素数
	int queueNum() {
		return urlQueue.size();
	}

	// 判断队列是否包含某元素
	public boolean contains(String url) {
		return urlQueue.contains(url);
	}
}

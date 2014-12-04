package net.justonlyone.snippets.other;

public class RuntimeInfo {

	public static void main(String[] args) {
		// 获得运行时服务器信息
		String s = String.format("totalMemory=%f;freeMemory=%f;maxMemory=%f;",
				Runtime.getRuntime().totalMemory() / 1048576.0, Runtime
						.getRuntime().freeMemory() / 1048576.0, Runtime
						.getRuntime().maxMemory() / 1048576.0);
		System.out.println(s);
	}

}

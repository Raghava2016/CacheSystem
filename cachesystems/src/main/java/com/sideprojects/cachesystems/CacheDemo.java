package com.sideprojects.cachesystems;

public class CacheDemo {
public static void main(String[] args) throws Exception {
	CacheSystem cacheSystem = new CacheSystem();
	cacheSystem.add("Test", "Test2", 1);
	System.out.println(cacheSystem.get("Test"));
	Thread.sleep(2000);
	System.out.println(cacheSystem.get("Test"));
}
}

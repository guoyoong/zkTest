package com.lenovo.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import com.lenovo.common.CommonFields;

public class CreateNodeSample {
	
	public static String path = "/zk-book/c1";
	public static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString(CommonFields.hosts)
			.sessionTimeoutMs(5000)
			.retryPolicy(new ExponentialBackoffRetry(1000, 3))
			.build();
			
	public static void main(String[] args) throws Exception {
		client.start();
		client.create()
			.creatingParentsIfNeeded()
			.withMode(CreateMode.PERSISTENT)
			.forPath(path, "init".getBytes());
	}
		
}

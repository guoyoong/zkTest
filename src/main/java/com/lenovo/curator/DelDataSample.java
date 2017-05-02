package com.lenovo.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.lenovo.common.CommonFields;

public class DelDataSample {
	
	public static String path = "/zk-book/c1";
	public static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString(CommonFields.hosts)
			.sessionTimeoutMs(5000)
			.retryPolicy(new ExponentialBackoffRetry(1000, 3))
			.build();
	
	public static void main(String[] args) throws Exception{
		client.start();
		client.create()
			.creatingParentsIfNeeded()
			.withMode(CreateMode.EPHEMERAL)
			.forPath(path, "init".getBytes());
		
		Stat stat = new Stat();
		client.getData().storingStatIn(stat).forPath(path);
		client.delete().deletingChildrenIfNeeded()
			.withVersion(stat.getVersion()).forPath(path);
	}
}

package com.lenovo.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.lenovo.common.CommonFields;

public class CreateSessionSample {
	
	public static void main(String[] args) throws InterruptedException {
		/**
		 * baseSleepTimeMs
		 * maxRetries
		 */
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(
				CommonFields.hosts, 5000, 3000, retryPolicy);
		client.start();
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	public void otherMethod() throws InterruptedException{
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(CommonFields.hosts)
				.sessionTimeoutMs(5000)
				.namespace("base")//项目的命名空间
				.retryPolicy(retryPolicy).build();
		client.start();
		Thread.sleep(Integer.MAX_VALUE);
	}
}

package com.lenovo.curator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import com.lenovo.common.CommonFields;

public class CreateNodeBackGroundSample {
	
	public static String path = "/zk-book";
	public static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString(CommonFields.hosts)
			.sessionTimeoutMs(5000)
			.retryPolicy(new ExponentialBackoffRetry(1000, 3))
			.build();
	public static CountDownLatch semaphore = new CountDownLatch(2);
	public static ExecutorService tp = Executors.newFixedThreadPool(2);
	
	public static void main(String[] args) throws Exception{
		client.start();
		System.out.println("Main thread: " + Thread.currentThread().getName());
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
			.inBackground(new BackgroundCallback() {
				
				public void processResult(CuratorFramework client, CuratorEvent event)
						throws Exception {
					System.out.println("event[code: "+ event.getResultCode()+","
							+ "type:"+event.getType()+"]");
					System.out.println("Thread of processResult:"+Thread.currentThread().getName());
					semaphore.countDown();
				}
			}, tp).forPath(path, "init".getBytes());
		
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
		.inBackground(new BackgroundCallback() {
			
			public void processResult(CuratorFramework client, CuratorEvent event)
					throws Exception {
				System.out.println("event[code: "+ event.getResultCode()+","
						+ "type:"+event.getType()+"]");
				System.out.println("Thread of processResult:"+Thread.currentThread().getName());
				semaphore.countDown();
			}
		}, tp).forPath(path, "init".getBytes());
		
		semaphore.await();
		tp.shutdown();
	}
}

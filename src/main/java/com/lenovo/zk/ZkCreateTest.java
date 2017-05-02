package com.lenovo.zk;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ZkCreateTest {
	
	public static String hosts = "192.168.64.11:2181,192.168.64.12:2181,192.168.64.13:2181,"
			+ "192.168.64.14:2181,192.168.64.15:2181";
	private static CountDownLatch countDownLatch = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception{
		ZooKeeper zk = new ZooKeeper(hosts, 5000, new ConnWatcher());
		countDownLatch.await();
		String path1 = zk.create("/zk-test-ephemeral-", "123".getBytes(), 
				Ids.OPEN_ACL_UNSAFE, //����֮�������ڵ���κβ���������Ȩ�޿���.
				CreateMode.EPHEMERAL);
		System.out.println("Success create znode:" + path1);
		
		String path2 = zk.create("/zk-test-ephemeral-", "123".getBytes(), 
				Ids.OPEN_ACL_UNSAFE, //����֮�������ڵ���κβ���������Ȩ�޿���.
				CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println("Success create znode:" + path2);
	}
	
	public static class ConnWatcher implements Watcher {
		public void process(WatchedEvent event) {
			System.out.println("Receive watched event:"+event);
			if (event.getState() == KeeperState.SyncConnected) {
				countDownLatch.countDown();
			}
		}
	}
}

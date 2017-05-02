package com.lenovo.zk;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ZkCreateASyncTest implements Watcher {

	private static CountDownLatch countDownLatch = new CountDownLatch(1);

	public static void main(String[] args) throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper(ZkCreateTest.hosts, 5000,
				new ZkCreateASyncTest());
		countDownLatch.await();

		zooKeeper.create("/zk-test-ephemeral-", "".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
				new IStringCallBack(), "I am context.");

		zooKeeper.create("/zk-test-ephemeral-", "".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
				new IStringCallBack(), "I am context.");

		zooKeeper.create("/zk-test-ephemeral-", "".getBytes(),
				Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,
				new IStringCallBack(), "I am context.");
		Thread.sleep(Integer.MAX_VALUE);
	}

	public void process(WatchedEvent event) {
		System.out.println("Receive watched event:" + event);
		if (event.getState() == KeeperState.SyncConnected) {
			countDownLatch.countDown();
		}
	}

	static class IStringCallBack implements AsyncCallback.StringCallback {

		public void processResult(int rc, String path, Object ctx, String name) {
			System.out.println("Create path result: [" + rc + "," + path + ","
					+ ctx + ", real path name:" + name);

		}

	}

}

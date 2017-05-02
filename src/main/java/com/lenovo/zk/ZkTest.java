package com.lenovo.zk;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

public class ZkTest {
	protected String hosts = "192.168.64.11:2181,192.168.64.12:2181,192.168.64.13:2181,"
			+ "192.168.64.14:2181,192.168.64.15:2181";
	/**
	 * 连接的超时时间, 毫秒
	 */
	private static final int SESSION_TIMEOUT = 5000;
	private CountDownLatch connectedSignal = new CountDownLatch(1);
	protected ZooKeeper zk;

	public void connect() {
		try {
			zk = new ZooKeeper(hosts, SESSION_TIMEOUT, new ConnWatcher());
			connectedSignal.await();
			
			//根据sessionID passwd创建一个会话
			long sessionId = zk.getSessionId();
			byte[] passwd = zk.getSessionPasswd();
			// illegal sessionId and sessionPwd
			zk = new ZooKeeper(hosts, SESSION_TIMEOUT, new ConnWatcher(),
					1l,"test".getBytes());
			
			// use correct sessionId and sessionPasswd
			zk = new ZooKeeper(hosts, SESSION_TIMEOUT, new ConnWatcher(),
					sessionId, passwd);
			Thread.sleep(Integer.MAX_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("zk establish");
	}

	public class ConnWatcher implements Watcher {
		public void process(WatchedEvent event) {
			System.out.println("Receive watched event:"+event);
			// 连接建立, 回调process接口时, 其event.getState()为KeeperState.SyncConnected
			if (event.getState() == KeeperState.SyncConnected) {
				// 放开栅栏, wait在connect方法上的线程将被唤醒
				connectedSignal.countDown();
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		ZkTest zkTest = new ZkTest();
		zkTest.connect();
	}
}

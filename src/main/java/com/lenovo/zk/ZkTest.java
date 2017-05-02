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
	 * timeout ms
	 */
	private static final int SESSION_TIMEOUT = 5000;
	private CountDownLatch connectedSignal = new CountDownLatch(1);
	protected ZooKeeper zk;

	public void connect() {
		try {
			zk = new ZooKeeper(hosts, SESSION_TIMEOUT, new ConnWatcher());
			connectedSignal.await();
			
			//base sessionID passwd creat 
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
			// , callback process, event.getState() KeeperState.SyncConnected
			if (event.getState() == KeeperState.SyncConnected) {
				// allow barrier , wait connect
				connectedSignal.countDown();
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		ZkTest zkTest = new ZkTest();
		zkTest.connect();
	}
}

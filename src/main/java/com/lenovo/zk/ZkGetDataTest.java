package com.lenovo.zk;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class ZkGetDataTest implements Watcher{

	private static CountDownLatch countDownLatch = new CountDownLatch(1);
	private static ZooKeeper zk = null;
	private static Stat stat = new Stat();
	
	public static void main(String[] args) throws Exception {
		String path = "/zk-book";
		zk = new ZooKeeper(ZkCreateTest.hosts, 5000, new ZkGetDataTest());
		countDownLatch.await();
		zk.create(path, "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println(new String(zk.getData(path, true, stat)));
		System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());
		zk.setData(path, "123111".getBytes(), -1);
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	public void process(WatchedEvent event) {
		if(KeeperState.SyncConnected == event.getState()){
			if(EventType.None == event.getType() && null == event.getPath()){
				countDownLatch.countDown();
			}else if(event.getType() == EventType.NodeDataChanged){
				try{
					System.out.println(new String(zk.getData(event.getPath(), true, stat)));
					System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

}

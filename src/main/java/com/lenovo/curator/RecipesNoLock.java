package com.lenovo.curator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.lenovo.common.CommonFields;

public class RecipesNoLock {
	
	public static String lock_path = "/curator_recipes_lock_path";
	public static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString(CommonFields.hosts)
			.sessionTimeoutMs(5000)
			.retryPolicy(new ExponentialBackoffRetry(1000, 3))
			.build();
	
	public static void main(String[] args) {
		client.start();
		final InterProcessMutex lock = new InterProcessMutex(client, lock_path);
		final CountDownLatch down = new CountDownLatch(1);
		for(int i=0; i<30; i++){
			new Thread(new Runnable() {
				
				public void run() {
					try{
						down.await();
						lock.acquire();
					}catch(Exception e){}
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:sss|SSS");
					String orderNo = sdf.format(new Date());
					System.out.println("::::"+orderNo);
					try{
						lock.release();
					}catch(Exception e){}
				}
			}).start();;
		}
		down.countDown();
	}
}

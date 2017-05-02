package com.lenovo.curator;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.lenovo.common.CommonFields;

public class RecipesCyclicBarrier {

	public static CyclicBarrier barrier = new CyclicBarrier(3);
	
	public static void JDKBarrier(){
		ExecutorService executor = Executors.newFixedThreadPool(3);
		executor.submit(new Thread( new Runner("player 1")));
		executor.submit(new Thread( new Runner("player 2")));
		executor.submit(new Thread( new Runner("player 3")));
		executor.shutdown();
	}
	
	static class Runner implements Runnable{
		private String name;
		public Runner(String name){
			this.name = name;
		}
		public void run() {
			System.out.println(name+" ready!");
			try{
				barrier.await();
			}catch(Exception e){}
			System.out.println(name+" run!");
		}
	}
	
	public static String barrierPath = "/curator_recipes_barrier_path";
	public static DistributedDoubleBarrier dBarrier;
	
	public static void main(String[] args) throws Exception {
		for(int i=0; i<5; i++){
			new Thread(new Runnable() {
				
				public void run() {
					try {
						CuratorFramework client = CuratorFrameworkFactory.builder()
								.connectString(CommonFields.hosts)
								.retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
						client.start();
//						dBarrier = new DistributedBarrier(client, barrierPath);
						dBarrier = new DistributedDoubleBarrier(client, barrierPath, 5);
						System.out.println(Thread.currentThread().getName()+" barrier setting");
						
//						dBarrier.setBarrier();
//						dBarrier.waitOnBarrier();
//						System.out.println("start...");
						
						dBarrier.enter();
						System.out.println("start...");
						Thread.sleep(Math.round(Math.random())*3000);
						dBarrier.leave();
						System.out.println("quit...");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();;
		}
//		Thread.sleep(2000);
//		dBarrier.removeBarrier();
	}
	
}

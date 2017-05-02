package com.lenovo.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.lenovo.common.CommonFields;

public class RecipesMasterSelect {

	/**
	 * create root node ,eg. /master_select,many machines create nodes same time 
	 *  /master_select/lock only one machie craete success£¬become master
	 */
	
	public static String master_path = "/curator_recipes_master_path";
	public static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString(CommonFields.hosts)
			.retryPolicy(new ExponentialBackoffRetry(1000,  3)).build();
	
	public static void main(String[] args) throws InterruptedException {
		client.start();
		@SuppressWarnings("resource")
		LeaderSelector selector = new LeaderSelector(client, master_path, 
				new LeaderSelectorListenerAdapter() {
					
					public void takeLeadership(CuratorFramework client) throws Exception {
						System.out.println(" master role!!" + client.getData());
						Thread.sleep(3000);
						System.out.println("release master privilige");
					}
				});
		
		selector.autoRequeue();
		selector.start();
	}
}

package com.lenovo.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

import com.lenovo.common.CommonFields;

public class RecipesDistAtomicInt {

	public static String distatomicintPat = "/curator_recipes_distatomicint_path";
	public static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString(CommonFields.hosts)
			.retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
	
	public static void main(String[] args) throws Exception{
		client.start();
		DistributedAtomicInteger atomicInteger = 
				new DistributedAtomicInteger(client, distatomicintPat, 
						new RetryNTimes(3, 1000));
		AtomicValue<Integer> rc = atomicInteger.add(8);
		System.out.println(rc.succeeded());
	}
}

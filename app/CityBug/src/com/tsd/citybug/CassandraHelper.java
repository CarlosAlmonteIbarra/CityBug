package com.tsd.citybug;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;

public class CassandraHelper {
	private String mServerIp = "192.168.1.69"; // "10.10.11.101"
	private String mKeyspace = "citybug";
	private Cluster mCluster;
	private Session mSession;

	public boolean connect() {
		if (mSession != null)
			return true;

		try {
			mCluster = Cluster.builder()
						  .addContactPoint(mServerIp)
						  .withRetryPolicy(DefaultRetryPolicy.INSTANCE)
						  .withLoadBalancingPolicy(new TokenAwarePolicy(new DCAwareRoundRobinPolicy()))
						  .build();
			mSession = mCluster.connect(mKeyspace);
			return true;
		} catch (Exception e) {
			mSession = null;
			return false;
		}
	}

	public boolean send(String macAddress, double latitude, double longitude, double height, String time, int eventType) {
		String query = "INSERT INTO events (id,mac_address,latitude,longitude,height,datetime,event_type) VALUES(now(), '";
		query += macAddress + "', ";
		query += latitude + ", " + longitude + ", " + height;
		query += ", '" + time + "', " + String.valueOf(eventType) + ");";
		try {
			mSession.execute(query);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
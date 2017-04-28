package com.tispr.fastdfs.pool;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

/**
 *
 */
public class ConnectionPool {

	// busy connection instances
	private ConcurrentHashMap<TrackerServer, Object> busyConnectionPool = null;
	// idle connection instances
	private ArrayBlockingQueue<TrackerServer> idleConnectionPool = null;
	// delay lock for initialization

	// the limit of connection instance
	private int size = 2;
	
	private Object obj = new Object();
	
	public static boolean initialize = false;
	
	//heart beat
	HeartBeat beat=null;

	public ConnectionPool(int size, int heartBeatTime) throws IOException {
		this.size = size;
		initClientGlobalWithFile();
		init();
		beat = new HeartBeat(this);
		beat.setHeartbeatTime(heartBeatTime);
		beat.beat();
	}

	/**
	 * init the connection pool
	 * 
	 * @param size
	 * @throws IOException 
	 */
	private void init() throws IOException {
		busyConnectionPool = new ConcurrentHashMap<TrackerServer, Object>();
		idleConnectionPool = new ArrayBlockingQueue<TrackerServer>(this.size);
		TrackerServer trackerServer = null;
		try {
			TrackerClient trackerClient = new TrackerClient();
			for (int i = 0; i < size; i++) {
				trackerServer = trackerClient.getConnection();
				org.csource.fastdfs.ProtoCommon.activeTest(trackerServer.getSocket());
				idleConnectionPool.add(trackerServer);
			}
		} catch (IOException e) {
			FileServerPoolSysout.warn("connection pool constructor throw ioexception");
			throw e;
		} 
	}

	// 1. pop one connection from the idleConnectionPool,
	// 2. push the connection into busyConnectionPool;
	// 3. return the connection
	// 4. if no idle connection, do wait for wait_time seconds, and check again
	/**
	 */
	public TrackerServer checkout(int waitTimes) throws InterruptedException,NullPointerException {
		TrackerServer client1 = idleConnectionPool.poll(waitTimes,
				TimeUnit.SECONDS);
		if (client1 == null) {
			FileServerPoolSysout
					.warn("connection pool  wait tracker time out ,return null");
			throw new NullPointerException(
					"connection pool wait time        out ,return null");
		}
		busyConnectionPool.put(client1, obj);
		return client1;
	}

	// 1. pop the connection from busyConnectionPool;
	// 2. push the connection into idleConnectionPool;
	// 3. do nessary cleanup works.
	public void checkin(TrackerServer client1) {
		if (busyConnectionPool.remove(client1) != null) {
			idleConnectionPool.add(client1);
		}
	}

	// so if the connection was broken due to some erros (like
	// : socket init failure, network broken etc), drop this connection
	// from the busyConnectionPool, and init one new connection.
	public synchronized void drop(TrackerServer trackerServer) {
		// first less connection
		try {
			trackerServer.close();
		} catch (IOException e1) {
		}
		if (busyConnectionPool.remove(trackerServer) != null) {
			try {
				FileServerPoolSysout
						.warn("connection pool drop a tracker connnection,remainder size:"+(busyConnectionPool.size() + idleConnectionPool
								.size()));
				TrackerClient trackerClient = new TrackerClient();
				trackerServer = trackerClient.getConnection();
				org.csource.fastdfs.ProtoCommon.activeTest(trackerServer.getSocket());
			} catch (Exception e) {
				trackerServer = null;
				FileServerPoolSysout
						.warn("when connection pool create new tracker connection throw "+e);
			} finally {
				if (!isContinued(trackerServer)) {
					return;
				}
				idleConnectionPool.add(trackerServer);
				FileServerPoolSysout.warn("ImageServerPool add a connnection,has size:"+ (busyConnectionPool.size() + idleConnectionPool
						.size()));
			}
		}
	}
	
	
	public boolean isContinued(TrackerServer trackerServer){
		if (trackerServer == null && hasConnectionException) {
			return false;
		}
		if (trackerServer == null) {
			hasConnectionException = true;
			// only a thread;
			detector();
		}
		if (hasConnectionException) {
			return false;
		}
		return true;
	}

	private void detector() {

		new Thread() {
			@Override
			public void run() {
				String msg = "connection pool detector new trakcer connection fail ";
				TrackerClient trackerClient = new TrackerClient();
				while (true) {
					TrackerServer trackerServer = null;
					try {
						Thread.sleep(5000);
						trackerServer = trackerClient.getConnection();
						org.csource.fastdfs.ProtoCommon.activeTest(trackerServer.getSocket());
					} catch (Exception e) {
						FileServerPoolSysout
								.warn("when connection pool detector new tracker connection throw "
										+ e);
						trackerServer = null;
					} finally {
						if (trackerServer != null) {
							msg = "connection pool detector new tracker connection success to "
									+ trackerServer.getInetSocketAddress().getHostName();
							try {
								trackerServer.close();
								trackerServer = null;
							} catch (IOException e) {
								trackerServer = null;
								FileServerPoolSysout
										.warn("when connection pool detector temp new tracker connection to close trackerServer throw IOException");
							}
							break;
						}
						FileServerPoolSysout
								.warn("connection pool detector find to current  remainder pool size:"
										+ (busyConnectionPool.size() + idleConnectionPool
												.size()));
						FileServerPoolSysout.warn(msg);
					}
				}
				FileServerPoolSysout.warn(msg);

				if (idleConnectionPool.size() != 0) {
					FileServerPoolSysout
							.warn("connection pool idleConnectionPool remander tracker connection, start  close former tracker connection");
					for (int i = 0; i < size; i++) {
						TrackerServer ts = idleConnectionPool.poll();
						if (ts != null) {
							try {
								ts.close();
							} catch (IOException e) {
								ts = null;
							}
						}
					}
				}
				// re init
				hasConnectionException = false;
				try {
					init();
				} catch (IOException e) {
					FileServerPoolSysout
							.warn("when connection pool detector init() IOException,i am so sorry.");
				}
			}
		}.start();
	}

	boolean hasConnectionException = false;

	public static void initClientGlobalWithFile() {
		try {
			if (!initialize) {
				ClientGlobal.init(System.getProperty("CONFIG")+"fileconfig.properties");
				initialize = true;
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public ArrayBlockingQueue<TrackerServer> getIdleConnectionPool() {
		return idleConnectionPool;
	}
	
	public ConcurrentHashMap<TrackerServer, Object> getBusyConnectionPool(){
		return busyConnectionPool;
	}
	
	
}

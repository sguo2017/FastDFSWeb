package com.tispr.fastdfs.pool;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.csource.fastdfs.TrackerServer;

class HeartBeat {
	
	private ConnectionPool pool = null;
	
	public HeartBeat(ConnectionPool pool){
		this.pool=pool;
	}

	public void beat(){
		TimerTask task=new TimerTask() {
			@Override
			public void run() {
				TrackerServer ts=null;
				FileServerPoolSysout.warn("ConnectionPool execution HeartBeat to fdfs server,have size:"+(pool.getIdleConnectionPool().size()+pool.getBusyConnectionPool().size()+", IdleConnectionPool has size:"+pool.getIdleConnectionPool().size()));
				for(int i=0;i<pool.getIdleConnectionPool().size();i++){
					try {
						ts=pool.checkout(100);
						org.csource.fastdfs.ProtoCommon.activeTest(ts.getSocket());
						pool.checkin(ts);
					} catch (InterruptedException e) {
						FileServerPoolSysout.warn("HeartBeat execution beat throw :"+e);
					}catch(NullPointerException e){
						FileServerPoolSysout.warn("HeartBeat execution beat throw :"+e);
						break;
					}
					catch (IOException e) {
						FileServerPoolSysout.warn("HeartBeat execution beat throw :"+e);
						pool.drop(ts);
					}
				}
			}
		};
		Timer timer=new Timer();
		timer.schedule(task, ahour, ahour);
	}
	public static int ahour=1000*60*60*1;
	public static int waitTimes=0;
	
	/**
	 */
	public void setHeartbeatTime(int time){
		ahour=time*1000;
	}
}

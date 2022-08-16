package com.solbox.delivery.ktcloudSDK;
public class ServerInformation {
	static private String VmId="";
	static private String volumeID="";
	static private String publicIP_ID="";
	static private String networkID="";
	static private String staticNAT_ID="";
	static private String firewallJobId="";
	static private String projectID="";

	static private String log="";
//	public ServerInformation(String VmId, String volumeID, String publicIP_ID,
//			String staticNAT_ID, String projectID, String firewallJobId) {
//		this.VmId = VmId;
//		this.volumeID = volumeID;
//		this.publicIP_ID = publicIP_ID;
//		this.staticNAT_ID = staticNAT_ID;
//		this.projectID = projectID;
//		this.firewallJobId=firewallJobId;
//		//this.networkID=networkID;
//	}

	public void setLog(String log){
		this.log = this.log + System.lineSeparator() + log;
	}

	public void initLog(){
		this.log = "";
	}

	public String getLog(){
		return this.log;
	}
	public String getVmId() {
		return VmId;
	}

	public void setVmId(String VmId) {
		this.VmId = VmId;
	}

	public String getVolumeID() {
		return volumeID;
	}

	public void setVolumeID(String volumeID) {
		this.volumeID = volumeID;
	}

	public String getPublicIP_ID() {
		return publicIP_ID;
	}

	public void setPublicIP_ID(String publicIP_ID) {
		this.publicIP_ID = publicIP_ID;
	}

	public String getNetworkID() {
		return networkID;
	}

	public void setNetworkID(String networkID) {
		this.networkID = networkID;
	}

	public String getStaticNAT_ID() {
		return staticNAT_ID;
	}

	public void setStaticNAT_ID(String staticNAT_ID) {
		this.staticNAT_ID = staticNAT_ID;
	}

	public String getFirewallJobId() {
		return firewallJobId;
	}

	public void setFirewallJobId(String firewallJobId) {
		this.firewallJobId = firewallJobId;
	}

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

}

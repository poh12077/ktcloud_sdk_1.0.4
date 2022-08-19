package com.solbox.delivery.ktcloudSDK;

public class ServerInformation {
    private String VmId = "";
    private String volumeID = "";
    private String publicIP_ID = "";
    private String networkID = "";
    private String staticNAT_ID = "";
    private String firewallJobIdOfInputPort = "";
    private String firewallJobIdOfOutputPort = "";
    private String projectID = "";

    private String log = "";
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

     void setLog(String log) {
        this.log = this.log + System.lineSeparator() + log;
    }

    public void initLog() {
        this.log = "";
    }

    public String getLog() {
        return this.log;
    }

    public String getVmId() {
        return VmId;
    }

     void setVmId(String VmId) {
        this.VmId = VmId;
    }

    public String getVolumeID() {
        return volumeID;
    }

     void setVolumeID(String volumeID) {
        this.volumeID = volumeID;
    }

    public String getPublicIP_ID() {
        return publicIP_ID;
    }

     void setPublicIP_ID(String publicIP_ID) {
        this.publicIP_ID = publicIP_ID;
    }

    public String getNetworkID() {
        return networkID;
    }

     void setNetworkID(String networkID) {
        this.networkID = networkID;
    }

    public String getStaticNAT_ID() {
        return staticNAT_ID;
    }

     void setStaticNAT_ID(String staticNAT_ID) {
        this.staticNAT_ID = staticNAT_ID;
    }

    public String getFirewallJobIdOfInputPort() {
        return firewallJobIdOfInputPort;
    }

     void setFirewallJobIdOfInputPort(String firewallJobId) {
        this.firewallJobIdOfInputPort = firewallJobId;
    }

    public String getFirewallJobIdOfOutputPort() {
        return firewallJobIdOfOutputPort;
    }

     void setFirewallJobIdOfOutputPort(String firewallJobId) {
        this.firewallJobIdOfOutputPort = firewallJobId;
    }

    public String getProjectID() {
        return projectID;
    }

     void setProjectID(String projectID) {
        this.projectID = projectID;
    }

}

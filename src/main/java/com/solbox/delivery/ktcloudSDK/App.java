package com.solbox.delivery.ktcloudSDK;


public class App {

	public static void main(String[] args) {
		try {

			String confPath = "C:\\Users\\young hwa park\\Desktop\\yhp\\source\\ktcloud\\ktcloud_sdk_1.0.4\\conf.json";

			ServerInformation serverInformation = KTCloudOpenAPI.createServer("nanana", "solbox",confPath);
			//ServerInformation serverInformation = KTCloudOpenAPI.createServer("nanana",confPath);

			KTCloudOpenAPI.deleteServer(serverInformation, confPath);
			//KTCloudOpenAPI.init(confPath);

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
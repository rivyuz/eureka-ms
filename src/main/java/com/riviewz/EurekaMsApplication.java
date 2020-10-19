package com.riviewz;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;

import com.netflix.appinfo.AmazonInfo;

@SpringBootApplication
@EnableEurekaServer
public class EurekaMsApplication {

	@Value("${server.port}")
    private int port;

	public static void main(String[] args) {
		SpringApplication.run(EurekaMsApplication.class, args);
	}
	
	@Bean
	public EurekaInstanceConfigBean eurekaInstanceConfigBean(InetUtils utils) {

		// The following is needed if running on Spring Cloud prior to the "Dalston" release train.
		final EurekaInstanceConfigBean instance = new EurekaInstanceConfigBean(utils)
		{
			@Scheduled(initialDelay = 30000L, fixedRate = 30000L)
			public void refreshInfo() {
				AmazonInfo newInfo = AmazonInfo.Builder.newBuilder().autoBuild("eureka");
				if (!this.getDataCenterInfo().equals(newInfo)) {
					((AmazonInfo) this.getDataCenterInfo()).setMetadata(newInfo.getMetadata());
					this.setHostname(newInfo.get(AmazonInfo.MetaDataKey.publicHostname));
					this.setIpAddress(newInfo.get(AmazonInfo.MetaDataKey.publicIpv4));
					this.setDataCenterInfo(newInfo);
					this.setNonSecurePort(port);
				}
			}         
		};
		AmazonInfo info = AmazonInfo.Builder.newBuilder().autoBuild("eureka");
		instance.setHostname(info.get(AmazonInfo.MetaDataKey.publicHostname));
		instance.setIpAddress(info.get(AmazonInfo.MetaDataKey.publicIpv4));
		instance.setDataCenterInfo(info);
		instance.setNonSecurePort(port);

		return instance;
	}

}

/*

	@Bean
	public EurekaInstanceConfigBean eurekaInstanceConfigBean(InetUtils utils) {

		// The following is needed if running on Spring Cloud prior to the "Dalston" release train.
		final EurekaInstanceConfigBean instance = new EurekaInstanceConfigBean(utils)
		{
			@Scheduled(initialDelay = 30000L, fixedRate = 30000L)
			public void refreshInfo() {
				AmazonInfo newInfo = AmazonInfo.Builder.newBuilder().autoBuild("eureka");
				if (!this.getDataCenterInfo().equals(newInfo)) {
					((AmazonInfo) this.getDataCenterInfo()).setMetadata(newInfo.getMetadata());
					this.setHostname(newInfo.get(AmazonInfo.MetaDataKey.publicHostname));
					this.setIpAddress(newInfo.get(AmazonInfo.MetaDataKey.publicIpv4));
					this.setDataCenterInfo(newInfo);
					this.setNonSecurePort(port);
				}
			}         
		};
		AmazonInfo info = AmazonInfo.Builder.newBuilder().autoBuild("eureka");
		instance.setHostname(info.get(AmazonInfo.MetaDataKey.publicHostname));
		instance.setIpAddress(info.get(AmazonInfo.MetaDataKey.publicIpv4));
		instance.setDataCenterInfo(info);
		instance.setNonSecurePort(port);

		return instance;
	}

*/
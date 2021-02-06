package com.roncoo.eshop.datalink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
/**
 * 数据直连服务
 * 数据直连服务，先在自己本地读取ehcache，读redis主集群，通过feign读取依赖服务的接口。
*当获取到数据后，将数据写入主集群，主集群同步到各个机房的从集群，同时数据直连服务获取的到数据返回给nginx，nginx会写入自己本地的local cache。
 * @author dream
 *
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class EshopDataLinkServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(EshopDataLinkServiceApplication.class, args); 
	}
	
	@Bean
	public JedisPool jedisPool() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(100);
		config.setMaxIdle(5);
		config.setMaxWaitMillis(1000 * 10); 
		config.setTestOnBorrow(true);
		return new JedisPool(config, "192.168.25.60", 1111);
	}
}

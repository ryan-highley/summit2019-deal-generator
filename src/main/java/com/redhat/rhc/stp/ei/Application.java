/*
 * Copyright 2016 Red Hat, Inc.
 * <p>
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */
package com.redhat.rhc.stp.ei;

import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({ "classpath:spring/camel-context.xml" })
public class Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	/**
	 * A main method to start this application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean(name = "amqp-component")
	AMQPComponent amqpComponent(AMQPConfiguration config) {
		String url = config.getProtocol() + "://" + config.getHost() + ":" + config.getPort();
		
		if (config.getQueryString() != null && config.getQueryString().length() > 0) {
			url = url + "?" + config.getQueryString();
		}
		
		LOGGER.info("Creating AMQP Component with URL: {}", url);
		
		JmsConnectionFactory qpid = new JmsConnectionFactory(config.getUsername(), config.getPassword(), url);

		return new AMQPComponent(qpid);
	}

}
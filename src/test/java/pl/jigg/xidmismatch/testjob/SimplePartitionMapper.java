/**
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package pl.jigg.xidmismatch.testjob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.inject.Named;
import java.util.Properties;

/**
 * Partition mapper artifact for the loader step of cdr-loader batch job.
 *
 * @author jigga
 */
@Named("SimplePartitionMapper")
public class SimplePartitionMapper implements javax.batch.api.partition.PartitionMapper {

	private static final String SIMPLE_NAME =
		SimplePartitionMapper.class.getSimpleName();

	private static final int MIN = 100;
	private static final int MAX = 1000;
	private static final int PARTITION_COUNT = 3;

	private Logger logger =
		LoggerFactory.getLogger(SimplePartitionMapper.class);

	@PostConstruct
	protected void init() {
		logger.debug("Initializing {} instance: <{}>.", SIMPLE_NAME, toString());
		logger.debug("{} instance <{}> initialized successfully.", SIMPLE_NAME, toString());
	}

	@PreDestroy
	protected void destroy() {
		logger.debug("Removing {} instance: <{}>.", SIMPLE_NAME, toString());
	}

	@Override
	public PartitionPlan mapPartitions() throws Exception {
		/* Creating new partition plan as an anonymous inner class */
		return new PartitionPlanImpl() {

			@Override
			public int getPartitions() {
				return PARTITION_COUNT;
			}

			@Override
			public Properties[] getPartitionProperties() {

				Properties[] partitionProperties =
					new Properties[getPartitions()];
				for (int i = 0; i < getPartitions(); i++) {
					partitionProperties[i] = new Properties();
					int itemCount = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
					partitionProperties[i].put("item-count", itemCount);
				}

				return partitionProperties;

			}

		};
	}

}

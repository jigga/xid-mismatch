/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.jigg.xidmismatch.testjob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jigg.xidmismatch.Subscriber;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.batch.api.chunk.ItemReader;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Properties;

/**
 * {@link javax.batch.api.chunk.ItemReader} of the loader step of the testjob batch job.
 *
 * @author jigga
 */
@Named("TestItemReader")
public class TestItemReader implements ItemReader {

	/*
	 * Holds this class' simple name.
	 */
	private static final String SIMPLE_NAME =
		TestItemReader.class.getSimpleName();

	private Logger logger = LoggerFactory.getLogger(TestItemReader.class);

	@Inject
	private JobContext ctx;

	private int itemCount;
	private int index;

	@PostConstruct
	protected void init() {
		logger.info("Initializing {} instance: {}.", SIMPLE_NAME, toString());
		logger.info("{} instance ({}) successfully initialized.", SIMPLE_NAME, toString());
	}

	@PreDestroy
	protected void destroy() {
		logger.info("Destroying {} instance: {}.", SIMPLE_NAME, toString());
	}

	@Override
	public void open(Serializable checkpoint) throws Exception {

		logger.info("Opening {} instance: <{}>.", SIMPLE_NAME, toString());
		logger.info("Checkpoint: <{}>.", checkpoint);
		if (checkpoint instanceof Number) {
			index = ((Number)checkpoint).intValue();
		}
		Long executionId = ctx.getExecutionId();
		Properties jobParameters =
			BatchRuntime.getJobOperator().getParameters(executionId);
		jobParameters.forEach((k, v) -> logger.info("Job partition parameter \"{}\"=\"{}\"", k, v));
		logger.info("jobParameters.getProperty(\"item-count\"): <{}>.", jobParameters.getProperty("item-count"));
		logger.info("jobParameters.get(\"item-count\"): <{}>.", jobParameters.get("item-count"));
		itemCount = (Integer)jobParameters.get("item-count");
		logger.info("itemCount: <{}>.", itemCount);
		logger.info("{} instance <{}> opened successfully.", SIMPLE_NAME, toString());

	}

	@Override
	public void close() throws Exception {
		logger.info("Closing {} instance: <{}>.", SIMPLE_NAME, toString());
		logger.info("{} instance <{}> closed successfully.", SIMPLE_NAME, toString());
	}

	@Override
	public Object readItem() throws Exception {
		if (index++ < itemCount) {
			Subscriber subscriber = new Subscriber();
			logger.info("Read item: <{}>.", subscriber);
			return subscriber;
		}
		return null;
	}

	@Override
	public Serializable checkpointInfo() throws Exception {
		return index;
	}

}

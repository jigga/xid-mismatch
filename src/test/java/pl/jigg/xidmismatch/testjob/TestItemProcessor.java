/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.jigg.xidmismatch.testjob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * {@link javax.batch.api.chunk.ItemProcessor} of the loader step of the testjob batch job.
 *
 * @author Jigga
 */
@Named("TestItemProcessor")
public class TestItemProcessor implements ItemProcessor {

	/*
	 * Holds this class' simple name.
	 */
	private static final String SIMPLE_NAME =
		TestItemProcessor.class.getSimpleName();

	private Logger logger =
		LoggerFactory.getLogger(TestItemProcessor.class);
	@Inject
	private JobContext ctx;
	private Random generator;

	/**
	 * Initialization method of this bean.
	 */
	@PostConstruct
	protected void init() {
		logger.info("Initializing {} instance <{}>.", SIMPLE_NAME, this);
		this.generator = new Random();
	}

	@Override
	public Object processItem(Object item) throws Exception {
		logger.info("Processing item: <{}>.", item);
		TimeUnit.MILLISECONDS.sleep(generator.nextInt(50));
		return item;
	}

}

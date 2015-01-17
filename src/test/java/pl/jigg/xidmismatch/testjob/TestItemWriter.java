/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.jigg.xidmismatch.testjob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jigg.xidmismatch.Subscriber;
import pl.jigg.xidmismatch.testjob.util.SubscriberPublisher;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.batch.api.chunk.ItemWriter;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link javax.batch.api.chunk.ItemWriter} of the loader step of the testjob batch job.
 *
 * @author jigga
 */
@Named("TestItemWriter")
public class TestItemWriter implements ItemWriter {

	/*
	 * Holds this class' simple name.
	 */
	private static final String SIMPLE_NAME =
		TestItemWriter.class.getSimpleName();

	private Logger logger =
		LoggerFactory.getLogger(TestItemWriter.class);

	@PersistenceContext(unitName = "xid-mismatch")
	private EntityManager entityManager;
	@Inject
	private StepContext stepContext;
	@Inject
	SubscriberPublisher publisher;

	@PostConstruct
	protected void init() {
		logger.info("Initializing {} instance: <{}>.", SIMPLE_NAME, toString());
		logger.info("{} instance <{}> initialized successfully.", SIMPLE_NAME, toString());
	}

	@PreDestroy
	protected void destroy() {
		logger.info("Destroying {} instance: <{}>.", SIMPLE_NAME, toString());
	}

	@Override
	public void open(Serializable checkpoint) throws Exception {
		logger.info("Opening {} for writing. Checkpoint info: <{}>", SIMPLE_NAME, checkpoint);
	}

	@Override
	public void close() throws Exception {
		logger.info("Closing {} instance: {}.", SIMPLE_NAME, toString());
		logger.info("{} instance ({}) successfully closed.", SIMPLE_NAME, toString());
	}

	@Override
	public void writeItems(List<Object> items) throws Exception {
		logger.info("Writing items: <{}>", items);
		List<Subscriber> subscribers = items.stream()
			.filter(item -> item instanceof Subscriber)
			.map(item -> (Subscriber)item)
			.collect(Collectors.toList());
		subscribers.forEach(entityManager::persist);
		entityManager.flush();
		publisher.publish(subscribers);
	}

	@Override
	public Serializable checkpointInfo() throws Exception {
		return null;
	}

}

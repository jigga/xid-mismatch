package pl.jigg.xidmismatch.testjob.util;

import pl.jigg.xidmismatch.Subscriber;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Topic;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author jigga
 */
@Stateless
public class StatelessSubscriberPublisher implements SubscriberPublisher {

	@Inject
	private JMSContext context;

	@Resource(mappedName = "jms/topic/subscribers")
	private Topic topic;

	public void publish(Subscriber subscriber) throws JMSException {
		context.createProducer().send(topic, subscriber);
	}

	@Override
	public void publish(Collection<Subscriber> subscribers) throws JMSException {
		context.createProducer().send(topic, new ArrayList<>(subscribers));
	}
}

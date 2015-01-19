package pl.jigg.xidmismatch.testjob.util;

import pl.jigg.xidmismatch.Subscriber;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author jigga
 */
@Stateless
public class StatelessSubscriberPublisher implements SubscriberPublisher {

	@Resource
	private ConnectionFactory connectionFactory;
	@Resource(mappedName = "jms/topic/subscribers")
	private Topic topic;

	public void publish(Subscriber subscriber) throws JMSException {
		try (JMSContext context = connectionFactory.createContext()){
			context.createProducer().send(topic, subscriber);
		}
	}

	@Override
	public void publish(Collection<Subscriber> subscribers) throws JMSException {
		try (JMSContext context = connectionFactory.createContext()){
			context.createProducer().send(topic, new ArrayList<>(subscribers));
		}
	}
}

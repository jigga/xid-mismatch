package pl.jigg.xidmismatch.testjob.util;

import pl.jigg.xidmismatch.Subscriber;

import javax.jms.JMSException;
import java.util.Collection;

/**
 * @author jigga
 */
public interface SubscriberPublisher {

	public void publish(Subscriber subscriber) throws JMSException;

	public void publish(Collection<Subscriber> subscribers) throws JMSException;

}

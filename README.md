# xid-mismatch

Contains junit arquillian test case for the following glassfish bug [GLASSFISH-21289](https://java.net/jira/browse/GLASSFISH-21289).

The test case itself is in the __XidMismatch__ class.

__@Deployment__ method of the test class creates the web archive and deploys it to the glassfish embedded container,
 whose configuration is in the __glassfish-embedded__ directory. __config/domain.xml__ contains the definition of
 all resources (JMS, JDBC, etc.) utilized by test artifacts.

 __testXidMismatch__ test method starts a __testjob__ batch job. It contains a single chunk-style step with partition mapper -
 step's partitioned as the problem occurs in a concurrent environment.

 __ItemReader__ creates random number (between __MIN__ and __MAX__ as defined in __SimplePartitionMapper__) of entity instances of type __Subscriber__.

 __ItemProcessor__ does nothing, but sleeps for 50 ms.

 __ItemWriter__ persists entities created by the reader and then publishes them to JMS topic (__jms/topic/subscribers__).
 All of this is done in a single, distributed transaction and here's where the problem occurs.

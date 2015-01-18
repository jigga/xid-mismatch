package pl.jigg.xidmismatch;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author jigga
 */
@RunWith(Arquillian.class)
public class XidMismatchTest {

	private static final Logger LOGGER =
		LoggerFactory.getLogger(XidMismatchTest.class);

	private static final List<BatchStatus> BATCH_STATUS_RUNNING =
		Arrays.asList(
			BatchStatus.STARTING,
			BatchStatus.STARTED,
			BatchStatus.STOPPING
		);

	@Deployment
	public static Archive<?> createArchive() {

		List<JavaArchive> libs = Arrays.asList(
			Maven.configureResolver().withClassPathResolution(true)
				.loadPomFromFile("pom.xml")
				.importCompileAndRuntimeDependencies()
				.resolve()
				.withTransitivity()
				.as(JavaArchive.class)
		);

		WebArchive testArchive = ShrinkWrap.create(WebArchive.class, XidMismatchTest.class.getSimpleName() + ".war")
			.addPackage("pl.jigg.xidmismatch")
			.addPackage("pl.jigg.xidmismatch.testjob")
			.addPackage("pl.jigg.xidmismatch.testjob.util")
			.addAsResource("META-INF/batch-jobs")
			.addAsWebInfResource("persistence.xml", "classes/META-INF/persistence.xml")
			.addAsWebInfResource("logback-test.xml", "classes/logback.xml")
			.addAsWebInfResource("beans.xml")
			.addAsLibraries(libs)
			;
		LOGGER.info("{}", testArchive.toString(true));

		return testArchive;

	}

	public XidMismatchTest() {
		LOGGER.info("Creating {} instance...", XidMismatchTest.class.getSimpleName());
	}

	/**
	 * Starts testjob batch job execution and waits for its completion. When it is completed, it's
	 * {@link javax.batch.runtime.JobExecution#getBatchStatus()} batch status is checked.
	 *
	 * @throws Exception    When the test case fails.
	 */
	@Test
	public void testXidMismatch() throws Exception {

		JobOperator operator =
			BatchRuntime.getJobOperator();
		long executionId =
			operator.start("testjob", new Properties());
		LOGGER.info("Started testjob batch job execution; waiting for job execution to complete...");

		pollJobStatus(executionId);

		LOGGER.info("Job execution finished for testjob batch job - asserting job status...");
		Assert.assertEquals(BatchStatus.COMPLETED, operator.getJobExecution(executionId).getBatchStatus());

	}

	private void pollJobStatus(Long executionId) {

		BatchStatus batchStatus =
			BatchRuntime.getJobOperator().getJobExecution(executionId).getBatchStatus();
		while (true) {
			if (BATCH_STATUS_RUNNING.contains(batchStatus)) {
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					LOGGER.warn(String.valueOf(e), e);
				}
				batchStatus =
					BatchRuntime.getJobOperator().getJobExecution(executionId).getBatchStatus();
			} else {
				break;
			}
		}

	}

}
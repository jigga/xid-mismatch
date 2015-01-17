package pl.jigg.xidmismatch;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author jigga
 */
@Entity
@Table(name = "SUBSCRIBER")
public class Subscriber implements Serializable {

	/** use serialVersionUID for interoperability */
	private static final long serialVersionUID = -1788685228228540664L;
	private static final Random GENERATOR = new Random();


	@Id
	@SequenceGenerator(name = "SubscriberIdGenerator", allocationSize = 1, sequenceName = "SEQ_SUBSCRIBER_ID")
	@GeneratedValue(generator = "SubscriberIdGenerator", strategy = GenerationType.SEQUENCE)
	@Column(name = "ID", nullable = false, precision = 20)
	private Long id;

	@Column(name = "ACCOUNT_TYPE")
	private Long accountType;

	@Column(name = "CLI")
	private String cli;

	@Column(name = "ACTIVATION_DATE")
	@Temporal(TemporalType.DATE)
	private Date activationDate;

	public Subscriber() {
		this.accountType = GENERATOR.nextLong();
		this.cli = UUID.randomUUID().toString();
		this.activationDate = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCli() {
		return cli;
	}

	public void setCli(String cli) {
		this.cli = cli;
	}

	public Long getAccountType() {
		return accountType;
	}

	public void setAccountType(Long accountType) {
		this.accountType = accountType;
	}

	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	@Override
	public String toString() {
		return String.format(
			"%s[id=%s, cli=%s, accountType=%s, activationDate=%s]",
			getClass().getSimpleName(),
			id,
			cli,
			accountType,
			activationDate
		);
	}

}

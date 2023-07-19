package integrations.turnitin.com.membersearcher.model;

import java.util.List;

public class MembershipList {
	private List<Membership> memberships;

	public List<Membership> getMemberships() {
		return memberships;
	}

	public MembershipList setMemberships(List<Membership> memberships) {
		this.memberships = memberships;
		return this;
	}
}

package integrations.turnitin.com.membersearcher.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Membership {
	private String id;

	@JsonProperty("user_id")
	private String userId;

	private String role;

	private User user;

	public String getId() {
		return id;
	}

	public Membership setId(String id) {
		this.id = id;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public Membership setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public String getRole() {
		return role;
	}

	public Membership setRole(String role) {
		this.role = role;
		return this;
	}

	public User getUser() {
		return user;
	}

	public Membership setUser(User user) {
		this.user = user;
		return this;
	}
}

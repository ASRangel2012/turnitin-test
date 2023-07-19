package integrations.turnitin.com.membersearcher.model;

import java.util.List;

public class UserList {
	private List<User> users;

	public List<User> getUsers() {
		return users;
	}

	public UserList setUsers(List<User> users) {
		this.users = users;
		return this;
	}
}

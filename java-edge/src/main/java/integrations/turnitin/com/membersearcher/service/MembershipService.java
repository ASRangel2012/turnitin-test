package integrations.turnitin.com.membersearcher.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;
import integrations.turnitin.com.membersearcher.client.MembershipBackendClient;
import integrations.turnitin.com.membersearcher.model.MembershipList;
import integrations.turnitin.com.membersearcher.model.Membership;
import integrations.turnitin.com.membersearcher.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembershipService {
    @Autowired
    private MembershipBackendClient membershipBackendClient;

    /**
     * Fetch all memberships with associated user details 
     * @return A CompletableFuture containing a fully populated MembershipList object.
     */
	public CompletableFuture<MembershipList> fetchAllMembershipsWithUsers() {
    	return membershipBackendClient.fetchMemberships()
            .thenCompose(members -> {
                //Collect all the user IDs from memberships into a list
                List<String> listOfUserIds = members.getMemberships().stream()
                        .map(Membership::getUserId)
                        .collect(Collectors.toList());

                //Get all users by passing in listOfUserIds to backend client 
                return membershipBackendClient.fetchUsers(listOfUserIds)
                        .thenApply(users -> {
                            //Map each userID with its User 
                            Map<String, User> userMap = users.getUsers().stream()
                                    .collect(Collectors.toMap(
										User::getId,
									 	user -> {
											//just return the user object. note: user can be modified here, if needed. 
											return user;
										}
									)); 
                            // Associate users with their corresponding memberships 
                            members.getMemberships().forEach(member -> {
                                User user = userMap.get(member.getUserId());
                                member.setUser(user);
                            });

                            return members;
                        });
            });
	}
}

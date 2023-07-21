package integrations.turnitin.com.membersearcher.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import integrations.turnitin.com.membersearcher.client.MembershipBackendClient;
import integrations.turnitin.com.membersearcher.model.MembershipList;
import integrations.turnitin.com.membersearcher.model.Membership;
import integrations.turnitin.com.membersearcher.model.User;
import integrations.turnitin.com.membersearcher.model.UserList;

/**
 * Service class to fetch memberships with associated user details.
 */
@Service
public class MembershipService {

    @Autowired
    private MembershipBackendClient membershipBackendClient;

    /**
     * Fetch all memberships with associated user details.
     *
     * @return A CompletableFuture containing a fully populated MembershipList object.
     */
    public CompletableFuture<MembershipList> fetchAllMembershipsWithUsers() {
        return membershipBackendClient.fetchMemberships()
                .thenCompose(members -> {
                    if (members.getMemberships().isEmpty()) {
                        //Return an empty MembershipList if there are no memberships
                        return CompletableFuture.completedFuture(new MembershipList());
                    } else {
                        return fetchUsersForMemberships(members)
                                .thenApply(users -> associateUsersWithMemberships(members, users));
                    }
                });
    }
    
    /**
     * Fetch users for the given memberships in bulk using their user IDs.
     *
     * @param memberships The list of memberships for which to fetch users.
     * @return A CompletableFuture containing a UserList object containing the fetched users.
     */
    private CompletableFuture<UserList> fetchUsersForMemberships(MembershipList memberships) {
        List<String> listOfUserIds = memberships.getMemberships().stream()
                .map(Membership::getUserId)
                .collect(Collectors.toList());

        return membershipBackendClient.fetchUsers(listOfUserIds);
    }

     /**
     * Associate fetched users with their corresponding memberships.
     *
     * @param userList The UserList containing the fetched users.
     * @return The MembershipList with associated users.
     */
    private MembershipList associateUsersWithMemberships(MembershipList memberships, UserList userList) {
        Map<String, User> userMap = userList.getUsers().stream()
                .collect(Collectors.toMap(User::getId, user -> user));
    
        memberships.getMemberships().forEach(member -> {
            User user = userMap.get(member.getUserId());
            // Set the user only if it exists, otherwise set it to null 
            member.setUser(user != null ? user : null);
        });
        return memberships;
    }
}


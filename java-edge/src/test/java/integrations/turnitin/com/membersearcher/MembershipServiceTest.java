package integrations.turnitin.com.membersearcher;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import integrations.turnitin.com.membersearcher.client.MembershipBackendClient;
import integrations.turnitin.com.membersearcher.model.Membership;
import integrations.turnitin.com.membersearcher.model.MembershipList;
import integrations.turnitin.com.membersearcher.model.User;
import integrations.turnitin.com.membersearcher.model.UserList;
import integrations.turnitin.com.membersearcher.service.MembershipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {
    @InjectMocks
    private MembershipService membershipService;

    @Mock
    private MembershipBackendClient membershipBackendClient;

    @Mock
    private ObjectMapper objectMapper;

    private MembershipList members;
    private UserList users;

    @BeforeEach
    public void init() {
        //Create some test data for memberships and users
        members = new MembershipList()
                .setMemberships(List.of(
                        new Membership().setId("a").setRole("instructor").setUserId("1"),
                        new Membership().setId("b").setRole("student").setUserId("2")
                ));

        users = new UserList()
                .setUsers(List.of(
                        new User().setId("1").setName("test one").setEmail("test1@example.com"),
                        new User().setId("2").setName("test two").setEmail("test2@example.com")
                ));

        //Mock the behavior of the backend client
        when(membershipBackendClient.fetchMemberships()).thenReturn(CompletableFuture.completedFuture(members));
        when(membershipBackendClient.fetchUsers(List.of("1", "2"))).thenReturn(CompletableFuture.completedFuture(users));
    }

    @Test
    public void testFetchAllMemberships() throws Exception {
        //Call the method to fetch memberships with associated users
        MembershipList fetchedMemberships = membershipService.fetchAllMembershipsWithUsers().get();

        //Verify that the returned list of memberships is not null and has the expected size
        assertThat(fetchedMemberships).isNotNull();
        assertThat(fetchedMemberships.getMemberships()).hasSize(2);

        //Verify that the users are associated with their corresponding memberships
        assertThat(fetchedMemberships.getMemberships().get(0).getUser()).isEqualTo(users.getUsers().get(0));
        assertThat(fetchedMemberships.getMemberships().get(1).getUser()).isEqualTo(users.getUsers().get(1));
    }
}

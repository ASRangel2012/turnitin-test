package integrations.turnitin.com.membersearcher;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;
import integrations.turnitin.com.membersearcher.client.MembershipBackendClient;
import integrations.turnitin.com.membersearcher.model.Membership;
import integrations.turnitin.com.membersearcher.model.MembershipList;
import integrations.turnitin.com.membersearcher.model.User;
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

	private User userOne;

	private User userTwo;

	@BeforeEach
	public void init() {
		members = new MembershipList()
				.setMemberships(List.of(
						new Membership()
								.setId("a")
								.setRole("instructor")
								.setUserId("1"),
						new Membership()
								.setId("b")
								.setRole("student")
								.setUserId("2")
				));
		userOne = new User()
				.setId("1")
				.setName("test one")
				.setEmail("test1@example.com");
		userTwo = new User()
				.setId("2")
				.setName("test two")
				.setEmail("test2@example.com");
		when(membershipBackendClient.fetchMemberships()).thenReturn(CompletableFuture.completedFuture(members));
		when(membershipBackendClient.fetchUser("1")).thenReturn(CompletableFuture.completedFuture(userOne));
		when(membershipBackendClient.fetchUser("2")).thenReturn(CompletableFuture.completedFuture(userTwo));
	}

	@Test
	public void TestFetchAllMemberships() throws Exception {

		MembershipList members = membershipService.fetchAllMembershipsWithUsers().get();
		assertThat(members.getMemberships().get(0).getUser()).isEqualTo(userOne);
		assertThat(members.getMemberships().get(1).getUser()).isEqualTo(userTwo);
	}
}

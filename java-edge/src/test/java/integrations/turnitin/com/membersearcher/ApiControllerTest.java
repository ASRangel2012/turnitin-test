package integrations.turnitin.com.membersearcher;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import integrations.turnitin.com.membersearcher.controller.ApiController;
import integrations.turnitin.com.membersearcher.model.Membership;
import integrations.turnitin.com.membersearcher.model.MembershipList;
import integrations.turnitin.com.membersearcher.model.User;
import integrations.turnitin.com.membersearcher.service.MembershipService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ApiController.class)
public class ApiControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MembershipService membershipService;

	@Test
	public void TestMembershipsEndpointReturnsMemberships() throws Exception {
		MembershipList members = new MembershipList()
				.setMemberships(List.of(
						new Membership()
								.setId("a")
								.setRole("instructor")
								.setUserId("1")
								.setUser(new User()
												 .setId("1")
												 .setName("test one")
												 .setEmail("test1@example.com")),
						new Membership()
								.setId("b")
								.setRole("student")
								.setUserId("2")
								.setUser(new User()
												 .setId("2")
												 .setName("test two")
												 .setEmail("test2@example.com"))
				));
		when(membershipService.fetchAllMembershipsWithUsers()).thenReturn(CompletableFuture.completedFuture(members));

		final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/course/members");

		final MvcResult result = mvc.perform(request).andReturn();
		mvc.perform(asyncDispatch(result))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.memberships").isNotEmpty())
				.andExpect(jsonPath("$.memberships[0].user.name").value("test one"));
	}
}

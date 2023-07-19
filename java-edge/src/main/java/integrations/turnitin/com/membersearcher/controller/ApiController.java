package integrations.turnitin.com.membersearcher.controller;

import java.util.concurrent.CompletableFuture;

import integrations.turnitin.com.membersearcher.model.MembershipList;
import integrations.turnitin.com.membersearcher.service.MembershipService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ApiController {

	@Autowired
	private MembershipService membershipService;

	@GetMapping("/course/members")
	public CompletableFuture<MembershipList> fetchAllMemberships() {
		return membershipService.fetchAllMembershipsWithUsers();
	}
}

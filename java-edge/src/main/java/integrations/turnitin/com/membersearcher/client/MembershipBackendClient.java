package integrations.turnitin.com.membersearcher.client;

import java.util.List;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import com.fasterxml.jackson.databind.ObjectMapper;
import integrations.turnitin.com.membersearcher.exception.ClientRequestException;
import integrations.turnitin.com.membersearcher.model.MembershipList;
import integrations.turnitin.com.membersearcher.model.UserList;
import integrations.turnitin.com.membersearcher.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

/**
 * Backend client class to interact with Membership service 
 * Provides methods to fetch memberships/user details 
 */
@Service
public class MembershipBackendClient {
	@Value("${backend.host:http://localhost:8041}")
	private String backendHost;
	private final HttpClient httpClient;

	private final ObjectMapper objectMapper;

	@Autowired
	public MembershipBackendClient(ObjectMapper objectMapper) {
		this.httpClient = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(30))
				.followRedirects(HttpClient.Redirect.NORMAL)
				.build();
		this.objectMapper = objectMapper;
	}

	/**
	 * Fetch all memberships from backend API
	 * 
	 * @return A CompletableFuture containing the MembershipList object with fetched memberships.
	 */
	public CompletableFuture<MembershipList> fetchMemberships() {
		return makeRequest("GET", backendHost + "/api.php/members", null, MembershipList.class);
	}

	/**
     * Fetches a specific users details from the backend API.
     *
     * @param userId The ID of the user to fetch.
     * @return A CompletableFuture  containing the User object with the fetched user details.
     */
	public CompletableFuture<User> fetchUser(String userId) {
		return makeRequest("GET", backendHost + "/api.php/users/" + userId, null, User.class);
	}
	
	/**
     * Fetches multiple users details in bulk from the backend API.
     *
     * @param userIds A list of user IDs to fetch.
     * @return A CompletableFuture containing the UserList object with fetched user details.
     */
	public CompletableFuture<UserList> fetchUsers(List<String> userIds) {
		//List of userIDs joined into a comma-separated string 
    	String joinedUserIds = String.join(",", userIds);
		//Create API URL with userIDs as a query parameters
    	String url = backendHost + "/api.php/users?userIds=" + joinedUserIds;
		//Create request with new API endpoint to fetch all users in bulk 
    	return makeRequest("GET", url, null, UserList.class);
	}


	private <T> CompletableFuture<T> makeRequest(String method, String url, Object body, Class<T> responseType) {
		URI uri = URI.create(url);

		HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(uri);
		if (responseType != Void.class) {
			requestBuilder.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		}
		String bodyString = "";
		if (body != null) {
			requestBuilder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			try {
				bodyString = objectMapper.writeValueAsString(body);
			} catch (IOException exception) {}
		}
		requestBuilder.method(method, HttpRequest.BodyPublishers.ofString(bodyString));

		return makeAsyncHttpRequest(requestBuilder.build(), responseType);
	}

	protected <T> CompletableFuture<T> makeAsyncHttpRequest(HttpRequest request, Class<T> responseType) {
		return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.exceptionally(ex -> {
					throw new ClientRequestException("Failed to call URL: " + request.uri().toString(), ex);
				})
				.thenApply(response -> {
					final HttpStatus status = HttpStatus.valueOf(response.statusCode());

					if (status.is2xxSuccessful()) {
						try {
							return responseType == Void.class ? null : objectMapper.readValue(response.body(), responseType);
						} catch (IOException ex) {
							throw new ClientRequestException("Could not deserialize the response", ex);
						}
					}
					throw new ClientRequestException("Bad Request");
				});
	}
}

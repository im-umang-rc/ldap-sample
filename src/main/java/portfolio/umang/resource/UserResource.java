package portfolio.umang.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import portfolio.umang.service.UserService;

import javax.naming.NamingException;
import java.util.Arrays;

@Path("/users")
public class UserResource {

    private final UserService userService;

    public UserResource() {
        this.userService = new UserService();
    }

    @GET
    public Response getUser(@QueryParam("users") String users) throws NamingException {
        return Response.ok(this.userService.getUsers(Arrays.asList(users.split(","))))
                .build();
    }
}

package portfolio.umang.service;

import portfolio.umang.dto.UserDTO;

import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    public List<UserDTO> getUsers(List<String> users) throws NamingException {
        List<UserDTO> usersAvailable = new ArrayList<>();
        List<String> servers = LdapService.getTargets();
        for (String user : users) {
            boolean status = false;
            for (String server: servers) {
                status = status || LdapService.isUserLocked(server, user);
            }
            usersAvailable.add(new UserDTO(user, status));
        }
        return usersAvailable;
    }
}

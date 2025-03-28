package portfolio.umang.service;

import portfolio.umang.dto.UserDTO;

import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static portfolio.umang.service.LdapService.getProperty;
import static portfolio.umang.service.LdapService.getUserStatus;

public class UserService {

    private static final String parentTemplate = "(&(objectClass=%s)(|%s))";
    private static final String childTemplate = "(%s=%s)";

    public List<UserDTO> getUsers(List<String> users) throws NamingException {
        String[] servers = getProperty("servers").split(",");
        Map<String, Boolean> overAllStatus = new HashMap<>();

        for (String server : servers) {
            StringBuilder childTemplateCollector = new StringBuilder();
            String uidAttr = getProperty(server + ".id");

            for (String user : users) {
                childTemplateCollector.append(
                        childTemplate.formatted(uidAttr, user)
                );
            }

            String searchString = parentTemplate.formatted(
                    getProperty(server + ".obj"),
                    childTemplateCollector.toString()
            );

            Map<String, Boolean> userStatus = getUserStatus(server, searchString);

            for (String user : userStatus.keySet()) {
                overAllStatus.compute(user, (k, v) -> v == null ? userStatus.get(user) : userStatus.get(user) || v);
            }
        }

        List<UserDTO> usersAvailable = new ArrayList<>();
        for (String user : overAllStatus.keySet()) {
            usersAvailable.add(new UserDTO(user, overAllStatus.get(user)));
        }
        return usersAvailable;
    }
}

package com.example.timesheets2.service;

import com.example.timesheets2.domain.User;
import com.example.timesheets2.exception.UnauthorizedException;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final static Attributes PERSON_ATTRIBUTE = new BasicAttributes("objectclass", "person");

    private final LdapContextSource ldapContextSource;

    UserService(LdapContextSource contextSource) {
        ldapContextSource = contextSource;
    }

    public String getUserDn() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            var ldapDetails = (LdapUserDetailsImpl) authentication.getPrincipal();
            if (ldapDetails != null) {
                return ldapDetails.getUsername();
            }
        }
        throw new UnauthorizedException();
    }

    public List<User> getUsers() throws NamingException {
        ArrayList<User> users = new ArrayList<>();
        var iterator = ldapContextSource.getReadOnlyContext().search("ou=people", PERSON_ATTRIBUTE).asIterator();
        while (iterator.hasNext()) {
            var attributes = iterator.next().getAttributes();
            users.add(new User((String) attributes.get("uid").get(), (String) attributes.get("cn").get()));
        }
        return users;
    }

}

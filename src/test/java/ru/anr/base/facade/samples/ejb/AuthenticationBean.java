package ru.anr.base.facade.samples.ejb;

import org.springframework.security.authentication.AuthenticationManager;
import ru.anr.base.facade.ejb.AbstractAuthenticationBean;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnits;

/**
 * Authentication Entry point.
 *
 * @author Alexey Romanchuk
 * @created Nov 28, 2014
 */
@Stateless(name = "AuthenticationBean", mappedName = "ejb/authenticationBean")
@Local(AuthenticationManager.class)
@PersistenceUnits({
        @PersistenceUnit(name = "TestUnit/EntityManagerFactory", unitName = "TestUnit")
})
public class AuthenticationBean extends AbstractAuthenticationBean {

}

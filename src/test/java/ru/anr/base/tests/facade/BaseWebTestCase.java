/**
 * 
 */
package ru.anr.base.tests.facade;

import org.junit.Ignore;
import org.springframework.test.context.ContextConfiguration;

import ru.anr.base.tests.AbstractGlassfishWebTestCase;

/**
 * Base test config
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 11, 2014
 *
 */
@ContextConfiguration(locations = "classpath:tests-context.xml")
@Ignore
public class BaseWebTestCase extends AbstractGlassfishWebTestCase {

}

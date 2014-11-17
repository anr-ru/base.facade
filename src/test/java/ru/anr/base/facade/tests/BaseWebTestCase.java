/**
 * 
 */
package ru.anr.base.facade.tests;

import org.junit.Ignore;
import org.springframework.test.context.ContextConfiguration;

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

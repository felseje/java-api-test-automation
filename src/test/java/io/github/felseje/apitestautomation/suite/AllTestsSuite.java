package io.github.felseje.apitestautomation.suite;

import io.github.felseje.apitestautomation.test.LoginIT;
import io.github.felseje.apitestautomation.test.UserIT;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Full Test Suite")
@SelectClasses({
    LoginIT.class,
    UserIT.class
})
class AllTestsSuite {

}

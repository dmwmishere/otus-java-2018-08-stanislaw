package ru.otus.shw10;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({JDBCTest.class, DBSrvTest.class})
public class TestRunner {
}

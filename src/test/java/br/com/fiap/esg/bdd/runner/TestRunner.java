package br.com.fiap.esg.bdd.runner;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(
        key = "cucumber.glue",
        value = "br.com.fiap.esg.bdd.steps,br.com.fiap.esg.bdd.infra,br.com.fiap.esg.bdd.hook"
)
@ConfigurationParameter(
        key = "cucumber.plugin",
        value = "pretty,html:target/cucumber-reports.html"
)
public class TestRunner {
}

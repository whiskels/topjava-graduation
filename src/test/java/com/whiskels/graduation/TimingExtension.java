package com.whiskels.graduation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.junit.jupiter.api.extension.*;

@Slf4j
public class TimingExtension implements
        BeforeTestExecutionCallback, AfterTestExecutionCallback, BeforeAllCallback, AfterAllCallback {

    private StopWatch stopWatch;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        stopWatch = new StopWatch("Execution time of " + extensionContext.getRequiredTestClass().getSimpleName());
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        String testName = extensionContext.getDisplayName();
        log.info("\nStart " + testName);
        stopWatch.start(testName);
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        stopWatch.stop();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        log.info('\n' + stopWatch.prettyPrint() + '\n');
    }
}
package org.wf.dp.dniprorada.engine.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.regex.Matcher;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

public class Abstract_MailTaskCustomTest {

    private static final String TEST_REPLACEMENT = "_REPLACED_";

    private TestableAbstract_MailTaskCustom tester;

    @Before
    public void beforeTest() throws IOException {
        TestableAbstract_MailTaskCustom testableInstance = new TestableAbstract_MailTaskCustom();
        tester = Mockito.spy(testableInstance);

        Mockito.doReturn(TEST_REPLACEMENT).when(tester).getPatternContentReplacement(any(Matcher.class));
    }

    @Test
    public void testStingNotContainsCorrectPatterns() throws IOException {
        String input = "Best regards! [pattern]";
        String result = tester.getPopulatedPatternContent(input);
        assertEquals("Best regards! [pattern]", result);
    }

    @Test
    public void testStingContainsSinglePattern() throws IOException {
        String input = "Best regards! [pattern/mail/feedback.html]";
        String result = tester.getPopulatedPatternContent(input);
        assertEquals("Best regards! _REPLACED_", result);
    }

    @Test
    public void testStingContainsCouplePatterns() throws IOException {
        String input = "Good morning! You wrote: [pattern/mail/quote.html] Best regards! [pattern/mail/feedback.html]";
        String result = tester.getPopulatedPatternContent(input);
        assertEquals("Good morning! You wrote: _REPLACED_ Best regards! _REPLACED_", result);
    }

    @Test
    public void testCustomGetPatternContentReplacement() throws IOException {
        TestableAbstract_MailTaskCustom instance = new TestableAbstract_MailTaskCustom();
        String input = "Best regards! [pattern/something]";
        String result = instance.getPopulatedPatternContent(input);
        assertEquals("Best regards! SOMETHING", result);
    }

    private static class TestableAbstract_MailTaskCustom extends Abstract_MailTaskCustom {

        public String getPopulatedPatternContent(String input) throws IOException {
            return populatePatternWithContent(input);
        }

        @Override
        String getPatternContentReplacement(Matcher matcher) throws IOException {
            String path = matcher.group(1);
            return path.toUpperCase();
        }

        @Override
        public void execute(DelegateExecution de) throws Exception {
            // Do nothing
        }

    }

}
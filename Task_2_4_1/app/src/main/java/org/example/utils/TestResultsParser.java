package org.example.utils;

import org.example.models.TestResults;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class TestResultsParser {
    public static TestResults parseTestResults(String fileToTest) {
        int passed = 0;
        int failed = 0;
        int skipped = 0;

        try {
            File reportFile = new File(fileToTest);
            if (!reportFile.exists()) {
                System.err.println("Test report file not found: " + reportFile.getAbsolutePath());
            }
            TestResults testResultsFromFile;
            if (reportFile.getName().endsWith(".html")) {
                testResultsFromFile = parseTestResultsFromHtml(reportFile.getAbsolutePath());
            } else if (reportFile.getName().endsWith(".xml")) {
                testResultsFromFile = parseTestResultsFromXml(reportFile.getAbsolutePath());
            } else {
                System.err.println("Unsupported test report format: " + reportFile.getName());
                testResultsFromFile = new TestResults(0, 0, 0);
            }
            passed += testResultsFromFile.getPassed();
            failed += testResultsFromFile.getFailed();
            skipped += testResultsFromFile.getSkipped();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (passed == 0 && failed == 0 && skipped == 0) {
            System.err.println("No test results found for project: " + fileToTest);
            return new TestResults(0, 0, 0);
        }

        return new TestResults(passed, failed, skipped);
    }

    public static TestResults parseTestResultsFromHtml(String htmlFilePath) {
        int passed = 0;
        int failed = 0;
        int skipped = 0;

        try {
            File htmlFile = new File(htmlFilePath);
            var factory = DocumentBuilderFactory.newInstance();
            var builder = factory.newDocumentBuilder();
            var document = builder.parse(htmlFile);
            document.getDocumentElement().normalize();

            var xPath = javax.xml.xpath.XPathFactory.newInstance().newXPath();

            String totalTestsStr = xPath.evaluate("//*[@id='tests']//*[@class='counter']", document);
            int totalTests = Integer.parseInt(totalTestsStr.trim());

            String failedStr = xPath.evaluate("//*[@id='failures']//*[@class='counter']", document);
            failed = Integer.parseInt(failedStr.trim());

            String skippedStr = xPath.evaluate("//*[@id='ignored']//*[@class='counter']", document);
            skipped = Integer.parseInt(skippedStr.trim());

            passed = totalTests - failed - skipped;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new TestResults(passed, failed, skipped);
    }

    public static TestResults parseTestResultsFromXml(String xmlFilePath) {
        int passed = 0;
        int failed = 0;
        int skipped = 0;
        int errors = 0;

        try {
            File xmlFile = new File(xmlFilePath);
            var factory = DocumentBuilderFactory.newInstance();
            var builder = factory.newDocumentBuilder();
            var document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            var testSuite = (Element) document.getElementsByTagName("testsuite").item(0);
            if (testSuite != null) {
                int totalTests = Integer.parseInt(testSuite.getAttribute("tests"));
                failed = Integer.parseInt(testSuite.getAttribute("failures"));
                skipped = Integer.parseInt(testSuite.getAttribute("skipped"));
                errors = Integer.parseInt(testSuite.getAttribute("errors"));
                passed = totalTests - failed - skipped - errors;
                errors += failed;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new TestResults(passed, failed, skipped);
    }
}
package org.example;

import java.util.List;
import java.util.logging.Logger;

import org.example.checkers.ReportGenerator;
import org.example.models.Student;
import org.example.utils.Configuration;
import org.example.utils.RepositoryProcessor;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        if (args.length < 3) {
            logger.info("Usage: java -jar myApp.jar <configFilePath> <outputReportPath> <mode>");
            logger.info("Modes: pull (pull repositories), recheck (recheck without pulling or cloning)");
            return;
        }

        String configFilePath = args[0];
        String outputReportPath = args[1];
        String mode = args[2];

        boolean pullRepositories = false;
        boolean recheckOnly = false;

        if (mode.equalsIgnoreCase("pull")) {
            pullRepositories = true;
        } else if (mode.equalsIgnoreCase("recheck")) {
            recheckOnly = true;
        } else {
            logger.severe("Invalid mode specified. Use 'pull' or 'recheck'.");
            return;
        }

        try {
            var config = new Configuration();
            config.loadFromGroovyFile(configFilePath);

            List<Student> studentsToCheck = RepositoryProcessor.filterStudentsToCheck(config);

            var studentResults = RepositoryProcessor.processRepositories(config, pullRepositories, recheckOnly, studentsToCheck);

            ReportGenerator.generateHtmlReport(config, studentResults, outputReportPath);
            ReportGenerator.generateGroupSummary(config, studentResults,
                    outputReportPath.replace(".html", "_summary.html"));
            ReportGenerator.generateCheckpointReports(config, studentResults,
                    outputReportPath.replace(".html", "_checkpoints.html"));

            logger.info("Report generated at: " + outputReportPath);
        } catch (Exception e) {
            logger.severe("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
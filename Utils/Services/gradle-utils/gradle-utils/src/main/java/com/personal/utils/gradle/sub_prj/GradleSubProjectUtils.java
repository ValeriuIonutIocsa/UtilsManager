package com.personal.utils.gradle.sub_prj;

import com.utils.io.processes.InputStreamReaderThread;
import com.utils.io.processes.ReadBytesHandlerLinesCollect;
import com.utils.log.Logger;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class GradleSubProjectUtils {

    private GradleSubProjectUtils() {
    }

    public static List<String> executeSubProjectDependencyTreeCommand(
            final String projectPathString) {

        List<String> subProjectDependencyTreeOutputLineList = null;
        try {
            final List<String> commandList = new ArrayList<>();
            final Path gradleWrapperPath = Paths.get(projectPathString, "gradlew.bat");
            commandList.add(gradleWrapperPath.toString());
            commandList.add("subProjectDependencyTree");

            final ProcessBuilder processBuilder = new ProcessBuilder()
                    .command(commandList)
                    .directory(new File(projectPathString))
                    .redirectErrorStream(true);
            final Process process = processBuilder.start();

            final ReadBytesHandlerLinesCollect readBytesHandlerLinesCollect = new ReadBytesHandlerLinesCollect();
            new InputStreamReaderThread("subProjectDependencyTree",
                    process.getInputStream(), StandardCharsets.UTF_8, readBytesHandlerLinesCollect).start();

            process.waitFor();

            subProjectDependencyTreeOutputLineList = readBytesHandlerLinesCollect.getLineList();

        } catch (final Exception exc) {
            Logger.printException(exc);
        }
        return subProjectDependencyTreeOutputLineList;
    }
}

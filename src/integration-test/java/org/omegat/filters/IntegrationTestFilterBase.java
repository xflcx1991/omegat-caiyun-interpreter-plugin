package org.omegat.filters;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.omegat.filters2.IFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import static org.testng.Assert.*;

import com.alibaba.fastjson.JSON;

/**
 * Created by miurahr on 16/09/18.
 */
public class IntegrationTestFilterBase extends TestFilterBase {

    protected void test(final IFilter filter, final String testcase) throws Exception {
        List<String> entries = parse(filter, "/" + testcase + ".txt");
        URL url = this.getClass().getResource("/" + testcase + ".json");
        if (url == null) {
            throw new IOException("Cannot find test expectation.");
        }
        try (BufferedReader reader = getBufferedReader(new File(url.getFile()), "UTF-8")) {
            String jsonString = IOUtils.toString(reader);
            ArrayList expected = JSON.parseObject(jsonString, ArrayList.class);
            assertEquals(entries, expected, testcase);
        }
    }

}

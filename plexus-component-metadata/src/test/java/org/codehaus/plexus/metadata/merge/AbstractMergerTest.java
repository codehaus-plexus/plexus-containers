package org.codehaus.plexus.metadata.merge;

import org.jdom2.Document;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.*;

public class AbstractMergerTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();


    @Test
    public void testMergeDescriptors() throws Exception {
       AbstractMerger am = new AbstractMerger() {
           public Document merge(Document dDocument, Document rDocument) throws MergeException {
               return null;
           }
       };
        File outputFile = testFolder.newFile("output");
        File desc1  = new File("org.codehaus.plexus.metadata.merge/dominant.xml");
        File desc2  = new File("org.codehaus.plexus.metadata.merge/recessive.xml");
        am.mergeDescriptors(outputFile, Arrays.asList( desc1, desc2));
    }
}
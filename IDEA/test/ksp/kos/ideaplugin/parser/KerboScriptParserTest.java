package ksp.kos.ideaplugin.parser;

import com.intellij.testFramework.ParsingTestCase;
import com.intellij.testFramework.TestDataPath;

@TestDataPath("$CONTENT_ROOT/test-data/kos/psi/")
public class KerboScriptParserTest extends ParsingTestCase {
    public KerboScriptParserTest() {
        super("kos/psi", "ks", true, new KerboScriptParserDefinition());
    }

    @Override
    protected String getTestDataPath() {
        return "test-data";
    }

    @Override
    protected boolean skipSpaces() {
        return false;
    }

    @Override
    protected boolean includeRanges() {
        return false;
    }

    protected void doTest() {
        doTest(true);
    }

    public void testEmpty() throws Exception {
        doTest();
    }
    public void testSimple() throws Exception {
        doTest();
    }
    public void testEmpty_function() throws Exception {
        doTest();
    }

    public void testSimple_function_call() throws Exception {
        doTest();
    }
}

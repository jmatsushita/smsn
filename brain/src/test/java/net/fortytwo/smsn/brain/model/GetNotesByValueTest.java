package net.fortytwo.smsn.brain.model;

import net.fortytwo.smsn.brain.BrainTestBase;
import net.fortytwo.smsn.brain.model.entities.Note;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GetNotesByValueTest extends BrainTestBase {
    private Note arthur;

    @Override
    protected TopicGraph createTopicGraph() throws IOException {
        return createNeo4jTopicGraph();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        arthur = createNote();
        arthur.setLabel("Arthur Dent");
    }

    @Test
    public void nonMatchingValueFails() throws Exception {
        queryResult = topicGraph.getNotesByTitleQuery("Zaphod", filter);
        assertEquals(0, sizeOf(queryResult));
    }

    @Test
    public void completelyMatchingValueSucceeds() throws Exception {
        queryResult = topicGraph.getNotesByTitleQuery("Arthur Dent", filter);
        assertEquals(1, sizeOf(queryResult));
    }

    @Test
    public void subsetOfWordsMatchingSucceeds() throws Exception {
        queryResult = topicGraph.getNotesByTitleQuery("Arthur Beeblebrox", filter);
        assertEquals(1, sizeOf(queryResult));
    }

    @Test
    public void outOfOrderWordsSucceeds() throws Exception {
        queryResult = topicGraph.getNotesByTitleQuery("Dent Arthur", filter);
        assertEquals(1, sizeOf(queryResult));
    }

    @Test
    public void partialValueSucceeds() throws Exception {
        queryResult = topicGraph.getNotesByTitleQuery("Arthur", filter);
        assertEquals(1, sizeOf(queryResult));
    }

    @Test
    public void partialWordFails() throws Exception {
        queryResult = topicGraph.getNotesByTitleQuery("Arth", filter);
        assertEquals(0, sizeOf(queryResult));
    }

    @Test
    public void matchingWildcardSucceeds() throws Exception {
        queryResult = topicGraph.getNotesByTitleQuery("Arth*", filter);
        assertEquals(1, sizeOf(queryResult));
    }

    @Test
    public void nonMatchingWildcardFails() throws Exception {
        queryResult = topicGraph.getNotesByTitleQuery("Zaph*", filter);
        assertEquals(0, sizeOf(queryResult));
    }

    @Test
    public void caseInsensitiveMatchSucceeds() throws Exception {
        queryResult = topicGraph.getNotesByTitleQuery("ARTHUR Dent", filter);
        assertEquals(1, sizeOf(queryResult));
        queryResult = topicGraph.getNotesByTitleQuery("aRTHur", filter);
        assertEquals(1, sizeOf(queryResult));
        queryResult = topicGraph.getNotesByTitleQuery("*dENT", filter);
        assertEquals(1, sizeOf(queryResult));
    }

    @Test
    public void quotedExactMatchSucceeds() throws Exception {
        queryResult = topicGraph.getNotesByTitleQuery("\"Arthur Dent\"", filter);
        assertEquals(1, sizeOf(queryResult));
        assertEquals(arthur.getTopic().getId(), queryResult.iterator().next().getTopic().getId());
    }

    private int sizeOf(final Iterable iter) {
        int count = 0;
        for (Object ignored : iter) {
            count++;
        }
        return count;
    }
}
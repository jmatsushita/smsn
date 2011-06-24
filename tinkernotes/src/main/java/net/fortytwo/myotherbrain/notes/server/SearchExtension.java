package net.fortytwo.myotherbrain.notes.server;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.rexster.RexsterResourceContext;
import com.tinkerpop.rexster.extension.ExtensionDefinition;
import com.tinkerpop.rexster.extension.ExtensionDescriptor;
import com.tinkerpop.rexster.extension.ExtensionNaming;
import com.tinkerpop.rexster.extension.ExtensionPoint;
import com.tinkerpop.rexster.extension.ExtensionRequestParameter;
import com.tinkerpop.rexster.extension.ExtensionResponse;
import com.tinkerpop.rexster.extension.RexsterContext;
import net.fortytwo.myotherbrain.notes.Filter;
import net.fortytwo.myotherbrain.notes.Note;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * User: josh
 * Date: 6/19/11
 * Time: 1:40 PM
 */
@ExtensionNaming(namespace = "tinkernotes", name = "search")
public class SearchExtension extends TinkerNotesExtension {

    @ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH)
    @ExtensionDescriptor(description = "an extension for performing full text search over MyOtherBrain using TinkerNotes")
    public ExtensionResponse handleRequest(@RexsterContext RexsterResourceContext context,
                                           @RexsterContext Graph graph,
                                           @ExtensionRequestParameter(name = "query", description = "full-text query") String query,
                                           @ExtensionRequestParameter(name = "minSharability", description = "minimum-sharability criterion for atoms in the view") Float minSharability,
                                           @ExtensionRequestParameter(name = "maxSharability", description = "maximum-sharability criterion for atoms in the view") Float maxSharability,
                                           @ExtensionRequestParameter(name = "minWeight", description = "minimum-weight criterion for atoms in the view") Float minWeight,
                                           @ExtensionRequestParameter(name = "maxWeight", description = "maximum-weight criterion for atoms in the view") Float maxWeight) {
        LOGGER.fine("search request for \"" + query + "\"");

        Filter filter = new Filter(minSharability, maxSharability, minWeight, maxWeight);

        Params p = new Params();
        p.graph = graph;
        p.filter = filter;
        p.query = query;
        return this.handleRequestInternal(p, null);
    }

    @Override
    protected ExtensionResponse handleRequestProtected(final Params p) throws Exception {
        addSearchResults(p);

        return ExtensionResponse.ok(p.map);
    }

    protected void addSearchResults(final Params p) throws IOException {
        Note n = p.m.search(p.query, p.filter);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            p.p.writeChildren(n, bos);
            p.map.put("view", bos.toString());
        } finally {
            bos.close();
        }
    }
}

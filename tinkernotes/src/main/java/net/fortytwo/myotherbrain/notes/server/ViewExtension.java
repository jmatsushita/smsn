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

/**
 * User: josh
 * Date: 6/19/11
 * Time: 1:40 PM
 */
@ExtensionNaming(namespace = "tinkernotes", name = "view")
public class ViewExtension extends TinkerNotesExtension {

    @ExtensionDefinition(extensionPoint = ExtensionPoint.GRAPH)
    @ExtensionDescriptor(description = "an extension for viewing a portion of a MyOtherBrain graph in the TinkerNotes format")
    public ExtensionResponse handleRequest(@RexsterContext RexsterResourceContext context,
                                           @RexsterContext Graph graph,
                                           @ExtensionRequestParameter(name = "root", description = "root atom (vertex) of the view") String rootKey,
                                           @ExtensionRequestParameter(name = "depth", description = "depth of the view") Integer depth,
                                           @ExtensionRequestParameter(name = "minVisibility", description = "minimum-visibility criterion for atoms in the view") Float minVisibility,
                                           @ExtensionRequestParameter(name = "maxVisibility", description = "maximum-visibility criterion for atoms in the view") Float maxVisibility,
                                           @ExtensionRequestParameter(name = "minWeight", description = "minimum-weight criterion for atoms in the view") Float minWeight,
                                           @ExtensionRequestParameter(name = "maxWeight", description = "maximum-weight criterion for atoms in the view") Float maxWeight,
                                           @ExtensionRequestParameter(name = "inverse", description = "whether to create an inverted view") Boolean inverse) {
        LOGGER.fine("view-notes request for: " + rootKey);

        Filter filter = new Filter(minVisibility, maxVisibility, minWeight, maxWeight);

        return this.handleRequestInternal(graph, rootKey, depth, filter, null, inverse);
    }

    @Override
    protected ExtensionResponse handleRequestProtected(final Params p) throws Exception {
        addView(p);

        return ExtensionResponse.ok(p.map);
    }
}

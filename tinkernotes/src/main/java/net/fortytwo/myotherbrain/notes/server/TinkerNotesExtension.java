package net.fortytwo.myotherbrain.notes.server;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.IndexableGraph;
import com.tinkerpop.frames.FramesManager;
import com.tinkerpop.rexster.extension.AbstractRexsterExtension;
import com.tinkerpop.rexster.extension.ExtensionResponse;
import net.fortytwo.myotherbrain.Atom;
import net.fortytwo.myotherbrain.notes.Filter;
import net.fortytwo.myotherbrain.notes.Note;
import net.fortytwo.myotherbrain.notes.NotesSemantics;
import net.fortytwo.myotherbrain.notes.NotesSyntax;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * User: josh
 * Date: 6/19/11
 * Time: 1:40 PM
 */
public abstract class TinkerNotesExtension extends AbstractRexsterExtension {
    protected static final Logger LOGGER = Logger.getLogger(TinkerNotesExtension.class.getName());

    protected ExtensionResponse handleRequestInternal(final Params p,
                                                      final String rootKey) {
        try {
            p.map = new HashMap<String, String>();

            if (!(p.graph instanceof IndexableGraph)) {
                return ExtensionResponse.error("graph must be an instance of IndexableGraph");
            }

            p.manager = new FramesManager(p.graph);
            p.m = new NotesSemantics((IndexableGraph) p.graph, p.manager);
            p.p = new NotesSyntax();

            if (null != p.depth) {
                if (p.depth < 1) {
                    return ExtensionResponse.error("depth must be at least 1");
                }

                if (p.depth > 5) {
                    return ExtensionResponse.error("depth may not be more than 5");
                }

                p.map.put("depth", "" + p.depth);
            }

            if (p.filter.minSharability < 0 || p.filter.maxSharability > 1) {
                return ExtensionResponse.error("minimum and maximum sharability must lie between 0 and 1 (inclusive)");
            }

            if (p.filter.maxSharability < p.filter.minSharability) {
                return ExtensionResponse.error("maximum sharability must be greater than or equal to minimum sharability");
            }

            if (p.filter.minWeight < 0 || p.filter.maxWeight > 1) {
                return ExtensionResponse.error("minimum and maximum weight must lie between 0 and 1 (inclusive)");
            }

            if (p.filter.maxWeight < p.filter.minWeight) {
                return ExtensionResponse.error("maximum weight must be greater than or equal to minimum weight");
            }

            p.map.put("minWeight", "" + p.filter.minWeight);
            p.map.put("maxWeight", "" + p.filter.maxWeight);
            p.map.put("minSharability", "" + p.filter.minSharability);
            p.map.put("maxSharability", "" + p.filter.maxSharability);

            if (null != rootKey) {
                p.root = p.m.getAtom(rootKey);
                if (null == p.root || !p.filter.isVisible(p.root)) {
                    return ExtensionResponse.error("root of view does not exist or is not visible: " + rootKey);
                }

                p.map.put("root", rootKey);
                p.map.put("title", null == p.root.getValue() || 0 == p.root.getValue().length() ? "[no title]" : p.root.getValue());
            }

            if (null != p.inverse) {
                p.map.put("inverse", "" + p.inverse);
            }

            return handleRequestProtected(p);
        } catch (Exception e) {
            // TODO
            e.printStackTrace(System.out);
            return ExtensionResponse.error(e);
        }
    }

    protected void addView(final Params p) throws IOException {
        Note n = p.m.view(p.root, p.depth, p.filter, p.inverse);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            p.p.writeChildren(n, bos);
            p.map.put("view", bos.toString());
        } finally {
            bos.close();
        }
    }

    protected abstract ExtensionResponse handleRequestProtected(Params p) throws Exception;

    protected class Params {
        public Map<String, String> map;
        public Graph graph;
        public FramesManager manager;
        public NotesSemantics m;
        public NotesSyntax p;
        public Atom root;
        public Integer depth;
        public String view;
        public Boolean inverse;
        public Filter filter;
        public String query;
    }
}

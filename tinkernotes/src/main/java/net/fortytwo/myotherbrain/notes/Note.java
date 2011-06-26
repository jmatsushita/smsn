package net.fortytwo.myotherbrain.notes;

import java.util.LinkedList;
import java.util.List;

/**
 * User: josh
 * Date: 5/18/11
 * Time: 5:51 PM
 */
public class Note extends NoteNode {
    private final String type;
    private final List<Note> children;
    private String qualifier;

    private float linkWeight;
    private float linkSharability;
    private float targetWeight;
    private float targetSharability;

    public Note(final String type,
                final String text) {
        super(text);

        this.type = type;
        children = new LinkedList<Note>();
    }

    public String getType() {
        return type;
    }

    public List<Note> getChildren() {
        return children;
    }

    public void addChild(Note child) {
        children.add(child);
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    @Override
    public String toString() {
        String d = getValue();
        if (null != d && d.length() > 20) {
            d = d.substring(0, 17) + "...";
        }
        String t = getType();
        if (null != t && t.length() > 20) {
            t = t.substring(0, 17) + "...";
        }
        return "note(" + t + ", " + d + ")";
    }

    public float getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(float targetWeight) {
        this.targetWeight = targetWeight;
    }

    public float getTargetSharability() {
        return targetSharability;
    }

    public void setTargetSharability(float targetSharability) {
        this.targetSharability = targetSharability;
    }

    public float getLinkWeight() {
        return linkWeight;
    }

    public void setLinkWeight(float linkWeight) {
        this.linkWeight = linkWeight;
    }

    public float getLinkSharability() {
        return linkSharability;
    }

    public void setLinkSharability(float linkSharability) {
        this.linkSharability = linkSharability;
    }
}
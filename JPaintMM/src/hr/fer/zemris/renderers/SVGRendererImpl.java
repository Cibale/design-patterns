package hr.fer.zemris.renderers;

import hr.fer.zemris.util.FileUtil;
import hr.fer.zemris.util.Point;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Renderer which "renders" as svg file.
 * Created by mmatak on 6/13/16.
 */
public class SVGRendererImpl implements Renderer {
    private List<String> lines = new ArrayList<>();
    private String fileName;

    public SVGRendererImpl(String fileName) {
        this.fileName = fileName;
        lines.add("<svg version=\"1.1\"\n" +
                "     baseProfile=\"full\"\n" +
                "     xmlns=\"http://www.w3.org/2000/svg\">");
    }

    @Override
    public void drawLine(Point s, Point e) {
        lines.add("<line x1=\"" + s.getX() + "\" y1=\"" + s.getY() +
                "\" x2=\"" + e.getX() + "\" y2=\"" + e.getY() +
                "\" style=\"stroke:rgb(0,0,255);stroke-width:1\" />");
    }

    @Override
    public void fillPolygon(Point[] points) {
        StringBuilder buff = new StringBuilder();
        buff.append("<polygon points=\"");
        for (Point point : points) {
            buff.append(point.getX()).append(",").append(point.getY()).append(" ");
        }
        buff.append("\"\nstyle=\"stroke:red;fill:blue;\" />");
        lines.add(buff.toString());

    }

    public void close() {
        lines.add("</svg>");
        FileUtil.saveFile(Paths.get(fileName), lines);
    }
}

package ru.vsu.cs.course1.graph.demo;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.*;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;
import ru.vsu.cs.course1.graph.Graph;
import ru.vsu.cs.course1.graph.GraphUtils;
import ru.vsu.cs.ereshkin_a_v.task07.Task;
import ru.vsu.cs.util.SwingUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GraphDemoFrame extends JFrame {
    private JTabbedPane tabbedPaneMain;
    private JPanel panelMain;
    private JPanel panelGraphTab;
    private JPanel panelGraphvizTab;
    private JPanel panelDotPainterContainer;
    private JButton buttonLoadDotFile;
    private JButton buttonDotPaint;
    private JTextArea textAreaDotFile;
    private JTextArea textAreaSystemOut;
    private JPanel panelGraphPainterContainer;
    private JButton buttonLoadGraphFromFile;
    private JTextArea textAreaGraphFile;
    private JComboBox comboBoxGraphType;
    private JButton buttonCreateGraph;
    private JSplitPane splitPaneGraphTab1;
    private JSplitPane splitPaneGraphTab2;
    private JSplitPane splitPaneGraphvizTab1;
    private JButton buttonSaveGraphToFile;
    private JButton buttonSaveDotFile;
    private JButton buttonSaveGraphSvgToFile;
    private JButton buttonSaveDotSvgToFile;
    private JComboBox comboBoxExample;
    private JButton buttonExampleExec;
    private JButton solveButton;
    private JButton checkForTreeButton;

    private JFileChooser fileChooserTxtOpen;
    private JFileChooser fileChooserDotOpen;
    private JFileChooser fileChooserTxtSave;
    private JFileChooser fileChooserDotSave;
    private JFileChooser fileChooserImgSave;

    private Graph graph = null;

    private SvgPanel panelGraphPainter;
    private SvgPanel panelGraphvizPainter;


    private static class SvgPanel extends JPanel {
        private String svg = null;
        private GraphicsNode svgGraphicsNode = null;

        public void paint(String svg) throws IOException {
            String xmlParser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory df = new SAXSVGDocumentFactory(xmlParser);
            SVGDocument doc = df.createSVGDocument(null, new StringReader(svg));
            UserAgent userAgent = new UserAgentAdapter();
            DocumentLoader loader = new DocumentLoader(userAgent);
            BridgeContext ctx = new BridgeContext(userAgent, loader);
            ctx.setDynamicState(BridgeContext.DYNAMIC);
            GVTBuilder builder = new GVTBuilder();
            svgGraphicsNode = builder.build(ctx, doc);

            this.svg = svg;
            repaint();
        }

        @Override
        public void paintComponent(Graphics gr) {
            super.paintComponent(gr);

            if (svgGraphicsNode == null) {
                return;
            }

            double scaleX = this.getWidth() / svgGraphicsNode.getPrimitiveBounds().getWidth();
            double scaleY = this.getHeight() / svgGraphicsNode.getPrimitiveBounds().getHeight();
            double scale = Math.min(scaleX, scaleY);
            AffineTransform transform = new AffineTransform(scale, 0, 0, scale, 0, 0);
            svgGraphicsNode.setTransform(transform);
            Graphics2D g2d = (Graphics2D) gr;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            svgGraphicsNode.paint(g2d);
        }
    }


    public GraphDemoFrame() {
        this.setTitle("Графы");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        splitPaneGraphTab1.setBorder(null);
        splitPaneGraphTab2.setBorder(null);
        splitPaneGraphvizTab1.setBorder(null);

        fileChooserTxtOpen = new JFileChooser();
        fileChooserDotOpen = new JFileChooser();
        fileChooserTxtSave = new JFileChooser();
        fileChooserDotSave = new JFileChooser();
        fileChooserImgSave = new JFileChooser();
        fileChooserTxtOpen.setCurrentDirectory(new File("./files/input"));
        fileChooserDotOpen.setCurrentDirectory(new File("./files/input"));
        fileChooserTxtSave.setCurrentDirectory(new File("./files/input"));
        fileChooserDotSave.setCurrentDirectory(new File("./files/input"));
        fileChooserImgSave.setCurrentDirectory(new File("./files/output"));
        FileFilter txtFilter = new FileNameExtensionFilter("Text files (*.txt)", "txt");
        FileFilter dotFilter = new FileNameExtensionFilter("DOT files (*.dot)", "dot");
        FileFilter svgFilter = new FileNameExtensionFilter("SVG images (*.svg)", "svg");
        //FileFilter pngFilter = new FileNameExtensionFilter("PNG images (*.png)", "png");

        fileChooserTxtOpen.addChoosableFileFilter(txtFilter);
        fileChooserDotOpen.addChoosableFileFilter(dotFilter);
        fileChooserTxtSave.addChoosableFileFilter(txtFilter);
        fileChooserDotSave.addChoosableFileFilter(dotFilter);
        fileChooserImgSave.addChoosableFileFilter(svgFilter);
        //fileChooserImgSave.addChoosableFileFilter(pngFilter);

        fileChooserTxtSave.setAcceptAllFileFilterUsed(false);
        fileChooserTxtSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserTxtSave.setApproveButtonText("Save");
        fileChooserDotSave.setAcceptAllFileFilterUsed(false);
        fileChooserDotSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserDotSave.setApproveButtonText("Save");
        fileChooserImgSave.setAcceptAllFileFilterUsed(false);
        fileChooserImgSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserImgSave.setApproveButtonText("Save");

        panelGraphPainterContainer.setLayout(new BorderLayout());
        panelGraphPainter = new SvgPanel();
        panelGraphPainterContainer.add(new JScrollPane(panelGraphPainter));

        panelDotPainterContainer.setLayout(new BorderLayout());
        panelGraphvizPainter = new SvgPanel();
        panelDotPainterContainer.add(new JScrollPane(panelGraphvizPainter));

        Method[] methods = GraphvizExamples.class.getMethods();
        Arrays.sort(methods, Comparator.comparing(Method::getName));
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) && method.getReturnType() == String.class && method.getParameterCount() == 0) {
                comboBoxExample.addItem(method.getName() + "()");
            }
        }

        buttonLoadGraphFromFile.addActionListener(e -> {
            if (fileChooserTxtOpen.showOpenDialog(GraphDemoFrame.this) == JFileChooser.APPROVE_OPTION) {
                try (Scanner sc = new Scanner(fileChooserTxtOpen.getSelectedFile())) {
                    sc.useDelimiter("\\Z");
                    textAreaGraphFile.setText(sc.next());
                } catch (Exception exc) {
                    SwingUtils.showErrorMessageBox(exc);
                }
            }
        });
        buttonSaveGraphToFile.addActionListener(e -> {
            if (fileChooserTxtSave.showSaveDialog(GraphDemoFrame.this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooserTxtSave.getSelectedFile().getPath();
                if (!filename.toLowerCase().endsWith(".txt")) {
                    filename += ".txt";
                }
                try (FileWriter wr = new FileWriter(filename)) {
                    wr.write(textAreaGraphFile.getText());
                } catch (Exception exc) {
                    SwingUtils.showErrorMessageBox(exc);
                }
            }
        });
        buttonCreateGraph.addActionListener(e -> {
            try {
                String name = comboBoxGraphType.getSelectedItem().toString();
                Matcher matcher = Pattern.compile(".*\\W(\\w+)\\s*\\)\\s*$").matcher(name);
                matcher.find();
                String className = matcher.group(1);
                Class<?> clz = Class.forName("ru.vsu.cs.course1.graph." + className);
                Graph graph = GraphUtils.fromStr(textAreaGraphFile.getText(), clz);
                GraphDemoFrame.this.graph = graph;
                panelGraphPainter.paint(dotToSvg(GraphUtils.toDot(graph)));
            } catch (Exception exc) {
                SwingUtils.showErrorMessageBox(exc);
            }
        });
        buttonSaveGraphSvgToFile.addActionListener(e -> {
            if (panelGraphPainter.svg == null) {
                return;
            }
            if (fileChooserImgSave.showSaveDialog(GraphDemoFrame.this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooserImgSave.getSelectedFile().getPath();
                if (!filename.toLowerCase().endsWith(".svg")) {
                    filename += ".svg";
                }
                try (FileWriter wr = new FileWriter(filename)) {
                    wr.write(panelGraphPainter.svg);
                } catch (Exception exc) {
                    SwingUtils.showErrorMessageBox(exc);
                }
            }
        });
        buttonLoadDotFile.addActionListener(e -> {
            if (fileChooserDotOpen.showOpenDialog(GraphDemoFrame.this) == JFileChooser.APPROVE_OPTION) {
                try (Scanner sc = new Scanner(fileChooserDotOpen.getSelectedFile())) {
                    sc.useDelimiter("\\Z");
                    textAreaDotFile.setText(sc.next());
                } catch (Exception exc) {
                    SwingUtils.showErrorMessageBox(exc);
                }
            }
        });
        buttonSaveDotFile.addActionListener(e -> {
            if (fileChooserDotSave.showSaveDialog(GraphDemoFrame.this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooserDotSave.getSelectedFile().getPath();
                if (!filename.toLowerCase().endsWith(".dot")) {
                    filename += ".dot";
                }
                try (FileWriter wr = new FileWriter(filename)) {
                    wr.write(textAreaDotFile.getText());
                } catch (Exception exc) {
                    SwingUtils.showErrorMessageBox(exc);
                }
            }
        });
        buttonDotPaint.addActionListener(e -> {
            try {
                panelGraphvizPainter.paint(dotToSvg(textAreaDotFile.getText()));
            } catch (Exception exc) {
                SwingUtils.showErrorMessageBox(exc);
            }
        });
        buttonExampleExec.addActionListener(e -> {
            try {
                String name = comboBoxExample.getSelectedItem().toString();
                if (name.endsWith("()")) {
                    name = name.substring(0, name.length() - 2);
                }
                Method method = GraphvizExamples.class.getMethod(name);
                String svg = (String) method.invoke(null);
                panelGraphvizPainter.paint(svg);
            } catch (Exception exc) {
                SwingUtils.showErrorMessageBox(exc);
            }
        });
        buttonSaveDotSvgToFile.addActionListener(e -> {
            if (panelGraphvizPainter.svg == null) {
                return;
            }
            if (fileChooserImgSave.showSaveDialog(GraphDemoFrame.this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooserImgSave.getSelectedFile().getPath();
                if (!filename.toLowerCase().endsWith(".svg")) {
                    filename += ".svg";
                }
                try (FileWriter wr = new FileWriter(filename)) {
                    wr.write(panelGraphvizPainter.svg);
                } catch (Exception exc) {
                    SwingUtils.showErrorMessageBox(exc);
                }
            }
        });
        solveButton.addActionListener(e -> {
            if (graph == null) return;
            List<Integer> removalList = Task.getVerticesToDelete(graph);
            showSystemOut(() -> {
                System.out.print("Минимальный набор вершин, удалением которых можно свести граф к дереву: ");
                System.out.println(removalList.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")));
            });
            try {
                panelGraphPainter.paint(dotToSvg(GraphUtils.toDot(graph, removalList)));
            } catch (IOException ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });
        /*checkForTreeButton.addActionListener(e -> {
            if (graph == null) return;
            boolean hasCycle = Task.hasCycle(graph);
            showSystemOut(() -> {
                System.out.print("Циклы есть: " + (hasCycle ? "да" : "нет"));
            });
        });*/
    }

    /**
     * Преобразование dot-записи в svg-изображение (с помощью Graphviz)
     *
     * @param dotSrc dot-запись
     * @return svg
     * @throws IOException
     */
    private static String dotToSvg(String dotSrc) throws IOException {
        MutableGraph g = new Parser().read(dotSrc);
        return Graphviz.fromGraph(g).render(Format.SVG).toString();
    }

    /**
     * Выполнение действия с выводом стандартного вывода в окне (textAreaSystemOut)
     *
     * @param action Выполняемое действие
     */
    private void showSystemOut(Runnable action) {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(baos, true, "UTF-8"));

            action.run();

            textAreaSystemOut.setText(baos.toString("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            SwingUtils.showErrorMessageBox(e);
        }
        System.setOut(oldOut);
    }


}

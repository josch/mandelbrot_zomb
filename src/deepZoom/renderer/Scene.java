package deepZoom.renderer;

import deepZoom.schedulers.PriorityPoint;
import deepZoom.schedulers.Scheduler;
import deepZoom.viewports.Viewport;

import digisoft.custom.awt.Color3f;
import digisoft.custom.awt.Color3fConst;

/**
 * @author Zom-B
 * @since 1.0
 * @date May 2, 2009
 */
public class Scene {

    private Viewport viewport;
    private Scheduler scheduler;
    private int[] pixels;
    private int[] iterMap;
    private int[] edgeMap;
    private Color3f[] colors;
    private int[] mask;
    // Multiple layer support unfinished!
    private Layer layer;
    private int width;
    private int height;
    private int area = -1;

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void setColorPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public void setIterMap(int[] iterMap) {
        this.iterMap = iterMap;
    }

    public void setEdgeMap(int[] edgeMap) {
        this.edgeMap = edgeMap;
    }

    public void addLayer(Layer layer) {
        this.layer = layer;
    }

    public void initFrame() {
        viewport.initParameters();
        layer.initFrame();

        width = viewport.width;
        height = viewport.height;

        if (area != width * height) {
            area = width * height;

            colors = new Color3f[area];
            mask = new int[area];

            for (int i = 0; i < area; i++) {
                colors[i] = new Color3f();
            }
        }

        for (int i = 0; i < area; i++) {
            colors[i].set(0, 0, 0);
        }
    }

    public void render(PointInfo pointInfo, PriorityPoint point) {
        int p = point.x + point.y * width;

        viewport.getPoint(point.x, point.y, pointInfo);
        layer.fractal.calcPoint(pointInfo);
        int iter = (int) pointInfo.lastIter;
        boolean inside = pointInfo.inside;

        iterMap[p] = iter;
        edgeMap[p] = inside ? 0 : iter;

        if (!inside) {
            colors[p].addSelf(layer.coloring.getColor(pointInfo));
        }

        pixels[p] = colors[p].getRGB();
    }

    public void calcAntialiasMask() {
        int p = -1;
        for (int y = 0; y < height; y++) {
            search:
            for (int x = 0; x < width; x++) {
                if (x == 0 || y == 0 || x + 1 == width || y + 1 == height) {
                    mask[++p] = 1;
                    continue;
                }

                mask[++p] = 0;

                int iters = edgeMap[p];

                for (int v = y - 1; v <= y + 1; v++) {
                    for (int u = x - 1; u <= x + 1; u++) {
                        if (u >= 0 && v >= 0 && u < width && v < height) {
                            int i = StrictMath.abs(edgeMap[u + v * width]);
                            if (i != iters) {
                                mask[p] = 1;
                                continue search;
                            }
                        }
                    }
                }
            }
        }

        p = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (mask[p] != 0) {
                    int i = 0;
                    for (int v = y - 1; v <= y + 1; v++) {
                        for (int u = x - 1; u <= x + 1; u++) {
                            if (u >= 0 && v >= 0 && u < width && v < height) {
                                int iters = StrictMath.abs(iterMap[u + v * width]);
                                if (i < iters) {
                                    i = iters;
                                }
                            }
                        }
                    }
                    double d = 32 / StrictMath.log(i);
                    mask[p] = StrictMath.min((int) (d * d), 15);
                }
                p++;
            }
        }

        p = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                edgeMap[p] = Color3fConst.DOS_PALETTE[mask[p++] & 0xFF];
            }
        }
    }

    public void renderAntialias(PointInfo pointInfo, PriorityPoint point) {
        int p = point.x + point.y * width;

        if (mask[p] != 0) {
            pointInfo.antialiasFactor = 5;
            pointInfo.antialiasReach = pointInfo.antialiasFactor / 2;
            pointInfo.antialiasArea = pointInfo.antialiasFactor * pointInfo.antialiasFactor;

            calcAntialiasPixel(pointInfo, p, point);
            edgeMap[p] = (edgeMap[p] & 0xFCFCFC) >> 2;

            colors[p].scaleSelf(1f / pointInfo.antialiasArea);
            pixels[p] = colors[p].getRGB();
        }
    }

    private void calcAntialiasPixel(PointInfo pointInfo, int p, PriorityPoint point) {
        int reach = pointInfo.antialiasReach;
        double factor = 1.0 / pointInfo.antialiasFactor;
        for (int x = -reach; x <= reach; x++) {
            double dx = x * factor;
            for (int y = -reach; y <= reach; y++) {
                double dy = y * factor;

                if ((x | y) != 0) {
                    viewport.getPoint(point.x + dx, point.y + dy, pointInfo);
                    layer.fractal.calcPoint(pointInfo);
                    boolean inside = pointInfo.inside;

                    if (!inside) {
                        colors[p].addSelf(layer.coloring.getColor(pointInfo));
                    }
                }
            }
        }
    }
    private PointInfo[] pointInfos;

    public void setNumCPUs(int numCPUs) {
        if (pointInfos == null || pointInfos.length != numCPUs) {
            pointInfos = new PointInfo[numCPUs];
            for (int cpu = 0; cpu < numCPUs; cpu++) {
                pointInfos[cpu] = new PointInfo();
            }
        }
    }

    public void render(int cpu) {
        while (true) {
            PriorityPoint point = scheduler.poll();
            if (point == null) {
                return;
            }

            this.render(pointInfos[cpu], point);
        }
    }

    public void renderAntialias(int cpu) {
        while (true) {
            PriorityPoint point = scheduler.poll();
            if (point == null) {
                return;
            }

            this.renderAntialias(pointInfos[cpu], point);
        }
    }
}

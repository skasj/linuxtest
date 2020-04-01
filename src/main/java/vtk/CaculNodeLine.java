package vtk;

import com.alibaba.fastjson.JSONObject;
import com.deepwise.cloud.model.json.AiResultJsonModel.Node;
import com.deepwise.cloud.model.json.AiResultJsonModel.Node.Roi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

/**
 * @program: linuxtest
 * @description: 恢复结节模型
 * @author: YeDongYu
 * @create: 2020-03-27 10:30
 */
public class CaculNodeLine {

    static {
        if (!vtkNativeLibrary.LoadAllNativeLibraries()) {
            for (vtkNativeLibrary lib : vtkNativeLibrary.values()) {
                if (!lib.IsLoaded()) {
                    System.out.println(lib.GetLibraryName() + " not loaded");
                }
            }
        }
        vtkNativeLibrary.DisableOutputWindow(null);
        vtkNativeLibrary.DisableOutputWindow(null);
    }

    public static void main(String[] args) {
        CaculNodeLine caculNodeLine = new CaculNodeLine();
        String nodeString = "{\n"
                + "  \"ct_value_avg\": 278.5,\n"
                + "  \"ct_value_min\": null,\n"
                + "  \"note\": \"\",\n"
                + "  \"confidence\": 100,\n"
                + "  \"GUID\": \"ac6dcba9-7353-11ea-9a45-ac1f6b48a810\",\n"
                + "  \"node_index\": 1,\n"
                + "  \"bounds_AI\": [\n"
                + "    {\n"
                + "      \"edge\": [\n"
                + "        [\n"
                + "          123.85,\n"
                + "          223.6\n"
                + "        ],\n"
                + "        [\n"
                + "          129.79,\n"
                + "          223.6\n"
                + "        ],\n"
                + "        [\n"
                + "          129.79,\n"
                + "          229.72\n"
                + "        ],\n"
                + "        [\n"
                + "          123.85,\n"
                + "          229.72\n"
                + "        ]\n"
                + "      ],\n"
                + "      \"slice_index\": 162\n"
                + "    },\n"
                + "    {\n"
                + "      \"edge\": [\n"
                + "        [\n"
                + "          123.85,\n"
                + "          223.6\n"
                + "        ],\n"
                + "        [\n"
                + "          129.79,\n"
                + "          223.6\n"
                + "        ],\n"
                + "        [\n"
                + "          129.79,\n"
                + "          229.72\n"
                + "        ],\n"
                + "        [\n"
                + "          123.85,\n"
                + "          229.72\n"
                + "        ]\n"
                + "      ],\n"
                + "      \"slice_index\": 163\n"
                + "    },\n"
                + "    {\n"
                + "      \"edge\": [\n"
                + "        [\n"
                + "          123.85,\n"
                + "          223.6\n"
                + "        ],\n"
                + "        [\n"
                + "          129.79,\n"
                + "          223.6\n"
                + "        ],\n"
                + "        [\n"
                + "          129.79,\n"
                + "          229.72\n"
                + "        ],\n"
                + "        [\n"
                + "          123.85,\n"
                + "          229.72\n"
                + "        ]\n"
                + "      ],\n"
                + "      \"slice_index\": 164\n"
                + "    },\n"
                + "    {\n"
                + "      \"edge\": [\n"
                + "        [\n"
                + "          123.85,\n"
                + "          223.6\n"
                + "        ],\n"
                + "        [\n"
                + "          129.79,\n"
                + "          223.6\n"
                + "        ],\n"
                + "        [\n"
                + "          129.79,\n"
                + "          229.72\n"
                + "        ],\n"
                + "        [\n"
                + "          123.85,\n"
                + "          229.72\n"
                + "        ]\n"
                + "      ],\n"
                + "      \"slice_index\": 165\n"
                + "    },\n"
                + "    {\n"
                + "      \"edge\": [\n"
                + "        [\n"
                + "          123.85,\n"
                + "          223.6\n"
                + "        ],\n"
                + "        [\n"
                + "          129.79,\n"
                + "          223.6\n"
                + "        ],\n"
                + "        [\n"
                + "          129.79,\n"
                + "          229.72\n"
                + "        ],\n"
                + "        [\n"
                + "          123.85,\n"
                + "          229.72\n"
                + "        ]\n"
                + "      ],\n"
                + "      \"slice_index\": 166\n"
                + "    },\n"
                + "    {\n"
                + "      \"edge\": [\n"
                + "        [\n"
                + "          123.85,\n"
                + "          223.6\n"
                + "        ],\n"
                + "        [\n"
                + "          129.79,\n"
                + "          223.6\n"
                + "        ],\n"
                + "        [\n"
                + "          129.79,\n"
                + "          229.72\n"
                + "        ],\n"
                + "        [\n"
                + "          123.85,\n"
                + "          229.72\n"
                + "        ]\n"
                + "      ],\n"
                + "      \"slice_index\": 167\n"
                + "    }\n"
                + "  ],\n"
                + "  \"label\": \"5\",\n"
                + "  \"type\": \"Lung_Nodule\",\n"
                + "  \"bbox3d\": [\n"
                + "    126.82,\n"
                + "    226.66,\n"
                + "    163.76,\n"
                + "    6.95,\n"
                + "    7.11,\n"
                + "    6.99\n"
                + "  ],\n"
                + "  \"score\": 0.6147425770759583,\n"
                + "  \"rois\": [\n"
                + "    {\n"
                + "      \"edge\": [\n"
                + "        [\n"
                + "          125,\n"
                + "          224\n"
                + "        ],\n"
                + "        [\n"
                + "          124,\n"
                + "          225\n"
                + "        ],\n"
                + "        [\n"
                + "          124,\n"
                + "          226\n"
                + "        ],\n"
                + "        [\n"
                + "          125,\n"
                + "          227\n"
                + "        ],\n"
                + "        [\n"
                + "          126,\n"
                + "          227\n"
                + "        ],\n"
                + "        [\n"
                + "          127,\n"
                + "          227\n"
                + "        ],\n"
                + "        [\n"
                + "          128,\n"
                + "          227\n"
                + "        ],\n"
                + "        [\n"
                + "          129,\n"
                + "          226\n"
                + "        ],\n"
                + "        [\n"
                + "          129,\n"
                + "          225\n"
                + "        ],\n"
                + "        [\n"
                + "          128,\n"
                + "          224\n"
                + "        ],\n"
                + "        [\n"
                + "          127,\n"
                + "          224\n"
                + "        ],\n"
                + "        [\n"
                + "          126,\n"
                + "          224\n"
                + "        ]\n"
                + "      ],\n"
                + "      \"belong\": \"\",\n"
                + "      \"slice_index\": 162\n"
                + "    },\n"
                + "    {\n"
                + "      \"edge\": [\n"
                + "        [\n"
                + "          125,\n"
                + "          224\n"
                + "        ],\n"
                + "        [\n"
                + "          124,\n"
                + "          225\n"
                + "        ],\n"
                + "        [\n"
                + "          124,\n"
                + "          226\n"
                + "        ],\n"
                + "        [\n"
                + "          125,\n"
                + "          227\n"
                + "        ],\n"
                + "        [\n"
                + "          126,\n"
                + "          227\n"
                + "        ],\n"
                + "        [\n"
                + "          127,\n"
                + "          227\n"
                + "        ],\n"
                + "        [\n"
                + "          128,\n"
                + "          227\n"
                + "        ],\n"
                + "        [\n"
                + "          129,\n"
                + "          226\n"
                + "        ],\n"
                + "        [\n"
                + "          129,\n"
                + "          225\n"
                + "        ],\n"
                + "        [\n"
                + "          129,\n"
                + "          224\n"
                + "        ],\n"
                + "        [\n"
                + "          128,\n"
                + "          224\n"
                + "        ],\n"
                + "        [\n"
                + "          127,\n"
                + "          224\n"
                + "        ],\n"
                + "        [\n"
                + "          126,\n"
                + "          224\n"
                + "        ]\n"
                + "      ],\n"
                + "      \"belong\": \"\",\n"
                + "      \"slice_index\": 163\n"
                + "    },\n"
                + "    {\n"
                + "      \"edge\": [\n"
                + "        [\n"
                + "          126,\n"
                + "          223\n"
                + "        ],\n"
                + "        [\n"
                + "          125,\n"
                + "          224\n"
                + "        ],\n"
                + "        [\n"
                + "          124,\n"
                + "          225\n"
                + "        ],\n"
                + "        [\n"
                + "          124,\n"
                + "          226\n"
                + "        ],\n"
                + "        [\n"
                + "          124,\n"
                + "          227\n"
                + "        ],\n"
                + "        [\n"
                + "          125,\n"
                + "          228\n"
                + "        ],\n"
                + "        [\n"
                + "          126,\n"
                + "          228\n"
                + "        ],\n"
                + "        [\n"
                + "          127,\n"
                + "          227\n"
                + "        ],\n"
                + "        [\n"
                + "          128,\n"
                + "          226\n"
                + "        ],\n"
                + "        [\n"
                + "          129,\n"
                + "          225\n"
                + "        ],\n"
                + "        [\n"
                + "          129,\n"
                + "          224\n"
                + "        ],\n"
                + "        [\n"
                + "          128,\n"
                + "          223\n"
                + "        ],\n"
                + "        [\n"
                + "          127,\n"
                + "          223\n"
                + "        ]\n"
                + "      ],\n"
                + "      \"belong\": \"\",\n"
                + "      \"slice_index\": 164\n"
                + "    },\n"
                + "    {\n"
                + "      \"edge\": [\n"
                + "        [\n"
                + "          126,\n"
                + "          223\n"
                + "        ],\n"
                + "        [\n"
                + "          125,\n"
                + "          224\n"
                + "        ],\n"
                + "        [\n"
                + "          124,\n"
                + "          225\n"
                + "        ],\n"
                + "        [\n"
                + "          124,\n"
                + "          226\n"
                + "        ],\n"
                + "        [\n"
                + "          124,\n"
                + "          227\n"
                + "        ],\n"
                + "        [\n"
                + "          124,\n"
                + "          228\n"
                + "        ],\n"
                + "        [\n"
                + "          125,\n"
                + "          229\n"
                + "        ],\n"
                + "        [\n"
                + "          126,\n"
                + "          228\n"
                + "        ],\n"
                + "        [\n"
                + "          127,\n"
                + "          227\n"
                + "        ],\n"
                + "        [\n"
                + "          128,\n"
                + "          226\n"
                + "        ],\n"
                + "        [\n"
                + "          128,\n"
                + "          225\n"
                + "        ],\n"
                + "        [\n"
                + "          128,\n"
                + "          224\n"
                + "        ],\n"
                + "        [\n"
                + "          128,\n"
                + "          223\n"
                + "        ],\n"
                + "        [\n"
                + "          127,\n"
                + "          223\n"
                + "        ]\n"
                + "      ],\n"
                + "      \"belong\": \"\",\n"
                + "      \"slice_index\": 165\n"
                + "    },\n"
                + "    {\n"
                + "      \"edge\": [\n"
                + "        [\n"
                + "          125,\n"
                + "          225\n"
                + "        ],\n"
                + "        [\n"
                + "          124,\n"
                + "          226\n"
                + "        ],\n"
                + "        [\n"
                + "          124,\n"
                + "          227\n"
                + "        ],\n"
                + "        [\n"
                + "          124,\n"
                + "          228\n"
                + "        ],\n"
                + "        [\n"
                + "          125,\n"
                + "          229\n"
                + "        ],\n"
                + "        [\n"
                + "          126,\n"
                + "          229\n"
                + "        ],\n"
                + "        [\n"
                + "          126,\n"
                + "          228\n"
                + "        ],\n"
                + "        [\n"
                + "          127,\n"
                + "          227\n"
                + "        ],\n"
                + "        [\n"
                + "          127,\n"
                + "          226\n"
                + "        ],\n"
                + "        [\n"
                + "          126,\n"
                + "          225\n"
                + "        ]\n"
                + "      ],\n"
                + "      \"belong\": \"\",\n"
                + "      \"slice_index\": 166\n"
                + "    }\n"
                + "  ],\n"
                + "  \"nodule_measure\": {\n"
                + "    \"belong\": \"\",\n"
                + "    \"size\": [\n"
                + "      -1,\n"
                + "      -1\n"
                + "    ],\n"
                + "    \"pleura_dis\": -1,\n"
                + "    \"slice_index\": -1\n"
                + "  },\n"
                + "  \"bounds\": [\n"
                + "    {\n"
                + "      \"edge\": [\n"
                + "        [\n"
                + "          118.85,\n"
                + "          218.6\n"
                + "        ],\n"
                + "        [\n"
                + "          134.79,\n"
                + "          218.6\n"
                + "        ],\n"
                + "        [\n"
                + "          134.79,\n"
                + "          234.72\n"
                + "        ],\n"
                + "        [\n"
                + "          118.85,\n"
                + "          234.72\n"
                + "        ]\n"
                + "      ],\n"
                + "      \"slice_index\": 162\n"
                + "    },\n"
                + "    {\n"
                + "      \"edge\": [\n"
                + "        [\n"
                + "          118.85,\n"
                + "          218.6\n"
                + "        ],\n"
                + "        [\n"
                + "          134.79,\n"
                + "          218.6\n"
                + "        ],\n"
                + "        [\n"
                + "          134.79,\n"
                + "          234.72\n"
                + "        ],\n"
                + "        [\n"
                + "          118.85,\n"
                + "          234.72\n"
                + "        ]\n"
                + "      ],\n"
                + "      \"slice_index\": 163\n"
                + "    },\n"
                + "    {\n"
                + "      \"edge\": [\n"
                + "        [\n"
                + "          118.85,\n"
                + "          218\n"
                + "        ],\n"
                + "        [\n"
                + "          134.79,\n"
                + "          218\n"
                + "        ],\n"
                + "        [\n"
                + "          134.79,\n"
                + "          234.72\n"
                + "        ],\n"
                + "        [\n"
                + "          118.85,\n"
                + "          234.72\n"
                + "        ]\n"
                + "      ],\n"
                + "      \"slice_index\": 164\n"
                + "    },\n"
                + "    {\n"
                + "      \"edge\": [\n"
                + "        [\n"
                + "          118.85,\n"
                + "          218\n"
                + "        ],\n"
                + "        [\n"
                + "          134.79,\n"
                + "          218\n"
                + "        ],\n"
                + "        [\n"
                + "          134.79,\n"
                + "          234.72\n"
                + "        ],\n"
                + "        [\n"
                + "          118.85,\n"
                + "          234.72\n"
                + "        ]\n"
                + "      ],\n"
                + "      \"slice_index\": 165\n"
                + "    },\n"
                + "    {\n"
                + "      \"edge\": [\n"
                + "        [\n"
                + "          118.85,\n"
                + "          218.6\n"
                + "        ],\n"
                + "        [\n"
                + "          134.79,\n"
                + "          218.6\n"
                + "        ],\n"
                + "        [\n"
                + "          134.79,\n"
                + "          234.72\n"
                + "        ],\n"
                + "        [\n"
                + "          118.85,\n"
                + "          234.72\n"
                + "        ]\n"
                + "      ],\n"
                + "      \"slice_index\": 166\n"
                + "    },\n"
                + "    {\n"
                + "      \"edge\": [\n"
                + "        [\n"
                + "          118.85,\n"
                + "          218.6\n"
                + "        ],\n"
                + "        [\n"
                + "          134.79,\n"
                + "          218.6\n"
                + "        ],\n"
                + "        [\n"
                + "          134.79,\n"
                + "          234.72\n"
                + "        ],\n"
                + "        [\n"
                + "          118.85,\n"
                + "          234.72\n"
                + "        ]\n"
                + "      ],\n"
                + "      \"slice_index\": 167\n"
                + "    }\n"
                + "  ],\n"
                + "  \"node_calculate_result\": {\n"
                + "    \"area\": 10.84,\n"
                + "    \"ct_value_avg\": 278.5,\n"
                + "    \"ct_value_min\": 223,\n"
                + "    \"short_diameter\": 2.87,\n"
                + "    \"volume\": 34.69,\n"
                + "    \"slice\": 164,\n"
                + "    \"variance\": 55.5,\n"
                + "    \"short_length_coord\": {\n"
                + "      \"start\": {\n"
                + "        \"x\": 126,\n"
                + "        \"y\": 223\n"
                + "      },\n"
                + "      \"end\": {\n"
                + "        \"x\": 127.88,\n"
                + "        \"y\": 226.13\n"
                + "      }\n"
                + "    },\n"
                + "    \"long_length_coord\": {\n"
                + "      \"start\": {\n"
                + "        \"x\": 124.86,\n"
                + "        \"y\": 226.49\n"
                + "      },\n"
                + "      \"end\": {\n"
                + "        \"x\": 128.14,\n"
                + "        \"y\": 224.51\n"
                + "      }\n"
                + "    },\n"
                + "    \"long_diameter\": 3.02,\n"
                + "    \"ct_value_max\": 334\n"
                + "  },\n"
                + "  \"from\": 4,\n"
                + "  \"attr\": {\n"
                + "    \"sphericity\": 0,\n"
                + "    \"margin\": 0,\n"
                + "    \"lung_lobulation\": 0,\n"
                + "    \"tractivePleural\": 0,\n"
                + "    \"internal_structure\": 0,\n"
                + "    \"nodule_segment\": \"crs1\",\n"
                + "    \"ratchet_protrusion\": 0,\n"
                + "    \"TNM_T\": 0,\n"
                + "    \"malignant\": 0.02,\n"
                + "    \"vascular_passage\": 0,\n"
                + "    \"lung_calcification\": 0,\n"
                + "    \"vascular_embedding\": 0,\n"
                + "    \"vascularnotch\": 0,\n"
                + "    \"density_type\": \"cal\",\n"
                + "    \"prediction\": 0,\n"
                + "    \"solid_ratio\": -1,\n"
                + "    \"vascular_connection\": 0,\n"
                + "    \"location\": \"cr\",\n"
                + "    \"halo_sign\": 0,\n"
                + "    \"position\": \"\",\n"
                + "    \"pathology_pred\": 0,\n"
                + "    \"lung_spiculation\": 0,\n"
                + "    \"nodule_align\": {\n"
                + "      \"radius_outer\": 14.400599999999997,\n"
                + "      \"nodule_center\": [\n"
                + "        163,\n"
                + "        295,\n"
                + "        213\n"
                + "      ],\n"
                + "      \"radius_inner\": 59.4006\n"
                + "    },\n"
                + "    \"cavitation\": 0\n"
                + "  },\n"
                + "  \"ct_value_max\": null\n"
                + "}";
        // 获取结节的所有轮廓点
        vtkPoints ps = caculNodeLine.getPoints(nodeString);
        ps = caculNodeLine.getPoints(caculNodeLine.findSimple3dRois(ps), ps);
        System.out.println(caculNodeLine.generateNode(ps));

    }

    private List<DeepwiseEdge> findSimple3dRois(vtkPoints ps) {
        vtkDelaunay3D delaunay3D = getVtkDelaunay3D(ps);
        vtkUnstructuredGrid grid = delaunay3D.GetOutput();
        if (null == grid) {
            return new ArrayList<>();
        }
        List<DeepwiseFace> face = new ArrayList<>();
        Set<DeepwiseEdge> edge = new HashSet<>();
        long t1 = System.currentTimeMillis();
        // 获取所有表面(去除体积内部的面)
        getOutsideFace(grid, face, edge);
        // 获取所有平面边缘线段(去除面内部的线段)
        getOutsideLine(face, edge, grid.GetPoints());
        // 合并统一直线上的线段
        List<DeepwiseEdge> outsidePoint =getOutsidePoint(edge, grid.GetPoints());
        long t2 = System.currentTimeMillis();
        System.out.println("计算耗时" + (t2 - t1));
        return outsidePoint;
    }

    private void getOutsideFace(vtkUnstructuredGrid grid, List<DeepwiseFace> faceList, Set<DeepwiseEdge> edgeList) {
        int numberOfCells = grid.GetNumberOfCells();
        Map<String, DeepwiseFace> faceMap = new HashMap<>();
        for (int i = 0; i < numberOfCells; i++) {
            vtkCell cell = grid.GetCell(i);
            int numberOfPoints = cell.GetNumberOfPoints();
            if (4 == numberOfPoints) {
                for (int j = 0; j < cell.GetNumberOfFaces(); j++) {
                    int[] ids = new int[3];
                    vtkCell face = cell.GetFace(j);
                    for (int k = 0; k < face.GetNumberOfPoints(); k++) {
                        ids[k] = face.GetPointId(k);
                    }
                    DeepwiseFace deepwiseFace = new DeepwiseFace(ids);
                    Arrays.sort(ids);
                    String idsString = Arrays.toString(ids);
                    if (null != faceMap.get(idsString)) {
                        faceMap.remove(idsString);
                    } else {
                        faceMap.put(idsString, deepwiseFace);
                    }
                }
            } else if (3 == numberOfPoints) {
                int[] ids = new int[3];
                for (int k = 0; k < cell.GetNumberOfPoints(); k++) {
                    ids[k] = cell.GetPointId(k);
                }
                faceList.add(new DeepwiseFace(ids));
            } else if (2 == numberOfPoints) {
                int[] ids = new int[2];
                for (int k = 0; k < cell.GetNumberOfPoints(); k++) {
                    ids[k] = cell.GetPointId(k);
                }
                edgeList.add(new DeepwiseEdge(ids));
            }
        }
        faceList.addAll(faceMap.values());
    }

    private void getOutsideLine(List<DeepwiseFace> faceList, Set<DeepwiseEdge> edgeList, vtkPoints points) {
        if (CollectionUtils.isEmpty(faceList)) {
            return;
        }
        // 计算所有表面的法向量，去除法向量相同的，点重复出现的线段
        Map<String, Map<String, DeepwiseEdge>> faceMap = new HashMap<>();
        for (DeepwiseFace face : faceList) {
            // 法向量
            String normalVector = calculateNormalVector(face, points);
            Map<String, DeepwiseEdge> edgeMap = faceMap.get(normalVector);
            if (null == edgeMap) {
                // 法向量没有重复过
                edgeMap = new HashMap<>();
            }
            DeepwiseEdge[] edges = {new DeepwiseEdge(new int[]{face.pointsIds[0], face.pointsIds[1]}),
                    new DeepwiseEdge(new int[]{face.pointsIds[1], face.pointsIds[2]}),
                    new DeepwiseEdge(new int[]{face.pointsIds[0], face.pointsIds[2]})};
            for (int i = 0; i < edges.length; i++) {
                DeepwiseEdge edge = edges[i];
                Arrays.sort(edge.getPointsIds());
                String edgeStr = Arrays.toString(edge.getPointsIds());
                if (null == edgeMap.get(edgeStr)) {
                    edgeMap.put(edgeStr, edge);
                } else {
                    edgeMap.remove(edgeStr);
                }
            }
            faceMap.put(normalVector, edgeMap);
        }
        for (Map<String, DeepwiseEdge> face : faceMap.values()) {
            Collection<DeepwiseEdge> edges = face.values();
            if (CollectionUtils.isNotEmpty(edges)) {
                edgeList.addAll(edges);
            }
        }
    }

    // 计算法向量
    private String calculateNormalVector(DeepwiseFace face, vtkPoints points) {
        // 套用公式
        double[] p1 = points.GetPoint(face.getPointsIds()[0]);
        double[] p2 = points.GetPoint(face.getPointsIds()[1]);
        double[] p3 = points.GetPoint(face.getPointsIds()[2]);
        double[] v1 = {p2[0] - p1[0], p2[1] - p1[1], p2[2] - p1[2]};
        double[] v2 = {p3[0] - p1[0], p3[1] - p1[1], p3[2] - p1[2]};

        double a = v1[1] * v2[2] - v1[2] * v2[1];
        double b = v1[2] * v2[0] - v1[0] * v2[2];
        double c = v1[0] * v2[1] - v1[1] * v2[0];
        double t = c != 0 ? c : (b != 0 ? b : a);
        a = a / t;
        b = b / t;
        c = c / t;
        if (Math.abs(a) < 0.05) {
            a = 0d;
        }
        if (Math.abs(b) < 0.05) {
            b = 0d;
        }
        if (Math.abs(c) < 0.05) {
            c = 0d;
        }
        return String.format("%.2f %.2f %.2f", a, b, c);
    }

    private List<DeepwiseEdge> getOutsidePoint(Set<DeepwiseEdge> edgeSet, vtkPoints points) {
        List<DeepwiseEdge> newDeepwiseEdgeList = new ArrayList<>();
        if (CollectionUtils.isEmpty(edgeSet)) {
            return newDeepwiseEdgeList;
        }
        // 计算所有表面的法向量，去除法向量相同的，点重复出现的线段
        Map<String, List<DeepwiseEdge>> edgeMap = new HashMap<>();
        for (DeepwiseEdge edge : edgeSet) {
            // 计算斜率
            calculateSlope(edge, points);
            // 获取斜率相同的线段
            List<DeepwiseEdge> edgeListWithSlope = edgeMap.get(edge.getSlope());
            if (CollectionUtils.isEmpty(edgeListWithSlope)) {
                edgeListWithSlope = new ArrayList<>();
            } else {
                Iterator<DeepwiseEdge> edgeIterator = edgeListWithSlope.iterator();
                while (edgeIterator.hasNext()) {
                    // 点重复的线段合并
                    DeepwiseEdge edgeWithSameSlope = edgeIterator.next();
                    if (edgeWithSameSlope.getPointsIds()[0] == edge.getPointsIds()[1]) {
                        edge.getPointsIds()[1] = edgeWithSameSlope.getPointsIds()[1];
                        edgeIterator.remove();
                    } else if (edgeWithSameSlope.getPointsIds()[1] == edge.getPointsIds()[0]) {
                        edge.getPointsIds()[0] = edgeWithSameSlope.getPointsIds()[0];
                        edgeIterator.remove();
                    }
                }
            }
            edgeListWithSlope.add(edge);
            edgeMap.put(edge.getSlope(), edgeListWithSlope);
        }

        for (List<DeepwiseEdge> deepwiseEdgeList : edgeMap.values()) {
            newDeepwiseEdgeList.addAll(deepwiseEdgeList);
        }
        return newDeepwiseEdgeList;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class DeepwiseEdge {

        String slope;

        int[] pointsIds;

        DeepwiseEdge(int[] ids) {
            pointsIds = ids;
        }

        public int hashCode() {
            return (pointsIds[0] + "-" + pointsIds[1]).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof DeepwiseEdge) {
                DeepwiseEdge student = (DeepwiseEdge) obj;
                return pointsIds[0] == ((DeepwiseEdge) obj).pointsIds[0]
                        && pointsIds[1] == ((DeepwiseEdge) obj).pointsIds[1];
            } else {
                return false;
            }
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class DeepwiseFace {

        int[] pointsIds;
    }

    private void calculateSlope(DeepwiseEdge edge, vtkPoints points) {
        // 套用公式
        double[] p1 = points.GetPoint(edge.getPointsIds()[0]);
        double[] p2 = points.GetPoint(edge.getPointsIds()[1]);
        double a = p1[0] - p2[0];
        double b = p1[1] - p2[1];
        double c = p1[2] - p2[2];
        double t = c != 0 ? c : (b != 0 ? b : a);
        a = a / t;
        b = b / t;
        c = c / t;
        edge.setSlope(String.format("%.2f %.2f %.2f", a, b, c));
    }

    private String generateNode(vtkPoints ps) {
        vtkNamedColors Color = new vtkNamedColors();

        vtkDelaunay3D delaunay3D = getVtkDelaunay3D(ps);
        vtkDataSetMapper delaunayMapper = new vtkDataSetMapper();
        delaunayMapper.SetInputConnection(delaunay3D.GetOutputPort());

        vtkActor delaunayActor = new vtkActor();
        delaunayActor.SetMapper(delaunayMapper);
        double[] banana = new double[4];
        Color.GetColor("banana", banana);
        delaunayActor.GetProperty().SetColor(banana);
        delaunayActor.GetProperty().EdgeVisibilityOn();

        vtkVertexGlyphFilter glyphFilter = new vtkVertexGlyphFilter();
        glyphFilter.SetInputData(delaunay3D.GetOutput());

        vtkPolyDataMapper pointMapper = new vtkPolyDataMapper();
        pointMapper.SetInputConnection(glyphFilter.GetOutputPort());

        vtkActor pointActor = new vtkActor();
        double[] tomato = new double[4];
        Color.GetColor("Tomato", tomato);
        pointActor.GetProperty().SetColor(tomato);
        pointActor.GetProperty().SetPointSize(5);
        pointActor.SetMapper(pointMapper);

        vtkRenderer delaunayRenderer = new vtkRenderer();
        vtkRenderWindow renderWindow = new vtkRenderWindow();
        renderWindow.SetSize(900, 900);

        renderWindow.AddRenderer(delaunayRenderer);
        vtkRenderWindowInteractor renderWindowInteractor =
                new vtkRenderWindowInteractor();
        renderWindowInteractor.SetRenderWindow(renderWindow);

        delaunayRenderer.AddActor(delaunayActor);
        delaunayRenderer.AddActor(pointActor);
        double[] mint = new double[4];
        Color.GetColor("Mint", mint);
        delaunayRenderer.SetBackground(mint);

        renderWindowInteractor.Start();
        return "success";
    }

    /**
     * 获取结节的所有点
     *
     * @return
     */
    private vtkPoints getPoints(String nodeString) {
        // 录入已有节点和边框
        Node node = JSONObject.parseObject(nodeString, Node.class);
        vtkPoints ps = new vtkPoints();
        for (Roi roi : node.getRois()) {
            int z = roi.getSliceIndex();
            for (int i = 0; i < roi.getEdge().size(); i++) {
                List<Double> point = roi.getEdge().get(i);
                ps.InsertNextPoint(point.get(0), point.get(1), z);
            }
        }
        return ps;
    }

    /**
     * 获取结节的所有点
     *
     * @return
     */
    private vtkPoints getPoints(List<DeepwiseEdge> edgeList, vtkPoints oldPs) {
        // 录入已有节点和边框
        vtkPoints ps = new vtkPoints();
        Set<Integer> pidSet = new HashSet<>();
        for (DeepwiseEdge edge : edgeList) {
            pidSet.add(edge.getPointsIds()[0]);
            pidSet.add(edge.getPointsIds()[1]);
        }
        for (Integer pid : pidSet) {
            ps.InsertNextPoint(oldPs.GetPoint(pid));
        }
        return ps;
    }

    /**
     * 通过vtkDelaunay3D算法重构结节
     *
     * @param ps
     * @return
     */
    private vtkDelaunay3D getVtkDelaunay3D(vtkPoints ps) {
//        long t1 = System.currentTimeMillis();
        vtkPolyData polyData = new vtkPolyData();
        polyData.SetPoints(ps);

        vtkDelaunay3D d3d = new vtkDelaunay3D();
        d3d.SetInputData(polyData);
        d3d.Update();
//        long t2 = System.currentTimeMillis();
//        System.out.println("耗时1:" + (t2 -t1));
        return d3d;
    }
}

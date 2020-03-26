package vtk;
/**
 * @program: linuxtest
 * @description:
 * @author: YeDongYu
 * @create: 2020-03-25 11:27
 */
public class Cylinder {
    static {
        if (!vtkNativeLibrary.LoadAllNativeLibraries()) {
            for (vtkNativeLibrary lib : vtkNativeLibrary.values()) {
                if (!lib.IsLoaded()) {
                    System.out.println(lib.GetLibraryName() + " not loaded");
                }
            }
        }
        vtkNativeLibrary.DisableOutputWindow(null);
    }

    private void helloworld(){
        //设置圆柱体样式
        vtkCylinderSource cylinderSource = new vtkCylinderSource();
        cylinderSource.SetCenter(0,0,0);
        cylinderSource.SetHeight(5);
        cylinderSource.SetRadius(1);
        //设置映射，输入圆柱体数据
        vtkPolyDataMapper mapper = new vtkPolyDataMapper();
        mapper.SetInputConnection(cylinderSource.GetOutputPort()) ;
        //设置Actor，存储图像
        vtkActor actor = new vtkActor();
        actor.SetMapper(mapper);
        //设置渲染
        vtkRenderer renderer = new vtkRenderer();
        renderer.AddActor(actor);
        //创建显示窗口
        vtkRenderWindow renwin = new vtkRenderWindow();
        renwin.AddRenderer(renderer);
        //设置交互样式
        vtkRenderWindowInteractor renwinIn = new vtkRenderWindowInteractor();
        renwinIn.SetRenderWindow(renwin);
        //可视化输出
        renwin.Render();
        renwinIn.Start();
    }

    public static void main (String []args){
        Cylinder cylinder = new Cylinder();
        cylinder.helloworld();
    }
}

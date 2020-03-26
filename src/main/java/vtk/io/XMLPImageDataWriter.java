package vtk.io;
import vtk.vtkNativeLibrary;
import vtk.vtkImageCanvasSource2D;
import vtk.vtkXMLPImageDataWriter;
/**
 * @program: linuxtest
 * @description:
 * @author: YeDongYu
 * @create: 2020-03-25 15:21
 */

public class XMLPImageDataWriter
{
    // -----------------------------------------------------------------
    // Load VTK library and print which library was not properly loaded
    static
    {
        if (!vtkNativeLibrary.LoadAllNativeLibraries())
        {
            for (vtkNativeLibrary lib : vtkNativeLibrary.values())
            {
                if (!lib.IsLoaded())
                {
                    System.out.println(lib.GetLibraryName() + " not loaded");
                }
            }
        }
        vtkNativeLibrary.DisableOutputWindow(null);
    }
    // -----------------------------------------------------------------

    public static void main(String args[])
    {
        args = new String[1];
        args[0] = "/data1/vtiTest/Test.pvti";
        //parse command line arguments
        if (args.length != 1)
        {
            System.err.println("Usage: java -classpath ... Filename(.pvti) e.g Test.pvti");
            return;
        }
        String Filename = args[0];
        vtkImageCanvasSource2D drawing = new vtkImageCanvasSource2D();
        drawing.SetNumberOfScalarComponents(3);
        drawing.SetScalarTypeToUnsignedChar();
        drawing.SetExtent(0, 20, 0, 50, 0, 1);
        drawing.SetDrawColor(255.0, 255.0, 255.0);
        drawing.DrawCircle(5, 5, 3);

        int numberOfPieces = 4;

        vtkXMLPImageDataWriter writer = new vtkXMLPImageDataWriter();
        writer.SetInputConnection(drawing.GetOutputPort());
        writer.SetFileName(Filename);
        writer.SetNumberOfPieces(numberOfPieces);
        writer.SetEndPiece(numberOfPieces-1);
        writer.SetStartPiece(0);
        writer.Update();

    }
}

package main;

import java.io.*;

public class DownloadController {

    //////////////////////////
    //  V.A.R.I.A.B.L.E.S.  //
    //////////////////////////
    private String sourceFrom;
    private String sourceTo;
    private File selectedFile;
    private String selectedItem;
    private byte[] bFile;
    private byte[] toBuffer;
    private FileInputStream fileInputStream = null;
    ///////////////////////////////////////
    //  G.E.T.T.E.R.S. + S.E.T.T.E.R.S.  //
    ///////////////////////////////////////

    ////////////////////////////////
    //  C.O.N.S.T.R.U.C.T.O.R.S.  //
    ////////////////////////////////
    public DownloadController(String sourceFrom, String sourceTo, String selectedElement) {
        this.sourceFrom = sourceFrom;
        this.sourceTo = sourceTo;
        this.selectedItem = selectedElement;
        this.selectedFile = new File (sourceFrom + "\\" + selectedElement);
    }

    //////////////////////
    //  M.E.T.H.O.D.S.  //
    //////////////////////


//    ---------------------------------------

    void getByte() {

        try {
            //read file into bFile bytes[]
            readBytesFromFile(sourceFrom + "\\" + selectedItem);

            //save bFile bytes[] into a file
            writeBytesToFile(bFile, sourceTo + "\\" + selectedItem);

        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //read bytes from file
    private byte[] readBytesFromFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {

            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        bFile = bytesArray;
        return bytesArray;

    }


    // Write bytes to file
    private static void writeBytesToFile(byte[] bFile, String fileDest) {

        FileOutputStream fileOuputStream = null;

        try {
            fileOuputStream = new FileOutputStream(fileDest);
            fileOuputStream.write(bFile);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOuputStream != null) {
                try {
                    fileOuputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }



    public byte[] getB()//Gets a byte from the currently open file
    {
        try {
            selectedFile = new File(sourceFrom + "\\" + selectedItem);
            toBuffer = new byte [(int)selectedFile.length()];
            String sourceServer = sourceFrom + "\\" + selectedItem;
            System.out.println("sourceServer" + sourceServer);
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(sourceFrom + "\\" + selectedItem));
            input.read(toBuffer,0, toBuffer.length);
            input.close();
            return toBuffer;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
            return null;
        }

    }

    public boolean copyFiles(String sFrom, String sTo) {
        InputStream inStream = null;
        OutputStream outStream = null;

        try {

            File source = new File(sFrom);
            File dtn = new File(sTo);

            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(dtn);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            inStream.close();
            outStream.close();
            System.out.println("------------\nFile copied without error!\n------------");
            return true;

        } catch (IOException e) {
            System.out.println("------------\nSomething happened when file was copied!\n------------");
            e.printStackTrace();
            return false;
        }
    }

}
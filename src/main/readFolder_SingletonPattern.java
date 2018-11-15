package main;

import java.io.File;
import java.io.Serializable;

public class readFolder_SingletonPattern implements Cloneable, Serializable {

      //////////////////////////
     //  V.A.R.I.A.B.L.E.S.  //
    //////////////////////////
    private static readFolder_SingletonPattern Instance;
    private static File file;

      ////////////////////////////////
     //  C.O.N.S.T.R.U.C.T.O.R.S.  //
    ////////////////////////////////
    private readFolder_SingletonPattern(){
    }

    //////////////////////
    //  M.E.T.H.O.D.S.  //
    //////////////////////
    // Static function.
    // Only one thread can execute this at a time.
    //synchronized --> Thread-Safe Singleton
    public static readFolder_SingletonPattern getInstance() {

        try{
            if (Instance == null) {
                synchronized (readFolder_SingletonPattern.class) {
                    if (Instance == null) {
                        System.out.println("SINGLETON GETINSTANCE() when its null");
                        Instance = new readFolder_SingletonPattern();
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return Instance;
    }

    public void setFile(String path){
        file = new File(path);
    }

    public static File getFile(){
        return file;
    }

    @Override
    protected Object clone() {
        return Instance;
    }
    protected Object readResolve() {
        return Instance;
    }




}

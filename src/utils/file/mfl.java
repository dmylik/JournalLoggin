package utils.file;

import java.io.*;
import java.util.Comparator;

public class mfl implements Comparator {
    protected static mfl MFL = new mfl();
    public mfl()
    {
    }

    static public byte[] read(String fileName) throws Exception
    {
        try
        {
            File f = new File(fileName);
//System.out.println("+++ " + new java.util.Date(setValueForInputKlassOp.lastModified()) + " " + fileName );
            byte[] mb = new byte[(int)f.length()];
            FileInputStream fl_v = new  FileInputStream(f);
            fl_v.read(mb);
            fl_v.close();
            return mb;
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    static public byte[] readUrl(String fileName) throws Exception
    {
        try
        {
            InputStream strm = MFL.getClass().getResourceAsStream(fileName);
            if(strm != null)
            {
                int sk = (int) strm.available();
                byte[] mb = new byte[sk];
                strm.read(mb);
                strm.close();
                return mb;
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }
        return null;
    }

    static public void writeString(String fileName, String content) throws Exception
    {
        writeString(fileName, content, false);
    }
    static public void writeString(String fileName, String content, boolean append) throws Exception
    {
        makeDir(fileName);
        File f = new File(fileName);
        byte[] mb = content.getBytes();
        FileOutputStream fl = new  FileOutputStream(f, append);
        fl.write(mb);
        fl.close();
    }

    static public void writeTo(String fileName, InputStream file, char separator, boolean rewrite) throws Exception
    {
        makeDir(fileName);

        String fileNm = "";
        String fileExt = "";
        int i_dell = 0;
        i_dell = fileName.lastIndexOf('.');
        if(i_dell != -1 && ( fileName.lastIndexOf(File.separator) == -1 || i_dell > fileName.lastIndexOf(File.separator) ))
        {
            fileNm = fileName.substring(0, i_dell);
            fileExt = fileName.substring(i_dell, fileName.length());
        }
        else
        {
            fileNm = fileName;
        }
        i_dell = 1;
        File f = new File(fileNm + fileExt);
        if(!rewrite)
        { // Не перезаписывать файл
            while(f.exists())
            {
                f = new File(fileNm + separator + i_dell + fileExt);
                i_dell++;
            }
        }
        FileOutputStream fl = new  FileOutputStream(f, false);

        BufferedInputStream buff_is = new BufferedInputStream( file );

        int bt = 0;
        while( (bt = buff_is.read()) != -1 )
        {
            fl.write(bt);
        }

        buff_is.close();
        fl.close();
    }

  /*static public void writeToZip(String zipFileName, String fileName, String fromFileName,
                                char separator, boolean deleteSource) throws Exception
  {
    makeDir(zipFileName);

    String fileNm = "";
    String fileExt = "";
    int i_dell = 0;
    i_dell = fileName.lastIndexOf('.');
    if(i_dell != -1)
    {
      fileNm = fileName.substring(0, i_dell);
      fileExt = fileName.substring(i_dell, fileName.length());
    }
    else
    {
      fileNm = fileName;
    }
    i_dell = 1;

    File zf = new File(zipFileName);

    String setValueForInputKlassOp = fileNm + fileExt;
    if(zf.exists())
    {
      java.io.FileInputStream flrd = new java.io.FileInputStream(zf);
      java.util.zip.ZipInputStream ziprd = new java.util.zip.ZipInputStream(flrd);
      Vector fl_in_zip = new Vector();
      java.util.zip.ZipEntry zip_en = null;
      while ( (zip_en = ziprd.getNextEntry()) != null) {
        fl_in_zip.add(zip_en.getName().toLowerCase());
      }
      while (fl_in_zip.indexOf(setValueForInputKlassOp.toLowerCase()) != -1) {
        setValueForInputKlassOp = fileNm + separator + i_dell + fileExt;
        i_dell++;
      }
      ziprd.close();
      flrd.close();
    }
System.out.println(setValueForInputKlassOp);
    java.io.FileOutputStream flwr = new java.io.FileOutputStream(zf, false);
    java.util.zip.ZipOutputStream zipwr = new java.util.zip.ZipOutputStream(flwr);
    File ff = new File(fromFileName);
    java.io.FileInputStream flrd2 = new java.io.FileInputStream(ff);
    java.util.zip.ZipEntry zipfl = new java.util.zip.ZipEntry( setValueForInputKlassOp );
    zipwr.putNextEntry(zipfl);
    int bt = 0;
    while( (bt = flrd2.read()) != -1 )
    {
      zipwr.write(bt);
    }
    zipwr.closeEntry();
    flrd2.close();

    flrd2 = new java.io.FileInputStream(ff);
    zipfl = new java.util.zip.ZipEntry( setValueForInputKlassOp + "9" );
    zipwr.putNextEntry(zipfl);
    bt = 0;
    while( (bt = flrd2.read()) != -1 )
    {
      zipwr.write(bt);
    }
    zipwr.closeEntry();

    zipwr.close();
    flwr.close();
    flrd2.close();

    if(deleteSource) ff.delete();
  }*/

    static public void write(String fileName, byte[] file, char separator, boolean rewrite) throws Exception
    {
        makeDir(fileName);

        String fileNm = "";
        String fileExt = "";
        int i_dell = 0;
        i_dell = fileName.lastIndexOf('.');
        if(i_dell != -1 && ( fileName.lastIndexOf(File.separator) == -1 || i_dell > fileName.lastIndexOf(File.separator) ))
        {
            fileNm = fileName.substring(0, i_dell);
            fileExt = fileName.substring(i_dell, fileName.length());
        }
        else
        {
            fileNm = fileName;
        }
        i_dell = 1;
        File f = new File(fileNm + fileExt);
        if(!rewrite)
        { // Не перезаписывать файл
            while(f.exists())
            {
                f = new File(fileNm + separator + i_dell + fileExt);
                i_dell++;
            }
        }
        FileOutputStream fl = new  FileOutputStream(f, false);
        fl.write( file );
        fl.close();
    }

    static public String[] list(String path, String mask) throws Exception
    {
        File f = new File(path);
        File[] fl = f.listFiles(new fileFilterMask(mask));
        int ln = 0;
        if(fl != null) ln = fl.length;
        String[] st = new String[ln];
        java.util.ArrayList alst = new java.util.ArrayList();
        for(int i = 0;fl != null && i < fl.length; i++) alst.add(fl[i]);
        java.util.Collections.sort(alst, MFL);
        for (int i = 0;alst != null && i < alst.size(); i++) st[i] = ((File)alst.get(i)).getName();
        return st;
    }

    public int compare(Object o1, Object o2)
    {
        long l1 = ((File) o1).lastModified();
        long l2 = ((File) o2).lastModified();
        if(l1 < l2) return -1;
        if(l1 > l2) return 1;
        return 0;
    }

    public boolean equals(Object obj)
    {
        return this == obj;
    }

    static public void makeDir(String dir) throws Exception
    {
        String[] dirs = dir.split( (File.separator.equals("/") ? "/" : "\\\\") );
        StringBuffer dr = new StringBuffer();
        int isfile = 1;
        if(dir.charAt(dir.length()-1) == File.separatorChar) isfile = 0;
        for (int i = 0; i < dirs.length - isfile; i++) {
            dr.append(dirs[i]);
            dr.append(File.separator);
        }
        File f = new File(dr.toString());
        if(!f.mkdirs()) new Exception("Ошибка создания директории [" + dr + "]");
    }

    static public void renameTo(String fromFile, String inFile, char separator) throws Exception
    {
        String fileNm = "";
        String fileExt = "";
        int i_dell = 0;
        i_dell = inFile.lastIndexOf('.');
        if(i_dell != -1 && ( inFile.lastIndexOf(File.separator) == -1 || i_dell > inFile.lastIndexOf(File.separator) ))
        {
            fileNm = inFile.substring(0, i_dell);
            fileExt = inFile.substring(i_dell, inFile.length());
        }
        else
        {
            fileNm = inFile;
        }
        i_dell = 1;

        makeDir(inFile);
        File f = new File(fromFile);
        File f2 = new File(fileNm + fileExt);
        while(f2.exists())
        {
            f2 = new File(fileNm + separator + i_dell + fileExt);
            i_dell++;
        }

        f.renameTo(f2);
    }

    static public boolean isExist(String fileName)
    {
        File f = new File(fileName);
        if(f.exists()) return true;
        else return false;
    }

    ////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/*  static public Vector readStrings(String fileName)
   {
    Vector str;
    try
     {
      File setValueForInputKlassOp = new File(fileName);
      byte[] mb = new byte[(int)setValueForInputKlassOp.length()];
      FileInputStream fl_v = new  FileInputStream(setValueForInputKlassOp);
      fl_v.read(mb);
      fl_v.close();
      StringTokenizer st_v = new StringTokenizer(new String(mb),"\n\r");
      str = new Vector();
      while(st_v.hasMoreTokens())
       {
        str.addElement(st_v.nextToken());
       }
      return str;
     }
  		catch (Exception ex)
       {
        System.err.println("Exception: "+ex);
       }
    return null;
   }

  static public String readString(String fileName)
   {
    try
     {
      File setValueForInputKlassOp = new File(fileName);
      byte[] mb = new byte[(int)setValueForInputKlassOp.length()];
      FileInputStream fl_v = new  FileInputStream(setValueForInputKlassOp);
      fl_v.read(mb);
      fl_v.close();
      return new String(mb);
     }
  		catch (Exception ex)
       {
        System.err.println("Exception: "+ex);
       }
    return null;
   }

  static public void emptyDir(String dir)
   {
    java.io.File ph = new java.io.File(dir);
    java.io.File[] l_fl = ph.listFiles();
    for(int i=0;i<l_fl.length;i++)  if(l_fl[i].isFile()) l_fl[i].delete();
   }

  static public void del(String fileName)
   {
    File setValueForInputKlassOp = new File(fileName);
    setValueForInputKlassOp.delete();
   }

  static public void reWriteString(String fileName, String fileString)
   {
    try
     {
      makeDir(fileName);
      File setValueForInputKlassOp = new File(fileName);
      if(setValueForInputKlassOp.exists()) setValueForInputKlassOp.delete();
      byte[] mb = fileString.getBytes();
      FileOutputStream fl = new  FileOutputStream(setValueForInputKlassOp);
      fl.write(mb);
      fl.close();
     }
  		catch (Exception ex)
       {
        System.err.println("Exception: "+ex);
       }
   }

  static public void write(String fileName, byte[] mb)
   {
    try
     {
      makeDir(fileName);
      File setValueForInputKlassOp = new File(fileName);
      FileOutputStream fl = new  FileOutputStream(setValueForInputKlassOp);
      fl.write(mb);
      fl.close();
     }
  		catch (Exception ex)
       {
        System.err.println("Exception: "+ex);
       }
   }*/
}

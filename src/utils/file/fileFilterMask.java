package utils.file;

import java.io.File;
import java.io.FileFilter;

public class fileFilterMask implements FileFilter {
    private String mask = "";
    public fileFilterMask(String Mask)
    {
        mask = Mask;
    }
    public boolean accept(File pathname)
    {
        try
        {
            if(!pathname.isFile()) return false;
            String nm = pathname.getName().toUpperCase();
            mask = mask.toUpperCase();
            if(mask.length() == 0) return true;
            else if(mask.equals("*")) return true;
            else if(mask.charAt(0) == '*' && mask.charAt(mask.length() -1) == '*')
            {
                if(nm.indexOf(mask.substring(1, mask.length()-1) ) != -1) return true;
                else return false;
            }
            else if(mask.charAt(0) == '*') return nm.endsWith(mask.substring(1) );
            else if(mask.charAt(mask.length() -1) == '*')
                return nm.startsWith(mask.substring(0, mask.length()-1) );
            else return nm.equals(mask);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
}

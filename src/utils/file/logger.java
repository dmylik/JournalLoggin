package utils.file;

import utils.dbStore.mf;

import javax.swing.text.JTextComponent;
import java.io.File;

public class logger {
    private String path = "";
    private JTextComponent txtPanel = null;
    public boolean print_consol = true;

    public logger(String path, JTextComponent txtPanel) throws Exception
    {
        this.path = path + (path.charAt(path.length()-1) != File.separatorChar ? File.separator : "");
        this.txtPanel = txtPanel;
    }

    public void write(String fileNm, String message)
    {
        try {
            String m = mf.currentDate("dd.MM.yy HH:mm:ss ") + message + "\n";
            if(print_consol) System.out.print(m);
            mfl.writeString(path + mf.currentDate("yyyy-MM") + File.separator + fileNm + mf.currentDate("_dd") + ".log", m, true);

            if(txtPanel != null)
            {
                String mm = txtPanel.getText() + fileNm.toUpperCase() + " " + m;
                if(mm.length() > 15000) mm = mm.substring(5000, mm.length());
                txtPanel.setText( mm );
                txtPanel.setCaretPosition(mm.length());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void write(String fileNm, Exception exception)
    {
        try
        {
            if(print_consol) exception.printStackTrace();

            StringBuffer m = new StringBuffer();
            m.append( mf.currentDate("dd.MM.yy HH:mm:ss ") );
            m.append( exception.getClass().getName() );
            m.append( ": " );
            m.append( exception.getMessage() );
            m.append("\n");
            StackTraceElement[] stel = exception.getStackTrace();
            for(int i = 0; i < stel.length; i++)
            {
                m.append("  at ");
                m.append( stel[i].toString() );
                m.append("\n");
            }
            m.append("\n");

            mfl.writeString(path + mf.currentDate("yyyy-MM") + File.separator + fileNm + mf.currentDate("_dd") + ".log", m.toString(), true);

            if(txtPanel != null)
            {
                String mm = txtPanel.getText() + fileNm.toUpperCase() + " " + m;
                if(mm.length() > 15000) mm = mm.substring(5000, mm.length());
                txtPanel.setText( mm );
                txtPanel.setCaretPosition(mm.length());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

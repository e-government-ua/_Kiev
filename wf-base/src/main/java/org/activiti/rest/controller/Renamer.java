import java.io.*;


/**
 *
 * @author Belyavtsev Vladimir Vladimirovich (BW)
 */
public class Renamer {
 
	private File root;
	private static String alpha = new String("אבגדהו¸זחטûיךכלםמןנסעףפץצקרשü‎‏ÿ");
	private static String[] _alpha = {"a","b","v","g","d","e","yo","g","z","i","y","i",
							   "k","l","m","n","o","p","r","s","t","u",
							   "f","h","tz","ch","sh","sh","'","e","yu","ya"};
	
	public Renamer(File f){
		new Renamer(f.getPath());
	}
	
	public Renamer(String dir){
		root = new File(dir);
		
		if(!root.isDirectory()){
			System.out.println("Wrong path!!!");
			System.exit(0);
		}
		
		String[] s = root.list();
		for(int i=0; i<s.length;i++){
			File f = new File(root.getPath()+"/"+s[i]);
			if(f.isDirectory()) 
				new Renamer(f);
		}
		startRename();
	}
	
	public void startRename(){		
		File[] fileList = root.listFiles(new OnlyExt("mp3"));
		for(int i=0; i<fileList.length;i++){
			if(renameFile(fileList[i]));
				//System.out.println("File: "+fileList[i].getName()+" sucsesfull renamed");
			else{;
				//System.out.println("File: "+fileList[i].getName()+" didn't renamed");
			}
		}
	}
	
	public boolean renameFile(File f){
		String name = f.getName();
                
                String nname = sRenamed(name);
                
		System.out.println(nname);
		try{
			File nf = new File(root,nname.toString());
			f.renameTo(nf);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	public static String sRenamed(String s){
		String name = s.toLowerCase();		
		StringBuilder sNew = new StringBuilder("");
		char[] aChar = name.toCharArray();
		for(int i=0; i<aChar.length;i++){
			int nAt = alpha.indexOf(aChar[i]);
			if(nAt != -1)
				sNew.append(_alpha[nAt]);
			else{
				sNew.append(aChar[i]);
			}
		}
                return sNew.toString();
            
        }
        
        
	public static void main(String[] args){
		Renamer r = new Renamer(args[0]);		
	}
}
 
class OnlyExt implements FilenameFilter{
 
	String ext;
	
	public OnlyExt(String s){
		ext = s;
	}
	
	public boolean accept(File dir, String name){
		return name.endsWith(ext) ;
	}
}
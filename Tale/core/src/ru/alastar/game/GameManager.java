package ru.alastar.game;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;

public class GameManager {
 public static ArrayList<Page> pages = new ArrayList<Page>();
 
 public static Page processPage(int n)
 {
	 if(pages.get(n) != null)
		 return pages.get(n);
	 else
		 return null;
 }
 
 @SuppressWarnings("unchecked")
public static void loadPages() throws IOException
 {
	 FileHandle pageFile = MainClass.getPagesFileDir();
     FileInputStream fileIn = new FileInputStream(pageFile.path());
	 ObjectInputStream in = new ObjectInputStream(fileIn);
     try {
         pages = (ArrayList<Page>)in.readObject();
     } catch (ClassNotFoundException ex) {
    	 ex.printStackTrace();
     } catch (IOException e) {
		e.printStackTrace();
	}
     in.close();
     fileIn.close();
 }

}

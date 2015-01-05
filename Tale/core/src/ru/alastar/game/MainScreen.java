package ru.alastar.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;






import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainScreen implements Screen, InputProcessor, GestureListener {

	MainClass mc;
	public static Stage gui;
    public static OrthographicCamera camera;
	protected static boolean music = true;
	Table mainMenuTable  = new Table() ;
	Table optScreenTable = new Table();
	Table loadScreenTable = new Table();
	Table gameScreenTable = new Table();
	ScrollPane buttonsList ;
	//Game tables
	Table bookTable = new Table();
	Table actionsTable = new Table();
	TextArea text;
	Table textTable = new Table();
	private int currentPage = 0;
	static Music musicObj;
	public MainScreen(MainClass mainClass) {
		
		mc = mainClass;
		gui = new Stage(); 
		CreateGUI();
        LaunchMusic();
		InputMultiplexer im = new InputMultiplexer();
        GestureDetector gd = new GestureDetector(this);
        im.addProcessor(gd);
        im.addProcessor(this);

        Gdx.input.setInputProcessor(im);
	}

	private static void LaunchMusic() {
		musicObj = Gdx.audio.newMusic(MainClass.getMusicDir()[MainClass.random.nextInt(MainClass.getMusicDir().length)]);
		musicObj.setLooping(false);
		musicObj.setOnCompletionListener(new OnCompletionListener(){

			@Override
			public void onCompletion(Music music) {
				LaunchMusic();
			}});
		musicObj.play();		
	}
	
	public static void StopMusic()
	{
		musicObj.stop();
	}

	private void CreateGUI() {
		mainMenuTable = new Table();
		mainMenuTable.setFillParent(true);
		
		Table menuTable = new Table();
		menuTable.setFillParent(true);
		
		optScreenTable = new Table();
		optScreenTable.setFillParent(true);
		
		Table optTable = new Table();
		optTable.setFillParent(true);
		
		gameScreenTable = new Table();
		gameScreenTable.setFillParent(true);
		
	    camera = new OrthographicCamera();
	    camera.setToOrtho(false, (int)(1280  / MainClass.bScreenWidth),
	                (int)(1024 / MainClass.bSreenHeight));

		SetRandomBackground();
		
		/////
		///MAIN MENU
		////
		Window menu = new Window("", mc.skin, "window");
        Label label = new Label(MainClass.getLocalizedMessage(9), mc.skin, "label");
        menuTable.pad(20,20,20,20);
		menuTable.add(label);
		menuTable.row().spaceBottom(5);
		
		TextButton start = new TextButton(MainClass.getLocalizedMessage(0), mc.skin, "button");
		start.pad(10, 10, 10, 10);
		start.addListener(new ChangeListener()
        {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				MainClass.clickSound.play(10.0f);
				mainMenuTable.setVisible(false);
				gameScreenTable.setVisible(true);
				PushPage(0);
			}
        });
		menuTable.add(start).size( (float) (200 / MainClass.bScreenWidth), 60 );
		menuTable.row().spaceBottom(5);
		
		TextButton load = new TextButton(MainClass.getLocalizedMessage(6), mc.skin, "button");
		load.pad(10, 10, 10, 10);
		load.align(Align.center);
		load.addListener(new ChangeListener()
        {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				MainClass.clickSound.play(10.0f);
				loadScreenTable.setVisible(true);
				mainMenuTable.setVisible(false);			}
        });
		menuTable.add(load).size( (float) (200 / MainClass.bScreenWidth), 60 );
		menuTable.row().spaceBottom(5);;
		
		TextButton options = new TextButton(MainClass.getLocalizedMessage(2), mc.skin, "button");
		options.pad(10, 10, 10, 10);
		options.align(Align.center);
		options.addListener(new ChangeListener()
        {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				MainClass.clickSound.play(10.0f);
				optScreenTable.setVisible(true);
				mainMenuTable.setVisible(false);
			}
        });
		menuTable.add(options).size( (float) (200 / MainClass.bScreenWidth), 60 );
		menuTable.row().spaceBottom(5);
		
		TextButton exit = new TextButton(MainClass.getLocalizedMessage(3), mc.skin, "button");
		exit.align(Align.center);
	    exit.pad(10, 10, 10, 10);
	    exit.addListener(new ChangeListener()
	        {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					MainClass.clickSound.play(10.0f);
					Gdx.app.exit();
				}
	        });
		menuTable.add(exit).size( (float) (200 / MainClass.bScreenWidth), 60 );
		
		menu.add(menuTable);
		
		mainMenuTable.add(menu);	
		
		mainMenuTable.pack();
		gui.addActor(mainMenuTable);
		
		////
		//OPTIONS
		///
		Window optionsMenu = new Window("", mc.skin, "window");
        Label optLabel = new Label("Menu", mc.skin, "label");
        optTable.pad(20,20,20,20);
        optTable.add(optLabel);
        optTable.row().spaceBottom(5);
		
		TextButton englishLang = new TextButton(MainClass.getLocalizedMessage(7), mc.skin, "button");
		englishLang.pad(10, 10, 10, 10);
		englishLang.addListener(new ChangeListener()
        {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				MainClass.clickSound.play(10.0f);
				MainClass.language = "english";
				try {
					GameManager.loadPages();
				} catch (IOException e) {
					e.printStackTrace();
				}
				MainClass.LoadLanguage();
				MainClass.SaveOptions();
				CreateGUI();
			}
        });
		optTable.add(englishLang).size( (float) (200 / MainClass.bScreenWidth), 60 );
		optTable.row().spaceBottom(5);
		
		TextButton russianLang = new TextButton(MainClass.getLocalizedMessage(8), mc.skin, "button");
		russianLang.pad(10, 10, 10, 10);
		russianLang.align(Align.center);
		russianLang.addListener(new ChangeListener()
        {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				MainClass.clickSound.play(10.0f);
				MainClass.language = "russian";
				try {
					GameManager.loadPages();
				} catch (IOException e) {
					e.printStackTrace();
				}
				MainClass.LoadLanguage();
				MainClass.SaveOptions();
				CreateGUI();
			}
        });
		optTable.add(russianLang).size( (float) (200 / MainClass.bScreenWidth), 60 );
		optTable.row().spaceBottom(5);
		
		TextButton backToMenu = new TextButton(MainClass.getLocalizedMessage(5), mc.skin, "button");
		backToMenu.align(Align.center);
		backToMenu.pad(10, 10, 10, 10);
		backToMenu.addListener(new ChangeListener()
	        {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					MainClass.clickSound.play(10.0f);
					optScreenTable.setVisible(false);
					mainMenuTable.setVisible(true);
				}
	        });
		optTable.add(backToMenu).size( (float) (200 / MainClass.bScreenWidth), 60 );
		
		optionsMenu.add(optTable);
		
		optScreenTable.add(optionsMenu);	
		
		optScreenTable.pack();
		gui.addActor(optScreenTable);
		optScreenTable.setVisible(false);
			
		CreateSaveGUI();
		
		/////
		///GAME SCREEN
		/////
		bookTable = new Table();
		bookTable.pad(20,20,20,20);
		
		textTable = new Table();

		actionsTable = new Table();
		actionsTable.setDebug(true);
        Table ActTable = new Table();
        ActTable.pad(25, 25, 25, 25).sizeBy(450, 760);
        ActTable.top();
        ActTable.left();
        actionsTable.top();
        actionsTable.left();

		Window book = new Window("", mc.skin, "window");
		Window minimenu = new Window("", mc.skin, "window");
		
		Table mMenuTable = new Table();
		mMenuTable.pad(20,20,20,20);
		mMenuTable.setFillParent(true);
		label = new Label(MainClass.getLocalizedMessage(9), mc.skin, "label");
		mMenuTable.add(label).size( (float) (100 / MainClass.bScreenWidth), 25 );
		mMenuTable.row().spaceBottom(15);
		
		TextButton toMenu = new TextButton("<-"+MainClass.getLocalizedMessage(9), mc.skin, "button");
		toMenu.pad(10, 10, 10, 10);
		toMenu.align(Align.center);
		toMenu.addListener(new ChangeListener()
        {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				MainClass.clickSound.play(10.0f);
				gameScreenTable.setVisible(false);
				mainMenuTable.setVisible(true);
			}
        });
		mMenuTable.add(toMenu).size( (float) (100 / MainClass.bScreenWidth), 25 );
		mMenuTable.row().spaceBottom(5);
		
		final TextButton swicthMusic = new TextButton(MainClass.getLocalizedMessage(10) + ": " + MainClass.getLocalizedMessage(11), mc.skin, "button");
		swicthMusic.pad(10, 10, 10, 10);
		swicthMusic.align(Align.center);
		swicthMusic.addListener(new ChangeListener()
        {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				MainClass.clickSound.play(10.0f);
               if(MainScreen.music)
               {
            	   MainScreen.music  = false;
            	   swicthMusic.setText(MainClass.getLocalizedMessage(10) + ": " + MainClass.getLocalizedMessage(12));
            	   StopMusic();
               }
               else
               {
            	   MainScreen.music = true;
            	   swicthMusic.setText(MainClass.getLocalizedMessage(10) + ": " + MainClass.getLocalizedMessage(11));
                   if(!musicObj.isPlaying())
                	   LaunchMusic();
               }
			}
        });
		mMenuTable.add(swicthMusic).size( (float) (100 / MainClass.bScreenWidth), 25 );
		mMenuTable.row().spaceBottom(5);
		
		final TextButton btnFastSave = new TextButton(MainClass.getLocalizedMessage(13), mc.skin, "button");
		btnFastSave.pad(10, 10, 10, 10);
		btnFastSave.align(Align.center);
		btnFastSave.addListener(new ChangeListener()
        {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				MainClass.clickSound.play(10.0f);
				SaveCurrentProgress();
				CreateSaveGUI();
			}
        });
		mMenuTable.add(btnFastSave).size( (float) (100 / MainClass.bScreenWidth), 25 );
		mMenuTable.row().spaceBottom(5);
		
		text = new TextArea("", mc.skin, "textInput");
        text.setDisabled(true);
        text.setFillParent(true);
        
        textTable.add(text).minSize(450, 760);
		textTable.pad(5,5,5,5);
		
		bookTable.add(textTable).size(450,760);
		ActTable.add(actionsTable).size(450,760);
		bookTable.add(ActTable);
		bookTable.left();
		bookTable.top();
		
		book.add(bookTable).size(900,800);
		minimenu.add(mMenuTable).size(120,120);
		
		gameScreenTable.add(minimenu).top();	
		gameScreenTable.add(book);	

		gameScreenTable.pack();
		gui.addActor(gameScreenTable);
		gameScreenTable.setVisible(false);
		
	}

	protected void CreateSaveGUI() {
		loadScreenTable = new Table();
		loadScreenTable.setFillParent(true);
		
		Table loadTable = new Table();
		loadTable.setFillParent(true);
		/////
		///LOAD SCREEN
		/////
		Window loadMenu = new Window("", mc.skin, "window");
        Label loadMenuLabel = new Label("Menu", mc.skin, "label");
        loadTable.pad(20,20,20,20);
        loadTable.add(loadMenuLabel);
        loadTable.row().spaceBottom(5);
        
		Table listTable = new Table();
		listTable.reset();
		listTable.setFillParent(true);
		ScrollPane list = new ScrollPane(listTable, mc.skin, "scroll");
		final FileHandle[] saves = MainClass.getSaveDir();
		for(int i = 0; i < saves.length; ++i){
			final File saveFile = saves[i].file();
			final TextButton saveBtn = new TextButton(saves[i].name(), mc.skin, "button");
			saveBtn.pad(10, 10, 10, 10);
			saveBtn.addListener(new ChangeListener()
	        {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					MainClass.clickSound.play(10.0f);
					loadScreenTable.setVisible(false);
					int deserializedId = 0;
				     FileInputStream fileIn;
				     ObjectInputStream in;
					try {
						fileIn = new FileInputStream(saveFile);

					 in = new ObjectInputStream(fileIn);
				    	 deserializedId = in.readInt();
					     in.close();
					     fileIn.close();
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

					PushPage(deserializedId);
					gameScreenTable.setVisible(true);
				}
	        });
			listTable.add(saveBtn).size( (float) (90 / MainClass.bScreenWidth), 25 );

			final TextButton deleteSaveBtn = new TextButton(MainClass.getLocalizedMessage(14), mc.skin, "button");
			deleteSaveBtn.pad(10, 10, 10, 10);
			deleteSaveBtn.addListener(new ChangeListener()
	        {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					MainClass.clickSound.play(10.0f);
					saveFile.delete();
					loadScreenTable.setVisible(false);		
					CreateSaveGUI();
					loadScreenTable.setVisible(true);		

				}
	        });
			listTable.add(deleteSaveBtn).size( (float) (90 / MainClass.bScreenWidth), 25 );
			listTable.row().space(5);
		}
		
		loadTable.add(list).size( (float) (200 / MainClass.bScreenWidth), 180 );
		loadTable.row().spaceBottom(5);
		list.setOverscroll(false, true);
		TextButton back = new TextButton(MainClass.getLocalizedMessage(5), mc.skin, "button");
		back.pad(10, 10, 10, 10);
		back.align(Align.center);
		back.addListener(new ChangeListener()
        {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				MainClass.clickSound.play(10.0f);
				loadScreenTable.setVisible(false);
				mainMenuTable.setVisible(true);
			}
        });
		loadTable.add(back).size( (float) (200 / MainClass.bScreenWidth), 60 );
		loadTable.row().spaceBottom(5);
		
		loadMenu.add(loadTable);
		
		loadScreenTable.add(loadMenu);	
		
		loadScreenTable.pack();
		gui.addActor(loadScreenTable);
		loadScreenTable.setVisible(false);		
	}

	protected void SaveCurrentProgress() {
		System.out.println("save page: " + currentPage);

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM_dd");
         String timeLog = dateFormat
                 .format(Calendar.getInstance().getTime());
		FileOutputStream fos;
		try {
		fos = new FileOutputStream(MainClass.getSavePath()+ "\\"+timeLog+"-"+currentPage);

		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeInt(currentPage);

		oos.flush();
		oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void SetRandomBackground() {
		int id = mc.random.nextInt(5);
		mc.curBackground = mc.textures[id];
	}
	
	private void PushPage(final int i) {
		final Page page = GameManager.processPage(i);
		text.setText(page.text);
		TextButton actionBtn;
		actionsTable.reset();
		currentPage = i;
		System.out.println("Current page: " + currentPage);
		for(int index = 0; index < page.actions.length; ++index)
		{	
			final Action act;
			act = page.actions[index];
			System.out.println("Adding button");
			actionBtn = new TextButton(act.name, mc.skin, "button");
			actionBtn.pad(10, 10, 10, 10);
			actionBtn.align(Align.center);
			actionBtn.addListener(new ChangeListener()
	        {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					MainClass.clickSound.play(10.0f);
					PushPage(act.sendTo);
					System.out.println("Click!");
				}
	        });	
			actionsTable.row().space(5);
			actionsTable.add(actionBtn).size( (float) (390 / MainClass.bScreenWidth), 60 );
			actionsTable.row().space(5);
		}
        actionsTable.top();
        actionsTable.left();
	}
	
	@Override
	public void show() {}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

		mc.batch.begin(); 
		mc.batch.setProjectionMatrix(camera.combined);
		mc.batch.draw(mc.curBackground, 0, 0, (float)MainClass.screenWidth, (float)MainClass.screenHeight); // draw background
		mc.batch.end();
		
		gui.draw();
		gui.act(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int width, int height) {
		  gui.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		gui.touchDown((int)x, (int)y, pointer, button);
        return true;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
	       gui.keyDown(keycode);
	        return true;
	}

	@Override
	public boolean keyUp(int keycode) {
	       gui.keyUp(keycode);
	        return true;
	}

	@Override
	public boolean keyTyped(char character) {
	       gui.keyTyped(character);
	        return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		gui.touchDown(screenX, screenY, pointer, button);
        return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		gui.touchUp(screenX, screenY, pointer, button);
        return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		gui.touchDragged(screenX, screenY, pointer);
        return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		gui.mouseMoved(screenX, screenY);
        return true;
	}

	@Override
	public boolean scrolled(int amount) {
		gui.scrolled(amount);
        return true;
	}

}

//
//  GameView.java: Handles the view rendering activities
//  MineSweeper
//
//  Created by Kavish Vinod Pahade on 2/8/14.
//  Copyright (c) 2014 Kavish Vinod Pahade. All rights reserved.
//


package com.minesweeper;
import com.minesweeper.R;

import android.R.bool;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class GameView extends View 
{

	private static final String TAG = "MineSweeper";

	   private float width;    // width of one tile
	   private float height;   // height of one tile
	   private int selX;       // X index of selection
	   private int selY;       // Y index of selection
	   private final Rect selRect = new Rect();
	   private boolean isSingleClick=false;
	   private boolean isDoubleClick=false;
	   GestureDetector gestureDetector;
	   int MinePositions[][];
	   private final Game game;
	   public static boolean GameWonFlag=false;
       private boolean GAMEFINISHSED=false;
       public static boolean GameLost=false;
       private int mines;
	   public GameView(Context context)
		{
				super(context);
				this.game = (Game) context;
				mines=game.getMineCount();
			    game.FillRandomMines(mines); 
	    	    MinePositions=game.getMinePositions();
			    gestureDetector=new GestureDetector(context, new GestureListener());
			    setFocusable(true);
		        setFocusableInTouchMode(true);
		}
		 
	   class GestureListener extends GestureDetector.SimpleOnGestureListener 
	   {
	       @Override
	       public boolean onDown(MotionEvent e) {
	           return true;
	       }
	   
	       // event when double tap occurs
	       @Override
	       public boolean onDoubleTap(MotionEvent e)
	       {
	    	    if ( MinePositions[selX][selY]==-1 )
	    	    {
	    	       GameOver(); //If the hit tile has a mine then game over helper function is called
	    	       invalidate();
	    	    }
	    	    else
	    	    {
		    	    if(MinePositions[selX][selY]!=-3 && MinePositions[selX][selY]!=-4) //Disabling double click on flags
		    	    {
		    	        isDoubleClick=true;
		    	        game.neighbourMines(selX, selY);
		    	        GameWon();  //Checking if the game is won
			    	    invalidate();
			    	    //GameWon();
		    	    }
	    	    }
	    	   return true;
	       }
	      

		public boolean onSingleTapConfirmed(MotionEvent event)
	       {
	    	   isSingleClick=true;
	    	   if(MinePositions[selX][selY]==-3) //Flag already present at this location then remove the flag
	           {
	               game.setMinePositions(selX, selY, 0);
	           }
	           else
	           {
	               if(MinePositions[selX][selY]==0)
	               {
		               game.setMinePositions(selX, selY, -3);
	               }
	           }

	    	   if(MinePositions[selX][selY]==-1)
	           {
	               game.setMinePositions(selX, selY, -4);				//-4 indicates if the flag posn has mines
	           }
	    	   
	    	   else if(MinePositions[selX][selY]==-4)
	           {
	               game.setMinePositions(selX, selY, -1);				//-4 indicates if the flag posn has mines
	           }
	    	   GameWon();
	    	   invalidate();
	    	   return true;
	       }
	   }
	
	   
	  
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
	      width = w / 16f;
	      height = h / 16f;
	      getRect(selX, selY, selRect);
	      Log.d(TAG, "onSizeChanged: width " + width + ", height "
	            + height);
	      super.onSizeChanged(w, h, oldw, oldh);
	}
	
	
   
	 @Override
	   protected void onDraw(Canvas canvas) {
	      // Draw the background...
	      Paint background = new Paint();
	      background.setColor(getResources().getColor(R.color.Game_Background));
	      canvas.drawRect(0, 0, getWidth(), getHeight(), background);

	      
	      // Draw the board...
	      
	      // Define colors for the grid lines
	      Paint dark = new Paint();
	      dark.setColor(getResources().getColor(R.color.puzzle_dark));
	      Paint hilite = new Paint();
	      hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
	      Paint light = new Paint();
	      light.setColor(getResources().getColor(R.color.puzzle_light));
	      for (int i = 0; i < 16; i++)
	     {
	          canvas.drawLine(0, i * height, getWidth(), i * height,
	               dark);
	         canvas.drawLine(0, i * height + 1, getWidth(), i * height
	               + 1, hilite);
	         canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
	         canvas.drawLine(i * width + 1, 0, i * width + 1,
	               getHeight(), hilite);
	      }
	     ///////////////Grid Drawn///////////////////////
	     
	     
	     //Displaying Flags
	     for(int i=0;i<16;i++)
	     {
	         for (int j=0;j<16;j++)
	         {
	             
	             if ( MinePositions[i][j]==-3 || MinePositions[i][j]==-4)
	             {
	            	 Paint paint = new Paint();
	    	         Bitmap mineImage = BitmapFactory.decodeResource(getResources(),
	    	                         R.drawable.flagicon);
	    	         Rect r=new Rect();
	    	         getRect(i,j,r);
	    	         canvas.drawBitmap(mineImage, null, r, null);
	             }
	         }
	     }
	       
	     if(isSingleClick)
	      {
		    isSingleClick=false;
	      }
	     if(isDoubleClick)
	     {
	    	 // neighbourMines(selX,selY);
	    	  //GameWon();
              isDoubleClick=false;
	     }
	   //Display values
	     Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
         foreground.setColor(getResources().getColor(
               R.color.puzzle_dark));
         foreground.setStyle(Style.FILL);
         foreground.setTextSize(height * 0.75f);
         foreground.setTextScaleX(width / height);
         foreground.setTextAlign(Paint.Align.CENTER);

         // Draw the number in the center of the tile
         FontMetrics fm = foreground.getFontMetrics();
         // Centering in X: use alignment (and X at midpoint)
         float x = width / 2;
         // Centering in Y: measure ascent/descent first
         float y = height / 2 - (fm.ascent + fm.descent) / 2;
          for (int i = 0; i < 16; i++)
         {
            for (int j = 0; j < 16; j++)
            {
         	   if(MinePositions[i][j]!=-1 && MinePositions[i][j]!=0 && MinePositions[i][j]!=-2 && MinePositions[i][j]!=-3 && MinePositions[i][j]!=-4)
                {
         		   canvas.drawText(Integer.toString(MinePositions[i][j]), i
                     * width + x, j * height + y, foreground);
                }
            }
         }
	     	     
	     
	     if(GameWonFlag==true)
	      {
	    	  AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
	            builder1.setMessage("Hurray you won the game");
	            builder1.setCancelable(false);
	            builder1.setPositiveButton("New Game",
	                    new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id)
	                {
	                	dialog.cancel();
	                	game.FillRandomMines(mines);
	                    MinePositions=game.getMinePositions();
	                	GameWonFlag=false;
	                    GAMEFINISHSED=false;
	                    invalidate();
	                }
	            });
	            AlertDialog alert11 = builder1.create();
	            alert11.show();
	      }
	      if(!GAMEFINISHSED) //displaying only the opened values other places are covered
	      {
	          for(int i=0;i<16 ;i++)
	          {    
	              for(int j=0;j<16;j++)
	              {
	                  if(MinePositions[i][j]==-1 || MinePositions[i][j]==0)
	                  {
	                	Rect r=new Rect();
	  		            Paint p=new Paint();
	  		            p.setColor(getResources().getColor(R.color.puzzle_selected));
	  		            getRect(i,j, r);
	  		            canvas.drawRect(r, p);
	                  }
	              }
	      
	          }
	      }

	      if(GameLost==true)
	      {
	    	  game.LostMusicPlay();
	    	  DisplayMines(canvas);
	    	  AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
	            builder1.setMessage("Sorry you lost the game");
	            builder1.setCancelable(false);
	            builder1.setPositiveButton("New Game",
	                    new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id)
	                {
	                	//Music.playLossMusic(R.raw.music);
	                	dialog.cancel();
	                	game.FillRandomMines(mines);
	                    MinePositions=game.getMinePositions();
	                	GameLost=false;
	                	game.LostMusicPlay();
	                    invalidate();
	                    //game.stopMusic();
	                }
	            });
	          
	            AlertDialog alert11 = builder1.create();
	            alert11.show();
	      }

	 }


	/*private void neighbourMines(int xpos, int ypos)
	 {
		 int minx, miny, maxx, maxy;
		    int result = 0;
		    if(MinePositions[xpos][ypos]!=-1 && MinePositions[xpos][ypos]!=-2 && MinePositions[xpos][ypos]!=-3 && MinePositions[xpos][ypos]!=-4 ) // termination condition for cascading of the mines
		    {
		        // Checking the boundary conditions
		        minx = (xpos <= 0 ? 0 : xpos - 1);
		        miny = (ypos <= 0 ? 0 : ypos - 1);
		        maxx = (xpos >= 15 ? 15 : xpos + 1);
		        maxy = (ypos >= 15 ? 15 : ypos + 1);
		       // Check all immediate neighbours for mines
		        for (int i = minx; i <= maxx; i++)
		        {
		            for (int j = miny; j <= maxy; j++)
		            {
		                if (MinePositions[i][j]==-1 || MinePositions[i][j]==-4)
		                {
		                    result++;
		                }
		            }
		        }
		        if(result>0) //&& (MinePositions[xpos][ypos]!=-3 || MinePositions[xpos][ypos]!=-4))
		        {
		           // MinePositions[xpos][ypos]=result;
		        	game.setMinePositions(xpos, ypos, result);
		        }
		        else
		        {
		              // MinePositions[xpos][ypos]=-2;   //Blank Position
		        	game.setMinePositions(xpos, ypos, -2);
		        	for (int i = minx; i <= maxx; i++)
		            {
		                for (int j = miny; j <= maxy; j++)
		                {
		                 neighbourMines(i,j);
		                    
		                }
		            }
		            
		        }
		    }
	 }*/

	 private void DisplayMines(Canvas canvas)      //Displaying all the mines when the user hits a mine
	 {
	     for(int i=0;i<16;i++)
	     {
	         for (int j=0;j<16;j++)
	         {
	             
	             if ( MinePositions[i][j]==-1 || MinePositions[i][j]==-4 )
	             {
	            	 Paint paint = new Paint();
	    	         Bitmap mineImage = BitmapFactory.decodeResource(getResources(),
	    	                         R.drawable.icon);
	    	         Rect r=new Rect();
	    	         getRect(i,j,r);
	    	         canvas.drawBitmap(mineImage, null, r, null);
	    		      isSingleClick=false;
	             }
	         }
	     }
	     
	 }
	 void DisplayFlag(Canvas canvas) //Helper function to display flag images
	 {
	     for(int i=0;i<16;i++)
	     {
	         for (int j=0;j<16;j++)
	         {
	             
	             if ( MinePositions[i][j]==-3 || MinePositions[i][j]==-4)
	             {
	            	 Paint paint = new Paint();
	    	         Bitmap mineImage = BitmapFactory.decodeResource(getResources(),
	    	                         R.drawable.flagicon);
	    	         Rect r=new Rect();
	    	         getRect(i,j,r);
	    	         canvas.drawBitmap(mineImage, null, r, null);
	    		      isSingleClick=false;
	             }
	         }
	     }
	     
	 }
	       
	public boolean onTouchEvent(MotionEvent event)
	 {
	       select((int) (event.getX() / width),
	           (int) (event.getY() / height));
	      Log.d(TAG, "onTouchEvent: x " + selX + ", y " + selY);
	      return gestureDetector.onTouchEvent(event);
	       //return true;
	   }
	 private void select(int x, int y) {
	     //invalidate(selRect);
	      selX = Math.min(Math.max(x, 0),16);
	      selY = Math.min(Math.max(y, 0),16);
	      getRect(selX, selY, selRect);	      
	      //invalidate(selRect);
	   }
	 private void getRect(int x, int y, Rect rect)
	 {
	      rect.set((int) (x * width), (int) (y * height), (int) (x
	            * width + width), (int) (y * height + height));
	 }
	 
	 protected void GameWon()
	 {
	 	int OpenedCount=0;
	     int flaggedMines=0;
	     for(int i=0;i<16 ;i++)
	     {
	         for(int j=0;j<16;j++)
	         {
	                 if(MinePositions[i][j]==0)
	                 {
	                     OpenedCount++;
	                 }

	                 if(MinePositions[i][j]==-4)
	                 {
	                     flaggedMines++;
	                 }

	           }
	     }
	     if(OpenedCount==0 || flaggedMines==mines)
	     {
	     	GameWonFlag=true;
	     	GAMEFINISHSED=true;
	     	 invalidate();
	     }
	    
	 }
	 protected void GameOver() 
     {
		      GameLost=true;
     }
	 
}
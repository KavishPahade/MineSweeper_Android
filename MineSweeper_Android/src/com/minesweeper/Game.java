//
//  Game.java: Handles the computation activities of the game.
//  MineSweeper
//
//  Created by Kavish Vinod Pahade on 2/8/14.
//  Copyright (c) 2014 Kavish Vinod Pahade. All rights reserved.
//
package com.minesweeper;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.*;


public class Game extends Activity {

	private GameView gameview;
	//private 
    private int no_of_Columns,no_of_Rows,no_of_mines;
    private int MinePositions[][];
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		  no_of_Columns=16;
	      no_of_Rows=16;
	      no_of_mines=20;
	      gameview = new GameView(this);
	      setContentView(gameview);
	      gameview.requestFocus();
	}
	protected void onResume() {
	      super.onResume();
	      Music.play(this, R.raw.music);
	   }

	   @Override
	   protected void onPause() {
	      super.onPause();
	      //Log.d(TAG, "onPause");
	      Music.stop(this);
	      //getPreferences(MODE_PRIVATE).edit().putString(PREF_PUZZLE,
	        //    toPuzzleString(puzzle)).commit();
	   }
	   protected void LostMusicPlay() {
		      //super.onResume();
		      Music.play(this, R.raw.music);
		   }
	   protected void stopMusic() {
		      //super.onResume();
		      Music.stop(this);
		   }
	protected int getMineCount()
	{
		return no_of_mines;
	}
	//Based on neighboring 3*3 grid, fills the MineGrid with appropriate values
	protected void neighbourMines(int xpos, int ypos)
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
		           MinePositions[xpos][ypos]=result;
		        	//game.setMinePositions(xpos, ypos, result);
		        }
		        else
		        {
		              MinePositions[xpos][ypos]=-2;   //Blank Position
		        	//game.setMinePositions(xpos, ypos, -2);
		        	for (int i = minx; i <= maxx; i++)
		            {
		                for (int j = miny; j <= maxy; j++)
		                {
		                 neighbourMines(i,j);
		                    
		                }
		            }
		            
		        }
		    }
	 }
	protected int[][] getMinePositions()
	{
		return MinePositions;
	}
	
	protected void setMinePositions(int x, int y, int val)
	{
		MinePositions[x][y]= val;
	}
	protected void FillRandomMines(int mines)
	{
		//Random
		int randx, randy;
		//int MinePositions[][]=new int[no_of_Rows][no_of_Columns];
		MinePositions=new int[no_of_Rows][no_of_Columns];

		//MinePositions=getMinePositions();
		for(int i=0;i<no_of_Columns ;i++)
	    {
	        
	        for(int j=0;j<no_of_Rows;j++)
	        {
	            MinePositions[i][j]=0;  //Initialising the array with zero
	        }
	    }
	    for(int i=0;i<mines;i++)  //Initialising the array with -1 randomly to indicate mines
	    {
	        do {
	        	Random rand = new Random();
	        	randx = rand.nextInt(no_of_Rows);
	        	rand = new Random();
	        	randy = rand.nextInt(no_of_Columns);
	        	//randx = arc4random()%no_of_Columns;
	            //randy = arc4random()%no_of_Rows;
	        } while (MinePositions[randx][randy]==-1);
	        MinePositions[randx][randy]=-1;
	    }
	}
	

	   @Override
	   public boolean onCreateOptionsMenu(Menu menu) {
	      super.onCreateOptionsMenu(menu);
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.game_view, menu);
	      return true;
	   }
	   @Override
	   public boolean onOptionsItemSelected(MenuItem item) {
	      switch (item.getItemId()) {
	      case R.id.action_settings:
	         startActivity(new Intent(this, Prefs.class));
	         return true;
	      // More items go here (if any) ...
	      }
	      return false;
	   }
	//@Override
	/*public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_view, menu);
		return true;
	}*/
	

}

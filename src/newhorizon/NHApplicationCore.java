package newhorizon;

import arc.ApplicationListener;
import arc.util.Log;
import mindustry.Vars;

public class NHApplicationCore implements ApplicationListener{
	public NHApplicationCore(){
		if(NewHorizon.DEBUGGING)Log.info("NH Listener Core Constructed");
	}
	
	@Override
	public void update(){
		if(Vars.state.isPlaying()){
		}
	}
	
	@Override
	public void dispose(){
		if(NewHorizon.DEBUGGING)Log.info("Disposed");
	}
	
	@Override
	public void init(){
	
	}
}

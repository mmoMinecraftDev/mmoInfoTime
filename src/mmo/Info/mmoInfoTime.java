package mmo.Info;

import java.util.GregorianCalendar;
import java.util.HashMap;
import mmo.Core.InfoAPI.MMOInfoEvent;
import mmo.Core.MMOPlugin;
import mmo.Core.MMOPlugin.Support;
import mmo.Core.util.EnumBitSet;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.Label;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.player.SpoutPlayer;

public class mmoInfoTime extends MMOPlugin
implements Listener
{
	private HashMap<Player, CustomLabel> widgets = new HashMap();

	public EnumBitSet mmoSupport(EnumBitSet support)
	{
		support.set(MMOPlugin.Support.MMO_NO_CONFIG);
		support.set(MMOPlugin.Support.MMO_AUTO_EXTRACT);
		return support;
	}

	public void onEnable() {
		super.onEnable();
		this.pm.registerEvents(this, this);
	}
	
	@EventHandler
	public void onMMOInfo(MMOInfoEvent event)
	{
		if (event.isToken("time")) {
			SpoutPlayer player = event.getPlayer();
			if (player.hasPermission("mmo.info.time")) {
				CustomLabel label = (CustomLabel)new CustomLabel().setResize(true).setFixed(true);
				this.widgets.put(player, label);
				event.setWidget(this.plugin, label);
				event.setIcon("clock.png");
			} else {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(Player player)
	{
		this.widgets.remove(player);
	}

	public class CustomLabel extends GenericLabel
	{
		private boolean check = true;		

		public CustomLabel() {
		}

		public void change() {
			this.check = true;
		}
		private transient int tick = 0;
		public void onTick()
		{
			if (tick++ % 100 == 0) {
				int hours = (int)((getServer().getWorld(getScreen().getPlayer().getWorld().getName()).getTime() / 1000L + 6L) % 24L);
			    //int minutes = (int)(60L * (getServer().getWorld(getScreen().getPlayer().getWorld().getName()).getTime() % 1000L) / 1000L);
			   
			    if (hours == 1)
			    setText(String.format(": " + "12am"));
			    if (hours > 1 && hours <= 11)
				setText(String.format(": " + hours + "am"));
			    if (hours == 12)
					setText(String.format(": " + hours + "pm"));
			    if (hours >= 13) 
			    	setText(String.format(": " + (hours-12) + "pm"));
			}
		}
	}
}

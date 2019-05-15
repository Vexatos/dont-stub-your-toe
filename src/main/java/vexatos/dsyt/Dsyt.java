package vexatos.dsyt;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;

public class Dsyt implements ClientModInitializer {

	private static boolean changed = false;

	@Override
	public void onInitializeClient() {
		ClientTickCallback.EVENT.register(client -> {
			if(client.player != null) {
				if(!changed) {
					if(!client.options.autoJump && client.player.isSprinting()) {
						changed = true;
						client.options.autoJump = true;
					}
				} else if(!client.player.isSprinting()) {
					client.options.autoJump = false;
					changed = false;
				}
			}
		});
	}
}

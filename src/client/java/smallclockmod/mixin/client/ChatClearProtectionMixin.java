package smallclockmod.mixin.client;

import smallclockmod.ClientMessages;
import smallclockmod.Filter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

@Mixin(ChatHud.class)
public class ChatClearProtectionMixin {

    @Unique
    private final static Filter[] Filters = {
            new Filter(100, Pattern.compile("^ {11,}$")),
            new Filter(100, Pattern.compile("(\\n){2,}$")),
            new Filter(33, Pattern.compile("^ {1,10}$")),
            new Filter(33, Pattern.compile("^$")),
    };

    @Unique
    private static final int ChatClearThreshold = 99;
    @Unique
    private static final AtomicInteger ScoreTracker = new AtomicInteger(0);
    @Unique
    private static final Text PreventedMessage = ClientMessages.New("Chat Clear Prevented");

	@Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", cancellable = true)
	private void init(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {

        String loggedName = "";
        if (indicator != null)
            loggedName = indicator.loggedName();

        if (!Objects.equals(loggedName, MessageIndicator.system().loggedName())) {
            // reset ChatCleared counted if it's not a system message
            ScoreTracker.getAndSet(0);
            return;
        }

        if (message.getString().equals(PreventedMessage.getString())) {
            // ignore if message is our own filter message
            return;
        }

        for (Filter p : Filters) {
            var matcher = p.getMatcher(message.getString());
            if (matcher.find()) {
                if (trySendClearedMessage(p.score()) >= ChatClearThreshold) {
                    // Cancel messages if over threshold
                    ci.cancel();
                };
                return;
            }
        }

        ScoreTracker.getAndSet(0);
    }

    @Unique
    private static int trySendClearedMessage(int score) {
        int currentScore = ScoreTracker.getAndAdd(score);
        int newScore = currentScore + score;

        // if this message pushes the score over ChatClearTargetScore then print message
        if (newScore >= ChatClearThreshold && currentScore < ChatClearThreshold) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(PreventedMessage);
        }
        return newScore;
    }
}
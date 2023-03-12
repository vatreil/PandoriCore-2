package fr.pandorica.command;

import fr.pandorica.friend.RequestFriend;
import fr.pandorica.gui.friend.AddFriend;
import fr.pandorica.gui.friend.PlayerFriend;
import fr.pandorica.redis.MessagePlayer.MessageBody;
import fr.pandorica.redis.MessagePlayer.MessageType;
import fr.pandorica.redis.RedisPlayerFriend;
import fr.pandorica.redis.RedisPlayerServer;
import fr.pandorica.redis.RedisSendStream;
import fr.pandorica.request.GetFriend;
import fr.pandorica.request.GetPlayer;
import fr.pandorica.request.PostFriend;
import fr.pandorica.utils.ParseComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.listener.manager.PacketListenerConsumer;
import net.minestom.server.network.packet.client.play.ClientCommandChatPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class Friend implements PacketListenerConsumer<ClientCommandChatPacket> {
    @Override
    public void accept(ClientCommandChatPacket packet, Player player) {
        String[] cmd = packet.message().split(" ");
        if (cmd[0].equalsIgnoreCase("f")) {

            if (cmd.length <= 1) {
                displayHelp(player);
                return;
            }

            RedisPlayerFriend redisPlayerFriend = new RedisPlayerFriend(player.getUuid());

            if (cmd.length == 2) {
                if (cmd[1].equalsIgnoreCase("accept")) {
                    // si il n'a pas de demande d'ami
                    if (!(redisPlayerFriend.haveRequestCurrently())) {
                        player.sendMessage("§cVous n'avez pas de demande d'ami");
                        return;
                    }

                    // si le player est deco
                    if (redisPlayerFriend.getKeyRequestFriend() == null) {
                        player.sendMessage("§cErreur lors de la création d'un ami");
                        return;
                    }

                    String receiver = redisPlayerFriend.getKeyRequestFriend();
                    if (new GetFriend(player.getUuid()).isFriendWith(UUID.fromString(receiver))) {
                        player.sendMessage("§cVous êtes déjà amis avec cette personne");
                        return;
                    }

                    new PostFriend(player.getUuid()).addFriend(UUID.fromString(receiver));
                    new PostFriend(UUID.fromString(receiver)).addFriend(player.getUuid());

                    player.sendMessage("§6Vous étes désormais amis avec §a" + new GetPlayer(UUID.fromString(redisPlayerFriend.getKeyRequestFriend())).getPseudo() + " §6.");

                    //RETURN MESSAGE SENDER
                    Map<String, String> messageBody = MessageBody.getBody(
                            MessageType.SEND_INFO,
                            UUID.fromString(redisPlayerFriend.getKeyRequestFriend()),
                            player.getUuid(),
                            "§6Vous étes désormais amis avec §a" + ParseComponent.getString(player.getDisplayName()),
                            ""
                    );
                    new RedisSendStream(new RedisPlayerServer(UUID.fromString(redisPlayerFriend.getKeyRequestFriend())).getServerInKeyPlayer(), messageBody).sendMessage();


                    redisPlayerFriend.delKeyRequestFriend();

                } else if (cmd[1].equalsIgnoreCase("refuse")) {

                    // si il n'a pas de demande d'ami
                    if (!(redisPlayerFriend.haveRequestCurrently())) {
                        player.sendMessage("§cVous n'avez pas de demande d'ami");
                        return;
                    }

                    // si le player est deco
                    if (redisPlayerFriend.getKeyRequestFriend() == null) {
                        player.sendMessage("§cErreur lors de la création d'un ami");
                        return;
                    }


                    if (new GetFriend(player.getUuid()).isFriendWith(UUID.fromString(redisPlayerFriend.getKeyRequestFriend()))) {
                        player.sendMessage("§cVous ètes déjà amis avec cette personne");
                        return;
                    }

                    player.sendMessage("§6Vous avez refusé la demande d'amis de §a" + new GetPlayer(UUID.fromString(redisPlayerFriend.getKeyRequestFriend())).getPseudo() + " §6.");

                    //RETURN MESSAGE SENDER
                    Map<String, String> messageBody = MessageBody.getBody(
                            MessageType.SEND_INFO,
                            UUID.fromString(redisPlayerFriend.getKeyRequestFriend()),
                            player.getUuid(),
                            "§6Le joueur §a" + ParseComponent.getString(player.getDisplayName()) + " §6à refusé votre demande d'ami.",
                            ""
                    );

                    new RedisSendStream(new RedisPlayerServer(UUID.fromString(redisPlayerFriend.getKeyRequestFriend())).getServerInKeyPlayer(), messageBody).sendMessage();

                    redisPlayerFriend.delKeyRequestFriend();

                }
            } else if (cmd.length == 3){
                if (cmd[1].equalsIgnoreCase("add")) {
                    ItemStack it = new RequestFriend().check(player, cmd[2]);
                    if (it != null){
                        AddFriend.openInventory(player, cmd[2], it);
                    }
                }
            }else if (cmd[1].equalsIgnoreCase("enable")) {
                GetFriend getFriend = new GetFriend(player.getUuid());
                if(getFriend.isAllow() == true) {
                    PostFriend postFriend = new PostFriend(player.getUuid());
                    postFriend.setAllow(true);
                    player.sendMessage("§eDemandes d'amis §aactivés");
                } else {
                    player.sendMessage("§cVous acceptez déjà les demandes d'amis");
                }
            } else if (cmd[1].equalsIgnoreCase("disable")) {
                GetFriend getFriend = new GetFriend(player.getUuid());
                if(getFriend.isAllow() != true) {
                    PostFriend postFriend = new PostFriend(player.getUuid());
                    postFriend.setAllow(false);
                    player.sendMessage("§eDemandes d'amis §cDesactivés");
                } else {
                    player.sendMessage("§cVous acceptez déjà les demandes d'amis");
                }
            } else {
                displayHelp(player);
            }
        }
        if (new ArrayList<>(Arrays.asList("friends", "friend")).contains(cmd[0])){
            displayHelp(player);
        }
    }

    private void displayHelp(Player player) {
        player.sendMessage("");
        player.sendMessage(Component.text("Commands friends :", NamedTextColor.GOLD));
        player.sendMessage(ParseComponent.getClickPreSetCmdWithDesc("/f add Username", "Ajouter un amis", "/f add"));
        player.sendMessage(ParseComponent.getClickPreSetCmdWithDesc("/f accept", "Accepter la demandes d'ami", "/f accept"));
        player.sendMessage(ParseComponent.getClickPreSetCmdWithDesc("/f refuse", "Refuser la demandes d'ami", "/f refuse"));
        player.sendMessage(ParseComponent.getClickPreSetCmdWithDesc("/f enable", "Activer les demandes d'amis", "/f enable"));
        player.sendMessage(ParseComponent.getClickPreSetCmdWithDesc("/f disable", "Désactiver les demandes d'amis", "/f disable"));
        player.sendMessage("");
    }
}

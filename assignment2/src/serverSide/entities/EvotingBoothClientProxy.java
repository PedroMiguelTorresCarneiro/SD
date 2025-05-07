/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverSide.entities;


import commInfra.ServerCom;
import serverSide.sharedRegions.interfaces.EvotingBoothInterface;

/**
 *
 * @author pedrocarneiro
 */
public class EvotingBoothClientProxy extends Proxy{
    public EvotingBoothClientProxy(ServerCom sconi, EvotingBoothInterface sharedRegionInterface) {
        super("ProxyEvotingBooth_" , sconi );
        this.sharedRegionInterface = sharedRegionInterface;
    }
}
